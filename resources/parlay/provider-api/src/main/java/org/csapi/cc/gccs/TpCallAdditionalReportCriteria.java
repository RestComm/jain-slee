package org.csapi.cc.gccs;

/**
 *	Generated from IDL definition of union "TpCallAdditionalReportCriteria"
 *	@author JacORB IDL compiler 
 */

public final class TpCallAdditionalReportCriteria
	implements org.omg.CORBA.portable.IDLEntity
{
	private org.csapi.cc.gccs.TpCallReportType discriminator;
	private int NoAnswerDuration;
	private org.csapi.cc.TpCallServiceCode ServiceCode;
	private short Dummy;

	public TpCallAdditionalReportCriteria ()
	{
	}

	public org.csapi.cc.gccs.TpCallReportType discriminator ()
	{
		return discriminator;
	}

	public int NoAnswerDuration ()
	{
		if (discriminator != org.csapi.cc.gccs.TpCallReportType.P_CALL_REPORT_NO_ANSWER)
			throw new org.omg.CORBA.BAD_OPERATION();
		return NoAnswerDuration;
	}

	public void NoAnswerDuration (int _x)
	{
		discriminator = org.csapi.cc.gccs.TpCallReportType.P_CALL_REPORT_NO_ANSWER;
		NoAnswerDuration = _x;
	}

	public org.csapi.cc.TpCallServiceCode ServiceCode ()
	{
		if (discriminator != org.csapi.cc.gccs.TpCallReportType.P_CALL_REPORT_SERVICE_CODE)
			throw new org.omg.CORBA.BAD_OPERATION();
		return ServiceCode;
	}

	public void ServiceCode (org.csapi.cc.TpCallServiceCode _x)
	{
		discriminator = org.csapi.cc.gccs.TpCallReportType.P_CALL_REPORT_SERVICE_CODE;
		ServiceCode = _x;
	}

	public short Dummy ()
	{
		if (discriminator != org.csapi.cc.gccs.TpCallReportType.P_CALL_REPORT_UNDEFINED && discriminator != org.csapi.cc.gccs.TpCallReportType.P_CALL_REPORT_PROGRESS && discriminator != org.csapi.cc.gccs.TpCallReportType.P_CALL_REPORT_ALERTING && discriminator != org.csapi.cc.gccs.TpCallReportType.P_CALL_REPORT_ANSWER && discriminator != org.csapi.cc.gccs.TpCallReportType.P_CALL_REPORT_BUSY && discriminator != org.csapi.cc.gccs.TpCallReportType.P_CALL_REPORT_DISCONNECT && discriminator != org.csapi.cc.gccs.TpCallReportType.P_CALL_REPORT_REDIRECTED && discriminator != org.csapi.cc.gccs.TpCallReportType.P_CALL_REPORT_ROUTING_FAILURE && discriminator != org.csapi.cc.gccs.TpCallReportType.P_CALL_REPORT_QUEUED && discriminator != org.csapi.cc.gccs.TpCallReportType.P_CALL_REPORT_NOT_REACHABLE)
			throw new org.omg.CORBA.BAD_OPERATION();
		return Dummy;
	}

	public void Dummy (short _x)
	{
		discriminator = org.csapi.cc.gccs.TpCallReportType.P_CALL_REPORT_UNDEFINED;
		Dummy = _x;
	}

	public void Dummy (org.csapi.cc.gccs.TpCallReportType _discriminator, short _x)
	{
		if (_discriminator != org.csapi.cc.gccs.TpCallReportType.P_CALL_REPORT_UNDEFINED && discriminator != org.csapi.cc.gccs.TpCallReportType.P_CALL_REPORT_PROGRESS && discriminator != org.csapi.cc.gccs.TpCallReportType.P_CALL_REPORT_ALERTING && discriminator != org.csapi.cc.gccs.TpCallReportType.P_CALL_REPORT_ANSWER && discriminator != org.csapi.cc.gccs.TpCallReportType.P_CALL_REPORT_BUSY && discriminator != org.csapi.cc.gccs.TpCallReportType.P_CALL_REPORT_DISCONNECT && discriminator != org.csapi.cc.gccs.TpCallReportType.P_CALL_REPORT_REDIRECTED && discriminator != org.csapi.cc.gccs.TpCallReportType.P_CALL_REPORT_ROUTING_FAILURE && discriminator != org.csapi.cc.gccs.TpCallReportType.P_CALL_REPORT_QUEUED && discriminator != org.csapi.cc.gccs.TpCallReportType.P_CALL_REPORT_NOT_REACHABLE)
			throw new org.omg.CORBA.BAD_OPERATION();
		discriminator = _discriminator;
		Dummy = _x;
	}

}
