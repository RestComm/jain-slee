/**
 * 
 */
package org.mobicents.slee.container;

import org.mobicents.slee.runtime.activity.ActivityContextHandle;

/**
 * @author martins
 *
 */
public class LogMessageFactory {

	private static final String NOT_APPLICABLE = "n/a";
	
	public static String newLogMessage(String sbbEntityId, String logMessage) {
		return newLogMessage(null, sbbEntityId, logMessage);
	}
	
	public static String newLogMessage(ActivityContextHandle ach, String logMessage) {
		return newLogMessage(ach, null, logMessage);
	}
	
	public static String newLogMessage(ActivityContextHandle ach, String sbbEntityId, String logMessage) {
		return "[ AC "+(ach != null ? ach : NOT_APPLICABLE )+" ] [ SBB ENTITY "+(sbbEntityId != null ? sbbEntityId : NOT_APPLICABLE)+" ] "+logMessage;
	}
}
