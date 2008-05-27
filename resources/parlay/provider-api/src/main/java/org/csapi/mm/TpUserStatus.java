package org.csapi.mm;

/**
 *	Generated from IDL definition of struct "TpUserStatus"
 *	@author JacORB IDL compiler 
 */

public final class TpUserStatus
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpUserStatus(){}
	public org.csapi.TpAddress UserID;
	public org.csapi.mm.TpMobilityError StatusCode;
	public org.csapi.mm.TpUserStatusIndicator Status;
	public org.csapi.mm.TpTerminalType TerminalType;
	public TpUserStatus(org.csapi.TpAddress UserID, org.csapi.mm.TpMobilityError StatusCode, org.csapi.mm.TpUserStatusIndicator Status, org.csapi.mm.TpTerminalType TerminalType)
	{
		this.UserID = UserID;
		this.StatusCode = StatusCode;
		this.Status = Status;
		this.TerminalType = TerminalType;
	}
}
