package org.csapi.dsc;

/**
 *	Generated from IDL definition of struct "TpDataSessionReportRequest"
 *	@author JacORB IDL compiler 
 */

public final class TpDataSessionReportRequest
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpDataSessionReportRequest(){}
	public org.csapi.dsc.TpDataSessionMonitorMode MonitorMode;
	public org.csapi.dsc.TpDataSessionReportType DataSessionReportType;
	public TpDataSessionReportRequest(org.csapi.dsc.TpDataSessionMonitorMode MonitorMode, org.csapi.dsc.TpDataSessionReportType DataSessionReportType)
	{
		this.MonitorMode = MonitorMode;
		this.DataSessionReportType = DataSessionReportType;
	}
}
