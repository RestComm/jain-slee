package org.csapi.cc.mpccs;

/**
 *	Generated from IDL definition of struct "TpAppCallLegCallBack"
 *	@author JacORB IDL compiler 
 */

public final class TpAppCallLegCallBack
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpAppCallLegCallBack(){}
	public org.csapi.cc.mpccs.IpAppMultiPartyCall AppMultiPartyCall;
	public org.csapi.cc.mpccs.IpAppCallLeg[] AppCallLegSet;
	public TpAppCallLegCallBack(org.csapi.cc.mpccs.IpAppMultiPartyCall AppMultiPartyCall, org.csapi.cc.mpccs.IpAppCallLeg[] AppCallLegSet)
	{
		this.AppMultiPartyCall = AppMultiPartyCall;
		this.AppCallLegSet = AppCallLegSet;
	}
}
