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

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.slee.ComponentID;

import org.jboss.logging.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.deployment.jboss.action.InstallDeployableUnitAction;
import org.mobicents.slee.container.deployment.jboss.action.ManagementAction;
import org.mobicents.slee.container.deployment.jboss.action.UninstallDeployableUnitAction;
import org.mobicents.slee.container.management.jmx.editors.ComponentIDPropertyEditor;

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
  private Map<String, Collection<ManagementAction>> postInstallActions = new HashMap<String, Collection<ManagementAction>>();

  // The pre-uninstall actions needed to deactivate/uninstall this DU components.
  private Map<String, Collection<ManagementAction>> preUninstallActions = new HashMap<String, Collection<ManagementAction>>();

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
    parseDUDeployConfig();
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
        && !postInstallActionsStrings.isEmpty()) {
      installActions.addAll(postInstallActionsStrings);
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
    if (!externalDependencies.isEmpty()) {
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

    if (!duplicates.isEmpty()) {
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
	  if (!postInstallActions.values().isEmpty()) {
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
	  if (!preUninstallActions.values().isEmpty()) {
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
    return isInstalled && !hasReferringDU();
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
	 * 
	 * @throws Exception
	 */
	private void parseDUDeployConfig() throws Exception  {
		JarFile componentJarFile = new JarFile(diURL.getFile());
		try {
			// Get the JarEntry for the deploy-config.xml
			JarEntry deployInfoXML = componentJarFile
					.getJarEntry("META-INF/deploy-config.xml");
			if (deployInfoXML != null) {
				DeployConfigParser deployConfigParser = new DeployConfigParser(
						componentJarFile.getInputStream(deployInfoXML),
						sleeContainerDeployer.getSleeContainer()
								.getResourceManagement());
				for (Entry<String, Collection<ManagementAction>> e : deployConfigParser.getPostInstallActions().entrySet()) {
					postInstallActions.put(e.getKey(), e.getValue());
				}
				for (Entry<String, Collection<ManagementAction>> e : deployConfigParser.getPreUninstallActions().entrySet()) {
					preUninstallActions.put(e.getKey(), e.getValue());
				}
			}			
		} finally {
			componentJarFile.close();						
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