package org.csapi.cc.gccs;

/**
 *	Generated from IDL definition of struct "TpCallEventInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpCallEventInfo
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpCallEventInfo(){}
	public org.csapi.TpAddress DestinationAddress;
	public org.csapi.TpAddress OriginatingAddress;
	public org.csapi.TpAddress OriginalDestinationAddress;
	public org.csapi.TpAddress RedirectingAddress;
	public org.csapi.cc.gccs.TpCallAppInfo[] CallAppInfo;
	public int CallEventName;
	public org.csapi.cc.gccs.TpCallNotificationType CallNotificationType;
	public org.csapi.cc.TpCallMonitorMode MonitorMode;
	public TpCallEventInfo(org.csapi.TpAddress DestinationAddress, org.csapi.TpAddress OriginatingAddress, org.csapi.TpAddress OriginalDestinationAddress, org.csapi.TpAddress RedirectingAddress, org.csapi.cc.gccs.TpCallAppInfo[] CallAppInfo, int CallEventName, org.csapi.cc.gccs.TpCallNotificationType CallNotificationType, org.csapi.cc.TpCallMonitorMode MonitorMode)
	{
		this.DestinationAddress = DestinationAddress;
		this.OriginatingAddress = OriginatingAddress;
		this.OriginalDestinationAddress = OriginalDestinationAddress;
		this.RedirectingAddress = RedirectingAddress;
		this.CallAppInfo = CallAppInfo;
		this.CallEventName = CallEventName;
		this.CallNotificationType = CallNotificationType;
		this.MonitorMode = MonitorMode;
	}
}
