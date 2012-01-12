package net.morrdusk.web

import controller.{EventController, DeviceController}
import org.scalatra.{UrlSupport, ScalatraServlet}
import org.scalatra.scalate.ScalateSupport
import com.mongodb.casbah.commons.conversions.scala.RegisterJodaTimeConversionHelpers._
import net.morrdusk.scheduling.JobScheduler
import org.fusesource.scalate.layout.DefaultLayoutStrategy
import org.openid4java.consumer._
import org.openid4java.discovery._
import org.openid4java.message.ax._
import org.openid4java.message._
import collection.mutable.ConcurrentMap
import scala.collection.JavaConversions._
import java.util.concurrent.ConcurrentHashMap
import org.slf4j.LoggerFactory
import sreg.{SRegRequest, SRegResponse, SRegMessage}
import net.morrdusk.request.AccessTokenRequester
import net.morrdusk.ApiKey
import dispatch.oauth.Token
import net.morrdusk.model.{AccessTokenDao, AccessToken}

case class AuthUser(identity: String, accessToken: Option[AccessToken], email: String, fullName: String)

class RoutingServlet(apiKey: ApiKey, scheduler: JobScheduler) extends ScalatraServlet with UrlSupport with ScalateSupport {
  val LOG = LoggerFactory.getLogger(getClass)

  protected def contextPath = request.getContextPath

  var sessionAuth: ConcurrentMap[String, AuthUser] = new ConcurrentHashMap[String, AuthUser]()
  val manager = new ConsumerManager

  before() {
    contentType = "text/html"
    templateEngine.layoutStrategy = new DefaultLayoutStrategy(templateEngine, "/layouts/default.jade")
  }

  get("/") {
    sessionAuth.get(session.getId) match {
      case Some(user) => new DeviceController(templateEngine, apiKey, user).index()
      case None => templateEngine.layout("/index.jade")
    }
  }

  post("/event/new") {
    sessionAuth.get(session.getId) match {
      case Some(user) => new EventController(templateEngine, apiKey, scheduler, user).newTemplate(params)
      case None => halt(403)
    }
  }

  post("/event/create") {
    sessionAuth.get(session.getId) match {
      case Some(user) => new EventController(templateEngine, apiKey, scheduler, user).create(params)
      case None => halt(403)
    }
  }

  post("/event/remove") {
    sessionAuth.get(session.getId) match {
      case Some(user) => new EventController(templateEngine, apiKey, scheduler, user).remove(params)
      case None => halt(403)
    }
  }

  get("/logout") {
    sessionAuth.get(session.getId) match {
      case Some(user) => sessionAuth -= session.getId
      case None =>
    }
    session.invalidate()
    redirect("/")
  }

  get("/login") {
    sessionAuth.get(session.getId) match {
      case None => {
        val discoveries = manager.discover("http://login.telldus.com")
        val discovered = manager.associate(discoveries)
        session.setAttribute("discovered", discovered)
        val authReq = manager.authenticate(discovered, makeUrl("/authenticated"))
        val fetch = SRegRequest.createFetchRequest()
        fetch.addAttribute("email",true)
        fetch.addAttribute("fullname", true)
        authReq.addExtension(fetch)
        redirect(authReq.getDestinationUrl(true))
      }
      case Some(user) => redirect("/")
    }
  }

  get("/authenticated") {
    val openidResp = new ParameterList(request.getParameterMap)
    val discovered = session.getAttribute("discovered").asInstanceOf[DiscoveryInformation]
    val receivingURL = request.getRequestURL
    val queryString = request.getQueryString
    if (queryString != null && queryString.length() > 0)
      receivingURL.append("?").append(request.getQueryString)
    val verification = manager.verify(receivingURL.toString, openidResp, discovered)
    val identity = verification.getVerifiedId
    if (identity != null) {
      val authSuccess = verification.getAuthResponse.asInstanceOf[AuthSuccess]
      if (authSuccess.hasExtension(SRegMessage.OPENID_NS_SREG)){
        val sregResp = authSuccess.getExtension(SRegMessage.OPENID_NS_SREG).asInstanceOf[SRegResponse]
        val email = sregResp.getAttributeValue("email")
        val fullName = sregResp.getAttributeValue("fullname")

        LOG.info("id: {}", identity.getIdentifier)
        LOG.info("email: {} fullName: {}", email, fullName)

        sessionAuth += (session.getId -> AuthUser(identity.getIdentifier, None, email, fullName))

        AccessTokenDao.findOneByID(id = identity.getIdentifier) match {
          case Some(accessToken) => {
            LOG.debug("found existing accesstoken: {}", accessToken)
            sessionAuth(session.getId) = AuthUser(identity.getIdentifier, Some(accessToken), email, fullName)
            redirect("/")
          }
          case None => {
            LOG.debug("no accesstoken for user")
            redirect("/authorize")
          }
        }
      }
    } else
      halt(403)
  }
  
  get("/authorize") {
    LOG.debug("authorize")
    sessionAuth.get(session.getId) match {
      case Some(user) => {
        val (requestToken, authorizeUrl) = new AccessTokenRequester().makeAuthorizeUrl(apiKey, makeUrl("/authorized"))
        LOG.debug("authorizeUrl={}", authorizeUrl)
        LOG.debug("requestToken1: {}", requestToken)
        session.setAttribute("requestToken", requestToken)
        redirect(authorizeUrl)
      }
      case None => halt(403)
    }
  }
  
  get("/authorized") {
    LOG.debug("authorized")
    sessionAuth.get(session.getId) match {
      case Some(user) => {
        val requestToken = session.getAttribute("requestToken").asInstanceOf[Token]
        LOG.debug("requestToken2: {}", requestToken)
        val accessToken = new AccessTokenRequester().getAccessToken(apiKey, requestToken)
        LOG.debug("accessToken: {}", accessToken)

        val token = AccessToken(identifier = user.identity,
                                value = accessToken.value,
                                secret = accessToken.secret)
        AccessTokenDao.insert(token)

        sessionAuth(session.getId) = AuthUser(user.identity, Some(token), user.email, user.fullName)

        redirect("/")
      }
      case None => halt(403)
    }
  }

  def makeUrl(path: String): String = {
    request.getScheme + "://" + request.getServerName + ":" + request.getServerPort + path
  }

}