package org.csapi.cc;

/**
 *	Generated from IDL definition of struct "TpNotificationRequestedSetEntry"
 *	@author JacORB IDL compiler 
 */

public final class TpNotificationRequestedSetEntry
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpNotificationRequestedSetEntry(){}
	public org.csapi.cc.TpNotificationRequested[] NotificationRequestSet;
	public boolean Final;
	public TpNotificationRequestedSetEntry(org.csapi.cc.TpNotificationRequested[] NotificationRequestSet, boolean Final)
	{
		this.NotificationRequestSet = NotificationRequestSet;
		this.Final = Final;
	}
}
