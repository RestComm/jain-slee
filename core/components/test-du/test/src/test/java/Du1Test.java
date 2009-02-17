import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.slee.management.DeploymentException;

import org.jboss.classloader.spi.ClassLoaderDomain;
import org.jboss.classloader.spi.ClassLoaderSystem;
import org.jboss.test.kernel.junit.MicrocontainerTest;
import org.mobicents.slee.container.component.deployment.DeployableUnitBuilder;


public class Du1Test extends MicrocontainerTest {

	public Du1Test(String name) {
		super(name);		
	}

	public void test() throws MalformedURLException, DeploymentException, URISyntaxException {	
		ClassLoaderDomain defaultDomain = ClassLoaderSystem.getInstance().getDefaultDomain();
		getLog().debug(defaultDomain.toLongString());
		TestComponentRepository componentRepository = new TestComponentRepository();
		URL url = Thread.currentThread().getContextClassLoader().getResource("Du1Test.class");
		File root = new File(url.toURI()).getParentFile();
		DeployableUnitBuilder builder = new DeployableUnitBuilder();
		builder.build(root.toURL().toString()+"components-test-du-1.jar", root, componentRepository);
		// ((Du1TestBean)getBean("Du1TestBean")).runTest();

	}
}
