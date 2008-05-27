package org.csapi.cc.mpccs;

/**
 *	Generated from IDL definition of struct "TpMultiPartyCallIdentifier"
 *	@author JacORB IDL compiler 
 */

public final class TpMultiPartyCallIdentifier
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpMultiPartyCallIdentifier(){}
	public org.csapi.cc.mpccs.IpMultiPartyCall CallReference;
	public int CallSessionID;
	public TpMultiPartyCallIdentifier(org.csapi.cc.mpccs.IpMultiPartyCall CallReference, int CallSessionID)
	{
		this.CallReference = CallReference;
		this.CallSessionID = CallSessionID;
	}
}
