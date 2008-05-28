package org.csapi.cm;

/**
 *	Generated from IDL definition of struct "TpNameDescrpTagString"
 *	@author JacORB IDL compiler 
 */

public final class TpNameDescrpTagString
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpNameDescrpTagString(){}
	public java.lang.String name;
	public java.lang.String description;
	public org.csapi.cm.TpTagValue tag;
	public java.lang.String value;
	public TpNameDescrpTagString(java.lang.String name, java.lang.String description, org.csapi.cm.TpTagValue tag, java.lang.String value)
	{
		this.name = name;
		this.description = description;
		this.tag = tag;
		this.value = value;
	}
}
