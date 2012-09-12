========================================================
! Welcome to Mobicents - The Open Source JAIN SLEE     !
========================================================

Mobicents is the First and Only Certified Open Source implementation of JAIN-SLEE 1.1.  

Mobicents home page: http://www.mobicents.org

This is the binary release of Mobicents JAIN SLEE 2.x.

Directory Structure
-------------------

 +--- jboss-5.X.Y.GA				(JBoss AS with the JAIN SLEE 2.x container deployed)
 +--- resources						(JAIN SLEE Resource Adaptors and related scripts)
 +--- enablers						(JAIN SLEE Application Enablers)
 +--- examples						(JAIN SLEE Application Examples)
 +--- tools/jopr-plugin		(JOPR Management Console)
 +--- tools/remote-slee-connection	(Remote SLEE Connection Tool)
 +--- tools/twiddle					(Twiddle Command Line Interface)
 +--- tools/eclipslee				(EclipSLEE Plugin)
 +--- extra/mobicents-media-server  (Mobicents Media Server Standalone, required by some examples, MGCP and MSControl RAs)
 +--- extra/mobicents-diameter		(Mobicents Diameter Mux, required by Diameter RAs)
 +--- extra/mobicents-ss7			(Mobicents SS7, required by JCC and MAP RAs)
 +--- extra/sip-balancer			(Mobicents SIP Load Balancer)
 
Quick start
-----------

(*) Starting the server - run this from the command line:
 
 jboss-5.X.Y.GA/bin/run.sh  (UNIX)
 jboss-5.X.Y.GA\bin\run.bat (Windows)

(*) Deploy and undeploy Resource Adaptors

Make sure that you have Apache Ant 1.7 or later installed and configured.

If you want to deploy with the mobicents DU deployer in the Application Server run this:
ant -f resources/<radir>/build.xml deploy (or undeploy)

(*) To deploy and run Examples 

Make sure that you have Apache Ant 1.7 installed and configured.

Simply run this script and the deployment should start:
ant -f examples/<exampledir>/build.xml deploy-all

EclipSLEE Plugin
----------------
Simply copy the jar into the plugins dir inside Eclipse IDE and restart it.

Documentation
-------------
Complete server documentation can be found in docs directory.