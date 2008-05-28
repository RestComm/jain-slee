package org.csapi.pam;

/**
 *	Generated from IDL definition of struct "TpPAMAANotificationData"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMAANotificationData
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpPAMAANotificationData(){}
	public java.lang.String Identity;
	public java.lang.String Agent;
	public TpPAMAANotificationData(java.lang.String Identity, java.lang.String Agent)
	{
		this.Identity = Identity;
		this.Agent = Agent;
	}
}
