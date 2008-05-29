This folder contains a draft of the provider-api source.
It is generated from the Parlay 4.2 UML model.


Notes:
======
- Current base namespace is org.mobicents.csapi.jr.resource. If this is to
  be proposed as a realisation annex to the Parlay Joint Working Group then
  this namespace would be changed to org.csapi.jr.slee.

- TODO There are javac warning at compilation like 
       mobicents-parlay-ra/source/provider-api/src/java/org/mobicents/csapi/jr/slee/cm/IpVPrPConnection.java:51:
                     warning: unmappable character for encoding UTF8
  perhaps because there are not UTF-8 characters in teh documantation in the ROSE mode.  
  TODO Also the carriage returns in comments are not mapped correctly dos2unix problem ?
                       

- Earlier versions were handcrafted from a Parlay 3.2 realisation, so there were minor errors 
  present. Initial commit was to to gather comments on general patterns, naming conventions and mapping
  rules used in the API.