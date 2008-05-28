package org.csapi;

/**
 *	Generated from IDL definition of struct "TpChargeAdviceInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpChargeAdviceInfo
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpChargeAdviceInfo(){}
	public org.csapi.TpCAIElements CurrentCAI;
	public org.csapi.TpCAIElements NextCAI;
	public TpChargeAdviceInfo(org.csapi.TpCAIElements CurrentCAI, org.csapi.TpCAIElements NextCAI)
	{
		this.CurrentCAI = CurrentCAI;
		this.NextCAI = NextCAI;
	}
}
