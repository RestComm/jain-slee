package org.csapi.cc.mmccs;

/**
 *	Generated from IDL definition of struct "TpAppMultiMediaCallLegCallBack"
 *	@author JacORB IDL compiler 
 */

public final class TpAppMultiMediaCallLegCallBack
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpAppMultiMediaCallLegCallBack(){}
	public org.csapi.cc.mmccs.IpAppMultiMediaCall AppMultiMediaCall;
	public org.csapi.cc.mmccs.IpAppMultiMediaCallLeg[] AppCallLegSet;
	public TpAppMultiMediaCallLegCallBack(org.csapi.cc.mmccs.IpAppMultiMediaCall AppMultiMediaCall, org.csapi.cc.mmccs.IpAppMultiMediaCallLeg[] AppCallLegSet)
	{
		this.AppMultiMediaCall = AppMultiMediaCall;
		this.AppCallLegSet = AppCallLegSet;
	}
}
