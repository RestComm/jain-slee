package org.csapi.fw;

/**
 *	Generated from IDL definition of struct "TpServiceDescription"
 *	@author JacORB IDL compiler 
 */

public final class TpServiceDescription
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpServiceDescription(){}
	public java.lang.String ServiceTypeName;
	public org.csapi.fw.TpServiceProperty[] ServicePropertyList;
	public TpServiceDescription(java.lang.String ServiceTypeName, org.csapi.fw.TpServiceProperty[] ServicePropertyList)
	{
		this.ServiceTypeName = ServiceTypeName;
		this.ServicePropertyList = ServicePropertyList;
	}
}
