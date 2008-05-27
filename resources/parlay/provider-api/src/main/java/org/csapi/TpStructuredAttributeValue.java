package org.csapi;

/**
 *	Generated from IDL definition of struct "TpStructuredAttributeValue"
 *	@author JacORB IDL compiler 
 */

public final class TpStructuredAttributeValue
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpStructuredAttributeValue(){}
	public java.lang.String Type;
	public org.omg.CORBA.Any Value;
	public TpStructuredAttributeValue(java.lang.String Type, org.omg.CORBA.Any Value)
	{
		this.Type = Type;
		this.Value = Value;
	}
}
