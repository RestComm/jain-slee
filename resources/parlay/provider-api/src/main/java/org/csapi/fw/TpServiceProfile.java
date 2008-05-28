package org.csapi.fw;

/**
 *	Generated from IDL definition of struct "TpServiceProfile"
 *	@author JacORB IDL compiler 
 */

public final class TpServiceProfile
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpServiceProfile(){}
	public java.lang.String ServiceProfileID;
	public org.csapi.fw.TpServiceProfileDescription ServiceProfileDescription;
	public TpServiceProfile(java.lang.String ServiceProfileID, org.csapi.fw.TpServiceProfileDescription ServiceProfileDescription)
	{
		this.ServiceProfileID = ServiceProfileID;
		this.ServiceProfileDescription = ServiceProfileDescription;
	}
}
