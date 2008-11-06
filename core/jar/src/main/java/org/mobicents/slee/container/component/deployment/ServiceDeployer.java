/*
 * The Open SLEE project.
 *
 * The source code contained in this file is in in the public domain.
 * It can be used in any project or product without prior permission,
 * license or royalty payments. There is no claim of correctness and
 * NO WARRANTY OF ANY KIND provided with this code.
 */
package org.mobicents.slee.container.component.deployment;

import java.io.File;
import java.io.FileReader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.slee.ComponentID;
import javax.slee.management.ComponentDescriptor;
import javax.slee.management.DeployableUnitID;
import javax.slee.management.DeploymentException;

import org.jboss.logging.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.DeployableUnitDescriptorImpl;
import org.mobicents.slee.container.component.DeployableUnitIDImpl;
import org.mobicents.slee.container.component.DeployedComponent;
import org.mobicents.slee.container.component.MobicentsServiceDescriptorInternalImpl;
import org.mobicents.slee.container.management.xml.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

/*
 * Emil -- original author
 * Ranga -- minor hacks
 *
 */

/**
 * Instances of this class represent DU components or in other words - JAR file
 * entries contained in a deployable unit jar such as SBBs, Profiles, RA Types
 * and etc.
 * @author Emil Ivov 
 * @author M. Ranganathan
 */
class ServiceDeployer {

	private File serviceXML = null;
	private List componentDescriptors = null;
	private SleeContainer componentContainer = null;
	private static Logger log;

	static {
		log = Logger.getLogger(ServiceDeployer.class);
	}

	ServiceDeployer() {

	}

	File getServiceXML() {
		return serviceXML;
	}

	/**
	 * Inits and parses
	 * @param jarEntry JarFile
	 * @param componentContainer the container where the component should be
	 * deployed.
	 * @throws DeploymentException
	 */
	protected void initDeployer(File serviceXML, SleeContainer container)
			throws DeploymentException {
		this.serviceXML = serviceXML;
		this.componentContainer = container;
		try {

			this.componentDescriptors = parseServiceDescriptors();
		} catch (Exception ex) {
			DeploymentException de = new DeploymentException(
					"Failed to parse deployment descriptor of "
							+ serviceXML.getName(), ex);
			log.error(de.getMessage(), ex);
			throw de;
		}
	}

	protected List parseServiceDescriptors() throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("Service = " + serviceXML);
		}
		InputSource inputSource = new InputSource(new FileReader(serviceXML));
		ServiceDeploymentDescriptorParser parser = new ServiceDeploymentDescriptorParser();

		Document serviceJarDocument = XMLUtils.parseDocument(inputSource, true);

		List serviceNodes = XMLUtils.getAllChildElements(serviceJarDocument
				.getDocumentElement(), "service");
		LinkedList componentDescriptors = new LinkedList();
		for (int i = serviceNodes.size() - 1; i >= 0; i--) {
			Element sbbNode = (Element) serviceNodes.get(i);
			componentDescriptors.add(parser.parseServiceComponent(sbbNode,
					new MobicentsServiceDescriptorInternalImpl()));

		}

		return componentDescriptors;
	}

	/**
	 * Installs the all component descriptors in the ComponentContainer specified
	 * to the ComponentDeployer in the initDeployer method..
	 * @param deployableUnitID -- the deployable unit id for the service.
	 * @throws Exception if an Exception occurs during deployment.
	 */
	protected void deployAndInstall(DeployableUnitID deployableUnitID)
			throws Exception {

		if (componentDescriptors == null || componentDescriptors.size() == 0) {
			return;
		}

		Iterator descriptors = componentDescriptors.iterator();
		while (descriptors.hasNext()) {
			DeployedComponent du = (DeployedComponent) descriptors.next();
			du.setDeployableUnit(deployableUnitID);
			DeployableUnitDescriptorImpl duImpl = ((DeployableUnitIDImpl) deployableUnitID)
					.getDescriptor();
			componentContainer.getComponentManagement().install(
					(ComponentDescriptor) du, duImpl);
			ComponentID componentID = ((ComponentDescriptor) du).getID();
			// Put this in our inventory of deployed components.
			duImpl.addComponent(componentID);

		}
	}
}
