
resolvers += "zentrope" at "http://zentrope.com/maven"

resolvers += "gseitz@github" at "http://gseitz.github.com/maven/"

addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.7.2")

addSbtPlugin("com.zentrope" %% "xsbt-scalate-precompile-plugin" % "1.6")

addSbtPlugin("com.github.gseitz" % "sbt-release" % "0.4")

