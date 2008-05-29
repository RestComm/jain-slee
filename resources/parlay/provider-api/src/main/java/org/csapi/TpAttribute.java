package org.csapi;

/**
 *	Generated from IDL definition of struct "TpAttribute"
 *	@author JacORB IDL compiler 
 */

public final class TpAttribute
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpAttribute(){}
	public java.lang.String AttributeName;
	public org.csapi.TpAttributeValue AttributeValue;
	public TpAttribute(java.lang.String AttributeName, org.csapi.TpAttributeValue AttributeValue)
	{
		this.AttributeName = AttributeName;
		this.AttributeValue = AttributeValue;
	}
}
