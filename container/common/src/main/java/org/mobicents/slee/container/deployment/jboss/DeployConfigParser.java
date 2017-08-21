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

package org.mobicents.slee.container.deployment.jboss;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.slee.resource.ConfigProperties;
import javax.slee.resource.ResourceAdaptorID;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.jboss.logging.Logger;
import org.mobicents.slee.container.deployment.jboss.action.ActivateResourceAdaptorEntityAction;
import org.mobicents.slee.container.deployment.jboss.action.BindLinkNameAction;
import org.mobicents.slee.container.deployment.jboss.action.CreateResourceAdaptorEntityAction;
import org.mobicents.slee.container.deployment.jboss.action.DeactivateResourceAdaptorEntityAction;
import org.mobicents.slee.container.deployment.jboss.action.ManagementAction;
import org.mobicents.slee.container.deployment.jboss.action.RemoveResourceAdaptorEntityAction;
import org.mobicents.slee.container.deployment.jboss.action.UnbindLinkNameAction;
import org.mobicents.slee.container.management.ResourceManagement;
import org.mobicents.slee.container.management.jmx.editors.ComponentIDPropertyEditor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Parses a deploy-config.xml file, creating management actions from its
 * content.
 * 
 * @author martins
 * 
 */
public class DeployConfigParser {

	private static Logger logger = Logger.getLogger(DeployConfigParser.class);

	// The post-install actions needed to install/activate this DU components.
	private Map<String, Collection<ManagementAction>> postInstallActions;

	// The pre-uninstall actions needed to deactivate/uninstall this DU
	// components.
	private Map<String, Collection<ManagementAction>> preUninstallActions;

	public DeployConfigParser(InputStream deployConfigInputStream,
			ResourceManagement resourceManagement) throws SAXException,
			ParserConfigurationException, IOException {
		parseDeployConfig(deployConfigInputStream, resourceManagement);
	}

	/**
	 * Parser for the deployment config xml.
	 * 
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * 
	 * @throws Exception
	 */
	private void parseDeployConfig(InputStream deployConfigInputStream,
			ResourceManagement resourceManagement) throws SAXException,
			ParserConfigurationException, IOException {

		if (deployConfigInputStream == null) {
			throw new NullPointerException("null deploy config input stream");
		}

		Document doc = null;
		try {

			// Read the file into a Document
			SchemaFactory schemaFactory = SchemaFactory
					.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = schemaFactory.newSchema(DeployConfigParser.class
					.getClassLoader().getResource("deploy-config.xsd"));

			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			// factory.setValidating(false);
			factory.setSchema(schema);
			DocumentBuilder builder = factory.newDocumentBuilder();

			builder.setErrorHandler(new ErrorHandler() {
				public void error(SAXParseException e) throws SAXException {
					logger.error("Error parsing deploy-config.xml", e);
					return;
				}

				public void fatalError(SAXParseException e) throws SAXException {
					logger.error("Fatal error parsing deploy-config.xml", e);
					return;
				}

				public void warning(SAXParseException e) throws SAXException {
					logger.warn("Warning parsing deploy-config.xml", e);
					return;
				}
			});

			doc = builder.parse(deployConfigInputStream);
		} finally {
			try {
				deployConfigInputStream.close();
			} catch (IOException e) {
				logger.error("failed to close deploy config input stream", e);
			}
		}

		Map<String, Collection<ManagementAction>> postInstallActions = new HashMap<String, Collection<ManagementAction>>();

		Map<String, Collection<ManagementAction>> preUninstallActions = new HashMap<String, Collection<ManagementAction>>();

		// By now we only care about <ra-entity> nodes
		NodeList raEntities = doc.getElementsByTagName("ra-entity");

		// The RA identifier
		String raId = null;

		// The collection of Post-Install Actions
		Collection<ManagementAction> cPostInstallActions = new ArrayList<ManagementAction>();

		// The collection of Pre-Uninstall Actions
		Collection<ManagementAction> cPreUninstallActions = new ArrayList<ManagementAction>();

		// Iterate through each ra-entity node
		for (int i = 0; i < raEntities.getLength(); i++) {
			Element raEntity = (Element) raEntities.item(i);

			// Get the component ID
			ComponentIDPropertyEditor cidpe = new ComponentIDPropertyEditor();
			cidpe.setAsText(raEntity.getAttribute("resource-adaptor-id"));

			raId = cidpe.getValue().toString();

			// The RA Entity Name
			String entityName = raEntity.getAttribute("entity-name");

			// Select the properties node
			NodeList propsNodeList = raEntity
					.getElementsByTagName("properties");

			if (propsNodeList.getLength() > 1) {
				logger.warn("Invalid ra-entity element, has more than one properties child. Reading only first.");
			}

			// The properties for this RA
			ConfigProperties props = new ConfigProperties();
			Element propsNode = (Element) propsNodeList.item(0);
			// Do we have any properties at all?
			if (propsNode != null) {
				// Select the property elements
				NodeList propsList = propsNode.getElementsByTagName("property");
				// For each element, add it to the Properties object
				for (int j = 0; j < propsList.getLength(); j++) {
					Element property = (Element) propsList.item(j);
					String propertyName = property.getAttribute("name");
					String propertyType = property.getAttribute("type");
					String propertyValue = property.getAttribute("value");
					props.addProperty(new ConfigProperties.Property(
							propertyName, propertyType,
							ConfigProperties.Property.toObject(propertyType,
									propertyValue)));
				}
			}

			// Create the Resource Adaptor ID
			cidpe.setAsText(raEntity.getAttribute("resource-adaptor-id"));

			ResourceAdaptorID componentID = (ResourceAdaptorID) cidpe
					.getValue();

			// Add the Create and Activate RA Entity actions to the Post-Install
			// Actions
			cPostInstallActions.add(new CreateResourceAdaptorEntityAction(
					componentID, entityName, props, resourceManagement));
			cPostInstallActions.add(new ActivateResourceAdaptorEntityAction(
					entityName, resourceManagement));

			// Each RA might have zero or more links.. get them
			NodeList links = raEntity.getElementsByTagName("ra-link");

			for (int j = 0; j < links.getLength(); j++) {
				String linkName = ((Element) links.item(j))
						.getAttribute("name");

				cPostInstallActions.add(new BindLinkNameAction(linkName,
						entityName, resourceManagement));

				cPreUninstallActions.add(new UnbindLinkNameAction(linkName,
						resourceManagement));
			}

			// Add the Deactivate and Remove RA Entity actions to the
			// Pre-Uninstall Actions
			cPreUninstallActions.add(new DeactivateResourceAdaptorEntityAction(
					entityName, resourceManagement));
			cPreUninstallActions.add(new RemoveResourceAdaptorEntityAction(
					entityName, resourceManagement));

			// Finally add the actions to the respective hashmap.
			if (raId != null) {
				// We need to check if we are updating or adding new ones.
				if (postInstallActions.containsKey(raId)) {
					postInstallActions.get(raId).addAll(cPostInstallActions);
				} else {
					postInstallActions.put(raId, cPostInstallActions);
				}

				// Same here...
				if (preUninstallActions.containsKey(raId)) {
					preUninstallActions.get(raId).addAll(cPreUninstallActions);
				} else {
					preUninstallActions.put(raId, cPreUninstallActions);
				}
			}

			// recreate the lists for the next round (might come a new RA ID)...
			cPostInstallActions = new ArrayList<ManagementAction>();
			cPreUninstallActions = new ArrayList<ManagementAction>();
			raId = null;

		}

		this.postInstallActions = Collections
				.unmodifiableMap(postInstallActions);
		this.preUninstallActions = Collections
				.unmodifiableMap(preUninstallActions);
	}

	public Map<String, Collection<ManagementAction>> getPostInstallActions() {
		return postInstallActions;
	}

	public Map<String, Collection<ManagementAction>> getPreUninstallActions() {
		return preUninstallActions;
	}
}
