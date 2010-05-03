Configuring SS7 requries few properties to be set.
For explanation please see: 
* http://groups.google.com/group/mobicents-public/web/java-call-control-jcc-resource-adaptorov
* http://groups.google.com/group/mobicents-public/web/mms-ss7-support

If one compiles jcc from source put 
* jcc-default-ra.properties into jcc/du/src/main/resources
* sccp.properties into jcc/jcc-camel-provider/java/src/main/resources/org/mobicents/ss7/sccp


DU structure should look as follows(sccp.properties are hidden in provider jar)

`jar -ft jcc-ra-DU-1.2.7.GA-SNAPSHOT.jar`
 
META-INF/
META-INF/MANIFEST.MF
jcc-events-1.2.7.GA-SNAPSHOT.jar
jcc-ratype-1.2.7.GA-SNAPSHOT.jar
jcc-ra-1.2.7.GA-SNAPSHOT.jar
library/
library/jcc-1.1.jar
library/mms-impl-2.0.0.BETA3-SNAPSHOT.jar
library/jcc-camel-1.2.7.GA-SNAPSHOT.jar
META-INF/deploy-config.xml
META-INF/deployable-unit.xml
jcc-default-ra.properties
META-INF/maven/
META-INF/maven/org.mobicents.resources/
META-INF/maven/org.mobicents.resources/jcc-ra-DU/
META-INF/maven/org.mobicents.resources/jcc-ra-DU/pom.xml
META-INF/maven/org.mobicents.resources/jcc-ra-DU/pom.properties



   