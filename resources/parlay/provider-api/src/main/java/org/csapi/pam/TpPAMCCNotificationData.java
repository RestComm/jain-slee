package org.csapi.pam;

/**
 *	Generated from IDL definition of struct "TpPAMCCNotificationData"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMCCNotificationData
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpPAMCCNotificationData(){}
	public java.lang.String Identity;
	public java.lang.String[] Capabilities;
	public TpPAMCCNotificationData(java.lang.String Identity, java.lang.String[] Capabilities)
	{
		this.Identity = Identity;
		this.Capabilities = Capabilities;
	}
}
