This is quick kick start guide. Please follow:

1. Configure JTA resources
 * set in jbossts-properties.xml file property as follows:
 	<properties depends="arjuna" name="jta">
        ....
		<property name="com.arjuna.ats.jta.allowMultipleLastResources" value="true"/>
    </properties>
2. Upload to local maven repo jboss-el, version 1.0-CR4+ - Beta and 2.x are not good. RC5 can be found in seam binary.  
 