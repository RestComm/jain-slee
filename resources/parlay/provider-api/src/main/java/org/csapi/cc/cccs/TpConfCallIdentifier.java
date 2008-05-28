package org.csapi.cc.cccs;

/**
 *	Generated from IDL definition of struct "TpConfCallIdentifier"
 *	@author JacORB IDL compiler 
 */

public final class TpConfCallIdentifier
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpConfCallIdentifier(){}
	public org.csapi.cc.cccs.IpConfCall ConfCallReference;
	public int ConfCallSessionID;
	public TpConfCallIdentifier(org.csapi.cc.cccs.IpConfCall ConfCallReference, int ConfCallSessionID)
	{
		this.ConfCallReference = ConfCallReference;
		this.ConfCallSessionID = ConfCallSessionID;
	}
}
