Description:
This example provides basic sip services, such as proxy, location and registrar services.

Requirements:
This example depends on the Mobicents SIP Resource Adaptor, ensure it is installed.

Install/Uninstall:
SIP Services can be deployed in two modes, with the proxy service reacting to SIP Invite messages or not (useful for other examples).
As a consequence of not having a single deploayble unit none is installed when "mvn install" is called. 
You need to *additionally* go to proxy-without-initial-invite-services-DU or proxy-with-initial-invite-services-DU directory and do "mvn install" too.
Uninstall is "mvn clean" where "mvn install" was invoked.

To install/uninstall sip-services and its dependencies, use the ant script "build.xml":

- ant deploy-all, deploys sip ra and proxy-with-initial-invite-services-DU
- ant undeploy-all, undeploys sip ra and proxy-with-initial-invite-services-DU
- ant deploy-all-without-initial-invite, deploys sip ra and proxy-without-initial-invite-services-DU
- ant undeploy-all-without-initial-invite, undeploys sip ra and proxy-without-initial-invite-services-DU