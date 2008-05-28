package org.csapi.pam;

/**
 *	Generated from IDL definition of struct "TpPAMIDEventData"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMIDEventData
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpPAMIDEventData(){}
	public java.lang.String[] IdentityName;
	public java.lang.String[] IdentityType;
	public TpPAMIDEventData(java.lang.String[] IdentityName, java.lang.String[] IdentityType)
	{
		this.IdentityName = IdentityName;
		this.IdentityType = IdentityType;
	}
}
