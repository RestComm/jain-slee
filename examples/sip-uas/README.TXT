This is an example application that can be used for a simple call setup (terminated by AS after 60 sec) load test.

Do "mvn install" to build and deploy, "mvn clean" to uninstall, $JBOSS_HOME env varibale must be set or manuallly provided through -Djboss.home=...

In sipp directory there is the log4j config file, which should be copied to $JBOSS_HOME/server/default/conf.

To run the test simply start the server, and once the app is activated execute run.sh/run.bat in sipp directory (requires sipp in OS PATH).

Have fun.