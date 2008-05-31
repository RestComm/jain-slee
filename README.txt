JAIN SLEE Server Build Instructions
------------------

1. You need Apache Maven 2 installed (http://maven.apache.org/)

2. You need JBoss AS 4.2 (download from http://labs.jboss.com/jbossas/downloads/)

3. Make environment variable JBOSS_HOME to point the directory where JBoss AS is installed
	on Linux: export JBOSS_HOME=/path/to/jboss
	on Windows: set JBOSS_HOME=/path/to/jboss (or right click "My Computer" -> Properties -> Advanced -> Environment Variables)

4. Execute "mvn install" in the directory where this README file is located

5. After a successful build the modules are installed in the server at JBOSS_HOME. Individual artifacts are available in the target directory for each module.


Adding project to Eclipse
---------------------------

1. Fetch all the project dependencies to your local Maven2 repository:

mvn mobicents:eclipse

2. Make sure Eclipse is CLOSED

3. Add M2_HOME variable to Eclipse executing:

mvn -Declipse.workspace=YOUR_PATH_TO_ECLIPSE_WORKSPACE eclipse:add-maven-repo"

4. Open Eclipse, in the File menu select Import, then "General->Existing Projects into Workspace", next and finally browse to this folder, the project should appear, be sure that the option "Copy project into workspace" is not checked before pressing "Finish".