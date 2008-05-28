package org.csapi.pam;

/**
 *	Generated from IDL definition of struct "TpPAMAPSNotificationData"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMAPSNotificationData
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpPAMAPSNotificationData(){}
	public java.lang.String Agent;
	public java.lang.String[] AttributeNames;
	public TpPAMAPSNotificationData(java.lang.String Agent, java.lang.String[] AttributeNames)
	{
		this.Agent = Agent;
		this.AttributeNames = AttributeNames;
	}
}
