package net.morrdusk

import model.{AccessTokenDao, EventDao}
import request.AccessTokenRequester
import scheduling.JobScheduler
import web.RoutingServlet

import org.slf4j.LoggerFactory
import org.eclipse.jetty.servlet.{ServletContextHandler, ServletHolder}
import org.eclipse.jetty.server.handler.{ContextHandlerCollection, ResourceHandler}
import org.eclipse.jetty.server.{Handler, Server}

class ApiKey(val key: String, val secret: String)

object JettyLauncher {
  val LOG = LoggerFactory.getLogger(getClass)

  val DEVELOPMENT = 0
  val PRODUCTION = 1

  def scheduleAllSavedEvents(scheduler: JobScheduler) {
    LOG.info("Scheduling all saved events")
    val events = EventDao.findAll()
    events.foreach(event => {
      AccessTokenDao.findOneByID(id = event.userIdentifier) match {
        case Some(accessToken) => {
          scheduler.schedule(event, accessToken)
        }
        case None => {
          LOG.warn("Missing access token for event id {} and user identifier {}", event.id, event.userIdentifier)
        }
      }
    })
  }

  def main(args: Array[String]) {
    if (args.length != 2) {
      println("Usage <api key> <api secret>")
    }

    val apiKey = new ApiKey(args(0), args(1))

    if (System.getProperty("environment") == "development") {
      LOG.info("Starting in development mode")
      System.setProperty("org.scalatra.environment", "development")

      /**
       * Run with ~ products in sbt and changes made to templates will be
       * automatically reloaded
       */
      System.setProperty("scalate.allowReload", "true")
      System.setProperty("scalate.allowCaching", "true")
    }
    else {
      LOG.info("Starting in production mode")
      System.setProperty("org.scalatra.environment", "production")
      System.setProperty("scalate.allowReload", "false")
      System.setProperty("scalate.allowCaching", "false")
    }

    val scheduler = JobScheduler(apiKey)
    scheduleAllSavedEvents(scheduler)

    val server = new Server(9000)

    val staticContext = new ServletContextHandler(ServletContextHandler.NO_SESSIONS)
    staticContext.setContextPath("/static")
    staticContext.setHandler(new ResourceHandler)
    staticContext.setResourceBase(JettyLauncher.getClass.getClassLoader.getResource("static").toExternalForm)

    val rootContext = new ServletContextHandler(ServletContextHandler.SESSIONS)
    rootContext.setContextPath("/")
    rootContext.addServlet(new ServletHolder(new RoutingServlet(apiKey, scheduler)), "/*")

    val contexts = new ContextHandlerCollection()
    contexts.setHandlers(Array[Handler](staticContext, rootContext))
    server.setHandler(contexts)

    server.start()
    server.join()
  }
}