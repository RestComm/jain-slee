package org.csapi.pam;

/**
 *	Generated from IDL definition of struct "TpPAMACPSNotificationData"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMACPSNotificationData
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpPAMACPSNotificationData(){}
	public java.lang.String Agent;
	public java.lang.String Capability;
	public java.lang.String[] AttributeNames;
	public TpPAMACPSNotificationData(java.lang.String Agent, java.lang.String Capability, java.lang.String[] AttributeNames)
	{
		this.Agent = Agent;
		this.Capability = Capability;
		this.AttributeNames = AttributeNames;
	}
}
