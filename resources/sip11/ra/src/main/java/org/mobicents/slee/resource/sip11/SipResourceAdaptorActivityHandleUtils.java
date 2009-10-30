package org.mobicents.slee.resource.sip11;

import javax.slee.resource.ActivityHandle;

public class SipResourceAdaptorActivityHandleUtils {

	public static boolean isUnconfirmedDialogActivityHandle(ActivityHandle activityHandle) {
		return activityHandle.getClass() == DialogWithoutIdActivityHandle.class;		
	}
	
	public static boolean isConfirmedDialogActivityHandle(ActivityHandle activityHandle) {
		return activityHandle.getClass() == DialogWithIdActivityHandle.class;		
	}
	
	public static boolean isTransactionActivityHandle(ActivityHandle activityHandle) {
		return activityHandle.getClass() == TransactionActivityHandle.class;		
	}
	
}
