/***************************************************
 *                                                 *
 *  Mobicents: The Open Source JSLEE Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/
package org.mobicents.slee.container.management.jmx;

import java.util.Arrays;

import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.management.StandardMBean;
import javax.slee.InvalidArgumentException;
import javax.slee.InvalidStateException;
import javax.slee.SbbID;
import javax.slee.management.DependencyException;
import javax.slee.management.LinkNameAlreadyBoundException;
import javax.slee.management.ManagementException;
import javax.slee.management.ResourceAdaptorEntityAlreadyExistsException;
import javax.slee.management.ResourceAdaptorEntityState;
import javax.slee.management.ResourceManagementMBean;
import javax.slee.management.UnrecognizedLinkNameException;
import javax.slee.management.UnrecognizedResourceAdaptorEntityException;
import javax.slee.management.UnrecognizedResourceAdaptorException;
import javax.slee.resource.ConfigProperties;
import javax.slee.resource.InvalidConfigurationException;
import javax.slee.resource.ResourceAdaptorID;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;

/**
 * 
 * @author Stefano Zappaterra
 * @author Eduardo Martins
 * 
 */
public class ResourceManagementMBeanImpl extends StandardMBean implements
		ResourceManagementMBean {

	private final static Logger logger = Logger
			.getLogger(ResourceManagementMBeanImpl.class);

	public final String OBJECT_NAME = "slee:name=ResourceAdaptorManagement";

	private ObjectName objectName;

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
		return objectName;
	}

	// ------- MANAGEMENT OPERATIONS

	public void createResourceAdaptorEntity(ResourceAdaptorID id,
			String entityName, ConfigProperties properties)
			throws NullPointerException, InvalidArgumentException,
			UnrecognizedResourceAdaptorException,
			ResourceAdaptorEntityAlreadyExistsException,
			InvalidConfigurationException, ManagementException {
		try {
			SleeContainer.lookupFromJndi().getResourceManagement()
					.createResourceAdaptorEntity(id, entityName, properties);
		} catch (NullPointerException e) {
			throw e;
		} catch (InvalidArgumentException e) {
			throw e;
		} catch (UnrecognizedResourceAdaptorException e) {
			throw e;
		} catch (ResourceAdaptorEntityAlreadyExistsException e) {
			throw e;
		} catch (InvalidConfigurationException e) {
			throw e;
		} catch (Throwable e) {
			String s = "failed to create RA entity with name " + entityName;
			logger.error(s, e);
			throw new ManagementException(s, e);
		}
	}

	public void activateResourceAdaptorEntity(String entityName)
			throws NullPointerException,
			UnrecognizedResourceAdaptorEntityException, InvalidStateException,
			ManagementException {
		try {
			SleeContainer.lookupFromJndi().getResourceManagement()
					.activateResourceAdaptorEntity(entityName);
		} catch (NullPointerException e) {
			throw e;
		} catch (UnrecognizedResourceAdaptorEntityException e) {
			throw e;
		} catch (InvalidStateException e) {
			throw e;
		} catch (Throwable e) {
			String s = "failed to activate RA entity with name " + entityName;
			logger.error(s, e);
			throw new ManagementException(s, e);
		}
	}

	public void deactivateResourceAdaptorEntity(String entityName)
			throws NullPointerException,
			UnrecognizedResourceAdaptorEntityException, InvalidStateException,
			ManagementException {
		try {
			SleeContainer.lookupFromJndi().getResourceManagement()
					.deactivateResourceAdaptorEntity(entityName);
		} catch (NullPointerException e) {
			throw e;
		} catch (UnrecognizedResourceAdaptorEntityException e) {
			throw e;
		} catch (InvalidStateException e) {
			throw e;
		} catch (Throwable e) {
			String s = "failed to deactivate RA entity with name " + entityName;
			logger.error(s, e);
			throw new ManagementException(s, e);
		}
	}

	public void removeResourceAdaptorEntity(String entityName)
			throws NullPointerException,
			UnrecognizedResourceAdaptorEntityException, InvalidStateException,
			DependencyException, ManagementException {
		try {
			final SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
			synchronized (sleeContainer.getManagementMonitor()) {
				sleeContainer.getResourceManagement()
						.removeResourceAdaptorEntity(entityName);
			}
		} catch (NullPointerException e) {
			throw e;
		} catch (UnrecognizedResourceAdaptorEntityException e) {
			throw e;
		} catch (InvalidStateException e) {
			throw e;
		} catch (DependencyException e) {
			throw e;
		} catch (Throwable e) {
			String s = "failed to remove RA entity with name " + entityName;
			logger.error(s, e);
			throw new ManagementException(s, e);
		}
	}

	public void updateConfigurationProperties(String entityName,
			ConfigProperties properties) throws NullPointerException,
			UnrecognizedResourceAdaptorEntityException, InvalidStateException,
			InvalidConfigurationException, ManagementException {
		try {
			SleeContainer.lookupFromJndi().getResourceManagement()
					.updateConfigurationProperties(entityName, properties);
		} catch (NullPointerException e) {
			throw e;
		} catch (UnrecognizedResourceAdaptorEntityException e) {
			throw e;
		} catch (InvalidStateException e) {
			throw e;
		} catch (InvalidConfigurationException e) {
			throw e;
		} catch (Throwable e) {
			String s = "failed to update configuration properties for RA entity with name "
					+ entityName;
			logger.error(s, e);
			throw new ManagementException(s, e);
		}
	}

	public void bindLinkName(String entityName, String linkName)
			throws NullPointerException, InvalidArgumentException,
			UnrecognizedResourceAdaptorEntityException,
			LinkNameAlreadyBoundException, ManagementException {
		try {
			SleeContainer.lookupFromJndi().getResourceManagement().bindLinkName(linkName,
						entityName);
		} catch (NullPointerException e) {
			throw e;
		} catch (UnrecognizedResourceAdaptorEntityException e) {
			throw e;
		} catch (LinkNameAlreadyBoundException e) {
			throw e;
		} catch (InvalidArgumentException e) {
			throw e;
		} catch (Throwable e) {
			String s = "failed to bind link name " + linkName
					+ " to RA entity with name " + entityName;
			logger.error(s, e);
			throw new ManagementException(s, e);
		}
	}

	public void unbindLinkName(String linkName) throws NullPointerException,
			UnrecognizedLinkNameException, DependencyException,
			ManagementException {
		try {
			SleeContainer.lookupFromJndi().getResourceManagement().unbindLinkName(linkName);
		} catch (NullPointerException e) {
			throw e;
		} catch (UnrecognizedLinkNameException e) {
			throw e;
		} catch (DependencyException e) {
			throw e;
		} catch (Throwable e) {
			String s = "failed to unbind link name " + linkName;
			logger.error(s, e);
			throw new ManagementException(s, e);
		}
	}

	public String[] getResourceAdaptorEntities(ResourceAdaptorEntityState state)
			throws NullPointerException, ManagementException {
		try {
			return SleeContainer.lookupFromJndi().getResourceManagement()
					.getResourceAdaptorEntities(state);
		} catch (NullPointerException e) {
			throw e;
		} catch (Throwable e) {
			String s = "failed to get RA entities with state " + state;
			logger.error(s, e);
			throw new ManagementException(s, e);
		}
	}

	public String[] getLinkNames() throws ManagementException {
		try {
			return SleeContainer.lookupFromJndi().getResourceManagement()
					.getLinkNames();
		} catch (Throwable e) {
			String s = "failed to get link names";
			logger.error(s, e);
			throw new ManagementException(s, e);
		}
	}

	public String[] getLinkNames(String entityName)
			throws NullPointerException,
			UnrecognizedResourceAdaptorEntityException, ManagementException {
		try {
			return SleeContainer.lookupFromJndi().getResourceManagement()
					.getLinkNames(entityName);
		} catch (NullPointerException e) {
			throw e;
		} catch (UnrecognizedResourceAdaptorEntityException e) {
			throw e;
		} catch (Throwable e) {
			String s = "failed to get link names for RA entity with name "
					+ entityName;
			logger.error(s, e);
			throw new ManagementException(s, e);
		}
	}

	public ConfigProperties getConfigurationProperties(ResourceAdaptorID raID)
			throws NullPointerException, UnrecognizedResourceAdaptorException,
			ManagementException {
		try {
			return SleeContainer.lookupFromJndi().getResourceManagement()
					.getConfigurationProperties(raID);
		} catch (NullPointerException e) {
			throw e;
		} catch (UnrecognizedResourceAdaptorException e) {
			throw e;
		} catch (Throwable e) {
			String s = "failed to get configuration propertiess for RA with id "
					+ raID;
			logger.error(s, e);
			throw new ManagementException(s, e);
		}
	}

	public ConfigProperties getConfigurationProperties(String entityName)
			throws NullPointerException,
			UnrecognizedResourceAdaptorEntityException, ManagementException {
		try {
			return SleeContainer.lookupFromJndi().getResourceManagement()
					.getConfigurationProperties(entityName);
		} catch (NullPointerException e) {
			throw e;
		} catch (UnrecognizedResourceAdaptorEntityException e) {
			throw e;
		} catch (Throwable e) {
			String s = "failed to get configuration properties for RA entity with name "
					+ entityName;
			logger.error(s, e);
			throw new ManagementException(s, e);
		}
	}

	public ResourceAdaptorID getResourceAdaptor(String entityName)
			throws NullPointerException,
			UnrecognizedResourceAdaptorEntityException, ManagementException {
		try {
			return SleeContainer.lookupFromJndi().getResourceManagement()
					.getResourceAdaptor(entityName);
		} catch (NullPointerException e) {
			throw e;
		} catch (UnrecognizedResourceAdaptorEntityException e) {
			throw e;
		} catch (Throwable e) {
			String s = "failed to get RA ID for RA entity with name "
					+ entityName;
			logger.error(s, e);
			throw new ManagementException(s, e);
		}
	}

	public String[] getResourceAdaptorEntities() throws ManagementException {
		try {
			return SleeContainer.lookupFromJndi().getResourceManagement()
					.getResourceAdaptorEntities();
		} catch (Throwable e) {
			String s = "failed to get RA entities";
			logger.error(s, e);
			throw new ManagementException(s, e);
		}
	}

	public String[] getResourceAdaptorEntities(ResourceAdaptorID id)
			throws NullPointerException, UnrecognizedResourceAdaptorException,
			ManagementException {
		try {
			return SleeContainer.lookupFromJndi().getResourceManagement()
					.getResourceAdaptorEntities(id);
		} catch (NullPointerException e) {
			throw e;
		} catch (UnrecognizedResourceAdaptorException e) {
			throw e;
		} catch (Throwable e) {
			String s = "failed to get RA entities for RA with id " + id;
			logger.error(s, e);
			throw new ManagementException(s, e);
		}
	}

	public String[] getResourceAdaptorEntities(String[] linkNames)
			throws NullPointerException, ManagementException {
		try {
			return SleeContainer.lookupFromJndi().getResourceManagement()
					.getResourceAdaptorEntities(linkNames);
		} catch (NullPointerException e) {
			throw e;
		} catch (Throwable e) {
			String s = "failed to get RA entities for link names "
					+ Arrays.asList(linkNames);
			logger.error(s, e);
			throw new ManagementException(s, e);
		}
	}

	public String getResourceAdaptorEntity(String linkName)
			throws NullPointerException, UnrecognizedLinkNameException,
			ManagementException {
		try {
			return SleeContainer.lookupFromJndi().getResourceManagement()
					.getResourceAdaptorEntityName(linkName);
		} catch (NullPointerException e) {
			throw e;
		} catch (UnrecognizedLinkNameException e) {
			throw e;
		} catch (Throwable e) {
			String s = "failed to get RA entities for link name " + linkName;
			logger.error(s, e);
			throw new ManagementException(s, e);
		}
	}

	public ResourceAdaptorEntityState getState(String entityName)
			throws NullPointerException,
			UnrecognizedResourceAdaptorEntityException, ManagementException {
		try {
			return SleeContainer.lookupFromJndi().getResourceManagement()
					.getState(entityName);
		} catch (NullPointerException e) {
			throw e;
		} catch (UnrecognizedResourceAdaptorEntityException e) {
			throw e;
		} catch (Throwable e) {
			String s = "failed to get state for RA entity with name "
					+ entityName;
			logger.error(s, e);
			throw new ManagementException(s, e);
		}
	}

	public SbbID[] getBoundSbbs(String linkName) throws NullPointerException,
			UnrecognizedLinkNameException, ManagementException {
		try {
			return SleeContainer.lookupFromJndi().getResourceManagement()
					.getBoundSbbs(linkName);
		} catch (NullPointerException e) {
			throw e;
		} catch (UnrecognizedLinkNameException e) {
			throw e;
		} catch (Throwable e) {
			String s = "failed to get bound sbbs to link name "
					+ linkName;
			logger.error(s, e);
			throw new ManagementException(s, e);
		}
	}

	public ObjectName getResourceUsageMBean(String entityName)
			throws NullPointerException,
			UnrecognizedResourceAdaptorEntityException,
			InvalidArgumentException, ManagementException {
		try {
			return SleeContainer.lookupFromJndi().getResourceManagement()
					.getResourceUsageMBean(entityName);
		} catch (NullPointerException e) {
			throw e;
		} catch (UnrecognizedResourceAdaptorEntityException e) {
			throw e;
		} catch (InvalidArgumentException e) {
			throw e;
		} catch (Throwable e) {
			String s = "failed to get resource usage mbean for RA entity with name "
					+ entityName;
			logger.error(s, e);
			throw new ManagementException(s, e);
		}
	}

}
