To run the SLEE 1.1 tck:
1) Install tck components in the Mobicents JAIN SLEE server invoking "mvn install"
2) Unzip testsuite.zip to any directory of your choice
3) In the extracted testsuite directory do "ant". To run a specific test, such as tests/activities/activitycontext/AutoDetachTest.xml, do "ant -Dtests=tests/activities/activitycontext/AutoDetachTest.xml"

To run tests against security permissions:
1. perform steps 1-3 from above
2. create policy file with similar content:


// ***************************************
// Trusted core Java code
//***************************************
grant codeBase "file:${java.home}/lib/ext/-" {
   permission java.security.AllPermission;
};
grant codeBase "file:${java.home}/lib/-" {
   permission java.security.AllPermission;
};
// For java.home pointing to the JDK jre directory
grant codeBase "file:${java.home}/../lib/-" {
   permission java.security.AllPermission;
};
grant codeBase "file:@JAVA_HOME@/lib/tools.jar" {
	permission java.security.AllPermission;
};



//********************************************
// Trusted core JBoss code (REAL URL Version)
//********************************************
grant codeBase "file:${jboss.defined.home}/bin/-" {
   permission java.security.AllPermission;
};

grant codeBase "file:${jboss.defined.home}/lib/-" {
   permission java.security.AllPermission;
};

grant codeBase "file:${jboss.defined.home}/lib/-" {
   permission java.security.AllPermission;
};

grant codeBase "file:${jboss.defined.home}/common/lib/-" {
   permission java.security.AllPermission;
};

grant codeBase "file:${jboss.defined.home}/server/lib/-" {
   permission java.security.AllPermission;
};

grant codeBase "file:${jboss.defined.home}/server/default/lib/-" {
   permission java.security.AllPermission;
};

grant codeBase "file:${jboss.defined.home}/server/default/deployers/-" {
   permission java.security.AllPermission;
};

grant codeBase "file:${jboss.defined.home}/server/default/work/-" {
   permission java.security.AllPermission;
};

grant codeBase "file:${jboss.defined.home}/server/default/deploy/mobicents.sar/lib/-" {
	permission java.security.AllPermission;
};
grant codeBase "file:${jboss.defined.home}/server/default/deploy/-" {
	permission java.security.AllPermission;


};

//Strictly for tck
grant codeBase "file:${jboss.defined.home}/server/default/deploy/tck-plugin-2.0.0-SNAPSHOT-bean.jar" {
	permission java.security.AllPermission;
};

//*******************************************************
// Trusted Specific JBoss Code (REAL URL Version)
//*******************************************************
grant codeBase "file:${jboss.defined.home}/server/default/deploy/http-invoker.sar/invoker.war/-" {
   permission javax.management.MBeanPermission "*", "addNotificationListener,getAttribute";
   permission java.lang.RuntimePermission "getClassLoader";
};

grant codeBase "file:${jboss.defined.home}/server/default/deploy/jmx-console.war/-" {
   permission java.security.AllPermission;
};

grant codeBase "file:${jboss.defined.home}/server/default/deploy/jmx-remoting.sar/-" {
   permission javax.management.MBeanTrustPermission "register";
   permission java.net.SocketPermission "*", "accept,listen,resolve";
   permission java.lang.RuntimePermission "getClassLoader";
};


grant codeBase "file:${jboss.defined.home}/server/default/deploy/jbossweb.sar/jsf-libs/-" {
   permission java.security.AllPermission;
};

grant codeBase "file:${jboss.defined.home}/server/default/deploy/jboss-local-jdbc.rar/-" {
   permission java.lang.RuntimePermission "getClassLoader";
};

grant codeBase "file:${jboss.defined.home}/server/default/deploy/jboss-local-jdbc.rar!/jboss-local-jdbc.jar" {
   permission java.lang.RuntimePermission "getClassLoader";
};

grant codeBase "file:${jboss.defined.home}/server/default/deploy/management/console-mgr.sar/-" {
   permission javax.management.MBeanTrustPermission "register";
   permission javax.management.MBeanPermission "*", "addNotificationListener,getAttribute";
   permission org.jboss.naming.JndiPermission "<<ALL BINDINGS>>","*";
   permission java.io.FilePermission "<<ALL FILES>>", "read";
   permission java.lang.RuntimePermission "getClassLoader";
};

grant codeBase "file:${jboss.defined.home}/server/default/deploy/uuid-key-generator.sar/-" {
   permission javax.management.MBeanTrustPermission "register";
   permission javax.management.MBeanPermission "*", "getAttribute";
   permission org.jboss.naming.JndiPermission "<<ALL BINDINGS>>","lookup,rebind,unbind";
   permission java.lang.RuntimePermission "getClassLoader";
};

grant codeBase "file:${jboss.defined.home}/server/default/deploy/jbossweb.sar/-" {
   permission java.security.AllPermission;
};

grant codeBase "file:${jboss.defined.home}/server/default/deploy/jms-ra.rar!/jms-ra.jar" {
   permission java.lang.RuntimePermission "setContextClassLoader";
   permission java.lang.RuntimePermission "getClassLoader";
   permission org.jboss.naming.JndiPermission "<<ALL BINDINGS>>","lookup";
   permission java.io.FilePermission "${jboss.defined.home}/lib/jboss-aop.jar", "read";
   permission javax.management.MBeanPermission "*", "getAttribute,invoke,setAttribute";
};

grant codeBase "file:${jboss.defined.home}/server/default/deploy/jms-ra.rar/jms-ra.jar!/" {
   permission java.lang.RuntimePermission "setContextClassLoader";
   permission java.lang.RuntimePermission "getClassLoader";
   permission org.jboss.naming.JndiPermission "<<ALL BINDINGS>>","lookup";
   permission java.io.FilePermission "${jboss.defined.home}/lib/jboss-aop.jar", "read";
   permission javax.management.MBeanPermission "*", "getAttribute,invoke,setAttribute";
};

grant codeBase "file:${jboss.defined.home}/server/default/deploy/quartz-ra.rar!/quartz-ra.jar" {
 permission java.security.AllPermission;
};



//****************************************************************
//  Default block of permissions
// Minimal permissions are allowed to everyone else
//****************************************************************
grant {


	permission java.util.PropertyPermission "*", "read";
	permission java.util.PropertyPermission "java.version", "read";
	permission java.util.PropertyPermission "java.vendor", "read";
	permission java.util.PropertyPermission "java.vendor.url", "read";
	permission java.util.PropertyPermission "java.class.version", "read";
	permission java.util.PropertyPermission "os.name", "read";
	permission java.util.PropertyPermission "os.version", "read";
	permission java.util.PropertyPermission "os.arch", "read";
	permission java.util.PropertyPermission "file.separator", "read";
	permission java.util.PropertyPermission "path.separator", "read";
	permission java.util.PropertyPermission "line.separator", "read";

	permission java.util.PropertyPermission "java.specification.version", "read";
	permission java.util.PropertyPermission "java.specification.vendor", "read";
	permission java.util.PropertyPermission "java.specification.name", "read";

	permission java.util.PropertyPermission "java.vm.specification.version", "read";
	permission java.util.PropertyPermission "java.vm.specification.vendor", "read";
	permission java.util.PropertyPermission "java.vm.specification.name", "read";
	permission java.util.PropertyPermission "java.vm.version", "read";
	permission java.util.PropertyPermission "java.vm.vendor", "read";
	permission java.util.PropertyPermission "java.vm.name", "read";

   	permission java.io.FilePermission "${jboss.defined.home}/server/default/tmp/-", "read";
   	permission java.io.FilePermission "${jboss.defined.home}/server/lib/quartz.jar/org/quartz/quartz.properties", "read";
   	permission org.jboss.naming.JndiPermission "<<ALL BINDINGS>>","lookup";
   	permission java.io.FilePermission "quartz.properties", "read";
 
};
3. exporr JAVA_OPTS like follwoing: export JAVA_OPTS="-Djboss.defined.home=D:/java/servers/jboss-5.0.0.GA -Djava.security.manager=default -Djava.security.policy=file://d:/java/jprojects/mobicentstrunk/servers/jain-slee/tck/jain-slee-1.0/testsuite/jain-slee-1.0-tck/config/slee-security-policy.txt"
where:
 - jboss.defined.home - is where container runs.
 - java.security.policy - points to security policy file.
3. run container with switch that enables this policy, for instance:
bin/run.bat -b 192.168.1.100 
