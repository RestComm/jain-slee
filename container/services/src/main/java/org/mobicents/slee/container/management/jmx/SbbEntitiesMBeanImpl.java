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

package org.mobicents.slee.container.management.jmx;

import java.beans.PropertyEditorManager;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.slee.ServiceID;
import javax.slee.management.ManagementException;
import javax.slee.management.ServiceState;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.sbb.GetChildRelationMethodDescriptor;
import org.mobicents.slee.container.management.jmx.editors.SbbEntityIDArrayPropertyEditor;
import org.mobicents.slee.container.management.jmx.editors.SbbEntityIDPropertyEditor;
import org.mobicents.slee.container.sbbentity.ChildRelation;
import org.mobicents.slee.container.sbbentity.SbbEntity;
import org.mobicents.slee.container.sbbentity.SbbEntityFactory;
import org.mobicents.slee.container.sbbentity.SbbEntityID;
import org.mobicents.slee.container.transaction.SleeTransactionManager;

public class SbbEntitiesMBeanImpl extends MobicentsServiceMBeanSupport implements SbbEntitiesMBeanImplMBean {

  private static final int SBB_ENTITY_ID = 0;
  private static final int PARENT_SBB_ENTITY_ID = 1;
  private static final int ROOT_SBB_ENTITY_ID = 2;
  private static final int SBB_ID = 3;
  private static final int SBB_ENTITY_PRIORITY = 4;
  private static final int SBB_ENTITY_SERV_CONV_NAME = 5;
  private static final int SBB_ENTITY_SERVICE_ID = 6;
  private static final int ACS = 7;

  private final SbbEntityFactory sbbEntityFactory;

  public SbbEntitiesMBeanImpl(SbbEntityFactory sbbEntityFactory) {
    super(sbbEntityFactory.getSleeContainer());
    this.sbbEntityFactory = sbbEntityFactory;
    PropertyEditorManager.registerEditor(SbbEntityID[].class, SbbEntityIDArrayPropertyEditor.class);
    PropertyEditorManager.registerEditor(SbbEntityID.class, SbbEntityIDPropertyEditor.class);
  }

  @Override
  public Object[] retrieveSbbEntitiesBySbbId(String sbbId) throws ManagementException {
    ArrayList result = new ArrayList();
    final SleeTransactionManager txMgr = getSleeContainer().getTransactionManager();
    boolean started = false;
    try {
      if (txMgr.getTransaction() == null) {
        txMgr.begin();
        started = true;
      }

      Iterator<SbbEntityID> sbbes = retrieveAllSbbEntitiesIds().iterator();
      while (sbbes.hasNext()) {
        try {
          SbbEntity sbbe = sbbEntityFactory.getSbbEntity(sbbes.next(), false);
          if (sbbe != null && sbbe.getSbbId().toString().equals(sbbId)) {
            Object res = sbbEntityToArray(sbbe);
            if (res != null)
              result.add(res);
          }
        }
        catch (Exception e) {
          // ignore
        }
      }
      return result.toArray();
    }
    catch (Exception e) {
      throw new ManagementException("Failed to build set of existent sbb entities", e);
    }
    finally {
      if (started) {
        try {
          txMgr.commit();
        }
        catch (Exception e) {
        }
      }
    }

  }

  @Override
  public Object[] retrieveAllSbbEntities() throws ManagementException {
    ArrayList result = new ArrayList();
    final SleeTransactionManager txMgr = getSleeContainer().getTransactionManager();
    boolean started = false;
    try {
      if (txMgr.getTransaction() == null) {
        txMgr.begin();
        started = true;
      }

      Iterator<SbbEntityID> sbbes = retrieveAllSbbEntitiesIds().iterator();
      while (sbbes.hasNext()) {
        try {
          SbbEntity sbbe = sbbEntityFactory.getSbbEntity(sbbes.next(), false);
          if (sbbe != null) {
            result.add(sbbEntityToArray(sbbe));
          }
        }
        catch (Exception e) {
          // ignore
        }
      }
      return result.toArray();
    }
    catch (Exception e) {
      throw new ManagementException("Failed to build set of existent sbb entities", e);
    }
    finally {
      if (started) {
        try {
          txMgr.commit();
        }
        catch (Exception e) {
        }
      }
    }
  }

  private Set<SbbEntityID> retrieveAllSbbEntitiesIds() throws SystemException, NullPointerException, ManagementException, NotSupportedException {
    Set<SbbEntityID> result = new HashSet<SbbEntityID>();

    for (ServiceID serviceID : sleeContainer.getServiceManagement().getServices(ServiceState.ACTIVE)) {
      try {
        for (SbbEntityID rootSbbId : sbbEntityFactory.getRootSbbEntityIDs(serviceID)) {
          result.addAll(getChildSbbEntities(rootSbbId));
        }
      }
      catch (Exception e) {
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
    }
    catch (Exception e) {
      // ignore
    }

    return result;
  }

  private Object[] sbbEntityToArray(SbbEntity entity) {
    Object[] info = new Object[8];
    try {

      if (entity == null)
        return null;
      info[SBB_ENTITY_ID] = entity.getSbbEntityId().toString();
      info[PARENT_SBB_ENTITY_ID] = String.valueOf(entity.getSbbEntityId().getParentSBBEntityID());
      info[ROOT_SBB_ENTITY_ID] = String.valueOf(entity.getSbbEntityId().getRootSBBEntityID());
      info[SBB_ID] = entity.getSbbId().toString();
      info[SBB_ENTITY_PRIORITY] = Byte.toString(entity.getPriority());
      info[SBB_ENTITY_SERV_CONV_NAME] = entity.getSbbEntityId().getServiceConvergenceName();
      info[SBB_ENTITY_SERVICE_ID] = String.valueOf(entity.getSbbEntityId().getServiceID());
      Set acsSet = entity.getActivityContexts();
      if (acsSet != null && !acsSet.isEmpty()) {
        Object[] acsArray = acsSet.toArray();
        String[] acs = new String[acsArray.length];
        info[ACS] = acs;
      }

    }
    catch (Exception e) {
      e.printStackTrace();
      return null;
    }
    return info;
  }

  private SbbEntity getSbbEntityById(SbbEntityID sbbeId) {
    try {
      return sbbEntityFactory.getSbbEntity(sbbeId, false);
    }
    catch (Exception e) {
      return null;
    }

  }

  public Object[] retrieveSbbEntityInfo(String sbbeId) throws ManagementException {
    final SleeTransactionManager txMgr = getSleeContainer().getTransactionManager();
    boolean started = false;
    try {
      if (txMgr.getTransaction() == null) {
        txMgr.begin();
        started = true;
      }

      SbbEntityIDPropertyEditor seipe = new SbbEntityIDPropertyEditor();
      seipe.setAsText(sbbeId);
      SbbEntity entity = getSbbEntityById((SbbEntityID) seipe.getValue());

      return sbbEntityToArray(entity);
    }
    catch (Exception e) {

      throw new ManagementException("Failed to build set of existent sbb entities", e);
    }
    finally {
      if (started) {
        try {
          txMgr.commit();
        }
        catch (Exception e) {

        }
      }
    }
  }

  public void removeSbbEntity(String sbbeId) throws ManagementException {
    try {
      SleeContainer sleeContainer = getSleeContainer();
      sleeContainer.getTransactionManager().begin();
      SbbEntityIDPropertyEditor seipe = new SbbEntityIDPropertyEditor();
      seipe.setAsText(sbbeId);
      sbbEntityFactory.removeSbbEntity(getSbbEntityById((SbbEntityID) seipe.getValue()), false);
      sleeContainer.getTransactionManager().commit();
    }
    catch (Exception e) {
      throw new ManagementException("Failed to remove existent sbb entity", e);
    }
  }

}
