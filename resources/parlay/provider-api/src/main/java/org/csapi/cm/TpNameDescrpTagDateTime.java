package org.csapi.cm;

/**
 *	Generated from IDL definition of struct "TpNameDescrpTagDateTime"
 *	@author JacORB IDL compiler 
 */

public final class TpNameDescrpTagDateTime
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpNameDescrpTagDateTime(){}
	public java.lang.String name;
	public java.lang.String description;
	public org.csapi.cm.TpTagValue tag;
	public java.lang.String value;
	public TpNameDescrpTagDateTime(java.lang.String name, java.lang.String description, org.csapi.cm.TpTagValue tag, java.lang.String value)
	{
		this.name = name;
		this.description = description;
		this.tag = tag;
		this.value = value;
	}
}
