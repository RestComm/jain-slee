Description:
This example echo's back the user voice/video (H263) and depends on Mobicents Media Server (MMS) 2.x.y. The MMS can either be embedded in JBoss AS or can be started as standalone. Refer to MMS document to know more about this. Once the example is deployed and MMS is started dial any numbers to test.

Requirements:
This example depends on the Mobicents SIP Resource Adaptor and MGCP Resource Adaptor ensure it is installed.

Install/Uninstall:
To install/uninstall mgcp-demo and its dependencies, use the ant script "build.xml":

- ant deploy-all, deploys sip ra, mgcp ra and the example DU
- ant undeploy-all, undeploys sip ra, mgcp ra and the example DU

