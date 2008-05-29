package org.csapi.pam;

/**
 *	Generated from IDL definition of struct "TpPAMWCNotificationData"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMWCNotificationData
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpPAMWCNotificationData(){}
	public org.csapi.pam.TpPAMEventName Event;
	public org.csapi.pam.TpPAMwatcherChangeType ChangeType;
	public java.lang.String Identity;
	public java.lang.String[] Watchers;
	public TpPAMWCNotificationData(org.csapi.pam.TpPAMEventName Event, org.csapi.pam.TpPAMwatcherChangeType ChangeType, java.lang.String Identity, java.lang.String[] Watchers)
	{
		this.Event = Event;
		this.ChangeType = ChangeType;
		this.Identity = Identity;
		this.Watchers = Watchers;
	}
}
