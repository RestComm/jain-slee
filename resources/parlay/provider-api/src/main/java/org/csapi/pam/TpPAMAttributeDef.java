package org.csapi.pam;

/**
 *	Generated from IDL definition of struct "TpPAMAttributeDef"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMAttributeDef
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpPAMAttributeDef(){}
	public java.lang.String Name;
	public java.lang.String Type;
	public boolean IsStatic;
	public boolean IsRevertOnExpiration;
	public org.omg.CORBA.Any DefaultValues;
	public TpPAMAttributeDef(java.lang.String Name, java.lang.String Type, boolean IsStatic, boolean IsRevertOnExpiration, org.omg.CORBA.Any DefaultValues)
	{
		this.Name = Name;
		this.Type = Type;
		this.IsStatic = IsStatic;
		this.IsRevertOnExpiration = IsRevertOnExpiration;
		this.DefaultValues = DefaultValues;
	}
}
