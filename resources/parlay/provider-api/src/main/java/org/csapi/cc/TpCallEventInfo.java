package org.csapi.cc;

/**
 *	Generated from IDL definition of struct "TpCallEventInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpCallEventInfo
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpCallEventInfo(){}
	public org.csapi.cc.TpCallEventType CallEventType;
	public org.csapi.cc.TpCallAdditionalEventInfo AdditionalCallEventInfo;
	public org.csapi.cc.TpCallMonitorMode CallMonitorMode;
	public java.lang.String CallEventTime;
	public TpCallEventInfo(org.csapi.cc.TpCallEventType CallEventType, org.csapi.cc.TpCallAdditionalEventInfo AdditionalCallEventInfo, org.csapi.cc.TpCallMonitorMode CallMonitorMode, java.lang.String CallEventTime)
	{
		this.CallEventType = CallEventType;
		this.AdditionalCallEventInfo = AdditionalCallEventInfo;
		this.CallMonitorMode = CallMonitorMode;
		this.CallEventTime = CallEventTime;
	}
}
