package org.csapi.cc.gccs;

/**
 *	Generated from IDL definition of struct "TpCallReport"
 *	@author JacORB IDL compiler 
 */

public final class TpCallReport
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpCallReport(){}
	public org.csapi.cc.TpCallMonitorMode MonitorMode;
	public java.lang.String CallEventTime;
	public org.csapi.cc.gccs.TpCallReportType CallReportType;
	public org.csapi.cc.gccs.TpCallAdditionalReportInfo AdditionalReportInfo;
	public TpCallReport(org.csapi.cc.TpCallMonitorMode MonitorMode, java.lang.String CallEventTime, org.csapi.cc.gccs.TpCallReportType CallReportType, org.csapi.cc.gccs.TpCallAdditionalReportInfo AdditionalReportInfo)
	{
		this.MonitorMode = MonitorMode;
		this.CallEventTime = CallEventTime;
		this.CallReportType = CallReportType;
		this.AdditionalReportInfo = AdditionalReportInfo;
	}
}
