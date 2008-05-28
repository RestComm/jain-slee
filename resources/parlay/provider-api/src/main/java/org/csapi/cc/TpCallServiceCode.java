package org.csapi.cc;

/**
 *	Generated from IDL definition of struct "TpCallServiceCode"
 *	@author JacORB IDL compiler 
 */

public final class TpCallServiceCode
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpCallServiceCode(){}
	public org.csapi.cc.TpCallServiceCodeType CallServiceCodeType;
	public java.lang.String ServiceCodeValue;
	public TpCallServiceCode(org.csapi.cc.TpCallServiceCodeType CallServiceCodeType, java.lang.String ServiceCodeValue)
	{
		this.CallServiceCodeType = CallServiceCodeType;
		this.ServiceCodeValue = ServiceCodeValue;
	}
}
