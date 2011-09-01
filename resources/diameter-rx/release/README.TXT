=======================================================================
 Mobicents Diameter : Rx Resource Adaptor
=======================================================================
                           v@rx_VERSION@

 Welcome to the Mobicents Diameter Rx Resource Adaptor v@rx_VERSION@ release!

--------------
 Introduction
--------------

 This is the Rx RA release of Mobicents Diameter solution.

 This RA provides Rx interface application (3GPP TS 29.214) implementation of Diameter for JAIN SLEE and depends on the Mobicents
 Diameter Mux and Base RA

 The RA provides a framework for the Rx reference point, which is used for authorization sessions

 The reference point is located between the Application Function (AF) and the Policy and Charging Rules Function (PCRF).

------------------
 Release Contents
------------------

  diameter-rx-ra-DU-@rx_VERSION@.jar (the JAIN SLEE Deployable Unit to be installed in Mobicents)

  du-management.xml (the auxiliar build file for managing deployment/undeployment)

  build.xml (the ant build file for deploying/undeploying the RA)

----------------
 How to Use it?
----------------

 In order to deploy/undeploy and correctly activate/deactivate the Resource Adaptor, you can:

 1) Simply copy the DU jar to the deploy folder of the JBoss running Mobicents or delete it from the deploy folder to
    uninstall it;

 2) Use the provided build.xml file and call the desired targets:
    - deploy/deploy-jmx for hard deploying or deploying via JMX interface;
    - undeploy/undeploy-jmx for hard undeploying or undeploying via JMX interface;

------------------
 More Information
------------------

 For more detailed information, please visit the following sites:

  » Mobicents Diameter Page (http://www.mobicents.org/diameter)

  » Mobicents Google Group, for posting questions (http://groups.google.com/group/mobicents-public)

  » Mobicents Homepage (http://www.mobicents.org)
