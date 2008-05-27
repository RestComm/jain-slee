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
	
	
	
	
	

