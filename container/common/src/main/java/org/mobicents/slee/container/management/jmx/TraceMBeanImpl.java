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

/***************************************************
 *                                                 *
 *  Restcomm: The Open Source VoIP Platform       *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/

package org.mobicents.slee.container.management.jmx;

import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.management.MBeanNotificationInfo;
import javax.slee.ComponentID;
import javax.slee.InvalidArgumentException;
import javax.slee.UnrecognizedComponentException;
import javax.slee.facilities.Level;
import javax.slee.facilities.TraceLevel;
import javax.slee.facilities.Tracer;
import javax.slee.management.ManagementException;
import javax.slee.management.NotificationSource;
import javax.slee.management.TraceMBean;
import javax.slee.management.UnrecognizedNotificationSourceException;
import javax.transaction.SystemException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.management.TraceManagement;
import org.mobicents.slee.container.transaction.SleeTransactionManager;
import org.mobicents.slee.container.transaction.TransactionalAction;
import org.mobicents.slee.runtime.facilities.TraceFacilityImpl;
import org.mobicents.slee.runtime.facilities.TracerImpl;
import org.mobicents.slee.runtime.facilities.TracerStorage;

/**
 * Implementation of the Trace MBean.
 * 
 * @author M. Ranganathan
 * @author baranowb
 * @author martins
 */
@SuppressWarnings("deprecation")
public class TraceMBeanImpl extends MobicentsServiceMBeanSupport implements TraceManagement,TraceMBeanImplMBean {

	private static final Logger logger = Logger.getLogger(TraceMBeanImpl.class);
	
	private final TraceFacilityImpl traceFacility;
	
	// used in sync level ops between log4j and slee tracers
	private static final int LOG4J_LEVEL_SYNC_PERIOD = 1; 
	private ScheduledFuture<?> scheduledFuture = null;
	
	/**
	 * Creates the trace mbean.
	 * 
	 */
	public TraceMBeanImpl() {
		super();
		this.traceFacility = new TraceFacilityImpl(this);
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.core.SleeContainerModule#setSleeContainer(org.mobicents.slee.core.SleeContainer)
	 */
	public void setSleeContainer(SleeContainer sleeContainer) {
		super.sleeContainer = sleeContainer;
	}
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.container.management.jmx.MobicentsServiceMBeanSupport#getSleeContainer()
	 */
	@Override
	public SleeContainer getSleeContainer() {
		return sleeContainer;
	}
	
	/**
	 *  
	 * @return the traceFacility
	 */
	public TraceFacilityImpl getTraceFacility() {
		return traceFacility;
	}

	@Override
	public String toString() {
		return "Trace MBean Impl : " 
			+ "\n+-- Tracers: "	+ tracerStorage.toString();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.slee.management.TraceMBean#setTraceLevel(javax.slee.ComponentID,
	 * javax.slee.facilities.Level)
	 */
	public void setTraceLevel(ComponentID componentId, Level level) throws NullPointerException, UnrecognizedComponentException, ManagementException {
		if (componentId == null)
			throw new NullPointerException("null component Id");
		this.traceFacility.checkComponentID(componentId);
		this.traceFacility.setTraceLevel(componentId, level);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.slee.management.TraceMBean#getTraceLevel(javax.slee.ComponentID)
	 */
	public Level getTraceLevel(ComponentID componentId) throws NullPointerException, UnrecognizedComponentException, ManagementException {
		if (componentId == null)
			throw new NullPointerException(" null component Id ");
		return this.traceFacility.getTraceLevel(componentId);
	}



	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.management.NotificationBroadcaster#getNotificationInfo()
	 */
	public MBeanNotificationInfo[] getNotificationInfo() {

		if (this.traceFacility == null)
			return null;

		String[] notificationTypes = this.traceFacility.getNotificationTypes();
		return new MBeanNotificationInfo[] { new MBeanNotificationInfo(notificationTypes, TraceMBean.TRACE_NOTIFICATION_TYPE,
				"SLEE Spec 1.0, #13.4. SBBs use the Trace Facility to generate trace messages intended for "
						+ "consumption by external management clients, such as a network management console or a management policy engine.") };
	}

	/*
	 * (non-Javadoc)
	 * @see javax.slee.management.TraceMBean#getTraceLevel(javax.slee.management.NotificationSource, java.lang.String)
	 */
	public TraceLevel getTraceLevel(NotificationSource src, String tracerName) throws NullPointerException, InvalidArgumentException, UnrecognizedNotificationSourceException, ManagementException {
		if(src == null)
		{
			throw new NullPointerException("NotificationSource must not be null!");
		}
		
		if(!this.isNotificationSourceDefined(src))
		{
			throw new UnrecognizedNotificationSourceException("Notification source not recognized: "+src);
		}
		TracerImpl.checkTracerName(tracerName, src);
		
		if(!this.isTracerDefined(src,tracerName))
		{
			//FIXME: what is valid tracer name? JDOC contradicts that not existing tracer name is invalid
			this.createTracer(src,tracerName,false);
		}
		
		
		
		TracerStorage ts = this.tracerStorage.get(src);
		if (ts == null) {
			throw new ManagementException("NotificationSource has been uninstalled from SLEE. Can not create tracer.");
		}
		try {
			return ts.getTracerLevel(tracerName);
		} catch (Exception e) {
			throw new ManagementException("Failed to get trace level due to: ", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see javax.slee.management.TraceMBean#getTracersSet(javax.slee.management.NotificationSource)
	 */
	public String[] getTracersSet(NotificationSource src) throws NullPointerException, UnrecognizedNotificationSourceException, ManagementException {
		if(src == null)
		{
			throw new NullPointerException("NotificationSource must nto be null!");
		}
		if(!this.isNotificationSourceDefined(src))
		{
			throw new UnrecognizedNotificationSourceException("Notification source not recognized: "+src);
		}

			
		TracerStorage ts = this.tracerStorage.get(src);
		if (ts == null) {
			throw new ManagementException("NotificationSource has been uninstalled from SLEE. Can not create tracer.");
		}
		return ts.getDefinedTracerNames();
	}

	/*
	 * (non-Javadoc)
	 * @see javax.slee.management.TraceMBean#getTracersUsed(javax.slee.management.NotificationSource)
	 */
	public String[] getTracersUsed(NotificationSource src) throws NullPointerException, UnrecognizedNotificationSourceException, ManagementException {
		if(src == null)
		{
			throw new NullPointerException("NotificationSource must nto be null!");
		}
		if(!this.isNotificationSourceDefined(src))
		{
			throw new UnrecognizedNotificationSourceException("Notification source not recognized: "+src);
		}
		TracerStorage ts = this.tracerStorage.get(src);
		if (ts == null) {
			throw new ManagementException("NotificationSource has been uninstalled from SLEE. Can not create tracer.");
		}
		return ts.getRequestedTracerNames();
	}

	/*
	 * (non-Javadoc)
	 * @see javax.slee.management.TraceMBean#setTraceLevel(javax.slee.management.NotificationSource, java.lang.String, javax.slee.facilities.TraceLevel)
	 */
	public void setTraceLevel(NotificationSource src, final String tracerName, TraceLevel lvl) throws NullPointerException, UnrecognizedNotificationSourceException, InvalidArgumentException,
			ManagementException {
		if(src == null)
		{
			throw new NullPointerException("NotificationSource must nto be null!");
		}
		
		if(lvl == null)
		{
			throw new NullPointerException("TraceLevel must not be null!");
		}
		if
		(!this.isNotificationSourceDefined(src))
		{
			throw new UnrecognizedNotificationSourceException("Notification source not recognized: "+src);
		}
		TracerImpl.checkTracerName(tracerName, src);
		
		if(!this.isTracerDefined(src,tracerName))
		{
			//FIXME: what is valid tracer name? JDOC contradicts that not existing tracer name is invalid
			this.createTracer(src,tracerName,false);
		}
		
		final TracerStorage ts = this.tracerStorage.get(src);
		if (ts == null) {
			throw new ManagementException("NotificationSource has been uninstalled from SLEE. Can not create tracer.");
		}
		try {
			
			
			try {
				final SleeTransactionManager sleeTransactionManager = sleeContainer.getTransactionManager();
				if(sleeTransactionManager.getTransaction()!=null)
				{
					final TraceLevel _oldLevel=ts.getTracerLevel(tracerName);
					TransactionalAction action = new TransactionalAction() {
						TraceLevel oldLevel = _oldLevel;
						public void execute() {
							try {
								ts.setTracerLevel(oldLevel, tracerName);
							} catch (InvalidArgumentException e) {
								logger.error(e.getMessage(),e);
							}
						}
					};
					sleeTransactionManager.getTransactionContext().getAfterRollbackActions().add(action);
				}
			} catch (SystemException e) {
				
				e.printStackTrace();
			}
			
			ts.setTracerLevel(lvl, tracerName);
			
			
		} catch (Exception e) {
			throw new ManagementException("Failed to set trace level due to: ", e);
		}

		
	}

	/*
	 * (non-Javadoc)
	 * @see javax.slee.management.TraceMBean#unsetTraceLevel(javax.slee.management.NotificationSource, java.lang.String)
	 */
	public void unsetTraceLevel(NotificationSource src, final String tracerName) throws NullPointerException, UnrecognizedNotificationSourceException, InvalidArgumentException, ManagementException {
		if(src == null)
		{
			throw new NullPointerException("NotificationSource must nto be null!");
		}
		
		if(!this.isNotificationSourceDefined(src))
		{
			throw new UnrecognizedNotificationSourceException("Notification source not recognized: "+src);
		}
		TracerImpl.checkTracerName(tracerName, src);
		
		if(!this.isTracerDefined(src,tracerName))
		{
			//FIXME: what is valid tracer name? JDOC contradicts that not existing tracer name is invalid
			this.createTracer(src,tracerName,false);
		}
		
		final TracerStorage ts = this.tracerStorage.get(src);
		if (ts == null) {
			throw new ManagementException("NotificationSource has been uninstalled from SLEE. Can not create tracer.");
		}
		try {
			
			
			final SleeTransactionManager sleeTransactionManager = sleeContainer.getTransactionManager();
			try {
				if(sleeTransactionManager.getTransaction()!=null)
				{
					final TraceLevel _oldLevel=ts.getTracerLevel(tracerName);
					TransactionalAction action = new TransactionalAction() {
						TraceLevel oldLevel = _oldLevel;
						public void execute() {
							try {
								ts.setTracerLevel(oldLevel, tracerName);
							} catch (InvalidArgumentException e) {
								logger.error(e.getMessage(),e);
							}
						}
					};
					sleeTransactionManager.getTransactionContext().getAfterRollbackActions().add(action);
				}
			} catch (SystemException e) {
				
				e.printStackTrace();
			}
			
			
			
			
			
			ts.unsetTracerLevel(tracerName);
		} catch (Exception e) {
			throw new ManagementException("Failed to unset trace level due to: ", e);
		}
		
	}

	//Non MBEAN methods
		
	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.container.management.TraceManagement#getTraceMBeanObjectName()
	 */
	public javax.management.ObjectName getTraceMBeanObjectName() { return getObjectName(); };
	
	// 1.1 TracerImpl are. We define internal private class to hide details :)

	private Map<NotificationSource, TracerStorage> tracerStorage = new ConcurrentHashMap<NotificationSource, TracerStorage>();

	/**
	 * This checks if tracer name is ok. It must not be null;
	 * 
	 * @param split
	 * @throws IllegalArgumentException
	 */
	public static void checkTracerName(String tracerName, NotificationSource notificationSource) throws IllegalArgumentException {

		if (tracerName.compareTo("") == 0) {
			// This is root
			return;
		}

		// String[] splitName = tracerName.split("\\.");
		StringTokenizer stringTokenizer = new StringTokenizer(tracerName, ".", true);

		int fqdnPartIndex = 0;

		// if(splitName.length==0)
		// {
		// throw new IllegalArgumentException("Passed tracer:" + tracerName +
		// ", name for source: " + notificationSource + ", is illegal");
		// }

		String lastToken = null;

		while (stringTokenizer.hasMoreTokens()) {
			String token = stringTokenizer.nextToken();
			if (lastToken == null) {
				// this is start
				lastToken = token;
			}

			if (lastToken.compareTo(token) == 0 && token.compareTo(".") == 0) {
				throw new IllegalArgumentException("Passed tracer:" + tracerName + ", name for source: " + notificationSource + ", is illegal");
			}

			if (token.compareTo(".") != 0) {
				for (int charIndex = 0; charIndex < token.length(); charIndex++) {
					Character c = token.charAt(charIndex);
					if (Character.isLetter(c) || Character.isDigit(c)) {
						// Its ok?
					} else {
						throw new IllegalArgumentException("Passed tracer:" + tracerName + " Token[" + token + "], name for source: " + notificationSource
								+ ", is illegal, contains illegal character: " + charIndex);
					}
				}

				fqdnPartIndex++;
			}
			lastToken = token;

		}

		if (lastToken.compareTo(".") == 0) {
			throw new IllegalArgumentException("Passed tracer:" + tracerName + ", name for source: " + notificationSource + ", is illegal");
		}

	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.container.management.jmx.TraceManagement#registerNotificationSource(javax.slee.management.NotificationSource)
	 */
	public void registerNotificationSource(final NotificationSource src) {
		if (!this.tracerStorage.containsKey(src)) {
			TracerStorage ts = new TracerStorage(src, this);
			this.tracerStorage.put(src, ts);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.container.management.jmx.TraceManagement#deregisterNotificationSource(javax.slee.management.NotificationSource)
	 */
	public void deregisterNotificationSource(final NotificationSource src) {
		
		this.tracerStorage.remove(src);
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.container.management.jmx.TraceManagement#isNotificationSourceDefined(javax.slee.management.NotificationSource)
	 */
	public boolean isNotificationSourceDefined(NotificationSource src) {
		return this.tracerStorage.containsKey(src);
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.container.management.jmx.TraceManagement#isTracerDefined(javax.slee.management.NotificationSource, java.lang.String)
	 */
	public boolean isTracerDefined(NotificationSource src, String tracerName) throws ManagementException {
		TracerStorage ts = this.tracerStorage.get(src);
		if (ts == null) {
			throw new ManagementException("NotificationSource has been uninstalled from SLEE. Can not create tracer.");
		}

		return ts.isTracerDefined(tracerName);
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.container.management.TraceManagement#createTracer(javax.slee.management.NotificationSource, java.lang.String, boolean)
	 */
	public Tracer createTracer(NotificationSource src, String tracerName, boolean createdBySource) throws NullPointerException, InvalidArgumentException {
	
		TracerImpl.checkTracerName(tracerName, src);
		
		TracerStorage ts = this.tracerStorage.get(src);
		if (ts == null) {
			throw new IllegalStateException("NotificationSource has been uninstalled from SLEE. Can not create tracer.");
		}

		return ts.createTracer(tracerName, createdBySource);
		
	}

	@Override
	public void sleeInitialization() {
		Runnable r = new Runnable() {			
			public void run() {
				for(TracerStorage ts : tracerStorage.values()) {
					ts.syncTracersWithLog4J();
				}				
			}
		};
		scheduledFuture = sleeContainer.getNonClusteredScheduler().scheduleWithFixedDelay(r, LOG4J_LEVEL_SYNC_PERIOD,LOG4J_LEVEL_SYNC_PERIOD, TimeUnit.MINUTES);
	}
	
	@Override
	public void sleeStarting() {
		
	}
	
	@Override
	public void sleeRunning() {
		
	}
	
	@Override
	public void sleeStopping() {
		
	}
	
	@Override
	public void sleeStopped() {
		
	}
	
	@Override
	public void sleeShutdown() {
		if (!sleeContainer.getNonClusteredScheduler().isShutdown()) {
			scheduledFuture.cancel(false);
		}
	}
	
}

