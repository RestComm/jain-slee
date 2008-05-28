package org.csapi.pam;

/**
 *	Generated from IDL definition of struct "TpPAMAttribute"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMAttribute
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpPAMAttribute(){}
	public java.lang.String AttributeName;
	public org.csapi.TpAttributeValue AttributeValue;
	public long ExpiresIn;
	public TpPAMAttribute(java.lang.String AttributeName, org.csapi.TpAttributeValue AttributeValue, long ExpiresIn)
	{
		this.AttributeName = AttributeName;
		this.AttributeValue = AttributeValue;
		this.ExpiresIn = ExpiresIn;
	}
}
