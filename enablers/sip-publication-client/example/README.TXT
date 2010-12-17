Simple example to illustrate how to use enabler. It can run against Mobicents Presence Server or again provided sipp script.
Refer to MSPS manual for information how to configure/deploy Mobicents Presence Server.
Example upon service startup, issues new publication request. In case of undeploy, it issues remove request.
Example does not perform REFRESH requests, since Enabler does this.

1. Deploy
  - deploy SIP RA(sip11)
  - deploy enabler
  - deploy example
2. Run sipp script(or start MSPS).
3. Observe.
4. Undeploy example, to make it remove publication.
