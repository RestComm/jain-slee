package org.csapi.pam;


/**
 *	Generated from IDL definition of struct "TpPAMWCEventData"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMWCEventDataHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.pam.TpPAMWCEventDataHelper.id(),"TpPAMWCEventData",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("Events", org.csapi.pam.TpPAMEventNameListHelper.type(), null),new org.omg.CORBA.StructMember("IdentityName", org.csapi.pam.TpPAMFQNameListHelper.type(), null),new org.omg.CORBA.StructMember("IdentityType", org.csapi.TpStringListHelper.type(), null),new org.omg.CORBA.StructMember("ReportingPeriod", org.csapi.pam.TpPAMTimeIntervalHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.pam.TpPAMWCEventData s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.pam.TpPAMWCEventData extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/pam/TpPAMWCEventData:1.0";
	}
	public static org.csapi.pam.TpPAMWCEventData read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.pam.TpPAMWCEventData result = new org.csapi.pam.TpPAMWCEventData();
		result.Events = org.csapi.pam.TpPAMEventNameListHelper.read(in);
		result.IdentityName = org.csapi.pam.TpPAMFQNameListHelper.read(in);
		result.IdentityType = org.csapi.TpStringListHelper.read(in);
		result.ReportingPeriod=in.read_longlong();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.pam.TpPAMWCEventData s)
	{
		org.csapi.pam.TpPAMEventNameListHelper.write(out,s.Events);
		org.csapi.pam.TpPAMFQNameListHelper.write(out,s.IdentityName);
		org.csapi.TpStringListHelper.write(out,s.IdentityType);
		out.write_longlong(s.ReportingPeriod);
	}
}
