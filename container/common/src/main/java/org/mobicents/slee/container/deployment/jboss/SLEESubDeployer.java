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
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.slee.InvalidStateException;
import javax.slee.management.DependencyException;

import org.apache.log4j.Logger;
import org.jboss.deployment.DeploymentException;
import org.jboss.deployment.SubDeployerSupport;
import org.mobicents.slee.container.component.du.DeployableUnitDescriptor;
import org.mobicents.slee.container.component.du.DeployableUnitDescriptorFactory;

/**
 * This is the Deployer main class where the AS will invoke the lifecycle
 * methods for new deployments.
 * 
 * @author Alexandre Mendonça
 * @author martins
 * @version 1.0
 */
@SuppressWarnings("deprecation")
public class SLEESubDeployer extends SubDeployerSupport implements
		SLEESubDeployerMBean {
	
	// Constants -----------------------------------------------------

	// The Logger.
	private static Logger logger = Logger.getLogger(SLEESubDeployer.class);

	// Attributes ----------------------------------------------------

	// The DIs to be accepted by this deployer.
	private ConcurrentHashMap<String, DeployableUnitWrapper> toAccept = new ConcurrentHashMap<String, DeployableUnitWrapper>();

	// Deployable Units present.
	private ConcurrentHashMap<String, DeployableUnit> deployableUnits = new ConcurrentHashMap<String, DeployableUnit>();

	private CopyOnWriteArrayList<String> undeploys = new CopyOnWriteArrayList<String>();

	private final SleeContainerDeployerImpl sleeContainerDeployer;

	public SLEESubDeployer(SleeContainerDeployerImpl sleeContainerDeployer) {
		this.sleeContainerDeployer = sleeContainerDeployer;
		setSuffixes(new String[] { "xml", "jar" });
	}

	// Constructors
	// -------------------------------------------------------------
	
	/**
	 * Method for deciding whether or not to accept the file.
	 */
	public boolean accepts(URL deployableUnitURL) {
		DeployableUnitWrapper du = new DeployableUnitWrapper(deployableUnitURL);

		URL url = du.getUrl();

		if (logger.isTraceEnabled()) {
			logger.trace("Method accepts called for " + url);
		}

		try {
			String fullPath = url.getFile();
			String fileName = fullPath.substring(fullPath.lastIndexOf('/') + 1,
					fullPath.length());

			// Is it in the toAccept list ? Direct accept.
			if (toAccept.containsKey(fileName)) {
				if (logger.isTraceEnabled()) {
					logger.trace("Accepting " + url.toString() + ".");
				}

				return true;
			}
			// If not it the accept list but it's a jar might be a DU jar...
			else if (fileName.endsWith(".jar")) {
				JarFile duJarFile = null;

				try {
					// Try to obtain the DU descriptor, if we got it, we're
					// accepting it!
					if (du.getEntry("META-INF/deployable-unit.xml") != null) {
						if (logger.isTraceEnabled()) {
							logger.trace("Accepting " + url.toString() + ".");
						}

						return true;
					}
				} finally {
					// Clean up!
					if (duJarFile != null) {
						try {
							duJarFile.close();
						} catch (IOException ignore) {
						} finally {
							duJarFile = null;
						}
					}
				}
			}
		} catch (Exception ignore) {
			// Ignore.. will reject.
		}

		// Uh-oh.. looks like it will stay outside.
		return false;
	}

	/**
	 * Initializer method for accepted files. Will parse descriptors at this
	 * point.
	 */
	public void init(URL deployableUnitURL) throws DeploymentException {
		URL url = deployableUnitURL;
		DeployableUnitWrapper du = new DeployableUnitWrapper(deployableUnitURL);

		if (logger.isTraceEnabled()) {
			logger.trace("Method init called for " + deployableUnitURL);
		}

		// Get the full path and filename for this du
		String fullPath = du.getFullPath();
		String fileName = du.getFileName();

		try {
			DeployableUnitWrapper duWrapper = null;

			// If we're able to remove it from toAccept was because it was
			// there!
			if ((duWrapper = toAccept.remove(fileName)) != null) {
				// Create a new Deployable Component from this DI.
				DeployableComponent dc = new DeployableComponent(du, url,
						fileName, sleeContainerDeployer);

				// Also get the deployable unit for this (it exists, we've
				// checked!)
				DeployableUnit deployerDU = deployableUnits.get(duWrapper
						.getFileName());

				for (DeployableComponent subDC : dc.getSubComponents()) {
					// Add the sub-component to the DU object.
					deployerDU.addComponent(subDC);
				}
			}
			// If the DU for this component doesn't exists.. it's a new DU!
			else if (fileName.endsWith(".jar")) {
				JarFile duJarFile = null;

				try {
					// Get a reference to the DU jar file
					duJarFile = new JarFile(fullPath);

					// Try to get the Deployable Unit descriptor
					JarEntry duXmlEntry = duJarFile
							.getJarEntry("META-INF/deployable-unit.xml");

					// Got descriptor?
					if (duXmlEntry != null) {
						// Create a new Deployable Unit object.
						DeployableUnit deployerDU = new DeployableUnit(du,sleeContainerDeployer);

						// Let's parse the descriptor to see what we've got...
						DeployableUnitDescriptorFactory dudf = sleeContainerDeployer
								.getSleeContainer().getComponentManagement()
								.getDeployableUnitManagement()
								.getDeployableUnitDescriptorFactory();
						DeployableUnitDescriptor duDesc = dudf.parse(duJarFile
								.getInputStream(duXmlEntry));

						// If the filename is present, an undeploy in on the way... let's wait
						while(deployableUnits.containsKey(fileName)) {
							Thread.sleep(getWaitTimeBetweenOperations());
						}
						
						// Add it to the deployable units map.
						deployableUnits.put(fileName, deployerDU);

						// Go through each jar entry in the DU descriptor
						for (String componentJarName : duDesc.getJarEntries()) {
							// Might have path... strip it!
							int beginIndex;

							if ((beginIndex = componentJarName.lastIndexOf('/')) == -1)
								beginIndex = componentJarName.lastIndexOf('\\');

							beginIndex++;

							// Got a clean jar name, no paths.
							componentJarName = componentJarName.substring(
									beginIndex, componentJarName.length());

							// Put it in the accept list.
							toAccept.put(componentJarName, du);
						}

						// Do the same as above... but for services
						for (String serviceXMLName : duDesc.getServiceEntries()) {
							// Might have path... strip it!
							int beginIndex;

							if ((beginIndex = serviceXMLName.lastIndexOf('/')) == -1)
								beginIndex = serviceXMLName.lastIndexOf('\\');

							beginIndex++;

							// Got a clean XML filename
							serviceXMLName = serviceXMLName.substring(
									beginIndex, serviceXMLName.length());

							// Add it to the accept list.
							toAccept.put(serviceXMLName, du);
						}
					}
				} finally {
					// Clean up!
					if (duJarFile != null) {
						try {
							duJarFile.close();
						} catch (IOException ignore) {
						} finally {
							duJarFile = null;
						}
					}
				}
			}
		} catch (Exception e) {
			// Something went wrong...
			logger.error("Deployment of " + fileName + " failed. ", e);

			return;
		}
	}

	/**
	 * This is where the fun begins. Time to deploy!
	 */
	public void start(URL deployableUnitURL) throws DeploymentException {
		DeployableUnitWrapper du = new DeployableUnitWrapper(deployableUnitURL);

		if (logger.isTraceEnabled()) {
			logger.trace("Method start called for " + du.getUrl());
		}

		try {
			// Get the deployable unit object
			DeployableUnit realDU = deployableUnits.get(du.getFileName());

			// If it exists, install it.
			if (realDU != null) {
				while (isInUndeployList(du.getFileName())) {
					Thread.sleep(getWaitTimeBetweenOperations());
				}
				sleeContainerDeployer.getDeploymentManager().installDeployableUnit(realDU);
			}
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	/**
	 * Fun has ended. Time to undeploy.
	 */
	public void stop(URL deployableUnitURL) throws DeploymentException {
		
		if (logger.isTraceEnabled()) {
			logger.trace("stop( deployableUnitURL = : " + deployableUnitURL+" )");
		}
		
		DeployableUnitWrapper du = new DeployableUnitWrapper(deployableUnitURL);

		DeployableUnit realDU = null;

		String fileName = du.getFileName();

		if ((realDU = deployableUnits.get(du.getFileName())) != null) {
			if (logger.isTraceEnabled()) {
				logger.trace("Got DU: " + realDU.getDeploymentInfoShortName());
			}

			if (!isInUndeployList(fileName)) {
				addToUndeployList(fileName);
			}

			try {
				// Uninstall it
				sleeContainerDeployer.getDeploymentManager().uninstallDeployableUnit(realDU);
				// Remove it from list if successful
				deployableUnits.remove(fileName);
				removeFromUndeployList(fileName);				
			} catch (DependencyException e) {
				// ignore, will be tried again once there is another undeployment
			} catch (Exception e) {
				Throwable cause = e.getCause();
				if (cause instanceof InvalidStateException) {
					logger.warn(cause.getLocalizedMessage() + "... WAITING ...");
				} else if (e instanceof DeploymentException) {
					throw new IllegalStateException(e.getLocalizedMessage(), e);
				} else {
					logger.error(e.getMessage(), e);
				}				
			}						
		}
	}

	

	// MBean Operations
	// ---------------------------------------------------------

	/**
	 * MBean operation for getting Deployer status.
	 */
	public String showStatus() throws DeploymentException {
		String output = "";

		output += "<p>Deployable Units List:</p>";

		for (String key : deployableUnits.keySet()) {
			output += "&lt;" + key + "&gt; [" + deployableUnits.get(key)
					+ "]<br>";

			for (String duComponent : deployableUnits.get(key).getComponents()) {
				output += "+-- " + duComponent + "<br>";
			}
		}

		output += "<p>To Accept List:</p>";

		for (String key : toAccept.keySet()) {
			output += "&lt;" + key + "&gt; [" + toAccept.get(key) + "]<br>";
		}

		output += "<p>Undeployments running:</p>";

		for (String undeploy : undeploys) {
			output += "+-- " + undeploy + "<br>";
		}

		output += "<p>Deployment Manager Status</p>";

		output += sleeContainerDeployer.getDeploymentManager().showStatus();

		return output;
	}

	/**
	 * MBean WaitTimeBetweenOperations property getter
	 */
	public long getWaitTimeBetweenOperations() {
		return sleeContainerDeployer.getDeploymentManager().waitTimeBetweenOperations;
	}

	/**
	 * MBean WaitTimeBetweenOperations property getter
	 */
	public void setWaitTimeBetweenOperations(long waitTime) {
		sleeContainerDeployer.getDeploymentManager().waitTimeBetweenOperations = waitTime;
	}

	// Aux Functions
	// ------------------------------------------------------------

	private boolean addToUndeployList(String du) {
		if (logger.isTraceEnabled()) {
			logger.trace("Adding " + du + " to running undeployments list ...");
			logger.trace("Current Undeploy List: " + undeploys.toString());
		}
		boolean added = undeploys.add(du);
		if (logger.isTraceEnabled()) {
			logger.trace("Added  " + du + " to running undeployments list = "
					+ added);
			logger.trace("Current Undeploy List: " + undeploys.toString());
		}
		return added;
	}

	private boolean removeFromUndeployList(String du) {
		if (logger.isTraceEnabled()) {
			logger.trace("Removing " + du
					+ " from running undeployments list ...");
			logger.trace("Current Undeploy List: " + undeploys.toString());
		}
		boolean removed = undeploys.remove(du);
		if (logger.isTraceEnabled()) {
			logger.trace("Removed  " + du
					+ " from running undeployments list = " + removed);
			logger.trace("Current Undeploy List: " + undeploys.toString());
		}
		return removed;
	}

	private boolean isInUndeployList(String du) {
		if (logger.isTraceEnabled()) {
			logger.trace("Checking if " + du
					+ " is in running undeployments list ...");
			logger.trace("Current Undeploy List: " + undeploys.toString());
		}
		boolean contains = undeploys.contains(du);
		if (logger.isTraceEnabled()) {
			logger.trace("Checked  if " + du
					+ " is in running undeployments list = " + contains);
			logger.trace("Current Undeploy List: " + undeploys.toString());
		}
		return contains;
	}

}