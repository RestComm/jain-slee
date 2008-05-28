package org.csapi.cc;

/**
 *	Generated from IDL definition of struct "TpCallEndedReport"
 *	@author JacORB IDL compiler 
 */

public final class TpCallEndedReport
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpCallEndedReport(){}
	public int CallLegSessionID;
	public org.csapi.cc.TpReleaseCause Cause;
	public TpCallEndedReport(int CallLegSessionID, org.csapi.cc.TpReleaseCause Cause)
	{
		this.CallLegSessionID = CallLegSessionID;
		this.Cause = Cause;
	}
}
