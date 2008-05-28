package org.csapi.dsc;

/**
 *	Generated from IDL definition of struct "TpDataSessionEventCriteria"
 *	@author JacORB IDL compiler 
 */

public final class TpDataSessionEventCriteria
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpDataSessionEventCriteria(){}
	public org.csapi.TpAddressRange DestinationAddress;
	public org.csapi.TpAddressRange OriginationAddress;
	public int DataSessionEventName;
	public org.csapi.dsc.TpDataSessionMonitorMode MonitorMode;
	public TpDataSessionEventCriteria(org.csapi.TpAddressRange DestinationAddress, org.csapi.TpAddressRange OriginationAddress, int DataSessionEventName, org.csapi.dsc.TpDataSessionMonitorMode MonitorMode)
	{
		this.DestinationAddress = DestinationAddress;
		this.OriginationAddress = OriginationAddress;
		this.DataSessionEventName = DataSessionEventName;
		this.MonitorMode = MonitorMode;
	}
}
