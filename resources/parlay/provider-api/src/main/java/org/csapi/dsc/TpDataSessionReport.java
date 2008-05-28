package org.csapi.dsc;

/**
 *	Generated from IDL definition of struct "TpDataSessionReport"
 *	@author JacORB IDL compiler 
 */

public final class TpDataSessionReport
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpDataSessionReport(){}
	public org.csapi.dsc.TpDataSessionMonitorMode MonitorMode;
	public java.lang.String DataSessionEventTime;
	public org.csapi.dsc.TpDataSessionReportType DataSessionReportType;
	public org.csapi.dsc.TpDataSessionAdditionalReportInfo AdditionalReportInfo;
	public TpDataSessionReport(org.csapi.dsc.TpDataSessionMonitorMode MonitorMode, java.lang.String DataSessionEventTime, org.csapi.dsc.TpDataSessionReportType DataSessionReportType, org.csapi.dsc.TpDataSessionAdditionalReportInfo AdditionalReportInfo)
	{
		this.MonitorMode = MonitorMode;
		this.DataSessionEventTime = DataSessionEventTime;
		this.DataSessionReportType = DataSessionReportType;
		this.AdditionalReportInfo = AdditionalReportInfo;
	}
}
