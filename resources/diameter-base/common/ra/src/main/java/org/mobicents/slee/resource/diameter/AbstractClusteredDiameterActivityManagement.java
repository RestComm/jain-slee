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

package org.mobicents.slee.resource.diameter;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import javax.slee.facilities.Tracer;
import javax.slee.transaction.SleeTransactionManager;
import javax.transaction.SystemException;
import javax.transaction.Transaction;

import net.java.slee.resource.diameter.base.DiameterActivity;

import org.jdiameter.api.Stack;
import org.mobicents.slee.resource.cluster.FaultTolerantResourceAdaptorContext;
import org.mobicents.slee.resource.cluster.FaultTolerantTimer;
import org.mobicents.slee.resource.cluster.FaultTolerantTimerTask;
import org.mobicents.slee.resource.cluster.FaultTolerantTimerTaskData;
import org.mobicents.slee.resource.cluster.ReplicatedData;
import org.mobicents.slee.resource.diameter.base.DiameterActivityHandle;
import org.mobicents.slee.resource.diameter.base.DiameterActivityImpl;

/**
 * Base for replication aware activity management
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public abstract class AbstractClusteredDiameterActivityManagement implements DiameterActivityManagement {

  protected Tracer tracer;

  protected Stack diameterStack;
  protected SleeTransactionManager sleeTxManager;
  // use string here to reduce repl overhead.
  protected ReplicatedData<String, DiameterActivity> replicatedData;
  protected FaultTolerantTimer faultTolerantTimer;
  protected long delay;

  public AbstractClusteredDiameterActivityManagement(FaultTolerantResourceAdaptorContext ftRAContext, long delay,Tracer tracer, Stack diameterStack, SleeTransactionManager sleeTxManager, ReplicatedData<String, DiameterActivity> replicatedData) {
    super();
    this.tracer = tracer;
    this.diameterStack = diameterStack;
    this.sleeTxManager = sleeTxManager;
    this.replicatedData = replicatedData;
    this.faultTolerantTimer = ftRAContext.getFaultTolerantTimer();
    this.delay = delay;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.mobicents.slee.resource.diameter.DiameterActivityManagement#get(org.mobicents.slee.resource.diameter.base.DiameterActivityHandle)
   */
  public DiameterActivity get(DiameterActivityHandle handle) {
    // tricky, now we need remote to kick in.
    // for that some impl methods need to be accessed...
    DiameterActivityImpl activity = (DiameterActivityImpl) this.replicatedData.get(handle.getId());
    //FIXME: add check for RA
    if (activity != null) {
      // now we have to set some resources...
      if(activity.getSessionListener() == null) {
        performBeforeReturn(activity);
      }
    }

    return activity;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.mobicents.slee.resource.diameter.DiameterActivityManagement#put(org.mobicents.slee.resource.diameter.base.DiameterActivityHandle,
   *   net.java.slee.resource.diameter.base.DiameterActivity)
   */
  public void put(DiameterActivityHandle handle, DiameterActivity activity) {
    // replicate even base?
    this.replicatedData.put(handle.getId(), activity);
  }

  public void update(DiameterActivityHandle handle, DiameterActivity activity) {
    this.replicatedData.put(handle.getId(), activity);
  }
  /*
   * (non-Javadoc)
   * 
   * @see org.mobicents.slee.resource.diameter.DiameterActivityManagement#remove(org.mobicents.slee.resource.diameter.base.DiameterActivityHandle)
   */
  public DiameterActivity remove(DiameterActivityHandle handle) {
    DiameterActivity ac = this.replicatedData.get(handle.getId());
    Transaction tx = null;
    try {
      tx = sleeTxManager.suspend();
    }
    catch (SystemException e) {
      // ignore
    }

    replicatedData.remove(handle.getId());

    if (tx != null) {
      try {
        sleeTxManager.resume(tx);
      }
      catch (Throwable e) {
        // ignore
      }
    }
    return ac;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.mobicents.slee.resource.diameter.DiameterActivityManagement#containsKey(org.mobicents.slee.resource.diameter.base.DiameterActivityHandle)
   */
  public boolean containsKey(DiameterActivityHandle activityHandle) {
    return this.replicatedData.contains(activityHandle.getId());
  }

  public void startActivityRemoveTimer(DiameterActivityHandle handle) {
    DiameterActivity da = this.get(handle);
    if(da != null) {
      this.faultTolerantTimer.schedule(new ActivityRemovalFaultTolerantTimerTask(handle), delay, TimeUnit.MILLISECONDS);
    }
  }

  public void stopActivityRemoveTimer(DiameterActivityHandle handle) {
    DiameterActivity da = this.get(handle);
    if(da != null) {
      synchronized (da) {
        this.faultTolerantTimer.cancel(handle.getId());
      }
    }
  }

  /**
   * This method should be implemented by each RA.
   * It should perform all management operations before activity is returned.
   * 
   * @param activity
   */
  protected abstract void performBeforeReturn(DiameterActivityImpl activity);

  private final class ActivityRemovalFaultTolerantTimerTask implements FaultTolerantTimerTask {
    private final FaultTolerantTimerTaskData taskData;

    public ActivityRemovalFaultTolerantTimerTask(DiameterActivityHandle handle) {
      this.taskData = new DiameterTimerTaskData(handle.getId());
    }

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    public void run() {
      try {
        DiameterActivityImpl da = (DiameterActivityImpl) get(new DiameterActivityHandle((String)getTaskData().getTaskID()));
        if (da != null) {
          synchronized (da) {
            if(da.isTerminateAfterProcessing()) {
              da.setTerminateAfterProcessing(false);
              da.endActivity();
            }
          }
        }
      }
      catch (Exception e) {
        e.printStackTrace();
      }
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.cluster.FaultTolerantTimerTask#getTaskData()
     */
    public FaultTolerantTimerTaskData getTaskData() {
      return taskData;
    }
  }

  private final class DiameterTimerTaskData implements FaultTolerantTimerTaskData {

    private static final long serialVersionUID = 1L;

    private final String sessionId;

    public DiameterTimerTaskData(String sessionId) {
      this.sessionId = sessionId;
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.cluster.FaultTolerantTimerTaskData#getTaskID()
     */
    @Override
    public Serializable getTaskID() {
      return this.sessionId;
    }
  }

}
