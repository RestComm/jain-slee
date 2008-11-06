/***************************************************
 *                                                 *
 *  Mobicents: The Open Source VoIP Platform       *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/

package org.mobicents.slee.container.management.jmx;

import java.io.Serializable;
import java.util.Iterator;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.management.StandardMBean;
import javax.slee.InvalidArgumentException;
import javax.slee.SbbID;
import javax.slee.ServiceID;
import javax.slee.UnrecognizedSbbException;
import javax.slee.UnrecognizedServiceException;
import javax.slee.management.ManagementException;
import javax.slee.management.ServiceUsageMBean;
import javax.slee.management.UsageParameterSetNameAlreadyExistsException;
import javax.slee.usage.UnrecognizedUsageParameterSetNameException;
import javax.transaction.SystemException;

import org.jboss.logging.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.InstalledUsageParameterSet;
import org.mobicents.slee.container.service.Service;
import org.mobicents.slee.container.service.ServiceComponent;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;

/**
 * 
 * See SLEE 1.0 #14.9.
 * 
 * The ServiceUsageMBean interface defines the management interface used to
 * interact with SBB usage parameter sets for SBBs in a Service. It defines the
 * methods to create, lookup, and remove SBB usage parameter sets from SBBs in
 * the Service.
 * 
 * @author M. Ranganathan
 * @author Ivelin Ivanov
 */
public class ServiceUsageMBeanImpl extends StandardMBean implements
		ServiceUsageMBean, Serializable {

	private static final long serialVersionUID = 2670146310843436229L;

	// This is the service ID for this service usage mbean.
	private ServiceID serviceID;

	private static Logger logger = Logger
			.getLogger(ServiceUsageMBeanImpl.class);

	// Note that this is just to save a lookup
	// It is not serialized.
	private transient ServiceComponent service;

	public ServiceUsageMBeanImpl() throws NotCompliantMBeanException {
		super(ServiceUsageMBean.class);
	}

	public ServiceUsageMBeanImpl(ServiceID serviceID)
			throws NotCompliantMBeanException, MalformedObjectNameException,
			NullPointerException {
		this();
		this.serviceID = serviceID;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.management.ServiceUsageMBean#getService()
	 */
	public ServiceID getService() throws ManagementException {

		return this.serviceID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.management.ServiceUsageMBean#createUsageParameterSet(javax.slee.SbbID,
	 *      java.lang.String)
	 */
	public void createUsageParameterSet(SbbID sbbId,
			String usageParameterSetName) throws NullPointerException,
			UnrecognizedSbbException, InvalidArgumentException,
			UsageParameterSetNameAlreadyExistsException, ManagementException {

		if (usageParameterSetName == null)
			throw new NullPointerException("Sbb usage param set is null");
		if (sbbId == null)
			throw new NullPointerException("Sbb ID is null!");
		if (usageParameterSetName.length() == 0)
			throw new InvalidArgumentException(
					"The lenght of the Usage Parameter Set Name is zero!");
		if (!isValidUsageParameterName(usageParameterSetName))
			throw new InvalidArgumentException(
					"The lenght of the Usage Parameter Set Name is zero!");

		SleeTransactionManager txmgr = SleeContainer.getTransactionManager();
		boolean rb = true;
		try {

			txmgr.begin();

			checkSbbUsageParams(sbbId);

			service.installUsageParameter(sbbId, usageParameterSetName);
			rb = false;
		} catch (UnrecognizedSbbException ex) {
			throw ex;
		} catch (InvalidArgumentException ex) {
			throw ex;
		} catch (InstanceAlreadyExistsException ex) {
			throw new UsageParameterSetNameAlreadyExistsException(
					"Duplicate usage parameter set name "
							+ usageParameterSetName);
		} catch (NullPointerException ex) {
			throw ex;
		} catch (UsageParameterSetNameAlreadyExistsException ex) {
			throw ex;
		} catch (Exception e) {
			throw new ManagementException("Unexpected exception!", e);
		} finally {
			try {
				if (rb)
					txmgr.setRollbackOnly();
				txmgr.commit();
			} catch (Exception ex) {
				throw new RuntimeException("Unexpected exception ", ex);
			}
		}

	}

	private boolean isValidUsageParameterName(String str) {
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (!(Character.isDigit(c) || Character.isLetter(c) || (c <= '\u007e' && c >= '\u0020'))) {
				return false;
			}
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.management.ServiceUsageMBean#removeUsageParameterSet(javax.slee.SbbID,
	 *      java.lang.String)
	 */
	public void removeUsageParameterSet(SbbID sbbId, String name)
			throws NullPointerException, UnrecognizedSbbException,
			InvalidArgumentException,
			UnrecognizedUsageParameterSetNameException, ManagementException {
		if (name == null)
			throw new NullPointerException("Sbb usage param set is null");
		if (sbbId == null)
			throw new NullPointerException("Sbb ID is null!");
		SleeTransactionManager txmgr = SleeContainer.getTransactionManager();
		boolean rb = true;
		try {
			txmgr.begin();
			SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
			if (!sleeContainer.getComponentManagement().isInstalled(sbbId))
				throw new UnrecognizedSbbException("Sbb not installed " + sbbId);

			// checkSbbUsageParams(sbbId);
			this.service = sleeContainer.getServiceManagement()
					.getServiceComponent(serviceID);

			service.removeUsageParameter(sbbId, name);
		} catch (UnrecognizedUsageParameterSetNameException ex) {
			throw ex;
		} catch (InvalidArgumentException ex) {
			throw ex;
		} catch (UnrecognizedSbbException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new ManagementException(
					"Could not uninstall usage parameter", ex);
		} finally {
			try {
				if (rb)
					txmgr.setRollbackOnly();
				txmgr.commit();
			} catch (SystemException ex) {
				logger.fatal("Unexpected exception !", ex);
				throw new RuntimeException("Unexpected exception !", ex);
			}

		}

	}

	/**
	 * This method returns a list containing the names of the named SBB usage
	 * parameter sets that belong to the SBB specified by the sbbID argument and
	 * the Service represented by the ServiceUsageMBean object.
	 * 
	 * @see javax.slee.management.ServiceUsageMBean#getUsageParameterSets(javax.slee.SbbID)
	 */
	public String[] getUsageParameterSets(SbbID sbbId)
			throws NullPointerException, UnrecognizedSbbException,
			InvalidArgumentException, ManagementException {

		if (sbbId == null)
			throw new NullPointerException("null sbb id specificed");

		SleeContainer sleeContainer = SleeContainer.lookupFromJndi();

		SleeTransactionManager txmgr = SleeContainer.getTransactionManager();
		String[] usageParameterSets = null;
		boolean rb = true;
		try {
			txmgr.begin();
			checkSbbUsageParams(sbbId);

			usageParameterSets = service.getNamedUsageParameterSets(sbbId);
			if (usageParameterSets == null)
				throw new InvalidArgumentException("No usage parameters found ");

			rb = false;
		} catch (UnrecognizedSbbException ex) {
			throw ex;
		} catch (InvalidArgumentException ex) {
			throw ex;

		} catch (Exception ex) {
			logger.error("unexpected exception ", ex);
			throw new ManagementException("Something bad happened ! ", ex);
		} finally {
			try {
				if (rb)
					txmgr.setRollbackOnly();
				txmgr.commit();
			} catch (SystemException ex) {
				logger.error("Txmgr failed", ex);
				throw new RuntimeException("Txmgr failed", ex);
			}
		}
		return usageParameterSets;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.management.ServiceUsageMBean#getSbbUsageMBean(javax.slee.SbbID)
	 */
	public ObjectName getSbbUsageMBean(SbbID sbbId)
			throws NullPointerException, UnrecognizedSbbException,
			InvalidArgumentException, ManagementException {
		if (sbbId == null)
			throw new NullPointerException("Sbb ID is null!");
		SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
		SleeTransactionManager txMgr = SleeContainer.getTransactionManager();
		boolean rb = true;

		try {
			txMgr.begin();

			checkSbbUsageParams(sbbId);
			ObjectName on = (ObjectName) service
					.getUsageParameterObjectName(sbbId);
			if (on == null)
				throw new InvalidArgumentException(
						"No usage mbean for this sbb ");
			rb = false;
			return on;
		} catch (UnrecognizedSbbException ex) {
			throw ex;
		} catch (InvalidArgumentException ex) {
			throw ex;

		} catch (Exception ex) {
			logger.error("Unexpected exception ", ex);
			throw new ManagementException("cannot getUsageMBean " + sbbId);
		} finally {
			try {
				if (rb)
					txMgr.setRollbackOnly();
				txMgr.commit();
			} catch (Exception e) {
				throw new RuntimeException("Unexpected error with tx manager",
						e);
			}

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.management.ServiceUsageMBean#getSbbUsageMBean(javax.slee.SbbID,
	 *      java.lang.String)
	 */
	public ObjectName getSbbUsageMBean(SbbID sbbId, String name)
			throws NullPointerException, UnrecognizedSbbException,
			InvalidArgumentException,
			UnrecognizedUsageParameterSetNameException, ManagementException {
		if (sbbId == null)
			throw new NullPointerException("Sbb ID is null!");
		if (name == null)
			throw new NullPointerException("Sbb usage param set is null");
		SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
		SleeTransactionManager txMgr = SleeContainer.getTransactionManager();
		boolean rb = true;

		try {
			txMgr.begin();

			checkSbbUsageParams(sbbId);
			ObjectName on = (ObjectName) service.getUsageParameterObjectName(
					sbbId, name);

			rb = false;
			return on;
		} catch (UnrecognizedSbbException ex) {
			throw ex;

		} catch (UnrecognizedUsageParameterSetNameException ex) {
			throw ex;

		} catch (InvalidArgumentException ex) {
			throw ex;

		} catch (Exception ex) {
			logger.error("unexpected exception getting usage mbean name = "
					+ name + " sbbid = " + sbbId, ex);
			throw new ManagementException("cannot getUsageMBean " + name, ex);
		} finally {
			try {
				if (rb)
					txMgr.setRollbackOnly();
				txMgr.commit();
			} catch (Exception e) {
				throw new RuntimeException("Unexpected error with tx manager",
						e);
			}

		}
	}

	/**
	 * Resets the usage parameters of only the SBB specified by the sbbID
	 * argument argument (within the Service represented by the
	 * ServiceUsageMBean object).
	 * 
	 * @see javax.slee.management.ServiceUsageMBean#resetAllUsageParameters(javax.slee.SbbID)
	 */
	public void resetAllUsageParameters(SbbID sbbId)
			throws NullPointerException, UnrecognizedSbbException,
			InvalidArgumentException, ManagementException {

		if (logger.isDebugEnabled()) {
			logger.debug("resetAllUsageParameters: " + sbbId);
		}
		if (sbbId == null)
			throw new NullPointerException("Sbb ID is null");
		SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
		SleeTransactionManager txMgr = SleeContainer.getTransactionManager();
		boolean rb = true;
		try {
			txMgr.begin();
			checkSbbUsageParams(sbbId);

			InstalledUsageParameterSet usageParam = Service
					.getDefaultUsageParameterSet(this.serviceID, sbbId);
			// note that the usage parameter may not yet be instantiated so this
			// may be null.

			if (usageParam != null)
				usageParam.reset();

			String[] paramNames = service.getNamedUsageParameterSets(sbbId);

			for (int i = 0; paramNames != null && i < paramNames.length; i++) {
				String name = paramNames[i];

				InstalledUsageParameterSet ups = Service
						.getNamedUsageParameter(serviceID, sbbId, name);
				if (ups != null)
					ups.reset();
			}

			rb = false;
		} catch (InvalidArgumentException ex) {
			throw ex;
		} catch (UnrecognizedSbbException ex) {
			throw ex;
		} catch (Exception ex) {
			String s = "unexpected exception in resetAllUsageParameters";
			logger.error(s, ex);
			throw new ManagementException(s);

		} finally {
			try {
				if (rb)
					txMgr.setRollbackOnly();
				txMgr.commit();
			} catch (Exception e) {
				throw new RuntimeException("Unexpected error with tx manager",
						e);
			}

		}
	}

	/**
	 * Resets the usage parameters of all SBBs within the Service represented by
	 * the ServiceUsageMBean object. The SLEE sets counter-type usage parameters
	 * to zero and removes all samples from sample-type usage parameters.
	 * 
	 * @see javax.slee.management.ServiceUsageMBean#resetAllUsageParameters()
	 */
	public void resetAllUsageParameters() throws ManagementException {

		SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
		SleeTransactionManager txMgr = SleeContainer.getTransactionManager();
		boolean rb = true;

		try {
			txMgr.begin();
			Service service = sleeContainer.getServiceManagement().getService(
					this.serviceID);
			for (Iterator it = Service.getAllUsageParameters(this.serviceID); it
					.hasNext();) {
				InstalledUsageParameterSet ups = (InstalledUsageParameterSet) it
						.next();
				ups.reset();
			}
			rb = false;
		} catch (Exception ex) {
			throw new ManagementException("Could not reset!", ex);
		} finally {
			try {
				if (rb)
					txMgr.setRollbackOnly();
				txMgr.commit();
			} catch (Exception ex) {
				throw new RuntimeException("unexpected system exception ", ex);
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.management.ServiceUsageMBean#close()
	 */
	public void close() throws ManagementException {
		// TODO Auto-generated method stub

	}

	private void checkSbbUsageParams(SbbID sbbId)
			throws UnrecognizedSbbException, InvalidArgumentException,
			UnrecognizedSbbException, SystemException,
			UnrecognizedServiceException {

		if (sbbId == null)
			throw new NullPointerException("SbbId is null!");

		SleeContainer sleeContainer = SleeContainer.lookupFromJndi();

		this.service = sleeContainer.getServiceManagement()
				.getServiceComponent(serviceID);

		if (!sleeContainer.getComponentManagement().isInstalled(sbbId))
			throw new UnrecognizedSbbException("Sbb not installed " + sbbId);

		if (!this.service.isComponent(sbbId))
			throw new UnrecognizedSbbException(
					"This sbb is not part of this service serviceID = "
							+ this.serviceID + " sbb id = " + sbbId);

	}

	public ObjectName getSbbUsageNotificationManager(SbbID arg0)
			throws NullPointerException, UnrecognizedSbbException,
			InvalidArgumentException, ManagementException {
		// TODO Auto-generated method stub
		return null;
	}

}
