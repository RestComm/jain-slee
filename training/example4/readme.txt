Before any example is deployed make sure that LabRA is deployed. 


Training Example4
In this example we will see how SBB Activity Context Interface are implemented to use the shared attributes.
In this example there are two services and two SBB's
For 1st ANY event received first FirstBounceSbb bounces the message
For 2nd ANY event received first SecondBounceSbb bounces the message  
For 3rd ANY event received first FirstBounceSbb bounces the message
---
---
----

This is possible as we store the state in shared attribute as who has bounced the message and who should next bounce it

Also see how slee library component is used
