package org.csapi.pam;

/**
 *	Generated from IDL definition of struct "TpPAMCCEventData"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMCCEventData
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpPAMCCEventData(){}
	public java.lang.String[] IdentityName;
	public java.lang.String[] IdentityType;
	public java.lang.String[] Capabilities;
	public TpPAMCCEventData(java.lang.String[] IdentityName, java.lang.String[] IdentityType, java.lang.String[] Capabilities)
	{
		this.IdentityName = IdentityName;
		this.IdentityType = IdentityType;
		this.Capabilities = Capabilities;
	}
}
