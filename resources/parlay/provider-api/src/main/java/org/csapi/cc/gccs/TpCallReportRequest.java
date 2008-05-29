package org.csapi.cc.gccs;

/**
 *	Generated from IDL definition of struct "TpCallReportRequest"
 *	@author JacORB IDL compiler 
 */

public final class TpCallReportRequest
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpCallReportRequest(){}
	public org.csapi.cc.TpCallMonitorMode MonitorMode;
	public org.csapi.cc.gccs.TpCallReportType CallReportType;
	public org.csapi.cc.gccs.TpCallAdditionalReportCriteria AdditionalReportCriteria;
	public TpCallReportRequest(org.csapi.cc.TpCallMonitorMode MonitorMode, org.csapi.cc.gccs.TpCallReportType CallReportType, org.csapi.cc.gccs.TpCallAdditionalReportCriteria AdditionalReportCriteria)
	{
		this.MonitorMode = MonitorMode;
		this.CallReportType = CallReportType;
		this.AdditionalReportCriteria = AdditionalReportCriteria;
	}
}
