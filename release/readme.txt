========================================================
! Welcome to Mobicents - The Open Source JAIN SLEE     !
========================================================

Mobicents is the First and Only Certified Open Source implementation of JAIN-SLEE 1.1.  

Mobicents home page: http://www.mobicents.org

This is the binary release of Mobicents JAIN SLEE 2.x.

Directory Structure
-------------------

 +--- jboss-5.X.Y.GA			(JBoss AS with the JAIN SLEE 2.x container deployed)
 +--- resources					(JAIN SLEE Resource Adaptors and related scripts)
 +--- examples					(JAIN SLEE Application Examples)
 +--- media-server  			(Mobicents Media Server Standalone, required by some examples)
 +--- tools/management-console	(JOPR Management Console)
 
Quick start
-----------

(*) Starting the server - run this from the command line:
 
 jboss-5.X.Y.GA/bin/run.sh  (UNIX)
 jboss-5.X.Y.GA\bin\run.bat (Windows)

(*) Deploy and undeploy Resource Adaptors

Make sure that you have Apache Ant 1.7 installed and configured.

If you want to deploy with the mobicents DU deployer in the Application Server run this:
ant -f resources/<radir>/build.xml deploy (or undeploy)

(*) To deploy and run Examples 

Make sure that you have Apache Ant 1.7 installed and configured.

Simply run this script and the deployment should start:
ant -f examples/<exampledir>/build.xml deploy-all

Documentation
-------------
Complete server documentation can be found in docs directory, other components documentation are in the component's directory.