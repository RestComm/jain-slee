/**
 * 
 */
package org.mobicents.slee.container;

import org.mobicents.slee.container.activity.ActivityContextHandle;

/**
 * @author martins
 *
 */
public class LogMessageFactory {

	private static final String NOT_APPLICABLE = "n/a";
	
	public static String newLogMessaget(String sbbEntityId, String logMessage) {
		return newLogMessaget(null, sbbEntityId, logMessage);
	}
	
	public static String newLogMessaget(ActivityContextHandle ach, String logMessage) {
		return newLogMessaget(ach, null, logMessage);
	}
	
	public static String newLogMessaget(ActivityContextHandle ach, String sbbEntityId, String logMessage) {
		return new StringBuilder("[AC = ").append(ach != null ? ach : NOT_APPLICABLE ).append(" , SBBE = ").append(sbbEntityId != null ? sbbEntityId : NOT_APPLICABLE).append("]\n\t").append(logMessage).toString();
	}
}
