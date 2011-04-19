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

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.slee.ComponentID;
import javax.slee.resource.ConfigProperties;
import javax.slee.resource.ResourceAdaptorID;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.jboss.logging.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.deployment.jboss.action.ActivateResourceAdaptorEntityAction;
import org.mobicents.slee.container.deployment.jboss.action.BindLinkNameAction;
import org.mobicents.slee.container.deployment.jboss.action.CreateResourceAdaptorEntityAction;
import org.mobicents.slee.container.deployment.jboss.action.DeactivateResourceAdaptorEntityAction;
import org.mobicents.slee.container.deployment.jboss.action.InstallDeployableUnitAction;
import org.mobicents.slee.container.deployment.jboss.action.ManagementAction;
import org.mobicents.slee.container.deployment.jboss.action.RemoveResourceAdaptorEntityAction;
import org.mobicents.slee.container.deployment.jboss.action.UnbindLinkNameAction;
import org.mobicents.slee.container.deployment.jboss.action.UninstallDeployableUnitAction;
import org.mobicents.slee.container.management.ResourceManagement;
import org.mobicents.slee.container.management.jmx.editors.ComponentIDPropertyEditor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * This class represents a SLEE Deployable Unit, represented by a collection of
 * Deployable Components. Contains all the DU dependencies, install/uninstall
 * actions needed for the DU and post-install and pre-uninstall actions.
 * 
 * @author Alexandre Mendonça
 * @version 1.0
 */
public class DeployableUnit {
  // The logger.
  private static Logger logger = Logger.getLogger(DeployableUnit.class);

  // The DeploymentInfo short name
  private String diShortName;

  // The DeploymentInfo URL object
  private URL diURL;

  // A collection of the Deployable Components in this DU
  private Collection<DeployableComponent> components = new ArrayList<DeployableComponent>();

  // A collection of the IDs of the components in this DU.
  private Collection<String> componentIDs = new ArrayList<String>();

  // A collection of the IDs of the components that this DU depends on. 
  private Collection<String> dependencies = new ArrayList<String>();

  // The install actions needed to install/activate this DU components.
  private Collection<ManagementAction> installActions = new ArrayList<ManagementAction>();

  // The post-install actions needed to install/activate this DU components.
  private HashMap<String, Collection<ManagementAction>> postInstallActions = new HashMap<String, Collection<ManagementAction>>();

  // The pre-uninstall actions needed to deactivate/uninstall this DU components.
  private HashMap<String, Collection<ManagementAction>> preUninstallActions = new HashMap<String, Collection<ManagementAction>>();

  // The install actions needed to deactivate/uninstall this DU components.
  private Collection<ManagementAction> uninstallActions = new ArrayList<ManagementAction>();

  // A flag indicating wether this DU is installed
  private boolean isInstalled = false;

  private final SleeContainerDeployerImpl sleeContainerDeployer;
  
  /**
   * Constructor.
   * @param duDeploymentInfo this DU deployment info.
   * @param deploymentManager the DeploymentManager in charge of this DU.
   * @throws Exception 
   */
  public DeployableUnit(DeployableUnitWrapper du,SleeContainerDeployerImpl sleeContainerDeployer) throws Exception
  {
	  this.sleeContainerDeployer = sleeContainerDeployer;
	  
    this.diShortName = du.getFileName();
    this.diURL = du.getUrl();

    // First action for the DU is always install.
    installActions.add(new InstallDeployableUnitAction(diURL.toString(), sleeContainerDeployer.getDeploymentMBean()));

    // Parse the deploy-config.xml to obtain post-install/pre-uninstall actions
    parseDeployConfig();
  }

  /**
   * Adder method for a Deployable Component.
   * @param dc the deployable component object.
   */
  public void addComponent(DeployableComponent dc) {
    if (logger.isTraceEnabled())
      logger.trace("Adding Component " + dc.getComponentKey());

    // Add the component ..
    components.add(dc);

    // .. the key ..
    componentIDs.add(dc.getComponentKey());

    // .. the dependencies ..
    dependencies.addAll(dc.getDependencies());

    // .. the install actions to be taken ..
    installActions.addAll(dc.getInstallActions());

    // .. post-install actions (if any) ..
    Collection<ManagementAction> postInstallActionsStrings = postInstallActions
    .remove(dc.getComponentKey());

    if (postInstallActionsStrings != null
        && postInstallActionsStrings.size() > 0) {
      installActions.addAll(postInstallActionsStrings);
    } else if (dc.getComponentType() == DeployableComponent.RA_COMPONENT) {
      ComponentID cid = dc.getComponentID();

      String raID = dc.getComponentKey();

      logger
      .warn("\r\n------------------------------------------------------------"
          + "\r\nNo RA Entity and Link config for "
          + raID
          + " found. Using default values!"
          + "\r\n------------------------------------------------------------");

      String raName = cid.getName();

      ResourceManagement resourceManagement = sleeContainerDeployer.getSleeContainer().getResourceManagement();
      
      // Add the default Create and Activate RA Entity actions to the Install Actions
      installActions.add(new CreateResourceAdaptorEntityAction((ResourceAdaptorID) cid, raName, new ConfigProperties(), resourceManagement));
      installActions.add(new ActivateResourceAdaptorEntityAction(raName, resourceManagement));

      // Create default link
      installActions.add(new BindLinkNameAction(raName, raName, resourceManagement));

      // Remove default link
      uninstallActions.add(new UnbindLinkNameAction(raName, resourceManagement));

      // Add the default Deactivate and Remove RA Entity actions to the Uninstall Actions
      uninstallActions.add(new DeactivateResourceAdaptorEntityAction(raName, resourceManagement));
      uninstallActions.add(new RemoveResourceAdaptorEntityAction(raName, resourceManagement));
    }

    // .. pre-uninstall actions (if any) ..
    Collection<ManagementAction> preUninstallActionsStrings = preUninstallActions
    .remove(dc.getComponentKey());

    if (preUninstallActionsStrings != null)
      uninstallActions.addAll(preUninstallActionsStrings);

    // .. and finally the uninstall actions to the DU.
    uninstallActions.addAll(dc.getUninstallActions());
  }

  /**
   * Method for checking if DU is self-sufficient.
   * @return true if the DU has no external dependencies.
   */
  public boolean isSelfSufficient() {
    // All dependencies in the DU components?
    return componentIDs.containsAll(dependencies);
  }

  /**
   * Method for obtaining the external dependencies for this DU, if any.
   * @return a Collection of external dependencies identifiers.
   */
  public Collection<String> getExternalDependencies() {
    // Take all dependencies...
    Collection<String> externalDependencies = new HashSet<String>(dependencies);

    // Remove those which are contained in this DU
    externalDependencies.removeAll(componentIDs);

    // Return what's left.
    return externalDependencies;
  }

  /**
   * Method for checking if the DU has all the dependencies needed to be deployed.
   * @param showMissing param to set whether to show or not missing dependencies.
   * @return true if all the dependencies are satisfied.
   */
  public boolean hasDependenciesSatisfied(boolean showMissing) {
    // First of all check if it is self-sufficient
    if (isSelfSufficient())
      return true;

    // If not self-sufficient, get the remaining dependencies
    Collection<String> externalDependencies = getExternalDependencies();

    // Remove those that are already installed...
    externalDependencies.removeAll(sleeContainerDeployer.getDeploymentManager().getDeployedComponents());

    // Some remaining?
    if (externalDependencies.size() > 0) {
      if (showMissing) {
        // List them to the user...
        String missingDepList = "";

        for (String missingDep : externalDependencies)
          missingDepList += "\r\n +-- " + missingDep;

        logger.info("Missing dependencies for " + this.diShortName
            + ":" + missingDepList);
      }

      // Return dependencies not satified.
      return false;
    }

    // OK, dependencies satisfied!
    return true;
  }

  /**
   * Method for checking if this DU contains any component that is already deployed.
   * @return true if there's a component that is already deployed.
   */
  public boolean hasDuplicates() {
    ArrayList<String> duplicates = new ArrayList<String>();

    // For each component in the DU ..
    for (String componentId : componentIDs) {
      // Check if it is already deployed
      if (sleeContainerDeployer.getDeploymentManager().getDeployedComponents().contains(componentId)) {
        duplicates.add(componentId);
      }
    }

    if (duplicates.size() > 0) {
      logger.warn("The deployable unit '" + this.diShortName + "' contains components that are already deployed. The following are already installed:");

      for (String dupComponent : duplicates) {
        logger.warn(" - " + dupComponent);
      }

      return true;
    }

    // If we got here, there's no dups.
    return false;
  }

  /**
   * Method for doing all the checking to make sure it is ready to be installed.
   * @param showMissing param to set whether to show or not missing dependencies.
   * @return true if all the pre-reqs are met.
   */
  public boolean isReadyToInstall(boolean showMissing) {
    // Check if the deps are satisfied and there are no dups.
    return hasDependenciesSatisfied(showMissing) && !hasDuplicates();
  }

  /**
   * Getter for the Install Actions.
   * @return a Collection of actions.
   */
  public Collection<ManagementAction> getInstallActions() {
    
	  ArrayList<ManagementAction> iActions = new ArrayList<ManagementAction>();

	  // if we have some remaining post install actions it means it is actions related with components already installed
	  // thus should be executed first
	  if (postInstallActions.values().size() > 0) {
		  for (String componentId : postInstallActions.keySet()) {
			  iActions.addAll(postInstallActions.get(componentId));
		  }
	  }

	  iActions.addAll(installActions);

	  return iActions;
  }

  /**
   * Getter for the Uninstall Actions.
   * @return a Collection of actions.
   */
  public Collection<ManagementAction> getUninstallActions() {
	  Collection<ManagementAction> uActions = new ArrayList<ManagementAction>(uninstallActions);

	  // ensures uninstall is the last action related with DU components
	  uActions.add(new UninstallDeployableUnitAction(diURL.toString(), sleeContainerDeployer.getDeploymentMBean()));

	  // if we have some remaining uninstall actions it means it is actions related with components not in DU
	  // thus should be executed last
	  if (preUninstallActions.values().size() > 0) {
		  for (String componentId : preUninstallActions.keySet()) {
			  uActions.addAll(preUninstallActions.get(componentId));
		  }
	  }

	  return uActions;
  }

  /**
   * Getter for this DU components.
   * @return a Collection of component identifiers.
   */
  public Collection<String> getComponents() {
    return componentIDs;
  }

  /**
   * Method for doing all the checking to make sure it is ready to be uninstalled.
   * @return true if all the pre-reqs are met.
   * @throws Exception
   */
  public boolean isReadyToUninstall() throws Exception {
    // Check DU for readiness ..
    return (isInstalled && !hasReferringDU());
  }

  /**
   * Method for checking if this DU components are referred by any others.
   * @return true if there are other DUs installed referring this.
   * @throws Exception
   */
  private boolean hasReferringDU() throws Exception {

    // Get SleeContainer instance from JNDI
    SleeContainer sC = SleeContainer.lookupFromJndi();

    for (String componentIdString : this.getComponents()) {
      ComponentIDPropertyEditor cidpe = new ComponentIDPropertyEditor();
      cidpe.setAsText( componentIdString );

      ComponentID componentId = (ComponentID) cidpe.getValue();

      for (ComponentID referringComponentId : sC.getComponentRepository().getReferringComponents(componentId)) {
        ComponentIDPropertyEditor rcidpe = new ComponentIDPropertyEditor();
        rcidpe.setValue( referringComponentId );

        String referringComponentIdString = rcidpe.getAsText();

        if (!this.getComponents().contains( referringComponentIdString )) {
          return true;
        }
      }
    }

    return false;
  }

  /**
   * Getter for the DeploymentInfo short name
   * @return a String containing the filename
   */
  public String getDeploymentInfoShortName() {
    return this.diShortName;
  }

  /**
   * Setter for the isInstalled flag.
   * @return a boolean indicating if the DU is already installed.
   */
  public boolean isInstalled() {
    return isInstalled;
  }

  /**
   * Setter for the isInstalled flag.
   * @param isInstalled the isInstalled flag indicating that the DU is already installed.
   */
  public void setInstalled(boolean isInstalled) {
    this.isInstalled = isInstalled;
  }

  /**
   * Parser for the deployment config xml.
   * @throws Exception
   */
  private void parseDeployConfig() throws Exception {
    JarFile componentJarFile = null;

    InputStream is = null;

    try {
      // Create a JarFile object
      componentJarFile = new JarFile(diURL.getFile());

      // Get the JarEntry for the deploy-config.xml
      JarEntry deployInfoXML = componentJarFile.getJarEntry("META-INF/deploy-config.xml");

      // If it exists, set an Input Stream on it 
      is = deployInfoXML != null ? componentJarFile.getInputStream(deployInfoXML) : null;

      if (is != null) {

        // Read the file into a Document
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = schemaFactory.newSchema(DeployableUnit.class.getClassLoader().getResource("deploy-config.xsd"));

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        //factory.setValidating(false);
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

        Document doc =  builder.parse(is);

        // By now we only care about <ra-entitu> nodes
        NodeList raEntities = doc.getElementsByTagName("ra-entity");

        // The RA identifier
        String raId = null;

        // The collection of Post-Install Actions
        Collection<ManagementAction> cPostInstallActions = new ArrayList<ManagementAction>();

        // The collection of Pre-Uninstall Actions
        Collection<ManagementAction> cPreUninstallActions = new ArrayList<ManagementAction>();

        final ResourceManagement resourceManagement = sleeContainerDeployer.getSleeContainer().getResourceManagement();
        
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
          NodeList propsNodeList = raEntity.getElementsByTagName("properties");

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
									ConfigProperties.Property.toObject(
											propertyType, propertyValue)));
        	  }
          }

          // Create the Resource Adaptor ID
          cidpe.setAsText(raEntity.getAttribute("resource-adaptor-id"));

          ResourceAdaptorID componentID = (ResourceAdaptorID) cidpe.getValue();

          // Add the Create and Activate RA Entity actions to the Post-Install Actions
          cPostInstallActions.add(new CreateResourceAdaptorEntityAction(componentID, entityName, props, resourceManagement));
          cPostInstallActions.add(new ActivateResourceAdaptorEntityAction(entityName, resourceManagement));

          // Each RA might have zero or more links.. get them
          NodeList links = raEntity.getElementsByTagName("ra-link");

          for (int j = 0; j < links.getLength(); j++) {
            String linkName = ((Element) links.item(j)).getAttribute("name");

            cPostInstallActions.add(new BindLinkNameAction(linkName, entityName, resourceManagement));

            cPreUninstallActions.add(new UnbindLinkNameAction(linkName, resourceManagement));
          }

          // Add the Deactivate and Remove RA Entity actions to the Pre-Uninstall Actions
          cPreUninstallActions.add(new DeactivateResourceAdaptorEntityAction(entityName, resourceManagement));
          cPreUninstallActions.add(new RemoveResourceAdaptorEntityAction(entityName, resourceManagement));

          // Finally add the actions to the respective hashmap.
          if (raId != null) {
            // We need to check if we are updating or adding new ones.
            if (postInstallActions.containsKey(raId)) {
              postInstallActions.get(raId).addAll(cPostInstallActions);
            }
            else {
              postInstallActions.put(raId, cPostInstallActions);
            }

            // Same here...
            if (preUninstallActions.containsKey(raId)) {
              preUninstallActions.get(raId).addAll(cPreUninstallActions);
            }
            else {
              preUninstallActions.put(raId, cPreUninstallActions);
            }
          }

          // recreate the lists for the next round (might come a new RA ID)...
          cPostInstallActions = new ArrayList<ManagementAction>();
          cPreUninstallActions = new ArrayList<ManagementAction>();
          raId = null;

        }
      }
    }
    finally {
      // Clean depoy-config.xml inputstream
      try {
        if (is != null) {
          is.close();
        }
      }
      finally {
        is = null;
      }

      // Clean jar input streams
      try {
        if (componentJarFile != null) {
          componentJarFile.close();
        }
      }
      finally {
        componentJarFile = null;
      }
    }
  }

  public URL getURL() {
    return diURL;
  }
  
  public boolean areComponentsStillPresent() {
    Collection<String> presentComponents = sleeContainerDeployer.getDeploymentManager().getDeployedComponents();

    for(String cId : this.componentIDs) {
      if(!presentComponents.contains(cId)) {
        return false;
      }
    }

    return true;
  }
}