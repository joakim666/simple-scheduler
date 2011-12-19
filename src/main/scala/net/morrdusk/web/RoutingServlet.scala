package net.morrdusk.web

import controller.{EventController, DeviceController}
import org.scalatra.{UrlSupport, ScalatraServlet}
import org.scalatra.scalate.ScalateSupport
import com.mongodb.casbah.commons.conversions.scala.RegisterJodaTimeConversionHelpers._
import net.morrdusk.scheduling.JobScheduler
import org.fusesource.scalate.layout.DefaultLayoutStrategy

class RoutingServlet(info: List[String], scheduler: JobScheduler) extends ScalatraServlet with UrlSupport with ScalateSupport {
  protected def contextPath = request.getContextPath

  before() {
    contentType = "text/html"
    templateEngine.layoutStrategy = new DefaultLayoutStrategy(templateEngine, "/layouts/default.jade")
  }

  get("/") {
    new DeviceController(templateEngine, info).index()
  }

  post("/event/new") {
    new EventController(templateEngine, info, scheduler).newTemplate(params)
  }

  post("/event/create") {
    new EventController(templateEngine, info, scheduler).create(params)
  }

  post("/event/remove") {
    new EventController(templateEngine, info, scheduler).remove(params)
  }

}