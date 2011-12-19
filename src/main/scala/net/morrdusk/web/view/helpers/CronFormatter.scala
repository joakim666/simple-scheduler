package net.morrdusk.web.view.helpers

object CronType extends Enumeration {
  type CronType = Value
  val Minute, Hour, Day, Week, Month, Year, Unknown = Value
}

import CronType._
import org.slf4j.LoggerFactory

object CronFormatter {
  val LOG = LoggerFactory.getLogger(getClass)

  def englishDescription(cron: String): String = {
    LOG.info("in englishDescription")
    val split = cron.split(" ")
    LOG.info("split: {}", split)
    val cronType = getType(split)
    LOG.info("type: {}", cronType)
    val res = "every " +
      (cronType match {
        case Minute =>
          "minute"
        case Hour =>
          LOG.info("hour")
          "hour at " + split(1) + " minutes past the hour"
        case Day =>
          LOG.info("day")
          "day at " + formatTimeInt(split(2)) + ":" + formatTimeInt(split(1))
        case Week =>
          LOG.info("week")
          "week on " + formatDayOfWeek(split(5).toInt) + " at " + formatTimeInt(split(2)) + ":" + formatTimeInt(split(1))
        case Month =>
          LOG.info("month")
          "month on day " + split(3) + " at " + formatTimeInt(split(2)) + ":" + formatTimeInt(split(1))
        case Year =>
          LOG.info("year")
          formatMonth(split(4).toInt) + " on day " + split(3) + " at " + formatTimeInt(split(2)) + ":" + formatTimeInt(split(1))
        case _ =>
          cron

      })
    res
  }

  protected

  def formatTimeInt(c: String): String = {
    if (c.toInt < 10)
      "0" + c
    else
      c.toString
  }

  def formatDayOfWeek(i: Int): String = {
    val weekDays = List("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
    weekDays(i-1)
  }

  def formatMonth(i: Int): String = {
    val monthNames = List("Jan", "Feb", "Mar", "Apr", "May", "Jun",
      "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
    monthNames(i-1)
  }

  def isMinute(cron: Array[String]): Boolean = {
    cron(1) == "*" && cron(2) == "*" && cron(3) == "*" &&
      cron(4) == "*" && cron(5) == "?"
  }

  def isHour(cron: Array[String]): Boolean = {
    cron(1) != "*" && cron(2) == "*" && cron(3) == "*" &&
      cron(4) == "*" && cron(5) == "?"
  }

  def isDay(cron: Array[String]): Boolean = {
    cron(1) != "*" && cron(2) != "*" && cron(3) == "*" &&
      cron(4) == "*" && cron(5) == "?"
  }

  def isWeek(cron: Array[String]): Boolean = {
    cron(1) != "*" && cron(2) != "*" && cron(3) == "?" &&
      cron(4) == "*" && cron(5) != "?"
  }

  def isMonth(cron: Array[String]): Boolean = {
    cron(1) != "*" && cron(2) != "*" && cron(3) != "*" &&
      cron(4) == "*" && cron(5) == "?"
  }

  def isYear(cron: Array[String]): Boolean = {
    cron(1) != "*" && cron(2) != "*" && cron(3) != "*" &&
      cron(4) != "*" && cron(5) == "?"
  }

  def getType(cron: Array[String]): CronType = {
    if (isMinute(cron)) {
      return Minute
    }
    else if (isHour(cron)) {
      return Hour
    }
    else if (isDay(cron)) {
      return Day
    }
    else if (isWeek(cron)) {
      return Week
    }
    else if (isMonth(cron)) {
      return Month
    }
    else if (isYear(cron)) {
      return Year
    }
    Unknown
  }
}