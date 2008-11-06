/*
 * Created on Dec 3, 2004
 * 
 * The Open SLEE Project
 * 
 * A SLEE for the People
 * 
 * The source code contained in this file is in in the public domain.          
 * It can be used in any project or product without prior permission, 	      
 * license or royalty payments. There is no claim of correctness and
 * NO WARRANTY OF ANY KIND provided with this code.
 */
package org.mobicents.slee.container.component.deployment;

import java.util.jar.*;
import javax.slee.management.*;
import org.w3c.dom.*;
import org.jboss.logging.*;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.MobicentsSbbDescriptorInternalImpl;
import org.mobicents.slee.container.component.DeployableUnitIDImpl;
import org.mobicents.slee.container.management.xml.*;
import java.io.*;
import java.util.*;

/**
 * Deploys the sbb component in the service container.
 * 
 * @author Emil Ivov (original)
 * @author M. Ranganathan
 * @author Tim Fox
 * 
 * @version 1.0
 */

class SbbComponentDeployer extends AbstractComponentDeployer {
	private static Logger logger;
	private SbbDeploymentDescriptorParser parser;
	private MCSbbDeploymentDescriptorParser mobicentsParser;
	private DeployableUnitIDImpl deployableUnitID;

	private JarEntry ddXmlEntry;
	static {
		logger = Logger.getLogger(SbbComponentDeployer.class);
	}

	public SbbComponentDeployer(DeployableUnitIDImpl deployableUnitID,
			JarEntry ddXmlEntry) {

		this.deployableUnitID = deployableUnitID;
		this.ddXmlEntry = ddXmlEntry;

		parser = new SbbDeploymentDescriptorParser();
		mobicentsParser = new MCSbbDeploymentDescriptorParser();
	}

	/**
	 * Parses the specified document into the corresponding ComponentDescriptor.
	 *
	 * @param document the deployment descriptor of the component
	 */
	protected List parseComponentDescriptors() throws Exception {
		logger.debug("Parsing an SBB from " + getJar().getName());
		JarFile jar = getJar();

		//Get the Sbb deployment Descriptor;
		if (ddXmlEntry == null) {
			throw new DeploymentException(
					"No SbbDeploymentDescriptor descriptor "
							+ "(META-INF/sbb-jar.xml) was found in "
							+ jar.getName());
		}

		//Parse the descriptor
		Document doc = null;
		try {
			doc = XMLUtils.parseDocument(jar.getInputStream(ddXmlEntry), true);
		} catch (IOException ex) {
			throw new DeploymentException(
					"Failed to extract the SBB deployment"
							+ " descriptor from " + jar.getName());
		}

		Element sbbJarNode = doc.getDocumentElement();

		//Get a list of the jars and services in the deployable unit.
		List sbbNodes = XMLUtils.getAllChildElements(sbbJarNode,
				XMLConstants.SBB_ND);
		if (sbbNodes.size() == 0) {
			throw new DeploymentException("The " + jar.getName()
					+ " deployment descriptor contains no sbb definitions");
		}

		/*
		There may be other deployment settings, over and above what is specified in 
		the specification
		For instance, the JNDI name of an EJBHome referred to by an <ejb-ref>
		This data should be in the mobicents-sbb-jar.xml file.
		(Analogy with jboss.xml and ejb-jar.xml - jboss.xml contains the JBoss
		specific deployment settings)
		 */
		JarEntry ddMobicentsXmlEntry = jar
				.getJarEntry("META-INF/mobicents-sbb-jar.xml");

		List sbbMobicentsNodes = null;
		if (ddMobicentsXmlEntry != null) {
			logger.debug("Parsing mobicents-sbb-jar.xml");

			Document docMobicents = null;
			try {
				docMobicents = XMLUtils.parseDocument(jar
						.getInputStream(ddMobicentsXmlEntry), true);
			} catch (IOException ex) {
				throw new DeploymentException(
						"Failed to extract the Mobicents SBB deployment"
								+ " descriptor from " + jar.getName());
			}

			Element sbbMobicentsJarNode = docMobicents.getDocumentElement();

			sbbMobicentsNodes = XMLUtils.getAllChildElements(
					sbbMobicentsJarNode, XMLConstants.MC_SBB_ND);
			if (sbbMobicentsNodes.size() == 0) {
				throw new DeploymentException(
						"The "
								+ jar.getName()
								+ " mobicents deployment descriptor contains no sbb definitions");
			}
		}

		//First we parse the sbb elements from sbb-jar.xml
		HashMap descriptors = new HashMap();
		for (int i = sbbNodes.size() - 1; i >= 0; i--) {
			Element sbbNode = (Element) sbbNodes.get(i);

			MobicentsSbbDescriptorInternalImpl descriptor = (MobicentsSbbDescriptorInternalImpl) parser
					.parseSbbComponent(sbbNode,
							new MobicentsSbbDescriptorInternalImpl());

			descriptor.setSource(getJar().getName());

			descriptors.put(descriptor.getID(), descriptor);
			// init component references map here, this is because the order of
			// deployment of sbbs in the same descriptor, and if references map
			// is not intalled and wrong order is used a deployment exception
			// would raise
			SleeContainer.lookupFromJndi().getComponentManagement()
					.initComponentReferencesMap(descriptor.getID());
		}

		//Then we parse the sbb elements from mobicents-sbb-jar.xml        
		if (sbbMobicentsNodes != null) {
			logger.debug("Parsing sbb elements from mobicents-sbb-jar.xml");

			for (int i = sbbMobicentsNodes.size() - 1; i >= 0; i--) {
				Element sbbNode = (Element) sbbMobicentsNodes.get(i);

				mobicentsParser.parseSbbComponent(sbbNode, descriptors);
			}
		}

		logger.debug("Done.");
		return new ArrayList(descriptors.values());
	}

}
