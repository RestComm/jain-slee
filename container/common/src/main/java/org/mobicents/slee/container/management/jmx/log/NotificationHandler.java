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

package org.mobicents.slee.container.management.jmx.log;

import java.util.ArrayList;
import java.util.logging.*;


import javax.management.Notification;
import javax.management.monitor.MonitorNotification;

public class NotificationHandler extends Handler {

	private int accumulatedEntries = 0;
	private int notificationInterval = 150;
	private StringBuffer stringBufferedResults = null;
	private ArrayList<MobicentsLocalLogRecord> records=new ArrayList<MobicentsLocalLogRecord>(notificationInterval);
	//This doesnt work, why?
	//private ArrayList<LogRecord> records=new ArrayList<MobicentsLocalLogRecord>(notificationInterval);
	private MobicentsLogManagerMBeanImpl notifier = null;
	private SimpleFormatter defaultFormatter = new SimpleFormatter();
	private long seq = 0;
	private String loggerName=null;

	private static final Logger logger=Logger.getLogger(NotificationHandler.class.getCanonicalName());
	
	public NotificationHandler(int notificationInterval,
			MobicentsLogManagerMBeanImpl notifier, String loggerName) {
		super();
		Logger.global.info("CREATE NOTI HANDLER["+loggerName+"]["+notificationInterval+"]");
		this.notificationInterval = notificationInterval;
		this.notifier = notifier;
		this.loggerName=loggerName;
		this.stringBufferedResults=new StringBuffer(500);
	}

	@Override
	public void close() throws SecurityException {
		
		this.stringBufferedResults=new StringBuffer();
		this.accumulatedEntries=0;
	}

	@Override
	public void flush() {
		
		//Logger.global.info("FLSHING!!!!!!!!!!!");
		String result=stringBufferedResults.toString();
		MobicentsLogNotification notification=new MobicentsLogNotification(MonitorNotification.THRESHOLD_VALUE_EXCEEDED,this,seq,System.currentTimeMillis(),result,records, loggerName);
		notifier.sendNotification(notification);
		this.stringBufferedResults=new StringBuffer();
		this.accumulatedEntries=0;
		this.records=new ArrayList<MobicentsLocalLogRecord>(notificationInterval);
		
	}

	@Override
	public void publish(LogRecord record) {

		if (this.getFilter() != null && !this.getFilter().isLoggable(record))
			return;

		//Logger.global.info("    ----   ["+(this.accumulatedEntries > notificationInterval)+"] ["+!(this.notificationInterval<0)+"]");
		if ( (this.accumulatedEntries > notificationInterval) && !(this.notificationInterval<0))
			this.flush();

		
		//Some init methods to fill fields iwth proper values
		record.getSourceClassName();
		record.getSourceMethodName();
		
		
		
		String result = null;
		if (this.getFormatter() != null)
			result = this.getFormatter().format(record);
		else
			result = this.defaultFormatter.format(record);

		MobicentsLocalLogRecord mlr=new MobicentsLocalLogRecord(record,result);
		
		records.add(mlr);
		
		this.stringBufferedResults.append(result+  "\n");
		this.accumulatedEntries++;

	}

	public String fetchLog() {

		String result = this.stringBufferedResults.toString();
		this.flush();
		return result;

	}

	@Override
	public String toString() {
		return this.getClass().getName()+" Interval["+this.notificationInterval+"] Accumulated["+this.accumulatedEntries+"] Entries["+this.stringBufferedResults+"]";
	}

	public int getNotificationInterval() {
		return notificationInterval;
	}

	public void setNotificationInterval(int notificationInterval) {
		if(this.notificationInterval<=notificationInterval)
			this.flush();
		this.notificationInterval = notificationInterval;
	}
	
	
	

}
