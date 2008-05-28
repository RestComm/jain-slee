package org.csapi.cc.gccs;

/**
 *	Generated from IDL definition of struct "TpCallEventCriteria"
 *	@author JacORB IDL compiler 
 */

public final class TpCallEventCriteria
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpCallEventCriteria(){}
	public org.csapi.TpAddressRange DestinationAddress;
	public org.csapi.TpAddressRange OriginatingAddress;
	public int CallEventName;
	public org.csapi.cc.gccs.TpCallNotificationType CallNotificationType;
	public org.csapi.cc.TpCallMonitorMode MonitorMode;
	public TpCallEventCriteria(org.csapi.TpAddressRange DestinationAddress, org.csapi.TpAddressRange OriginatingAddress, int CallEventName, org.csapi.cc.gccs.TpCallNotificationType CallNotificationType, org.csapi.cc.TpCallMonitorMode MonitorMode)
	{
		this.DestinationAddress = DestinationAddress;
		this.OriginatingAddress = OriginatingAddress;
		this.CallEventName = CallEventName;
		this.CallNotificationType = CallNotificationType;
		this.MonitorMode = MonitorMode;
	}
}
