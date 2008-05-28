The Mobicents DeployableUnit Plugin (aka mobicents-du-plugin)
release 1.0
-----------------------------------------------------
http://www.mobicents.org

1. INTRODUCTION

The Mobicents Deployable Unit Plugin is a sub-project of Mobicents.  
Mobicents du plugin's mission is to build slee deployable unit.

2. RELEASE INFO

mobicents-du-plugin requires J2SE 1.4 or higher.

Release contents:
* "src/main/java" contains the Java source files for the framework
* "src/test/java" contains the Java source files for plugin's test suite
* "src/test/resources" contains the resources files for plugin's test suite

Latest info is available at the public website: https://mobicents.org/

The Mobicents Deployable Unit Plugin is released under the terms of the Gnu Lesser General Public License (see LICENSE.txt).

This product includes software developed by the Apache Software Foundation (http://www.apache.org).

3. GETTING STARTED

In order to use the plugin you need to download and install the following technologies:
* Sun JDK 1.5+ from http://java.sun.com (or from a Linux repository)
	* Set the JAVA_HOME environment variable.
	* On Linux open /etc/profile or ~/.bash_profile and add export JAVAHOME=.... The JDK might be under /usr/lib/sun-jdk-1.x.x. On Windows open Control Panel/System/Advanced/Environment Variables. The JDK will probably be under C:\Program Files\java\jdk1.x.x.	

* Maven 2.0.6+ from http://maven.apache.org
	* Set the M2_HOME environment variable.
	* Adjust the PATH environment variable to add ;$M2_HOME/bin (Linux) or ;%M2_HOME%\bin (Windows).
	* On Windows, the use of cygwin (with OpenSSH and Subversion) from http://www.cygwin.com to run maven is required for very advanced functionality (ssh repository deploy, ...)

* One Java IDE (see below for configuration) with Subversion support
    * Eclipse 3.1+ from http://www.eclipse.org
          o Install one Subversion team support plugin:
                + Subversive plugin from http://www.polarion.org
                + Subclipse plugin from http://subclipse.tigris.org
          o Install Web Tools from http://www.eclipse.org for XML support (or MyEclipse)
    * NetBeans 5+ from http://www.netbeans.org
          o Subversion support is not that good yet, use a separate subversion client
    * IntelliJ IDEA 5.1+ from http://www.jetbains.com
          o Subversion is fully supported since IDEA 5

* One separate Subversion client (optional if already supported in the IDE) It's possible and even handy to combine multiple Subversion clients.

    * Subversion (shell client) from http://subversion.tigris.org
    * Tortoise for Windows explorer from http://tortoisesvn.tigris.org
    * Any Subversion client referenced in Links on http://subversion.tigris.org	

* Getting the sources from TBD
    
4. BUILDING

Open your command line (CygWin, bash, DOS, ...) and run in the project root:

mvn install

This builds the project and installs it into your local repository.

NOTE: The first time you run maven it will download all dependencies and cache them in your local repository in ~/.m2/repository, and it may not work the first time. If you have a bad connection to Ibiblio (Maven's default repository), you may have to execute this command a bunch of times (5 or more) until maven finally gets all the dependencies downloaded, try switching to a mirror of Ibiblio (see developer FAQ). Ignoring the multiple runs, the initial download of all these files will take a while. Run it again afterwards to see how fast it really is. Then take a look at the faq.html to run it even faster.
Once a multiproject install has been run, it's possible (not required) to build a single module with the same command in a module root.

Other build commands:
mvn clean
mvn compile
mvn test
mvn package
mvn install
mvn site

Build result

Take a look in the target directory in the project root and every module. You 'll find the module jar there, test reports and the website if you've generated it.

5. CONFIGURING YOUR FAVORITE IDE

Maven can do this for you.

* Eclipse
	* see http://maven.apache.org/eclipse-plugin.html
* NetBeans
	* see http://maven.apache.org/netbeans-module.html
* IntelliJ IDEA
	* see http://maven.apache.org/plugins/maven-idea-plugin/

6. RUNNING PLUGIN

The file "src/test/resources/unit/test01/pom.xml" shows all possible configurations of the plugin.

7. RUNNING TESTCASE

In order to run testcase you need Mobicents running:
mvn test

8. GENERATE WEBSITE

To generate the website simply do:

mvn site

Then take a look at target/site/index.html.
		
