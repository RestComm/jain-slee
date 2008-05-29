package org.csapi.cm;

/**
 *	Generated from IDL definition of struct "TpNameDescrpTagDir"
 *	@author JacORB IDL compiler 
 */

public final class TpNameDescrpTagDir
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpNameDescrpTagDir(){}
	public java.lang.String name;
	public java.lang.String description;
	public org.csapi.cm.TpTagValue tag;
	public org.csapi.cm.TpTrafficDirection value;
	public TpNameDescrpTagDir(java.lang.String name, java.lang.String description, org.csapi.cm.TpTagValue tag, org.csapi.cm.TpTrafficDirection value)
	{
		this.name = name;
		this.description = description;
		this.tag = tag;
		this.value = value;
	}
}
