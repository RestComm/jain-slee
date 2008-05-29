package org.csapi.dsc;

/**
 *	Generated from IDL definition of union "TpDataSessionAdditionalReportInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpDataSessionAdditionalReportInfo
	implements org.omg.CORBA.portable.IDLEntity
{
	private org.csapi.dsc.TpDataSessionReportType discriminator;
	private org.csapi.dsc.TpDataSessionReleaseCause DataSessionDisconnect;
	private short Dummy;

	public TpDataSessionAdditionalReportInfo ()
	{
	}

	public org.csapi.dsc.TpDataSessionReportType discriminator ()
	{
		return discriminator;
	}

	public org.csapi.dsc.TpDataSessionReleaseCause DataSessionDisconnect ()
	{
		if (discriminator != org.csapi.dsc.TpDataSessionReportType.P_DATA_SESSION_REPORT_DISCONNECT)
			throw new org.omg.CORBA.BAD_OPERATION();
		return DataSessionDisconnect;
	}

	public void DataSessionDisconnect (org.csapi.dsc.TpDataSessionReleaseCause _x)
	{
		discriminator = org.csapi.dsc.TpDataSessionReportType.P_DATA_SESSION_REPORT_DISCONNECT;
		DataSessionDisconnect = _x;
	}

	public short Dummy ()
	{
		if (discriminator != org.csapi.dsc.TpDataSessionReportType.P_DATA_SESSION_REPORT_UNDEFINED && discriminator != org.csapi.dsc.TpDataSessionReportType.P_DATA_SESSION_REPORT_CONNECTED)
			throw new org.omg.CORBA.BAD_OPERATION();
		return Dummy;
	}

	public void Dummy (short _x)
	{
		discriminator = org.csapi.dsc.TpDataSessionReportType.P_DATA_SESSION_REPORT_UNDEFINED;
		Dummy = _x;
	}

	public void Dummy (org.csapi.dsc.TpDataSessionReportType _discriminator, short _x)
	{
		if (_discriminator != org.csapi.dsc.TpDataSessionReportType.P_DATA_SESSION_REPORT_UNDEFINED && discriminator != org.csapi.dsc.TpDataSessionReportType.P_DATA_SESSION_REPORT_CONNECTED)
			throw new org.omg.CORBA.BAD_OPERATION();
		discriminator = _discriminator;
		Dummy = _x;
	}

}
