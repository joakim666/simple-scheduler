Simple-Scheduler
----------------

This is a scheduler for the [Telldus Live!](http://live.telldus.com/) API written in Scala.

**Telldus Live!** is an easy way to control gear controlled with a [TellStick](http://www.telldus.se/products/tellstick)
using the internet. **Simple-Scheduler** is a scheduler with a web interface that uses the Telldus Live! to turn on and off devices
at given times.

Simple-Scheduler has a built in web server with an interface designed for mobile devices. This makes it easy to start (just **java -jar ...**)
and simple to use from your phone or tablet.

Scroll down for a screenshot.

### Used technologies and tools

* [Scala]((http://www.scala-lang.org/) the language it's written in
* [Simple Build Tool](http://code.google.com/p/simple-build-tool/) for building
* [Jetty](http://jetty.codehaus.org/jetty/) as the embedded web server
* [jQuery Mobile](http://jquerymobile.com/) for the mobile interface
* [Scalatra](https://github.com/scalatra/scalatra) as the web framework
* [Scalate](http://scalate.fusesource.org/) as the template engine
* [MongoDB](http://www.mongodb.org/) as the database to save the scheduled events in
* [Salat](https://github.com/novus/salat) for Scala case class to MongoDB mapping
* [openid4java](http://code.google.com/p/openid4java/) for authentication against the Telldus OpenID server
* [dispatch-oauth](http://databinder.net/dispatch-doc/dispatch/oauth/package.html) for OAUTH authorization against the Telldus API
* [Quartz](http://quartz-scheduler.org/) as the scheduler


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

### Start the scheduler

Start the scheduler with the key and secret retrieved in step 2 above as the paramters

    java -jar simple-scheduler.jar <key> <secret>

### That's it!

Once started the web interface is available at **http://localhost:9000**


### Screenshot

![Screenshot of iPad using the schedular](https://raw.github.com/joakim666/simple-scheduler/master/screenshots/iPad_screenshot.jpg)


### Feedback

Comments and improvements are very welcome!
