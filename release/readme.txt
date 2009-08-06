
========================================================
! Welcome to Mobicents - The Open Source JAIN SLEE     !
========================================================

Mobicents is the First and Only Certified Open Source implementation of JAIN-SLEE.  

Mobicents home page: http://www.mobicents.org

This is the release bundle of Mobicents.

Directory Structure
-------------------

There are 3 top-level directories:

 +jboss-5.X.Y.GA	(JBoss AS with the JAIN SLEE container deployed)
 +resources		(various resource adaptors and related scripts)
 +examples		(various examples)
 
 
How to use?
-----------

(*) Starting the server - run this from the command line:
 jboss-5.X.Y.GA/bin/run.sh  (UNIX)
 jboss-5.X.Y.GA\bin\run.bat (Windows)

(*) Deploy and undeploy Resource Adaptors

Make sure that you have Apache Ant 1.7 installed and configured.

If you want to deploy with the mobicents DU deployer in the Application Server run this:
ant -f resources/<radir>/build.xml deploy (or undeploy)

As an alternative, if the Application Server is in a remote host or you prefer a non persistent deploy, you can use JMX:
ant -f resources/<radir>/build.xml deploy-jmx (or undeploy-jmx)
Note that JMX host and port can be configured in the build.xml script, through properties jnpHost and jnpPort.


(*) To deploy and run examples 

Make sure that you have Apache Ant 1.7 installed and configured.

Simply run this script and the deployment should start:
ant -f examples/<exampledir>/build.xml deploy-all

Again there are also targets to deploy/undeploy via JMX (deploy-all-jmx/undeploy-all-jmx). 

Take a look at this page for additional information:
http://groups.google.com/group/mobicents-public/web