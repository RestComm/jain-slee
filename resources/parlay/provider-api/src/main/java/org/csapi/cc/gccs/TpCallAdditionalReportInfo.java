package org.csapi.cc.gccs;

/**
 *	Generated from IDL definition of union "TpCallAdditionalReportInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpCallAdditionalReportInfo
	implements org.omg.CORBA.portable.IDLEntity
{
	private org.csapi.cc.gccs.TpCallReportType discriminator;
	private org.csapi.cc.gccs.TpCallReleaseCause Busy;
	private org.csapi.cc.gccs.TpCallReleaseCause CallDisconnect;
	private org.csapi.TpAddress ForwardAddress;
	private org.csapi.cc.TpCallServiceCode ServiceCode;
	private org.csapi.cc.gccs.TpCallReleaseCause RoutingFailure;
	private java.lang.String QueueStatus;
	private org.csapi.cc.gccs.TpCallReleaseCause NotReachable;
	private short Dummy;

	public TpCallAdditionalReportInfo ()
	{
	}

	public org.csapi.cc.gccs.TpCallReportType discriminator ()
	{
		return discriminator;
	}

	public org.csapi.cc.gccs.TpCallReleaseCause Busy ()
	{
		if (discriminator != org.csapi.cc.gccs.TpCallReportType.P_CALL_REPORT_BUSY)
			throw new org.omg.CORBA.BAD_OPERATION();
		return Busy;
	}

	public void Busy (org.csapi.cc.gccs.TpCallReleaseCause _x)
	{
		discriminator = org.csapi.cc.gccs.TpCallReportType.P_CALL_REPORT_BUSY;
		Busy = _x;
	}

	public org.csapi.cc.gccs.TpCallReleaseCause CallDisconnect ()
	{
		if (discriminator != org.csapi.cc.gccs.TpCallReportType.P_CALL_REPORT_DISCONNECT)
			throw new org.omg.CORBA.BAD_OPERATION();
		return CallDisconnect;
	}

	public void CallDisconnect (org.csapi.cc.gccs.TpCallReleaseCause _x)
	{
		discriminator = org.csapi.cc.gccs.TpCallReportType.P_CALL_REPORT_DISCONNECT;
		CallDisconnect = _x;
	}

	public org.csapi.TpAddress ForwardAddress ()
	{
		if (discriminator != org.csapi.cc.gccs.TpCallReportType.P_CALL_REPORT_REDIRECTED)
			throw new org.omg.CORBA.BAD_OPERATION();
		return ForwardAddress;
	}

	public void ForwardAddress (org.csapi.TpAddress _x)
	{
		discriminator = org.csapi.cc.gccs.TpCallReportType.P_CALL_REPORT_REDIRECTED;
		ForwardAddress = _x;
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

	public org.csapi.cc.gccs.TpCallReleaseCause RoutingFailure ()
	{
		if (discriminator != org.csapi.cc.gccs.TpCallReportType.P_CALL_REPORT_ROUTING_FAILURE)
			throw new org.omg.CORBA.BAD_OPERATION();
		return RoutingFailure;
	}

	public void RoutingFailure (org.csapi.cc.gccs.TpCallReleaseCause _x)
	{
		discriminator = org.csapi.cc.gccs.TpCallReportType.P_CALL_REPORT_ROUTING_FAILURE;
		RoutingFailure = _x;
	}

	public java.lang.String QueueStatus ()
	{
		if (discriminator != org.csapi.cc.gccs.TpCallReportType.P_CALL_REPORT_QUEUED)
			throw new org.omg.CORBA.BAD_OPERATION();
		return QueueStatus;
	}

	public void QueueStatus (java.lang.String _x)
	{
		discriminator = org.csapi.cc.gccs.TpCallReportType.P_CALL_REPORT_QUEUED;
		QueueStatus = _x;
	}

	public org.csapi.cc.gccs.TpCallReleaseCause NotReachable ()
	{
		if (discriminator != org.csapi.cc.gccs.TpCallReportType.P_CALL_REPORT_NOT_REACHABLE)
			throw new org.omg.CORBA.BAD_OPERATION();
		return NotReachable;
	}

	public void NotReachable (org.csapi.cc.gccs.TpCallReleaseCause _x)
	{
		discriminator = org.csapi.cc.gccs.TpCallReportType.P_CALL_REPORT_NOT_REACHABLE;
		NotReachable = _x;
	}

	public short Dummy ()
	{
		if (discriminator != org.csapi.cc.gccs.TpCallReportType.P_CALL_REPORT_UNDEFINED && discriminator != org.csapi.cc.gccs.TpCallReportType.P_CALL_REPORT_PROGRESS && discriminator != org.csapi.cc.gccs.TpCallReportType.P_CALL_REPORT_ALERTING && discriminator != org.csapi.cc.gccs.TpCallReportType.P_CALL_REPORT_ANSWER && discriminator != org.csapi.cc.gccs.TpCallReportType.P_CALL_REPORT_NO_ANSWER)
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
		if (_discriminator != org.csapi.cc.gccs.TpCallReportType.P_CALL_REPORT_UNDEFINED && discriminator != org.csapi.cc.gccs.TpCallReportType.P_CALL_REPORT_PROGRESS && discriminator != org.csapi.cc.gccs.TpCallReportType.P_CALL_REPORT_ALERTING && discriminator != org.csapi.cc.gccs.TpCallReportType.P_CALL_REPORT_ANSWER && discriminator != org.csapi.cc.gccs.TpCallReportType.P_CALL_REPORT_NO_ANSWER)
			throw new org.omg.CORBA.BAD_OPERATION();
		discriminator = _discriminator;
		Dummy = _x;
	}

}
