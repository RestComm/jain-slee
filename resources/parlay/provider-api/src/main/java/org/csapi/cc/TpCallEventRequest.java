package org.csapi.cc;

/**
 *	Generated from IDL definition of struct "TpCallEventRequest"
 *	@author JacORB IDL compiler 
 */

public final class TpCallEventRequest
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpCallEventRequest(){}
	public org.csapi.cc.TpCallEventType CallEventType;
	public org.csapi.cc.TpAdditionalCallEventCriteria AdditionalCallEventCriteria;
	public org.csapi.cc.TpCallMonitorMode CallMonitorMode;
	public TpCallEventRequest(org.csapi.cc.TpCallEventType CallEventType, org.csapi.cc.TpAdditionalCallEventCriteria AdditionalCallEventCriteria, org.csapi.cc.TpCallMonitorMode CallMonitorMode)
	{
		this.CallEventType = CallEventType;
		this.AdditionalCallEventCriteria = AdditionalCallEventCriteria;
		this.CallMonitorMode = CallMonitorMode;
	}
}
