package org.csapi.cc.mmccs;

/**
 *	Generated from IDL definition of struct "TpMediaNotificationRequested"
 *	@author JacORB IDL compiler 
 */

public final class TpMediaNotificationRequested
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpMediaNotificationRequested(){}
	public org.csapi.cc.mmccs.TpNotificationMediaRequest AppNotificationMediaRequest;
	public int AssignmentID;
	public TpMediaNotificationRequested(org.csapi.cc.mmccs.TpNotificationMediaRequest AppNotificationMediaRequest, int AssignmentID)
	{
		this.AppNotificationMediaRequest = AppNotificationMediaRequest;
		this.AssignmentID = AssignmentID;
	}
}
