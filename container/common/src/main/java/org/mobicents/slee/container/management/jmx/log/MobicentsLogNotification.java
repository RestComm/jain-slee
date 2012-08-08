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
import java.util.logging.LogRecord;

import javax.management.Notification;

public class MobicentsLogNotification extends Notification {

	
	private String loggerName=null;
	private ArrayList<MobicentsLocalLogRecord> records=null;
	
	public MobicentsLogNotification(String type, Object source,
			long sequenceNumber, long timeStamp, String message,ArrayList<MobicentsLocalLogRecord> records, String loggerName) {
		super(type, source, sequenceNumber, timeStamp, message);
		this.loggerName=loggerName;
		this.records=records;
	}

	

	public MobicentsLogNotification(String type, Object source,
			long sequenceNumber, String message,ArrayList<MobicentsLocalLogRecord> records, String loggerName) {
		super(type, source, sequenceNumber, message);
		this.records=records;
		this.loggerName=loggerName;
	}



	public String getLoggerName() {
		return loggerName;
	}



	public ArrayList<MobicentsLocalLogRecord> getRecords() {
		return records;
	}


}
	
	
	
	
	

