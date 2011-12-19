Simple-Scheduler
----------------

This is a simple scheduler for the [Telldus Live!](http://live.telldus.com/) API written in Scala.

**Telldus Live!** is an easy way to control gear controlled with a [TellStick](http://www.telldus.se/products/tellstick)
using the internet. **Simple-Scheduler** is a simple scheduler that uses the Telldus Live! to turn on and off devices
at given times. This makes it possible for the scheduler to run on a different machine than the one where the TellStick
is connected.

Simple-Scheduler uses an embedded [jetty](http://jetty.codehaus.org/jetty/) server to present a [jQuery mobile](http://jquerymobile.com/)
interface, that makes it simple to add, remove and view events from an iPad or iPhone.

Scroll down for a screenshot.

### Disclaimer

I am still learning Scala and my code is far from perfect. I don't mind suggestions on how to make it better.

### Requirements

* [Telldus TellStick](http://www.telldus.se/products/tellstick)
* TelldusCenter installed and configured
* [Scala](http://www.scala-lang.org/) >= 2.9.1
* [Simple Build Tool - sbt](http://code.google.com/p/simple-build-tool/) >= 0.11.1
* [MongoDB](http://www.mongodb.org/) installed and running on the machine that will run the scheduler

### How to get started

1. Active Tellus Live! in your TelldusCenter application by following the instructions [here](http://live.telldus.com/help/activate).
2. Go to [https://api.telldus.com/keys/generatePrivate](https://api.telldus.com/keys/generatePrivate) to generate
  a private key used to identify and authorized the scheduler. The key will be used on the command line when starting
  the scheduler.
3. Go to the root of the project and do **sbt assembly**. This will compile and package the scheduler as one big jar file.

### The first time

For the scheduler to be able to turn on and off devices it needs an access token. Once it has an access token it can be
reused. Get an access token by starting the scheduler like this:

    java -jar simple-scheduler.jar <public key> <private key>

Follow the prompts and the access token and all the command line arguments needed to start the scheduler will be
printed.

Save the public key, private key, access token and access token secret.

### Start the scheduler

Now that you have an access token it's time to start the scheduler.

    java -jar simple-scheduler.jar <public key> <private key> <access token> <access token secret>

### That's it!

The web interface is available at [http://localhost:9000]


### Screenshot

![Screenshot of iPad using the schedular](https://raw.github.com/joakim666/simple-scheduler/master/screenshots/iPad_screenshot.jpg)


### Feedback

Comments and improvements are very welcome!
