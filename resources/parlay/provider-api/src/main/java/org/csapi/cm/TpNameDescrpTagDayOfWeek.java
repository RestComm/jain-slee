package org.csapi.cm;

/**
 *	Generated from IDL definition of struct "TpNameDescrpTagDayOfWeek"
 *	@author JacORB IDL compiler 
 */

public final class TpNameDescrpTagDayOfWeek
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpNameDescrpTagDayOfWeek(){}
	public java.lang.String name;
	public java.lang.String description;
	public org.csapi.cm.TpTagValue tag;
	public int value;
	public TpNameDescrpTagDayOfWeek(java.lang.String name, java.lang.String description, org.csapi.cm.TpTagValue tag, int value)
	{
		this.name = name;
		this.description = description;
		this.tag = tag;
		this.value = value;
	}
}
