package org.csapi.cc.gccs;

/**
 *	Generated from IDL definition of struct "TpCallIdentifier"
 *	@author JacORB IDL compiler 
 */

public final class TpCallIdentifier
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpCallIdentifier(){}
	public org.csapi.cc.gccs.IpCall CallReference;
	public int CallSessionID;
	public TpCallIdentifier(org.csapi.cc.gccs.IpCall CallReference, int CallSessionID)
	{
		this.CallReference = CallReference;
		this.CallSessionID = CallSessionID;
	}
}
