package org.mobicents.slee.container.management.jmx;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.management.NotCompliantMBeanException;
import javax.slee.SbbID;
import javax.slee.ServiceID;
import javax.slee.management.ManagementException;
import javax.slee.management.ServiceState;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.sbb.GetChildRelationMethodDescriptor;
import org.mobicents.slee.container.sbbentity.ChildRelation;
import org.mobicents.slee.container.sbbentity.SbbEntity;
import org.mobicents.slee.container.sbbentity.SbbEntityFactory;
import org.mobicents.slee.container.sbbentity.SbbEntityID;
import org.mobicents.slee.container.service.Service;

public class SbbEntitiesMBeanImpl extends MobicentsServiceMBeanSupport implements
		SbbEntitiesMBeanImplMBean {
	
	private final SbbEntityFactory sbbEntityFactory;
	
	public SbbEntitiesMBeanImpl(SleeContainer sleeContainer) throws NotCompliantMBeanException {
		super(sleeContainer,SbbEntitiesMBeanImplMBean.class);
		this.sbbEntityFactory = sleeContainer.getSbbEntityFactory();
	}

	@Override
	public Object[] retrieveSbbEntitiesBySbbId(SbbID sbbId) throws ManagementException {
		ArrayList result = new ArrayList();
		try {

			Iterator<SbbEntityID> sbbes = retrieveAllSbbEntitiesIds().iterator();
			while (sbbes.hasNext()) {
				try {
					SbbEntity sbbe = sbbEntityFactory.getSbbEntity(sbbes.next(),false);
					if (sbbe != null && sbbe.getSbbId().equals(sbbId)) {
						result.add(sbbEntityToArray(sbbe));
					}
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

	@Override
	public Object[] retrieveAllSbbEntities() throws ManagementException {
		ArrayList result = new ArrayList();
		try {

			Iterator<SbbEntityID> sbbes = retrieveAllSbbEntitiesIds().iterator();
			while (sbbes.hasNext()) {
				try {
					SbbEntity sbbe = sbbEntityFactory.getSbbEntity(sbbes.next(),false);
					if (sbbe != null) {
						result.add(sbbEntityToArray(sbbe));
					}
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

	private Set<SbbEntityID> retrieveAllSbbEntitiesIds() throws SystemException, NullPointerException, ManagementException, NotSupportedException {
		Set<SbbEntityID> result = new HashSet<SbbEntityID>();

		final SleeContainer sleeContainer = getSleeContainer();

		try {
			sleeContainer.getTransactionManager().begin();

			for (ServiceID serviceID : sleeContainer.getServiceManagement().getServices(ServiceState.ACTIVE)) {
				try {
					for (SbbEntityID rootSbbId : sbbEntityFactory.getRootSbbEntityIDs(serviceID)) {
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

	private Set<SbbEntityID> getChildSbbEntities(SbbEntityID sbbEntityId) {

		Set<SbbEntityID> result = new HashSet<SbbEntityID>();

		try {
			SbbEntity sbbEntity = getSbbEntityById(sbbEntityId);
			for (GetChildRelationMethodDescriptor method : sbbEntity.getSbbComponent().getDescriptor().getGetChildRelationMethodsMap().values()) {
				ChildRelation childRelationImpl = sbbEntity.getChildRelation(method.getChildRelationMethodName());
				if (childRelationImpl != null) {
					for (SbbEntityID childSbbEntityId : childRelationImpl.getSbbEntitySet()) {
						result.addAll(getChildSbbEntities(childSbbEntityId));
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
			SleeContainer sleeContainer = getSleeContainer();
			sleeContainer.getTransactionManager().begin();
			if (entity == null)
				return null;
			info[0] = entity.getSbbEntityId().toString();
			info[1] = String.valueOf(entity.getSbbEntityId().getParentSBBEntityID());
			info[2] = String.valueOf(entity.getSbbEntityId().getRootSBBEntityID());
			info[3] = entity.getSbbId().toString();
			info[4] = Byte.toString(entity.getPriority());
			info[5] = entity.getSbbEntityId().getServiceConvergenceName();
			// FIXME to remove in mmc
			info[6] = null;
			info[7] = String.valueOf(entity.getSbbEntityId().getServiceID());
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

	private SbbEntity getSbbEntityById(SbbEntityID sbbeId) {
		try {
			return sbbEntityFactory.getSbbEntity(sbbeId, false);
		} catch (Exception e) {
			return null;
		}

	}

	public Object[] retrieveSbbEntityInfo(SbbEntityID sbbeId) {
		SbbEntity entity = getSbbEntityById(sbbeId);
		return sbbEntityToArray(entity);
	}

	public void removeSbbEntity(SbbEntityID sbbeId) {
		try {
			SleeContainer sleeContainer = getSleeContainer();
			sleeContainer.getTransactionManager().begin();
			sbbEntityFactory.removeSbbEntity(getSbbEntityById(sbbeId),false);
			sleeContainer.getTransactionManager().commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
