To run the SLEE 1.1 tck:
1) Install tck components in the Mobicents JAIN SLEE server invoking "mvn install"
2) Unzip testsuite.zip to any directory of your choice
3) In the extracted testsuite directory do "ant". To run a specific test, such as tests/activities/activitycontext/AutoDetachTest.xml, do "ant -Dtests=tests/activities/activitycontext/AutoDetachTest.xml"

To run tests against security permissions:
1. perform steps 1-3 from above
2. create policy file with similar content:

// Standard extensions get all permissions by default

grant codeBase "file:/${java.home}/lib/ext/*" {
	permission java.security.AllPermission;
};

// Allow javac and jar full access; actual access is limited by
// the caller's security context.
grant codeBase "file:/${java.home}/lib/tools.jar" {
	permission java.security.AllPermission;
};

grant codeBase "file:/${jboss.home.dir}/bin/*" {
	permission java.security.AllPermission;
};

grant codeBase "file:/${jboss.home.dir}/server/all/lib/*" {
	permission java.security.AllPermission;
};

grant codeBase "file:/${jboss.home.dir}/server/default/lib/*" {
	permission java.security.AllPermission;
};

grant codeBase "file:/${jboss.home.dir}/lib/*" {
	permission java.security.AllPermission;
};

grant codeBase "file:/${jboss.home.dir}/server/all/deploy/-" {
	permission java.security.AllPermission;
};
grant codeBase "file:/${jboss.home.dir}/server/default/deploy/-" {
	permission java.security.AllPermission;
};

//This is not rquired
grant codeBase "file:${jboss.home.dir}/server/default/deploy/mobicents.sar/lib/-" {
	permission java.security.AllPermission;
};


// Java "standard" properties that can be read by anyone
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


};
3. run container with switch that enables this policy, for instance:
bin/run.bat -b 192.168.1.100 -Djava.security.manager -Djava.security.policy=file://d:/java/jprojects/mobicentstrunk/servers/jain-slee/tck/jain-slee-1.0/testsuite/jain-slee-1.0-tck/config/slee-security-policy.txt 
