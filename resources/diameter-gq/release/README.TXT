=======================================================================
 Mobicents Diameter : Gq' Resource Adaptor
=======================================================================
                           v@gq_VERSION@

 Welcome to the Mobicents Diameter Gq' Resource Adaptor v@gq_VERSION@ release!

--------------
 Introduction
--------------

 This is the Gq' RA release of Mobicents Diameter solution.

 This RA provides Gq' interface application (3GPP TS 29.210) implementation of Diameter for JAIN SLEE and depends on the Mobicents
 Diameter Mux, Base RA and Credit-Control (CCA) RA to be deployed before using it.

 The RA provides a framework for the Gq' reference point, which is used for provisioning service data flow based charging rules.

 The reference point is located between the Traffic Plane Function (TPF) and the Charging Rules Function (CRF), also known as 
 Service Data Flow Based Charging Rules Function. 

------------------
 Release Contents
------------------

  diameter-gq-ra-DU-@gq_VERSION@.jar (the JAIN SLEE Deployable Unit to be installed in Mobicents)

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
