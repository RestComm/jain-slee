package org.csapi.cm;

/**
 *	Generated from IDL definition of struct "TpNameDescrpTagMonth"
 *	@author JacORB IDL compiler 
 */

public final class TpNameDescrpTagMonth
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpNameDescrpTagMonth(){}
	public java.lang.String name;
	public java.lang.String description;
	public org.csapi.cm.TpTagValue tag;
	public int value;
	public TpNameDescrpTagMonth(java.lang.String name, java.lang.String description, org.csapi.cm.TpTagValue tag, int value)
	{
		this.name = name;
		this.description = description;
		this.tag = tag;
		this.value = value;
	}
}
