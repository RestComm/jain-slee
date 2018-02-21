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
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.slee.ComponentID;
import javax.slee.ServiceID;

import org.jboss.logging.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.ComponentDescriptorFactory;
import org.mobicents.slee.container.component.event.EventTypeDescriptor;
import org.mobicents.slee.container.component.event.EventTypeDescriptorFactory;
import org.mobicents.slee.container.component.library.LibraryDescriptor;
import org.mobicents.slee.container.component.library.LibraryDescriptorFactory;
import org.mobicents.slee.container.component.profile.ProfileSpecificationDescriptor;
import org.mobicents.slee.container.component.profile.ProfileSpecificationDescriptorFactory;
import org.mobicents.slee.container.component.ra.ResourceAdaptorDescriptor;
import org.mobicents.slee.container.component.ra.ResourceAdaptorDescriptorFactory;
import org.mobicents.slee.container.component.ratype.ResourceAdaptorTypeDescriptor;
import org.mobicents.slee.container.component.ratype.ResourceAdaptorTypeDescriptorFactory;
import org.mobicents.slee.container.component.sbb.ResourceAdaptorEntityBindingDescriptor;
import org.mobicents.slee.container.component.sbb.ResourceAdaptorTypeBindingDescriptor;
import org.mobicents.slee.container.component.sbb.SbbDescriptor;
import org.mobicents.slee.container.component.sbb.SbbDescriptorFactory;
import org.mobicents.slee.container.component.service.ServiceDescriptor;
import org.mobicents.slee.container.component.service.ServiceDescriptorFactory;
import org.mobicents.slee.container.deployment.jboss.action.ActivateServiceAction;
import org.mobicents.slee.container.deployment.jboss.action.DeactivateServiceAction;
import org.mobicents.slee.container.deployment.jboss.action.ManagementAction;
import org.mobicents.slee.container.management.jmx.editors.ComponentIDPropertyEditor;

/**
 * This class represents a SLEE Deployable Component such as a Profile
 * Specification, Event Type Definition, RA Type, Resource Adaptor, SBB or
 * Service. It also contains the dependencies and the install/uninstall actions
 * needed for that component.
 * 
 * @author Alexandre Mendonça
 * @version 1.0
 */
public class DeployableComponent {
	// The logger.
	private static Logger logger = Logger.getLogger(DeployableComponent.class);

	public final static int PROFILESPEC_COMPONENT = 1;
	public final static int EVENTTYPE_COMPONENT = 2;
	public final static int RATYPE_COMPONENT = 3;
	public final static int RA_COMPONENT = 4;
	public final static int SBB_COMPONENT = 5;
	public final static int SERVICE_COMPONENT = 6;
	public final static int LIBRARY_COMPONENT = 7;

	// The DeploymentInfo short name
	private String diShortName;

	// The DeploymentInfo URL object
	private URL diURL;

	// The ID of the component.
	private ComponentID componentID;

	// The dependencies for this Component.
	private Collection<String> dependencies = new ArrayList<String>();

	// The actions needed to perform installation for this component.
	private Collection<ManagementAction> installActions = new ArrayList<ManagementAction>();

	// The actions needed to perform installation for this component.
	private Collection<ManagementAction> uninstallActions = new ArrayList<ManagementAction>();

	// The key identifying the component (type[name#vendor#version]).
	private String componentKey;

	// An indicator for the component type.
	private int componentType = -1;

	private DeployableUnitWrapper duWrapper;

	// The components inside this component
	private Collection<DeployableComponent> subComponents = new ArrayList<DeployableComponent>();

	private final SleeContainerDeployerImpl sleeContainerDeployer;

	/**
	 * Private constructor for the sub-components.
	 * 
	 * @param dc
	 *            the base DeployableComponent
	 * @throws Exception
	 */
	private DeployableComponent(DeployableComponent dc,
			SleeContainerDeployerImpl sleeContainerDeployer) throws Exception {

		this.sleeContainerDeployer = sleeContainerDeployer;
		this.diShortName = dc.diShortName;
		this.diURL = dc.diURL;

		// We want no sub-sub-components...
		this.subComponents = null;
	}

	public DeployableComponent(DeployableUnitWrapper duWrapper, URL url,
			String fileName, SleeContainerDeployerImpl sleeContainerDeployer)
			throws Exception {
		this.sleeContainerDeployer = sleeContainerDeployer;
		this.duWrapper = duWrapper;
		this.diShortName = fileName;
		this.diURL = url;

		// Parse the component descriptor to obtain dependencies.
		this.subComponents = parseDescriptor();
	}

	/**
	 * Getter for this component dependencies.
	 * 
	 * @return a Collection<String> with dependencies IDs.
	 */
	public Collection<String> getDependencies() {
		return this.dependencies;
	}

	/**
	 * Method for checking if the component is deployable, ie, meets the
	 * pre-reqs.
	 * 
	 * @param deployedComponents
	 *            a Collection<String> with the IDs of the already deployed
	 *            components.
	 * @return true if it can be deployed.
	 */
	public boolean isDeployable(Collection<String> deployedComponents) {
		return deployedComponents.containsAll(dependencies);
	}

	/**
	 * Getter for the Component Key String.
	 * 
	 * @return a String with the component key (type[name#vendor#version]).
	 */
	public String getComponentKey() {
		return this.componentKey;
	}

	/**
	 * Parser for the deployment descriptor. Minimal version obtained from
	 * Container.
	 * 
	 * @return a String containing the Component ID.
	 * @throws IOException
	 */
	private Collection<DeployableComponent> parseDescriptor()
			throws IOException {
		if (logger.isTraceEnabled()) {
			logger.trace("Parsing Descriptor for " + this.diURL.toString());
		}

		Collection<DeployableComponent> deployableComponents = new ArrayList<DeployableComponent>();

		SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
		ComponentDescriptorFactory componentDescriptorFactory = sleeContainer
				.getComponentManagement().getComponentDescriptorFactory();

		// Special case for the services...
		if (this.diShortName.endsWith(".xml")) {
			if (logger.isTraceEnabled()) {
				logger.trace("Parsing Service Descriptor.");
			}

			InputStream is = null;

			try {
				is = diURL.openStream();

				ServiceDescriptorFactory sdf = componentDescriptorFactory
						.getServiceDescriptorFactory();
				List<? extends ServiceDescriptor> serviceDescriptors = sdf
						.parse(is);

				for (ServiceDescriptor sd : serviceDescriptors) {
					DeployableComponent dc = new DeployableComponent(this,sleeContainerDeployer);

					dc.componentType = SERVICE_COMPONENT;

					dc.componentID = sd.getServiceID();

					dc.componentKey = getComponentIdAsString(dc.componentID);

					if (logger.isTraceEnabled()) {
						logger.trace("Component ID: " + dc.componentKey);
						logger.trace("------------------------------ Dependencies ------------------------------");
					}

					// Get the set of this sbb dependencies
					Set<ComponentID> serviceDependencies = sd
							.getDependenciesSet();

					// Iterate through dependencies set
					for (ComponentID dependencyId : serviceDependencies) {
						// Add the dependency
						dc.dependencies
								.add(getComponentIdAsString(dependencyId));

						if (logger.isTraceEnabled()) {
							logger.trace(getComponentIdAsString(dependencyId));
						}
					}

					if (logger.isTraceEnabled()) {
						logger.trace("--------------------------- End of Dependencies --------------------------");
					}

					dc.installActions.add(new ActivateServiceAction(
							(ServiceID) dc.componentID, sleeContainerDeployer.getSleeContainer().getServiceManagement()));

					dc.uninstallActions.add(new DeactivateServiceAction(
							(ServiceID) dc.componentID, sleeContainerDeployer.getSleeContainer().getServiceManagement()));

					deployableComponents.add(dc);
				}

				return deployableComponents;
			} catch (Exception e) {
				logger.error("", e);
				return null;
			} finally {
				// Clean up!
				if (is != null) {
					try {
						is.close();
					} finally {
						is = null;
					}
				}
			}
		}

		try {
			URL descriptorXML = null;

			// Determine whether the type of this instance is an sbb, event, RA
			// type, etc.
			if ((descriptorXML = duWrapper.getEntry("META-INF/sbb-jar.xml")) != null) {
				if (logger.isTraceEnabled()) {
					logger.trace("Parsing SBB Descriptor.");
				}

				InputStream is = null;

				try {
					is = descriptorXML.openStream();

					// Parse the descriptor using the factory
					SbbDescriptorFactory sbbdf = componentDescriptorFactory
							.getSbbDescriptorFactory();
					List<? extends SbbDescriptor> sbbDescriptors = sbbdf
							.parse(is);

					if (sbbDescriptors.isEmpty()) {
						logger.warn("The "
								+ duWrapper.getFileName()
								+ " deployment descriptor contains no sbb definitions");
						return null;
					}

					for (SbbDescriptor sbbDescriptor : sbbDescriptors) {
						DeployableComponent dc = new DeployableComponent(this,sleeContainerDeployer);

						dc.componentType = SBB_COMPONENT;

						// Get the Component ID
						dc.componentID = sbbDescriptor.getSbbID();

						// Get the Component Key
						dc.componentKey = getComponentIdAsString(dc.componentID);

						if (logger.isTraceEnabled()) {
							logger.trace("Component ID: " + dc.componentKey);
							logger.trace("------------------------------ Dependencies ------------------------------");
						}

						// Get the set of this sbb dependencies
						Set<ComponentID> sbbDependencies = sbbDescriptor
								.getDependenciesSet();

						// Iterate through dependencies set
						for (ComponentID dependencyId : sbbDependencies) {
							// Add the dependency
							dc.dependencies
									.add(getComponentIdAsString(dependencyId));

							if (logger.isTraceEnabled()) {
								logger.trace(getComponentIdAsString(dependencyId));
							}
						}

						// FIXME: This is special case for Links. Maybe it
						// should be treated in SbbDescriptorImpl?
						for (ResourceAdaptorTypeBindingDescriptor raTypeBinding : sbbDescriptor
								.getResourceAdaptorTypeBindings()) {
							for (ResourceAdaptorEntityBindingDescriptor raEntityBinding : raTypeBinding
									.getResourceAdaptorEntityBinding()) {
								String raLink = raEntityBinding
										.getResourceAdaptorEntityLink();

								// Add the dependency
								dc.dependencies.add(raLink);

								if (logger.isTraceEnabled()) {
									logger.trace(raLink);
								}
							}
						}

						if (logger.isTraceEnabled()) {
							logger.trace("--------------------------- End of Dependencies --------------------------");
						}

						deployableComponents.add(dc);
					}
				} catch (Exception e) {
					logger.error("", e);
				} finally {
					// Clean up!
					if (is != null) {
						try {
							is.close();
						} finally {
							is = null;
						}
					}
				}
			} else if ((descriptorXML = duWrapper
					.getEntry("META-INF/profile-spec-jar.xml")) != null) {
				if (logger.isTraceEnabled()) {
					logger.trace("Parsing Profile Specification Descriptor.");
				}

				InputStream is = null;

				try {
					// Get the InputStream
					is = descriptorXML.openStream();

					// Parse the descriptor using the factory
					ProfileSpecificationDescriptorFactory psdf = componentDescriptorFactory
							.getProfileSpecificationDescriptorFactory();
					List<? extends ProfileSpecificationDescriptor> psDescriptors = psdf
							.parse(is);

					// Get a list of the profile specifications in the
					// deployable unit.
					if (psDescriptors.isEmpty()) {
						logger.warn("The "
								+ duWrapper.getFileName()
								+ " deployment descriptor contains no profile-spec definitions");
						return null;
					}

					// Iterate through the profile spec nodes
					for (ProfileSpecificationDescriptor psDescriptor : psDescriptors) {
						DeployableComponent dc = new DeployableComponent(this,sleeContainerDeployer);

						// Set Component Type
						dc.componentType = PROFILESPEC_COMPONENT;

						// Get the Component ID
						dc.componentID = psDescriptor
								.getProfileSpecificationID();

						// Get the Component Key
						dc.componentKey = getComponentIdAsString(dc.componentID);

						if (logger.isTraceEnabled()) {
							logger.trace("Component ID: " + dc.componentKey);
							logger.trace("------------------------------ Dependencies ------------------------------");
						}

						// Get the set of this sbb dependencies
						Set<ComponentID> psDependencies = psDescriptor
								.getDependenciesSet();

						// Iterate through dependencies set
						for (ComponentID dependencyId : psDependencies) {
							// Add the dependency
							dc.dependencies
									.add(getComponentIdAsString(dependencyId));

							if (logger.isTraceEnabled()) {
								logger.trace(getComponentIdAsString(dependencyId));
							}
						}

						if (logger.isTraceEnabled()) {
							logger.trace("--------------------------- End of Dependencies --------------------------");
						}

						deployableComponents.add(dc);
					}
				} catch (Exception e) {
					logger.error("", e);
				} finally {
					// Clean up!
					if (is != null) {
						try {
							is.close();
						} finally {
							is = null;
						}
					}
				}
			} else if ((descriptorXML = duWrapper
					.getEntry("META-INF/event-jar.xml")) != null) {
				if (logger.isTraceEnabled()) {
					logger.trace("Parsing Event Definition Descriptor.");
				}

				InputStream is = null;

				try {
					// Get the InputStream
					is = descriptorXML.openStream();

					// Parse the descriptor using the factory
					EventTypeDescriptorFactory etdf = componentDescriptorFactory
							.getEventTypeDescriptorFactory();
					List<? extends EventTypeDescriptor> etDescriptors = etdf
							.parse(is);

					if (etDescriptors == null || etDescriptors.isEmpty()) {
						logger.warn("The "
								+ duWrapper.getFileName()
								+ " deployment descriptor contains no event-type definitions");
						return null;
					}

					for (EventTypeDescriptor etDescriptor : etDescriptors) {
						DeployableComponent dc = new DeployableComponent(this,sleeContainerDeployer);

						// Set Component Type
						dc.componentType = EVENTTYPE_COMPONENT;

						// Get the Component ID
						dc.componentID = etDescriptor.getEventTypeID();

						// Get the Component Key
						dc.componentKey = getComponentIdAsString(dc.componentID);

						if (logger.isTraceEnabled()) {
							logger.trace("Component ID: " + dc.componentKey);
							logger.trace("------------------------------ Dependencies ------------------------------");
						}

						// Get the set of this sbb dependencies
						Set<ComponentID> etDependencies = etDescriptor
								.getDependenciesSet();

						// Iterate through dependencies set
						for (ComponentID dependencyId : etDependencies) {
							// Add the dependency
							dc.dependencies
									.add(getComponentIdAsString(dependencyId));

							if (logger.isTraceEnabled()) {
								logger.trace(getComponentIdAsString(dependencyId));
							}
						}

						if (logger.isTraceEnabled()) {
							logger.trace("--------------------------- End of Dependencies --------------------------");
						}

						deployableComponents.add(dc);
					}

				} catch (Exception e) {
					logger.error("", e);
				} finally {
					// Clean up!
					if (is != null) {
						try {
							is.close();
						} finally {
							is = null;
						}
					}
				}
			} else if ((descriptorXML = duWrapper
					.getEntry("META-INF/resource-adaptor-type-jar.xml")) != null) {
				if (logger.isTraceEnabled()) {
					logger.trace("Parsing Resource Adaptor Type Descriptor.");
				}

				InputStream is = null;

				try {
					// Get the InputStream
					is = descriptorXML.openStream();

					// Parse the descriptor using the factory
					ResourceAdaptorTypeDescriptorFactory ratdf = componentDescriptorFactory
							.getResourceAdaptorTypeDescriptorFactory();
					List<? extends ResourceAdaptorTypeDescriptor> ratDescriptors = ratdf
							.parse(is);

					if (ratDescriptors == null || ratDescriptors.isEmpty()) {
						logger.warn("The "
								+ duWrapper.getFileName()
								+ " deployment descriptor contains no resource-adaptor-type definitions");
						return null;
					}

					// Go through all the Resource Adaptor Type Elements
					for (ResourceAdaptorTypeDescriptor ratDescriptor : ratDescriptors) {
						DeployableComponent dc = new DeployableComponent(this,sleeContainerDeployer);

						// Set Component Type
						dc.componentType = RATYPE_COMPONENT;

						// Get the Component ID
						dc.componentID = ratDescriptor
								.getResourceAdaptorTypeID();

						// Get the Component Key
						dc.componentKey = getComponentIdAsString(dc.componentID);

						if (logger.isTraceEnabled()) {
							logger.trace("Component ID: " + dc.componentKey);
							logger.trace("------------------------------ Dependencies ------------------------------");
						}

						// Get the set of this sbb dependencies
						Set<ComponentID> ratDependencies = ratDescriptor
								.getDependenciesSet();

						// Iterate through dependencies set
						for (ComponentID dependencyId : ratDependencies) {
							// Add the dependency
							dc.dependencies
									.add(getComponentIdAsString(dependencyId));

							if (logger.isTraceEnabled()) {
								logger.trace(getComponentIdAsString(dependencyId));
							}
						}

						if (logger.isTraceEnabled()) {
							logger.trace("--------------------------- End of Dependencies --------------------------");
						}

						deployableComponents.add(dc);
					}
				} catch (Exception e) {
					logger.error("", e);
				} finally {
					// Clean up!
					if (is != null) {
						try {
							is.close();
						} finally {
							is = null;
						}
					}
				}
			} else if ((descriptorXML = duWrapper
					.getEntry("META-INF/resource-adaptor-jar.xml")) != null) {
				if (logger.isTraceEnabled()) {
					logger.trace("Parsing Resource Adaptor Descriptor.");
				}

				InputStream is = null;

				try {
					// Get the InputStream
					is = descriptorXML.openStream();

					// Parse the descriptor using the factory
					ResourceAdaptorDescriptorFactory radf = componentDescriptorFactory
							.getResourceAdaptorDescriptorFactory();
					List<? extends ResourceAdaptorDescriptor> raDescriptors = radf
							.parse(is);

					DeployConfigParser sleeDeployConfigParser = sleeContainerDeployer.getSLEEDeployConfigParser();

					// Go through all the Resource Adaptor Elements
					for (ResourceAdaptorDescriptor raDescriptor : raDescriptors) {
						DeployableComponent dc = new DeployableComponent(this,sleeContainerDeployer);

						// Set Component Type
						dc.componentType = RA_COMPONENT;

						// Set the Component ID
						dc.componentID = raDescriptor.getResourceAdaptorID();

						// Set the Component Key
						dc.componentKey = getComponentIdAsString(dc.componentID);

						if (logger.isTraceEnabled()) {
							logger.trace("Component ID: " + dc.componentKey);
							logger.trace("------------------------------ Dependencies ------------------------------");
						}

						// Get the set of this sbb dependencies
						Set<ComponentID> raDependencies = raDescriptor
								.getDependenciesSet();

						// Iterate through dependencies set
						for (ComponentID dependencyId : raDependencies) {
							// Add the dependency
							dc.dependencies
									.add(getComponentIdAsString(dependencyId));

							if (logger.isTraceEnabled()) {
								logger.trace(getComponentIdAsString(dependencyId));
							}
						}

						if (logger.isTraceEnabled()) {
							logger.trace("--------------------------- End of Dependencies --------------------------");
						}

						// get management actions for this ra, in SLEE's deploy config
						if (sleeDeployConfigParser != null) {
							Collection<ManagementAction> managementActions = sleeDeployConfigParser.getPostInstallActions().get(dc.getComponentKey());
							if (managementActions != null) {
								dc.installActions.addAll(managementActions);
							}
							managementActions = sleeDeployConfigParser.getPreUninstallActions().get(dc.getComponentKey());
							if (managementActions != null) {
								dc.uninstallActions.addAll(managementActions);
							}
						}
						
						deployableComponents.add(dc);
					}
				} catch (Exception e) {
					logger.error("", e);
				} finally {
					// Clean up!
					if (is != null) {
						try {
							is.close();
						} finally {
							is = null;
						}
					}
				}
			} else if ((descriptorXML = duWrapper
					.getEntry("META-INF/library-jar.xml")) != null) {
				if (logger.isTraceEnabled()) {
					logger.trace("Parsing Library Descriptor.");
				}

				InputStream is = null;

				try {
					// Get the InputStream
					is = descriptorXML.openStream();

					// Parse the descriptor using the factory
					LibraryDescriptorFactory ldf = componentDescriptorFactory
							.getLibraryDescriptorFactory();
					List<? extends LibraryDescriptor> libraryDescriptors = ldf
							.parse(is);

					// Go through all the Resource Adaptor Elements
					for (LibraryDescriptor libraryDescriptor : libraryDescriptors) {
						DeployableComponent dc = new DeployableComponent(this,sleeContainerDeployer);

						// Set Component Type
						dc.componentType = LIBRARY_COMPONENT;

						// Set the Component ID
						dc.componentID = libraryDescriptor.getLibraryID();

						// Set the Component Key
						dc.componentKey = getComponentIdAsString(dc.componentID);

						if (logger.isTraceEnabled()) {
							logger.trace("Component ID: " + dc.componentKey);
							logger.trace("------------------------------ Dependencies ------------------------------");
						}

						// Get the set of this sbb dependencies
						Set<ComponentID> libraryDependencies = libraryDescriptor
								.getDependenciesSet();

						// Iterate through dependencies set
						for (ComponentID dependencyId : libraryDependencies) {
							// Add the dependency
							dc.dependencies
									.add(getComponentIdAsString(dependencyId));

							if (logger.isTraceEnabled()) {
								logger.trace(getComponentIdAsString(dependencyId));
							}
						}

						if (logger.isTraceEnabled()) {
							logger.trace("--------------------------- End of Dependencies --------------------------");
						}

						deployableComponents.add(dc);
					}
				} catch (Exception e) {
					logger.error("", e);
				} finally {
					// Clean up!
					if (is != null) {
						try {
							is.close();
						} finally {
							is = null;
						}
					}
				}
			} else {
				logger.warn("\r\n--------------------------------------------------------------------------------\r\n"
						+ "No Component Descriptor found in '"
						+ duWrapper.getFileName()
						+ "'.\r\n"
						+ "--------------------------------------------------------------------------------");

				return new ArrayList<DeployableComponent>();
			}
		} finally {
		}

		return deployableComponents;
	}

	/**
	 * Getter for Install Actions.
	 * 
	 * @return a Collection of Object[] with the actions needed to install this
	 *         component.
	 */
	public Collection<ManagementAction> getInstallActions() {
		return installActions;
	}

	/**
	 * Getter for Uninstall Actions.
	 * 
	 * @return a Collection of Object[] with the actions needed to uninstall
	 *         this component.
	 */
	public Collection<ManagementAction> getUninstallActions() {
		return uninstallActions;
	}

	/**
	 * Getter for component type.
	 * 
	 * @return an int identifying the component type.
	 */
	public int getComponentType() {
		return componentType;
	}

	/**
	 * Getter for component id.
	 * 
	 * @return the ComponentID for the component.
	 */
	public ComponentID getComponentID() {
		return componentID;
	}

	/**
	 * Getter for the sub components.
	 * 
	 * @return Collection of DeployableComponents
	 */
	public Collection<DeployableComponent> getSubComponents() {
		return this.subComponents;
	}

	private String getComponentIdAsString(ComponentID componentId) {
		ComponentIDPropertyEditor cidPropertyEditor = new ComponentIDPropertyEditor();
		cidPropertyEditor.setValue(componentId);

		return cidPropertyEditor.getAsText();
	}
}