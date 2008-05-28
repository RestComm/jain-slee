package org.csapi.cs;

/**
 *	Generated from IDL definition of struct "TpApplicationDescription"
 *	@author JacORB IDL compiler 
 */

public final class TpApplicationDescription
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpApplicationDescription(){}
	public java.lang.String Text;
	public org.csapi.cs.TpAppInformation[] AppInformation;
	public TpApplicationDescription(java.lang.String Text, org.csapi.cs.TpAppInformation[] AppInformation)
	{
		this.Text = Text;
		this.AppInformation = AppInformation;
	}
}
