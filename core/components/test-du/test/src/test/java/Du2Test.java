/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

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
