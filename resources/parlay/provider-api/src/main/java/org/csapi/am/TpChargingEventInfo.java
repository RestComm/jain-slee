package org.csapi.am;

/**
 *	Generated from IDL definition of struct "TpChargingEventInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpChargingEventInfo
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpChargingEventInfo(){}
	public org.csapi.am.TpChargingEventName ChargingEventName;
	public org.csapi.am.TpBalanceInfo CurrentBalanceInfo;
	public java.lang.String ChargingEventTime;
	public TpChargingEventInfo(org.csapi.am.TpChargingEventName ChargingEventName, org.csapi.am.TpBalanceInfo CurrentBalanceInfo, java.lang.String ChargingEventTime)
	{
		this.ChargingEventName = ChargingEventName;
		this.CurrentBalanceInfo = CurrentBalanceInfo;
		this.ChargingEventTime = ChargingEventTime;
	}
}
