package org.csapi.cc.mmccs;

/**
 *	Generated from IDL definition of struct "TpMultiMediaCallLegIdentifier"
 *	@author JacORB IDL compiler 
 */

public final class TpMultiMediaCallLegIdentifier
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpMultiMediaCallLegIdentifier(){}
	public org.csapi.cc.mmccs.IpMultiMediaCallLeg MMCallLegReference;
	public int MMCallLegSessionID;
	public TpMultiMediaCallLegIdentifier(org.csapi.cc.mmccs.IpMultiMediaCallLeg MMCallLegReference, int MMCallLegSessionID)
	{
		this.MMCallLegReference = MMCallLegReference;
		this.MMCallLegSessionID = MMCallLegSessionID;
	}
}
