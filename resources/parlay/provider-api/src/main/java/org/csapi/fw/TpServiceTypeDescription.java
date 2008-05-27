package org.csapi.fw;

/**
 *	Generated from IDL definition of struct "TpServiceTypeDescription"
 *	@author JacORB IDL compiler 
 */

public final class TpServiceTypeDescription
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpServiceTypeDescription(){}
	public org.csapi.fw.TpServiceTypeProperty[] ServiceTypePropertyList;
	public java.lang.String[] ServiceTypeNameList;
	public boolean AvailableOrUnavailable;
	public TpServiceTypeDescription(org.csapi.fw.TpServiceTypeProperty[] ServiceTypePropertyList, java.lang.String[] ServiceTypeNameList, boolean AvailableOrUnavailable)
	{
		this.ServiceTypePropertyList = ServiceTypePropertyList;
		this.ServiceTypeNameList = ServiceTypeNameList;
		this.AvailableOrUnavailable = AvailableOrUnavailable;
	}
}
