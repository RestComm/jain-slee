package org.csapi.cm;

/**
 *	Generated from IDL definition of struct "TpNameDescrpTagExcessLoadAction"
 *	@author JacORB IDL compiler 
 */

public final class TpNameDescrpTagExcessLoadAction
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpNameDescrpTagExcessLoadAction(){}
	public java.lang.String name;
	public java.lang.String description;
	public org.csapi.cm.TpTagValue tag;
	public org.csapi.cm.TpAction value;
	public TpNameDescrpTagExcessLoadAction(java.lang.String name, java.lang.String description, org.csapi.cm.TpTagValue tag, org.csapi.cm.TpAction value)
	{
		this.name = name;
		this.description = description;
		this.tag = tag;
		this.value = value;
	}
}
