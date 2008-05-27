/***************************************************
 *                                                 *
 *  Mobicents: The Open Source JSLEE Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/
package org.mobicents.slee.container.management.jmx;

import java.util.Properties;

import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.management.StandardMBean;
import javax.slee.CreateException;
import javax.slee.InvalidArgumentException;
import javax.slee.InvalidStateException;
import javax.slee.management.DependencyException;
import javax.slee.management.LinkNameAlreadyBoundException;
import javax.slee.management.ManagementException;
import javax.slee.management.ResourceAdaptorEntityAlreadyExistsException;
import javax.slee.management.ResourceAdaptorEntityState;
import javax.slee.management.ResourceManagementMBean;
import javax.slee.management.UnrecognizedLinkNameException;
import javax.slee.management.UnrecognizedResourceAdaptorEntityException;
import javax.slee.management.UnrecognizedResourceAdaptorException;
import javax.slee.resource.ResourceAdaptorID;
import javax.slee.resource.ResourceException;

import org.jboss.logging.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.ResourceAdaptorIDImpl;

/**
 * 
 * @author Stefano Zappaterra
 * 
 */
public class ResourceManagementMBeanImpl extends StandardMBean implements
		ResourceManagementMBean {
	public final String OBJECT_NAME = "slee:name=ResourceAdaptorManagement";

	private ObjectName objectName;

	private static final Logger logger = Logger
			.getLogger(ResourceManagementMBeanImpl.class);

	public ResourceManagementMBeanImpl() throws NotCompliantMBeanException {
		super(ResourceManagementMBean.class);
		try {
			objectName = new ObjectName(OBJECT_NAME);
		} catch (Exception ex) {
			throw new NotCompliantMBeanException("Object name is malformed : "
					+ OBJECT_NAME);
		}
	}

	public ObjectName getObjectName() {
		return this.objectName;
	}

	public void createResourceAdaptorEntity(ResourceAdaptorID id,
			String entityName, Properties properties)
			throws java.lang.NullPointerException, InvalidArgumentException,
			UnrecognizedResourceAdaptorException,
			ResourceAdaptorEntityAlreadyExistsException, ResourceException,
			ManagementException {
		if(logger.isDebugEnabled()) {
			logger.debug("Creating RA Entity. Id: " + id
					+ ", name: " + entityName + ", properties: " + properties);
		}
		try {
			SleeContainer.lookupFromJndi().createResourceAdaptorEntity((ResourceAdaptorIDImpl) id,
					entityName, properties);
			logger.info("Created RA Entity. Id: " + id
					+ ", name: " + entityName + ", properties: " + properties);
		} catch (CreateException e) {
			logger.error("Error creating RA Entity. Id: " + id
					+ ", name: " + entityName + ", properties: " + properties,e);
			throw new ManagementException(e.getMessage(), e);
		}
	}

	/*
	 * public void createResourceAdaptorEntity(String raIDString, String name,
	 * String purl) throws Exception { SleeContainer container =
	 * SleeContainer.lookupFromJndi(); URL propsURL = new URL(purl); InputStream
	 * is = propsURL.openStream(); Properties props = new Properties();
	 * props.load(is); ComponentKey ckey = new ComponentKey(raIDString);
	 * ResourceAdaptorIDImpl raID = new ResourceAdaptorIDImpl(ckey);
	 * container.createResourceAdaptorEntity((ResourceAdaptorIDImpl) raID, name,
	 * props); is.close(); }
	 */

	public void removeResourceAdaptorEntity(String entityName)
			throws java.lang.NullPointerException,
			UnrecognizedResourceAdaptorEntityException, InvalidStateException,
			DependencyException, ManagementException {
		if(logger.isDebugEnabled()) {
			logger.debug("Removing RA Entity " + entityName);
		}
		SleeContainer.lookupFromJndi().removeResourceAdaptorEntity(entityName);
		logger.info("Removed RA Entity " + entityName);
	}

	public void updateConfigurationProperties(String entityName,
			Properties properties) throws java.lang.NullPointerException,
			UnrecognizedResourceAdaptorEntityException, InvalidStateException,
			InvalidArgumentException, ResourceException, ManagementException {
		if(logger.isDebugEnabled()) {
			logger.debug("Updating RA Entity with properties: "
				+ properties);
		}
		SleeContainer.lookupFromJndi().updateConfigurationProperties(entityName, properties);
		logger.info("Updated RA Entity with properties: "
				+ properties);
	}

	public void activateResourceAdaptorEntity(String entityName)
			throws java.lang.NullPointerException,
			UnrecognizedResourceAdaptorEntityException, InvalidStateException,
			ResourceException, ManagementException {
		if(logger.isDebugEnabled()) {
			logger.debug("Activating RA Entity "
					+ entityName);
		}
		SleeContainer.lookupFromJndi().activateResourceAdaptorEntity(entityName);
		logger.info("Activated RA Entity "
				+ entityName);
	}

	public void deactivateResourceAdaptorEntity(String name)
			throws java.lang.NullPointerException,
			UnrecognizedResourceAdaptorEntityException, InvalidStateException,
			ManagementException {
		if(logger.isDebugEnabled()) {
			logger.debug("Deactivating RA Entity "
					+ name);
		}
		SleeContainer.lookupFromJndi().deactivateResourceAdaptorEntity(name);
		logger.info("Deactivated RA Entity " + name);
	}

	public void bindLinkName(String entityName, String linkName)
			throws NullPointerException, InvalidArgumentException,
			UnrecognizedResourceAdaptorEntityException,
			LinkNameAlreadyBoundException, ManagementException {
		if(logger.isDebugEnabled()) {
			logger.debug("Creating Link between RA Entity " + entityName
				+ " and Name " + linkName);
		}
		SleeContainer.lookupFromJndi().createResourceAdaptorEntityLink(linkName, entityName);
		logger.info("Created Link between RA Entity " + entityName
				+ " and Name " + linkName);
	}

	public Properties getConfigurationProperties(ResourceAdaptorID id)
			throws NullPointerException,
			javax.slee.management.UnrecognizedResourceAdaptorException,
			ManagementException {
		return SleeContainer.lookupFromJndi().getRAProperties(id);
	}

	public Properties getConfigurationProperties(String entityName)
			throws NullPointerException,
			UnrecognizedResourceAdaptorEntityException, ManagementException {
		return SleeContainer.lookupFromJndi().getRAEntityProperties(entityName);
	}

	public String[] getLinkNames() throws ManagementException {
		return SleeContainer.lookupFromJndi().getResourceAdaptorEntityLinks();
	}

	public String[] getLinkNames(String entityName)
			throws NullPointerException,
			UnrecognizedResourceAdaptorEntityException, ManagementException {
		return SleeContainer.lookupFromJndi().getResourceAdaptorEntityLinks(entityName);
	}

	public ResourceAdaptorID getResourceAdaptor(String entityName)
			throws NullPointerException,
			UnrecognizedResourceAdaptorEntityException, ManagementException {
		return SleeContainer.lookupFromJndi().getResourceAdaptorID(entityName);
	}

	public String[] getResourceAdaptorEntities() throws ManagementException {
		return SleeContainer.lookupFromJndi().getResourceAdaptorEntityNames();
	}

	public String[] getResourceAdaptorEntities(ResourceAdaptorID id)
			throws NullPointerException,
			javax.slee.management.UnrecognizedResourceAdaptorException,
			ManagementException {
		return SleeContainer.lookupFromJndi().getResourceAdaptorEntityNames(id);
	}

	public String[] getResourceAdaptorEntities(ResourceAdaptorEntityState state)
			throws NullPointerException, ManagementException {
		return SleeContainer.lookupFromJndi().getResourceAdaptorEntities(state);
	}

	public String[] getResourceAdaptorEntities(String[] linkNames)
			throws NullPointerException, ManagementException {
		return SleeContainer.lookupFromJndi().getResourceAdaptorEntityNames(linkNames);
	}

	public String getResourceAdaptorEntity(String linkName)
			throws NullPointerException, UnrecognizedLinkNameException,
			ManagementException {
		return SleeContainer.lookupFromJndi().getResourceAdaptorEntityName(linkName);
	}

	public ResourceAdaptorEntityState getState(String entityName)
			throws NullPointerException,
			UnrecognizedResourceAdaptorEntityException, ManagementException {
		return SleeContainer.lookupFromJndi().getResourceAdaptorEntity(entityName).getState();
	}

	public void unbindLinkName(String linkName) throws NullPointerException,
			UnrecognizedLinkNameException, DependencyException,
			ManagementException {
		if(logger.isDebugEnabled()) {
			logger.debug("Removing RA Entity Link " + linkName);
		}
		SleeContainer.lookupFromJndi().removeResourceAdaptorEntityLink(linkName);
		logger.info("Removed RA Entity Link " + linkName);
	}
}
