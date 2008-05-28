package org.csapi.cc;

/**
 *	Generated from IDL definition of struct "TpCallLegInfoReport"
 *	@author JacORB IDL compiler 
 */

public final class TpCallLegInfoReport
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpCallLegInfoReport(){}
	public int CallLegInfoType;
	public java.lang.String CallLegStartTime;
	public java.lang.String CallLegConnectedToResourceTime;
	public java.lang.String CallLegConnectedToAddressTime;
	public java.lang.String CallLegEndTime;
	public org.csapi.TpAddress ConnectedAddress;
	public org.csapi.cc.TpReleaseCause CallLegReleaseCause;
	public org.csapi.cc.TpCallAppInfo[] CallAppInfo;
	public TpCallLegInfoReport(int CallLegInfoType, java.lang.String CallLegStartTime, java.lang.String CallLegConnectedToResourceTime, java.lang.String CallLegConnectedToAddressTime, java.lang.String CallLegEndTime, org.csapi.TpAddress ConnectedAddress, org.csapi.cc.TpReleaseCause CallLegReleaseCause, org.csapi.cc.TpCallAppInfo[] CallAppInfo)
	{
		this.CallLegInfoType = CallLegInfoType;
		this.CallLegStartTime = CallLegStartTime;
		this.CallLegConnectedToResourceTime = CallLegConnectedToResourceTime;
		this.CallLegConnectedToAddressTime = CallLegConnectedToAddressTime;
		this.CallLegEndTime = CallLegEndTime;
		this.ConnectedAddress = ConnectedAddress;
		this.CallLegReleaseCause = CallLegReleaseCause;
		this.CallAppInfo = CallAppInfo;
	}
}
