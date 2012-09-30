=================================================================
! Welcome to Telscale - The Open Source Carrier Grade JAIN SLEE !
=================================================================

Telscale JAIN SLEE is the First and Only Certified Open Source implementation of JAIN-SLEE 1.1, carrier grade and with professional support.  

Telscale JAIN SLEE home page: http://www.telestax.com

This is the binary release of Telscale JAIN SLEE 6.x.

Directory Structure
-------------------

 +--- jboss-5.X.Y.GA			(JBoss AS with the JAIN SLEE container deployed)
 +--- resources				(JAIN SLEE Resource Adaptors and related scripts)
 +--- enablers				(JAIN SLEE Application Enablers)
 +--- examples				(JAIN SLEE Application Examples)
 +--- tools/jopr-plugin		(Jopr Management Console)
 +--- tools/remote-slee-connection	(Remote SLEE Connection Tool)
 +--- tools/twiddle			(Twiddle Command Line Interface)
 +--- tools/eclipslee			(EclipSLEE Plugin)
 +--- extra/telscale-diameter		(Telscale Diameter Mux, required by Diameter RAs)
 +--- extra/telscale-ss7		(Telscale SS7, required by MAP RA)
 +--- extra/sip-balancer		(Telscale SIP Load Balancer)
 
Quick start
-----------

(*) Starting the server - run this from the command line:
 
 jboss-5.X.Y.GA/bin/run.sh  (UNIX)
 jboss-5.X.Y.GA\bin\run.bat (Windows)

(*) Deploy and undeploy Resource Adaptors

Make sure that you have Apache Ant 1.7 or later installed and configured.

If you want to deploy with the persistent DU deployer in the Application Server run this:
ant -f resources/<ra.dir>/build.xml

(*) To deploy and run Examples 

Make sure that you have Apache Ant 1.7 installed and configured.

Simply run this script and the deployment should start:
ant -f examples/<example.dir>/build.xml

EclipSLEE Plugin
----------------
Simply copy the jar into the plugins dir inside Eclipse IDE and restart it.

Documentation
-------------
Complete server documentation can be found in docs directory.