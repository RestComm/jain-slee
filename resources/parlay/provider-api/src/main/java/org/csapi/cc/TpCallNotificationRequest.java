package org.csapi.cc;

/**
 *	Generated from IDL definition of struct "TpCallNotificationRequest"
 *	@author JacORB IDL compiler 
 */

public final class TpCallNotificationRequest
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpCallNotificationRequest(){}
	public org.csapi.cc.TpCallNotificationScope CallNotificationScope;
	public org.csapi.cc.TpCallEventRequest[] CallEventsRequested;
	public TpCallNotificationRequest(org.csapi.cc.TpCallNotificationScope CallNotificationScope, org.csapi.cc.TpCallEventRequest[] CallEventsRequested)
	{
		this.CallNotificationScope = CallNotificationScope;
		this.CallEventsRequested = CallEventsRequested;
	}
}
