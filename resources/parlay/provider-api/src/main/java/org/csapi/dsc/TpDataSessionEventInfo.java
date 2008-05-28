package org.csapi.dsc;

/**
 *	Generated from IDL definition of struct "TpDataSessionEventInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpDataSessionEventInfo
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpDataSessionEventInfo(){}
	public org.csapi.TpAddress DestinationAddress;
	public org.csapi.TpAddress OriginatingAddress;
	public int DataSessionEventName;
	public org.csapi.dsc.TpDataSessionMonitorMode MonitorMode;
	public org.csapi.TpDataSessionQosClass QoSClass;
	public TpDataSessionEventInfo(org.csapi.TpAddress DestinationAddress, org.csapi.TpAddress OriginatingAddress, int DataSessionEventName, org.csapi.dsc.TpDataSessionMonitorMode MonitorMode, org.csapi.TpDataSessionQosClass QoSClass)
	{
		this.DestinationAddress = DestinationAddress;
		this.OriginatingAddress = OriginatingAddress;
		this.DataSessionEventName = DataSessionEventName;
		this.MonitorMode = MonitorMode;
		this.QoSClass = QoSClass;
	}
}
