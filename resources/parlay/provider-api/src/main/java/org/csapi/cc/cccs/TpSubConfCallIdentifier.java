package org.csapi.cc.cccs;

/**
 *	Generated from IDL definition of struct "TpSubConfCallIdentifier"
 *	@author JacORB IDL compiler 
 */

public final class TpSubConfCallIdentifier
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpSubConfCallIdentifier(){}
	public org.csapi.cc.cccs.IpSubConfCall SubConfCallReference;
	public int SubConfCallSessionID;
	public TpSubConfCallIdentifier(org.csapi.cc.cccs.IpSubConfCall SubConfCallReference, int SubConfCallSessionID)
	{
		this.SubConfCallReference = SubConfCallReference;
		this.SubConfCallSessionID = SubConfCallSessionID;
	}
}
