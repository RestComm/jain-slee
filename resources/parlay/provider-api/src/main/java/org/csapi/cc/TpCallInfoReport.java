package org.csapi.cc;

/**
 *	Generated from IDL definition of struct "TpCallInfoReport"
 *	@author JacORB IDL compiler 
 */

public final class TpCallInfoReport
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpCallInfoReport(){}
	public int CallInfoType;
	public java.lang.String CallInitiationStartTime;
	public java.lang.String CallConnectedToResourceTime;
	public java.lang.String CallConnectedToDestinationTime;
	public java.lang.String CallEndTime;
	public org.csapi.cc.TpReleaseCause Cause;
	public TpCallInfoReport(int CallInfoType, java.lang.String CallInitiationStartTime, java.lang.String CallConnectedToResourceTime, java.lang.String CallConnectedToDestinationTime, java.lang.String CallEndTime, org.csapi.cc.TpReleaseCause Cause)
	{
		this.CallInfoType = CallInfoType;
		this.CallInitiationStartTime = CallInitiationStartTime;
		this.CallConnectedToResourceTime = CallConnectedToResourceTime;
		this.CallConnectedToDestinationTime = CallConnectedToDestinationTime;
		this.CallEndTime = CallEndTime;
		this.Cause = Cause;
	}
}
