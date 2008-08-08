
========================================================
! Welcome to Mobicents - The Open Source SLEE          !
========================================================

Mobicents is the First and Only Certified Open Source implementation of JSLEE.  

Mobicents home page: http://www.mobicents.org

This is the release bundle of Mobicents.

Directory Structure
-------------------

There are 4 top-level directories:

 +jboss-4.X.Y.GA	(JBossAS, the JAIN SLEE container, Sip Servlets container, Media Server and other base services)
 +resources			(various resource adaptors and related scripts)
 +examples			(various examples)
 
 
How to use?
-----------

(*) Starting the server - run this from the command line:
 jboss-4.X.Y.GA/bin/run.sh  (UNIX)
 jboss-4.X.Y.GA\bin\run.bat (Windows)
 
 To verify that the SLEE server started successfully, open your favorite web browser and point it to
  http://localhost:8080/management-console
 You should see a nice web application indicating that Mobicents is Running. 
 There is a lot more to the Management Console; look around and see what's in store for you.

(*) Deploy and undeploy Resource Adaptors

Make sure that you have Apache Ant 1.7 installed and configured.
If the Application Server is running you can use JMX to deploy directly:
ant -f resources/<radir>/build.xml deploy-jmx (or undeploy-jmx)

If you want to deploy with the mobicents DU deployer in the Application Server run this:
ant -f resources/<radir>/build.xml deploy (or undeploy)

(*) To deploy and run examples 

Make sure that you have Apache Ant 1.7 installed and configured.
Simply run this script and the deployment should start:
ant -f examples/<exampledir>/build.xml deploy-all

Take a look at this page for additional information:
http://groups.google.com/group/mobicents-public/web