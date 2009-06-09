To run the SLEE 1.1 tck:

1) Install tck components in the Mobicents JAIN SLEE server invoking "mvn install"
2) Unzip testsuite.zip to any directory of your choice
3) In the extracted testsuite directory do "ant". To run a specific test, such as tests/activities/activitycontext/AutoDetachTest.xml, do "ant -Dtests=tests/activities/activitycontext/AutoDetachTest.xml"

Note: To run tests against security permissions the JAVA_OPTS environment variable must be configured with special options: 

-Djboss.defined.home=c:/jboss-5.0.0.GA -Djava.security.manager=default -Djava.security.policy=file://c:/workspace/mobicents/servers/jain-slee/tck/jain-slee-1.1/tck-security.policy

where:
 - jboss.defined.home - is where container runs.
 - java.security.policy - points to tck-security.policy file in current directory