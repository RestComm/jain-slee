package org.csapi.cc.mpccs;

/**
 *	Generated from IDL definition of struct "TpCallLegIdentifier"
 *	@author JacORB IDL compiler 
 */

public final class TpCallLegIdentifier
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpCallLegIdentifier(){}
	public org.csapi.cc.mpccs.IpCallLeg CallLegReference;
	public int CallLegSessionID;
	public TpCallLegIdentifier(org.csapi.cc.mpccs.IpCallLeg CallLegReference, int CallLegSessionID)
	{
		this.CallLegReference = CallLegReference;
		this.CallLegSessionID = CallLegSessionID;
	}
}
