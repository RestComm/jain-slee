package org.csapi.cc.mmccs;

/**
 *	Generated from IDL definition of struct "TpMultiMediaCallIdentifier"
 *	@author JacORB IDL compiler 
 */

public final class TpMultiMediaCallIdentifier
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpMultiMediaCallIdentifier(){}
	public org.csapi.cc.mmccs.IpMultiMediaCall MMCallReference;
	public int MMCallSessionID;
	public TpMultiMediaCallIdentifier(org.csapi.cc.mmccs.IpMultiMediaCall MMCallReference, int MMCallSessionID)
	{
		this.MMCallReference = MMCallReference;
		this.MMCallSessionID = MMCallSessionID;
	}
}
