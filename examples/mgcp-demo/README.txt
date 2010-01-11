Description:
This example is to show case the features supported by Mobicents Media Server(MMS) 2.x.y. The MMS can either be embedded in JBoss AS or can be started as standalone. Refer to MMS document to know more about this. The example is acting as Media Gateway Controller controlling the Media Gateway (Mobicents Media Server) using the industry standard protocol Media Gateway Control Protocol (MGCP - RFC 3435)

Once the example is deployed and MMS is started dial following numbers to test respective functionalities.

1) 2010 : This is to test the IVR feature of MMS. User is requested to press the DTMF and respective announcement should be played by MMS
2) 2011 : This is to test the Recording feature of MMS. 
3) 2012 : This is to test the Conference feature of MMS. Once dialed in, user should get a song pplayed as one of the legs of conference. Other SIP UA can dial 2012 and join same conference and they can talk to each other.
4) 2013 : This is to test the Text-to-Speech (TTS) feature of MMS. User is requested to press the DTMF and respective TTS should be played by MMS

Requirements:
This example depends on the Mobicents SIP Resource Adaptor and MGCP Resource Adaptor ensure it is installed.

Install/Uninstall:
To install/uninstall mgcp-demo and its dependencies, use the ant script "build.xml":

- ant deploy-all, deploys sip ra, mgcp ra and the example DU
- ant undeploy-all, undeploys sip ra, mgcp ra and the example DU

