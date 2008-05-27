package org.csapi.fw;

/**
 *	Generated from IDL definition of struct "TpServiceTypeProperty"
 *	@author JacORB IDL compiler 
 */

public final class TpServiceTypeProperty
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpServiceTypeProperty(){}
	public java.lang.String ServicePropertyName;
	public org.csapi.fw.TpServiceTypePropertyMode ServiceTypePropertyMode;
	public java.lang.String ServicePropertyTypeName;
	public TpServiceTypeProperty(java.lang.String ServicePropertyName, org.csapi.fw.TpServiceTypePropertyMode ServiceTypePropertyMode, java.lang.String ServicePropertyTypeName)
	{
		this.ServicePropertyName = ServicePropertyName;
		this.ServiceTypePropertyMode = ServiceTypePropertyMode;
		this.ServicePropertyTypeName = ServicePropertyTypeName;
	}
}
