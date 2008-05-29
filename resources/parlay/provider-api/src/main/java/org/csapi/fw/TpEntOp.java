package org.csapi.fw;

/**
 *	Generated from IDL definition of struct "TpEntOp"
 *	@author JacORB IDL compiler 
 */

public final class TpEntOp
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpEntOp(){}
	public java.lang.String EntOpID;
	public org.csapi.fw.TpProperty[] EntOpProperties;
	public TpEntOp(java.lang.String EntOpID, org.csapi.fw.TpProperty[] EntOpProperties)
	{
		this.EntOpID = EntOpID;
		this.EntOpProperties = EntOpProperties;
	}
}
