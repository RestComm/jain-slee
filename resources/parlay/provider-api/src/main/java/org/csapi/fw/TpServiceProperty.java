package org.csapi.fw;

/**
 *	Generated from IDL definition of struct "TpServiceProperty"
 *	@author JacORB IDL compiler 
 */

public final class TpServiceProperty
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpServiceProperty(){}
	public java.lang.String ServicePropertyName;
	public java.lang.String[] ServicePropertyValueList;
	public TpServiceProperty(java.lang.String ServicePropertyName, java.lang.String[] ServicePropertyValueList)
	{
		this.ServicePropertyName = ServicePropertyName;
		this.ServicePropertyValueList = ServicePropertyValueList;
	}
}
