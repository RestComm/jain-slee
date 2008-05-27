/***************************************************
 *                                                 *
 *  Mobicents: The Open Source JSLEE Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/
package org.mobicents.slee.container.management.jmx;

import java.net.InetAddress;

import org.jboss.logging.Logger;
import org.jboss.util.StringPropertyReplacer;

/**
 * SleeBeanShellUtil is a class created in order to let you create simpler
 * auto-deploy scripts.
 * 
 * @author Victor Hugo
 * @author amit.bhayani
 * 
 */
public class SleeBeanShellUtil {
	public Logger log = Logger
			.getLogger(org.mobicents.slee.container.management.jmx.SleeBeanShellUtil.class
					.getName());

	public SleeBeanShellUtil() throws Exception {
		init(null, null);

	}

	public SleeBeanShellUtil(String user, String password) throws Exception {
		init(user, password);
	}

	private void init(String user, String password) throws Exception {
		try {
			InetAddress myIpAddress = InetAddress.getByName(System
					.getProperty("jboss.bind.address"));
			sci = new SleeCommandInterface("jnp://"
					+ myIpAddress.getHostAddress() + ":1099", user, password);

		} catch (Exception e) {
			// Log the error
			log.error("Failed to create instance of SleeCommandInterface", e);
			throw e;
		}
	}

	/**
	 * Installs the service, RA
	 * 
	 * @param url
	 *            URL to the *.jar
	 */
	public Object install(String url) throws Exception {
		Object obj = null;
		try {
			if (url != null)
				obj = sci.invokeOperation("-install", StringPropertyReplacer
						.replaceProperties(url), null, null);
		} catch (java.lang.SecurityException seEx) {			
			// Log the error
			log.error("Security Exception: " + sci.commandBean + "."
					+ sci.commandString + "\n" + seEx.getCause().toString(),
					seEx);
			throw seEx;
		} catch (Exception e) {
			// Log the error
			log.error("Bad result: " + sci.commandBean + "."
					+ sci.commandString + "\n" + e.getCause().toString(), e);
			throw e;
		}
		return obj;
	}

	/**
	 * Uninstall the Service or RA
	 * 
	 * @param url
	 */
	public void unInstall(String url) throws Exception {
		try {
			if (url != null)
				sci.invokeOperation("-uninstall", StringPropertyReplacer
						.replaceProperties(url), null, null);
		} catch (java.lang.SecurityException seEx) {
			// Log the error
			log.error("Security Exception: " + sci.commandBean + "."
					+ sci.commandString + "\n" + seEx.getCause().toString(),
					seEx);
			throw seEx;
		} catch (Exception e) {
			// Log the error
			log.error("Bad result: " + sci.commandBean + "."
					+ sci.commandString + "\n" + e.getCause().toString(), e);
			throw e;
		}

	}

	/**
	 * Get a deployable unit identifier for a deployable unit jar file that has
	 * been installed.
	 * 
	 * @param url
	 *            the url that the deployable unit jar file was installed from
	 * @return a DeployableUnitID that identifies the installed jar file
	 */
	public Object getDeploymentId(String url) throws Exception {
		Object deployableUnitID = null;
		try {
			if (url != null)
				deployableUnitID = sci.invokeOperation("-getDeploymentId",
						StringPropertyReplacer.replaceProperties(url), null,
						null);
		} catch (java.lang.SecurityException seEx) {
			// Log the error
			log.error("Security Exception: " + sci.commandBean + "."
					+ sci.commandString + "\n" + seEx.getCause().toString(),
					seEx);
			throw seEx;
		} catch (Exception e) {
			// Log the error
			log.error("Bad result: " + sci.commandBean + "."
					+ sci.commandString + "\n" + e.getCause().toString(), e);
			throw e;
		}
		return deployableUnitID;
	}

	/**
	 * Get the deployable unit descriptor for a deployable unit.
	 * 
	 * @param deployableUnitID
	 *            the identifier of the deployable unit
	 * @return the deployable unit descriptor for the deployable unit
	 */
	public Object getDescriptor(String deployableUnitID) throws Exception {
		Object deployableUnitDescriptor = null;
		try {
			if (deployableUnitID != null)
				deployableUnitDescriptor = sci.invokeOperation(
						"-getDescriptor", deployableUnitID, null, null);
		} catch (java.lang.SecurityException seEx) {
			// Log the error
			log.error("Security Exception: " + sci.commandBean + "."
					+ sci.commandString + "\n" + seEx.getCause().toString(),
					seEx);
			throw seEx;
		} catch (Exception e) {
			// Log the error
			log.error("Bad result: " + sci.commandBean + "."
					+ sci.commandString + "\n" + e.getCause().toString(), e);
			throw e;
		}
		return deployableUnitDescriptor;
	}

	/**
	 * Activate a Service. The Service must currently be in the
	 * ServiceState.INACTIVE state, and transitions to ServiceState.ACTIVE state
	 * during this method invocation.
	 * 
	 * @param serviceId
	 *            the component identifier of the Service.
	 */
	public void activateService(String serviceId) throws Exception {
		try {
			if (serviceId != null)
				sci.invokeOperation("-activateService", serviceId, null, null);

		} catch (java.lang.SecurityException seEx) {
			// Log the error
			log.error("Security Exception: " + sci.commandBean + "."
					+ sci.commandString + "\n" + seEx.getCause().toString(),
					seEx);
			throw seEx;
		} catch (Exception e) {
			// Log the error
			log.error("Bad result: " + sci.commandBean + "."
					+ sci.commandString + "\n" + e.getCause().toString(), e);
			throw e;
		}

	}

	/**
	 * Deactivate a Service. The Service must currently be in the
	 * ServiceState.ACTIVE state, and transitions to ServiceState.STOPPING state
	 * during this method invocation.
	 * 
	 * @param serviceId
	 *            the component identifier of the Service.
	 */
	public void deactivateService(String serviceId) throws Exception {
		try {
			if (serviceId != null)
				sci
						.invokeOperation("-deactivateService", serviceId, null,
								null);

		} catch (java.lang.SecurityException seEx) {
			// Log the error
			log.error("Security Exception: " + sci.commandBean + "."
					+ sci.commandString + "\n" + seEx.getCause().toString(),
					seEx);
			throw seEx;
		} catch (Exception e) {
			// Log the error
			log.error("Bad result: " + sci.commandBean + "."
					+ sci.commandString + "\n" + e.getCause().toString(), e);
			throw e;
		}

	}

	/**
	 * Get the current state of a Service.
	 * 
	 * @param serviceId
	 *            the component identifier of the Service.
	 * @return the current state of the Service.
	 */
	public Object getServiceState(String serviceId) throws Exception {
		Object serviceState = null;
		try {
			if (serviceId != null)
				serviceState = sci.invokeOperation("-getServiceState",
						serviceId, null, null);

		} catch (java.lang.SecurityException seEx) {
			// Log the error
			log.error("Security Exception: " + sci.commandBean + "."
					+ sci.commandString + "\n" + seEx.getCause().toString(),
					seEx);
			throw seEx;
		} catch (Exception e) {
			// Log the error
			log.error("Bad result: " + sci.commandBean + "."
					+ sci.commandString + "\n" + e.getCause().toString(), e);
			throw e;
		}
		return serviceState;
	}

	/**
	 * @deprecated Set the trace filter level for a particular component. The
	 *             TraceMBean only generates trace notifications if the trace
	 *             level of a trace message generated by a component is equal to
	 *             or greater than the trace filter level set for that
	 *             component.
	 * 
	 * @param componentId
	 *            the identifier of the component
	 * @param traceLevel
	 *            the new trace filter level for the component
	 */
	public void setTraceLevel(String componentId, String traceLevel)
			throws Exception {
		try {
			if (componentId != null && traceLevel != null)
				sci.invokeOperation("-setTraceLevel", componentId, traceLevel,
						null);

		} catch (java.lang.SecurityException seEx) {
			// Log the error
			log.error("Security Exception: " + sci.commandBean + "."
					+ sci.commandString + "\n" + seEx.getCause().toString(),
					seEx);
			throw seEx;
		} catch (Exception e) {
			// Log the error
			log.error("Bad result: " + sci.commandBean + "."
					+ sci.commandString + "\n" + e.getCause().toString(), e);
			throw e;
		}

	}

	/**
	 * @deprecated Get the trace filter level for a particular component.
	 * @param componentId
	 *            the identifier of the component.
	 * @return the trace filter level for the component.
	 */
	public Object getTraceLevel(String componentId) throws Exception {
		Object level = null;
		try {
			if (componentId != null)
				level = sci.invokeOperation("-getTraceLevel", componentId,
						null, null);

		} catch (java.lang.SecurityException seEx) {
			// Log the error
			log.error("Security Exception: " + sci.commandBean + "."
					+ sci.commandString + "\n" + seEx.getCause().toString(),
					seEx);
			throw seEx;
		} catch (Exception e) {
			// Log the error
			log.error("Bad result: " + sci.commandBean + "."
					+ sci.commandString + "\n" + e.getCause().toString(), e);
			throw e;
		}
		return level;
	}

	/**
	 * Create a resource adaptor entity using the specified configuration
	 * properties. The resource adaptor entity is initialized and enters the
	 * ResourceAdaptorEntityState.INACTIVE state before this method returns.
	 * 
	 * @param resourceAdaptorID
	 *            the identifier of the resource adaptor the resource adaptor
	 *            entity should be constructed from.
	 * @param entityName
	 *            the name of the resource adaptor entity to create.
	 * @param propertiesURL
	 *            URL pointing to properties file that must be used to configure RA Entity
	 */
	public void createRaEntity(String resourceAdaptorID, String entityName,
			String propertiesURL) throws Exception {

		try {

			if (resourceAdaptorID != null & entityName != null)
				sci.invokeOperation("-createRaEntity", resourceAdaptorID,
						entityName, propertiesURL);

		} catch (java.lang.SecurityException seEx) {
			// Log the error
			log.error("Security Exception: " + sci.commandBean + "."
					+ sci.commandString + "\n" + seEx.getCause().toString(),
					seEx);
			throw seEx;
		} catch (Exception e) {
			// Log the error
			log.error("Bad result: " + sci.commandBean + "."
					+ sci.commandString + "\n" + e.getCause().toString(), e);
			throw e;
		}

	}

	/**
	 * Activate a resource adaptor entity. The resource adaptor entity must be
	 * in the ResourceAdaptorEntityState.INACTIVE state, and transitions to the
	 * ResourceAdaptorEntityState.ACTIVE state during this method invocation.
	 * 
	 * @param entityName
	 *            the name of the resource adaptor entity.
	 */
	public void activateRaEntity(String entityName) throws Exception {
		try {

			if (entityName != null)
				sci
						.invokeOperation("-activateRaEntity", entityName, null,
								null);

		} catch (java.lang.SecurityException seEx) {
			// Log the error
			log.error("Security Exception: " + sci.commandBean + "."
					+ sci.commandString + "\n" + seEx.getCause().toString(),
					seEx);
			throw seEx;
		} catch (Exception e) {
			// Log the error
			log.error("Bad result: " + sci.commandBean + "."
					+ sci.commandString + "\n" + e.getCause().toString(), e);
			throw e;
		}

	}

	/**
	 * Deactivate a resource adaptor entity. The resource adaptor entity must be
	 * in the ResourceAdaptorEntityState.ACTIVE state, and transitions to the
	 * ResourceAdaptorEntityState.STOPPING state during this method invocation.
	 * The resource adaptor entity spontaneously returns to the
	 * ResourceAdaptorEntityState.INACTIVE state once all activities created by
	 * the resource adaptor entity have ended.
	 * 
	 * @param entityName
	 *            the name of the resource adaptor entity.
	 */
	public void deactivateRaEntity(String entityName) throws Exception {
		try {

			if (entityName != null) {
				sci.invokeOperation("-deactivateRaEntity", entityName, null,
						null);
			}
		} catch (java.lang.SecurityException seEx) {
			// Log the error
			log.error("Security Exception: " + sci.commandBean + "."
					+ sci.commandString + "\n" + seEx.getCause().toString(),
					seEx);
			throw seEx;
		} catch (Exception e) {
			// Log the error
			log.error("Bad result: " + sci.commandBean + "."
					+ sci.commandString + "\n" + e.getCause().toString(), e);
			throw e;
		}

	}

	/**
	 * Remove a resource adaptor entity. The resource adaptor entity must be in
	 * the ResourceAdaptorEntityState.INACTIVE state.
	 * 
	 * @param entityName
	 *            the name of the resource adaptor entity.
	 */
	public void removeRaEntity(String entityName) throws Exception {
		try {

			if (entityName != null) {
				sci.invokeOperation("-removeRaEntity", entityName, null, null);
			}
		} catch (java.lang.SecurityException seEx) {
			// Log the error
			log.error("Security Exception: " + sci.commandBean + "."
					+ sci.commandString + "\n" + seEx.getCause().toString(),
					seEx);
			throw seEx;
		} catch (Exception e) {
			// Log the error
			log.error("Bad result: " + sci.commandBean + "."
					+ sci.commandString + "\n" + e.getCause().toString(), e);
			throw e;
		}
	}

	/**
	 * Bind a resource adaptor entity to a link name. Link names are used to
	 * establish the bindings between SBBs and resource adaptor entities.
	 * 
	 * @param linkName
	 *            the link name. The name must be unique within the scope of the
	 *            SLEE.
	 * @param entityName
	 *            the name of the resource adaptor entity.
	 */
	public void createRaLink(String linkName, String entityName)
			throws Exception {
		try {

			if (linkName != null & entityName != null)
				sci
						.invokeOperation("-createRaLink", linkName, entityName,
								null);

		} catch (java.lang.SecurityException seEx) {
			// Log the error
			log.error("Security Exception: " + sci.commandBean + "."
					+ sci.commandString + "\n" + seEx.getCause().toString(),
					seEx);
			throw seEx;
		} catch (Exception e) {
			// Log the error
			log.error("Bad result: " + sci.commandBean + "."
					+ sci.commandString + "\n" + e.getCause().toString(), e);
			throw e;
		}

	}

	/**
	 * Remove a link name binding from a resource adaptor entity.
	 * 
	 * @param linkName
	 *            the link name.
	 */
	public void removeRaLink(String linkName) throws Exception {
		try {
			if (linkName != null)
				sci.invokeOperation("-removeRaLink", linkName, null, null);

		} catch (java.lang.SecurityException seEx) {
			// Log the error
			log.error("Security Exception: " + sci.commandBean + "."
					+ sci.commandString + "\n" + seEx.getCause().toString(),
					seEx);
			throw seEx;
		} catch (Exception e) {
			// Log the error
			log.error("Bad result: " + sci.commandBean + "."
					+ sci.commandString + "\n" + e.getCause().toString(), e);
			throw e;
		}

	}

	/**
	 * Installing and Activating a service
	 * 
	 * @param url
	 *            File URL
	 * @param serviceId
	 *            ServiceID[Name#Vendor#Version]
	 */
	public void deployService(String url, String serviceId) throws Exception {

		install(url);
		activateService(serviceId);

	}

	/**
	 * Deploying a Resource Adaptor
	 * 
	 * @param raTypeURL
	 *            Resource Adaptor Type file url
	 * @param raURL
	 *            Resource Adaptor file url
	 * @param raID
	 *            ResourceAdaptorID[Name#Vendor#Version]
	 * @param raName
	 *            Resorce Adaptor Name
	 * @param linkName
	 *            Optional
	 * @param props
	 *            Optional
	 */
	public void deployRa(String raTypeURL, String raURL, String raID,
			String raName, String linkName, String props) throws Exception {

		install(raTypeURL);
		install(raURL);
		createRaEntity(raID, raName, props);
		activateRaEntity(raName);
		createRaLink(linkName, raName);
	}

	public void undeployService(String url, String serviceId) throws Exception {
		deactivateService(serviceId);
		unInstall(url);
	}

	public void undeployRa(String raTypeURL, String raURL, String raName,
			String linkName) throws Exception {

		removeRaLink(linkName);
		deactivateRaEntity(raName);
		removeRaEntity(raName);
		unInstall(raURL);
		unInstall(raTypeURL);
	}

	/**
	 * Creating a Profile Table
	 * 
	 * @param profileSpecID
	 *            ProfileSpecificationID[Name#Vendor#Version]
	 * @param profileTableName
	 *            Profile Table Name
	 */
	public void createProfileTable(String profileSpecID, String profileTableName)
			throws Exception {

		try {
			sci.invokeOperation("-createProfileTable", profileSpecID,
					profileTableName, null);

		} catch (java.lang.SecurityException seEx) {
			// Log the error
			log.error("Security Exception: " + sci.commandBean + "."
					+ sci.commandString + "\n" + seEx.getCause().toString(),
					seEx);
			throw seEx;
		} catch (Exception e) {
			// Log the error
			log.warn("Bad result: " + sci.commandBean + "." + sci.commandString
					+ "\n" + e.getCause().toString());
			throw e;
		}
	}

	/**
	 * Remove a profile table.
	 * 
	 * @param profileTableName
	 *            the name of the profile table to remove.
	 */
	public void removeProfileTable(String profileTableName) throws Exception {
		try {
			sci.invokeOperation("-removeProfileTable", profileTableName, null,
					null);

		} catch (java.lang.SecurityException seEx) {
			// Log the error
			log.error("Security Exception: " + sci.commandBean + "."
					+ sci.commandString + "\n" + seEx.getCause().toString(),
					seEx);
			throw seEx;
		} catch (Exception e) {
			// Log the error
			log.error("Bad result: " + sci.commandBean + "."
					+ sci.commandString + "\n" + e.getCause().toString(), e);
			throw e;
		}
	}

	/**
	 * 
	 * @param profileTableName
	 * @param profileName
	 * @return Object that will be used in the auto-deploy to do the setting and
	 *         the committing of the profile.
	 */
	public Object createProfile(String profileTableName, String profileName)
			throws Exception {
		Object profileObject = null;

		try {
			profileObject = sci.invokeOperation("-createProfile",
					profileTableName, profileName, null);

		} catch (java.lang.SecurityException seEx) {
			// Log the error
			log.error("Security Exception: " + sci.commandBean + "."
					+ sci.commandString + "\n" + seEx.getCause().toString(),
					seEx);
			throw seEx;
		} catch (Exception e) {
			// Log the error
			log.error("Bad result: " + sci.commandBean + "."
					+ sci.commandString + "\n" + e.getCause().toString(), e);
			throw e;
		}

		return profileObject;
	}

	/**
	 * Remove a profile from a profile table.
	 * 
	 * @param profileTableName
	 *            the name of the profile table to remove the profile from.
	 * @param profileName
	 *            the name of the profile to remove.
	 */
	public void removeProfile(String profileTableName, String profileName)
			throws Exception {
		try {
			sci.invokeOperation("-removeProfile", profileTableName,
					profileName, null);

		} catch (java.lang.SecurityException seEx) {
			// Log the error
			log.error("Security Exception: " + sci.commandBean + "."
					+ sci.commandString + "\n" + seEx.getCause().toString(),
					seEx);
			throw seEx;
		} catch (Exception e) {
			// Log the error
			log.error("Bad result: " + sci.commandBean + "."
					+ sci.commandString + "\n" + e.getCause().toString(), e);
			throw e;
		}
	}

	private SleeCommandInterface sci;
}