package org.csapi.cm;

/**
 *	Generated from IDL definition of struct "TpNameDescrpTagTimeOfDay"
 *	@author JacORB IDL compiler 
 */

public final class TpNameDescrpTagTimeOfDay
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpNameDescrpTagTimeOfDay(){}
	public java.lang.String name;
	public java.lang.String description;
	public org.csapi.cm.TpTagValue tag;
	public java.lang.String value;
	public TpNameDescrpTagTimeOfDay(java.lang.String name, java.lang.String description, org.csapi.cm.TpTagValue tag, java.lang.String value)
	{
		this.name = name;
		this.description = description;
		this.tag = tag;
		this.value = value;
	}
}
