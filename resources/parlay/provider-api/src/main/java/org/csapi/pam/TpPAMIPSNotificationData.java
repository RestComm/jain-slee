package org.csapi.pam;

/**
 *	Generated from IDL definition of struct "TpPAMIPSNotificationData"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMIPSNotificationData
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpPAMIPSNotificationData(){}
	public java.lang.String Identity;
	public org.csapi.pam.TpPAMPresenceData[] Attributes;
	public TpPAMIPSNotificationData(java.lang.String Identity, org.csapi.pam.TpPAMPresenceData[] Attributes)
	{
		this.Identity = Identity;
		this.Attributes = Attributes;
	}
}
