import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.slee.management.DeploymentException;

import org.jboss.classloader.spi.ClassLoaderDomain;
import org.jboss.classloader.spi.ClassLoaderSystem;
import org.jboss.test.kernel.junit.MicrocontainerTest;
import org.mobicents.slee.container.component.ComponentManagementImpl;
import org.mobicents.slee.container.component.deployment.DeployableUnitBuilderImpl;
import org.mobicents.slee.container.component.deployment.classloading.ClassLoadingConfiguration;

public class Du2Test extends MicrocontainerTest {

	public Du2Test(String name) {
		super(name);		
	}

	@SuppressWarnings("deprecation")
	public void test() throws MalformedURLException, DeploymentException, URISyntaxException {	
		ClassLoaderDomain defaultDomain = ClassLoaderSystem.getInstance().getDefaultDomain();
		getLog().debug(defaultDomain.toLongString());
		ComponentManagementImpl componentManagement = new ComponentManagementImpl(new ClassLoadingConfiguration());
		URL url = Thread.currentThread().getContextClassLoader().getResource("Du2Test.class");
		File root = new File(url.toURI()).getParentFile();
		DeployableUnitBuilderImpl builder = componentManagement.getDeployableUnitManagement().getDeployableUnitBuilder();
		try {
			builder.build(root.toURL().toString()+"components-test-du-2.jar", root, componentManagement.getComponentRepository());
		}
		catch (DeploymentException e) {
			getLog().debug("got expected exception",e);
			return;
		}		
		fail("the du2 building should throw a DeploymentException due to break of class loading isolation");
	}
}
