Simple-Scheduler
----------------

This is a simple scheduler for the [Telldus Live!](http://live.telldus.com/) API written in Scala.

**Telldus Live!** is an easy way to control gear controlled with a [TellStick](http://www.telldus.se/products/tellstick)
using the internet. **Simple-Scheduler** is a simple scheduler that uses the Telldus Live! to turn on and off devices
at given times. This makes it possible for the scheduler to run on a different machine than the one where the TellStick
is connected.

### Example of configuration

    Scheduler.add(new TurnOn(Devices.OUTDOOR_LIGHTS, info), JobSchedule.cron("0 0 19 * * *")) // @19:00
    Scheduler.add(new TurnOn(Devices.HOME_OFFICE, info), JobSchedule.cron("1 0 19 * * *")) // @19:00
    Scheduler.add(new TurnOn(Devices.LIVING_ROOM_HIGH_LAMP, info), JobSchedule.cron("2 0 19 * * *")) // @19:00

### Requirements

* [Telldus TellStick](http://www.telldus.se/products/tellstick)
* TelldusCenter installed and configured
* [Scala](http://www.scala-lang.org/)
* [Simple Build Tool - sbt](http://code.google.com/p/simple-build-tool/)

### How to get started

1. Active Tellus Live! in your TelldusCenter by following the instructions [here](http://live.telldus.com/help/activate).
2. Go to [https://api.telldus.com/explore/devices/list](https://api.telldus.com/explore/devices/list) select JSON and
  press 'Send'. Update the **Devices.scala** file with names and ids of your devices.
3. Update the scheduler configuration in the **Main.scala** file.
4. Go to [https://api.telldus.com/keys/generatePrivate](https://api.telldus.com/keys/generatePrivate) to generate
  a private key used to identify and authorized the scheduler. The key will be used on the command line when starting
  the scheduler.
5. Go to the root of the project and do **sbt proguard**. This will compile and package the scheduler as one big jar file.

### The first time

For the scheduler to be able to turn on and off devices it needs an access token. Once it has an access token it can be
reused. Get an access token by starting the scheduler like this:

    java -jar simple-scheduler_2.8.1-0.1.min.jar <public key> <private key>

Follow the prompts and the access token and all the command line arguments needed to start the scheduler will be
printed.

Save the public key, private key, access token and access token secret.

### Start the scheduler

Now that you have an access token it's time to start the scheduler.

    java -jar simple-scheduler_2.8.1-0.1.min.jar <public key> <private key> <access token> <access token secret>

### That's it!

Comments and improvements are very welcome!
