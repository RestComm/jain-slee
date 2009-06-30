/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.mobicents.slee.container.management.jmx;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.management.StandardMBean;
import javax.slee.ServiceID;
import javax.slee.management.ManagementException;
import javax.slee.management.ServiceState;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb.MGetChildRelationMethod;
import org.mobicents.slee.container.service.Service;
import org.mobicents.slee.runtime.sbbentity.ChildRelationImpl;
import org.mobicents.slee.runtime.sbbentity.SbbEntity;
import org.mobicents.slee.runtime.sbbentity.SbbEntityFactory;

public class SbbEntitiesMBeanImpl extends StandardMBean implements
		SbbEntitiesMBeanImplMBean {
	public final String OBJECT_NAME = "slee:name=SbbEntitiesMBean";

	private ObjectName objectName;

	public SbbEntitiesMBeanImpl() throws NotCompliantMBeanException {
		super(SbbEntitiesMBeanImplMBean.class);
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

	public Object[] retrieveSbbEntitiesBySbbId(String sbbId) {
		// FIXME retrieveSbbEntitiesBySbbId
		// emmartins: this one is even uglier than going through all sbb
		// entity trees, because you need to find all child relations with this
		// Id, in all services, and then collect all child sbb entities, why not
		// only by service ID, code in
		// retreiveAllSbbEntitiesIds can be reused
		return null;
	}

	public Object[] retrieveAllSbbEntities() throws ManagementException {
		ArrayList result = new ArrayList();
		try {

			Iterator<String> sbbes = retrieveAllSbbEntitiesIds().iterator();
			while (sbbes.hasNext()) {
				try {
					SbbEntity sbbe = SbbEntityFactory
							.getSbbEntityWithoutLock(sbbes.next());
					result.add(sbbEntityToArray(sbbe));
				} catch (Exception e) {
					// ignore
				}
			}
			return result.toArray();
		} catch (Exception e) {
			throw new ManagementException(
					"Failed to build set of existent sbb entities", e);
		}
	}

	private Set<String> retrieveAllSbbEntitiesIds() throws SystemException, NullPointerException, ManagementException, NotSupportedException {
		Set<String> result = new HashSet<String>();

		SleeContainer sleeContainer = SleeContainer.lookupFromJndi();

		try {
			sleeContainer.getTransactionManager().begin();

			for (ServiceID serviceID : sleeContainer.getServiceManagement().getServices(ServiceState.ACTIVE)) {
				try {
					Service service = sleeContainer.getServiceManagement()
							.getService(serviceID);
					for (String rootSbbId : (Collection<String>) service
							.getChildObj()) {
						result.addAll(getChildSbbEntities(rootSbbId));
					}
				} catch (Exception e) {
					// ignore
				}
			}
		} finally {
			try {
				sleeContainer.getTransactionManager().rollback();
			} catch (SystemException e) {
				// ignore
			}
		}

		return result;
	}

	private static Set<String> getChildSbbEntities(String sbbEntityId) {

		Set<String> result = new HashSet<String>();

		try {
			SbbEntity sbbEntity = SbbEntityFactory.getSbbEntityWithoutLock(sbbEntityId);
			for (MGetChildRelationMethod method : sbbEntity.getSbbComponent().getDescriptor().getGetChildRelationMethods().values()) {
				ChildRelationImpl childRelationImpl = sbbEntity
						.getChildRelation(method.getChildRelationMethodName());
				if (childRelationImpl != null) {
					for (String childSbbEntityId : (Set<String>) childRelationImpl
							.getSbbEntitySet()) {
						result.addAll(getChildSbbEntities(sbbEntityId));
					}
				}
			}
			result.add(sbbEntityId);
		} catch (Exception e) {
			// ignore
		}

		return result;
	}

	private Object[] sbbEntityToArray(SbbEntity entity) {
		Object[] info = new Object[10];
		try {
			SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
			sleeContainer.getTransactionManager().begin();
			if (entity == null)
				return null;
			info[0] = entity.getSbbEntityId();
			info[1] = entity.getParentSbbEntityId();
			info[2] = entity.getRootSbbId();
			info[3] = entity.getSbbId().toString();
			info[4] = Byte.toString(entity.getPriority());
			info[5] = entity.getServiceConvergenceName();
			// FIXME to remove in mmc
			info[6] = null;
			if (entity.getServiceId() != null)
				info[7] = entity.getServiceId().toString();
			// FIXME to remove in mmc
			info[8] = null;
			Set acsSet = entity.getActivityContexts();
			if (acsSet != null && acsSet.size() > 0) {
				Object[] acsArray = acsSet.toArray();
				String[] acs = new String[acsArray.length];
				info[9] = acs;
			}
			sleeContainer.getTransactionManager().commit();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return info;
	}

	private SbbEntity getSbbEntityById(String sbbeId) {
		try {
			return SbbEntityFactory.getSbbEntityWithoutLock(sbbeId);
		} catch (Exception e) {
			return null;
		}

	}

	public Object[] retrieveSbbEntityInfo(String sbbeId) {
		SbbEntity entity = getSbbEntityById(sbbeId);
		return sbbEntityToArray(entity);
	}

	public void removeSbbEntity(String sbbeId) {
		try {
			SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
			sleeContainer.getTransactionManager().begin();
			SbbEntityFactory.removeSbbEntity(getSbbEntityById(sbbeId), true);
			sleeContainer.getTransactionManager().commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
