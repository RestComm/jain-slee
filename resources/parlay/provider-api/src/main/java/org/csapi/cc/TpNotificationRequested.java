package org.csapi.cc;

/**
 *	Generated from IDL definition of struct "TpNotificationRequested"
 *	@author JacORB IDL compiler 
 */

public final class TpNotificationRequested
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpNotificationRequested(){}
	public org.csapi.cc.TpCallNotificationRequest AppCallNotificationRequest;
	public int AssignmentID;
	public TpNotificationRequested(org.csapi.cc.TpCallNotificationRequest AppCallNotificationRequest, int AssignmentID)
	{
		this.AppCallNotificationRequest = AppCallNotificationRequest;
		this.AssignmentID = AssignmentID;
	}
}
