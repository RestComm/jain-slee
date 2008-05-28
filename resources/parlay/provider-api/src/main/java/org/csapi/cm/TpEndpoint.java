package org.csapi.cm;

/**
 *	Generated from IDL definition of struct "TpEndpoint"
 *	@author JacORB IDL compiler 
 */

public final class TpEndpoint
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpEndpoint(){}
	public org.csapi.cm.TpSiteOrSap type;
	public java.lang.String id;
	public TpEndpoint(org.csapi.cm.TpSiteOrSap type, java.lang.String id)
	{
		this.type = type;
		this.id = id;
	}
}
