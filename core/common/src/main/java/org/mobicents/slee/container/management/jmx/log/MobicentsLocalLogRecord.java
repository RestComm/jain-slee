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

import java.util.logging.Level;
import java.util.logging.LogRecord;


/**
 * Conviniance class just to copy all entries from log record but with use of previously formatted message.
 * This allows us to retain all info about log message and have it fomratted for GUI, this info is afterwards 
 * copied (again, but otherwise we would have to use String[] ...) into GWT class which can be used on client
 * @author baranowb
 *
 */
public class MobicentsLocalLogRecord extends LogRecord {

	private String formattedMessage=null;

	public MobicentsLocalLogRecord(LogRecord record,String formattedMessage)
	{
		super(record.getLevel(),record.getMessage());
		this.formattedMessage=formattedMessage;
		//This actually should be already present depending on message formatter, but...
		//Why LogRecord doesnt have clone?
		super.setLoggerName(record.getLoggerName());
		super.setMillis(record.getMillis());
		super.setParameters(record.getParameters());
		super.setResourceBundle(record.getResourceBundle());
		super.setResourceBundleName(record.getResourceBundleName());
		super.setSequenceNumber(record.getSequenceNumber());
		super.setSourceClassName(record.getSourceClassName());
		super.setSourceMethodName(record.getSourceMethodName());
		super.setThreadID(record.getThreadID());
		//This wont be sent as class can be not mirrored in GWT ;[
		super.setThrown(record.getThrown());
	}

	public String getFormattedMessage() {
		return formattedMessage;
	}
	
	
}
