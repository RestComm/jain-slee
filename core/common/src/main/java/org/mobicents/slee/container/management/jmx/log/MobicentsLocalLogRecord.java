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
