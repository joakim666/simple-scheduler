import net.morrdusk.action.{TurnOff, TurnOn}
import net.morrdusk.request.AccessTokenRequester

object Main {

  /*def main(args: Array[String]) {
    if (args.length == 2) {
      val accessToken = new AccessTokenRequester().requestAccessToken(args(0), args(1))
      println
      println("Your access token is: " + accessToken.value + " " + accessToken.secret)
      println
      println("The command line parameters to use: " + args(0) + " " + args(1) + " " + accessToken.value + " " + accessToken.secret)
    }
    else if (args.length == 4) {
      startScheduler(args(0), args(1), args(2), args(3))
    }
    else {
      println("Usage:")
      println("    To get an access token java -jar ... <public key> <private key>")
      println("    To run the scheduler java -jar ... <public key> <private key> <access token> <access token secret>\n")
    }
  }

  def startScheduler(key: String, secret: String, accessToken: String, accessTokenSecret: String) {
    val info = List(key, secret, accessToken, accessTokenSecret)

    Scheduler.add(new TurnOn(Devices.OUTDOOR_LIGHTS, info), JobSchedule.cron("0 15 19 * * *")) // @19:15
    Scheduler.add(new TurnOn(Devices.HOME_OFFICE, info), JobSchedule.cron("1 15 19 * * *")) // @19:15
    Scheduler.add(new TurnOn(Devices.LIVING_ROOM_HIGH_LAMP, info), JobSchedule.cron("2 15 19 * * *")) // @19:15

    Scheduler.add(new TurnOff(Devices.HOME_OFFICE, info), JobSchedule.cron("0 0 23 * * *")) // @23:00
    Scheduler.add(new TurnOff(Devices.LIVING_ROOM_HIGH_LAMP, info), JobSchedule.cron("1 0 23 * * *")) // @23:00
    Scheduler.add(new TurnOff(Devices.LIVING_ROOM_SOFA_LAMP, info), JobSchedule.cron("2 0 23 * * *")) // @23:00
    Scheduler.add(new TurnOff(Devices.OUTDOOR_LIGHTS, info), JobSchedule.cron("3 0 23 * * *")) // @23:00
  }*/
}