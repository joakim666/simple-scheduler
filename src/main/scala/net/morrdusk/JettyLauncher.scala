package net.morrdusk

import model.EventDao
import org.mortbay.jetty.Server
import request.AccessTokenRequester
import scheduling.JobScheduler
import web.RoutingServlet

import org.mortbay.jetty.servlet.{Context, ServletHolder}
import org.mortbay.jetty.handler.ResourceHandler
import org.slf4j.LoggerFactory
import com.mongodb.casbah.commons.MongoDBObject

object JettyLauncher {
  val LOG = LoggerFactory.getLogger(getClass)

  val DEVELOPMENT = 0
  val PRODUCTION = 1

  def scheduleAllSavedEvents(scheduler: JobScheduler) {
    LOG.info("Scheduling all saved events")
    val events = EventDao.findAll()
    events.foreach(event => {
      scheduler.schedule(event)
    })
  }

  def main(args: Array[String]) {
    if (args.length == 2) {
      val accessToken = new AccessTokenRequester().requestAccessToken(args(0), args(1))
      println
      println("Your access token is: " + accessToken.value + " " + accessToken.secret)
      println
      println("The command line parameters to use: " + args(0) + " " + args(1) + " " + accessToken.value + " " + accessToken.secret)
      
      System.exit(0)
    }

    
    
    val info = List(args(0), args(1), args(2), args(3))

    if (System.getProperty("environment") == "development") {
      System.setProperty("org.scalatra.environment", "development")

      /**
       * Run with ~ products in sbt and changes made to templates will be
       * automatically reloaded
       */
      System.setProperty("scalate.allowReload", "true")
      System.setProperty("scalate.allowCaching", "true")
    }
    else {
      System.setProperty("org.scalatra.environment", "production")
      System.setProperty("scalate.allowReload", "false")
      System.setProperty("scalate.allowCaching", "false")
    }

    val scheduler = JobScheduler(info)
    scheduleAllSavedEvents(scheduler)

    val server = new Server(9000)

    val static = new Context(server, "/static", Context.NO_SESSIONS)
    static.setHandler(new ResourceHandler)
    static.setResourceBase(JettyLauncher.getClass.getClassLoader.getResource("static").toExternalForm)

    val root = new Context(server, "/", Context.SESSIONS)
    root.addServlet(new ServletHolder(new RoutingServlet(info, scheduler)), "/*")
    server.start()
    server.join()
  }
}