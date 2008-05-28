package org.csapi.pam;

/**
 *	Generated from IDL definition of struct "TpPAMAccessControlData"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMAccessControlData
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpPAMAccessControlData(){}
	public org.csapi.pam.TpPAMACLDefault DefaultPolicy;
	public java.lang.String[] AllowList;
	public java.lang.String[] DenyList;
	public TpPAMAccessControlData(org.csapi.pam.TpPAMACLDefault DefaultPolicy, java.lang.String[] AllowList, java.lang.String[] DenyList)
	{
		this.DefaultPolicy = DefaultPolicy;
		this.AllowList = AllowList;
		this.DenyList = DenyList;
	}
}
