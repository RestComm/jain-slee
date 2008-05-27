package org.csapi.pam;

/**
 *	Generated from IDL definition of struct "TpPAMContext"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMContext
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpPAMContext(){}
	public org.csapi.pam.TpPAMContextData ContextData;
	public org.csapi.TpAttribute[] AskerData;
	public TpPAMContext(org.csapi.pam.TpPAMContextData ContextData, org.csapi.TpAttribute[] AskerData)
	{
		this.ContextData = ContextData;
		this.AskerData = AskerData;
	}
}
