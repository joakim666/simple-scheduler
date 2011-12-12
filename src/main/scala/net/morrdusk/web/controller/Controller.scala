package net.morrdusk.web.controller

import org.fusesource.scalate.TemplateEngine

abstract class Controller(templateEngine: TemplateEngine) {

  def render(templateFileName: String, values: Map[String,Any]) {
    templateEngine.layout("/" + templateFileName, values)
  }
}