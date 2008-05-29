package org.csapi.dsc;

/**
 *	Generated from IDL definition of alias "TpDataSessionReportRequestSet"
 *	@author JacORB IDL compiler 
 */

public final class TpDataSessionReportRequestSetHelper
{
	private static org.omg.CORBA.TypeCode _type = null;

	public static void insert (org.omg.CORBA.Any any, org.csapi.dsc.TpDataSessionReportRequest[] s)
	{
		any.type (type ());
		write (any.create_output_stream (), s);
	}

	public static org.csapi.dsc.TpDataSessionReportRequest[] extract (final org.omg.CORBA.Any any)
	{
		return read (any.create_input_stream ());
	}

	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_alias_tc(org.csapi.dsc.TpDataSessionReportRequestSetHelper.id(), "TpDataSessionReportRequestSet",org.omg.CORBA.ORB.init().create_sequence_tc(0, org.csapi.dsc.TpDataSessionReportRequestHelper.type()));
		}
		return _type;
	}

	public static String id()
	{
		return "IDL:org/csapi/dsc/TpDataSessionReportRequestSet:1.0";
	}
	public static org.csapi.dsc.TpDataSessionReportRequest[] read (final org.omg.CORBA.portable.InputStream _in)
	{
		org.csapi.dsc.TpDataSessionReportRequest[] _result;
		int _l_result10 = _in.read_long();
		_result = new org.csapi.dsc.TpDataSessionReportRequest[_l_result10];
		for (int i=0;i<_result.length;i++)
		{
			_result[i]=org.csapi.dsc.TpDataSessionReportRequestHelper.read(_in);
		}

		return _result;
	}

	public static void write (final org.omg.CORBA.portable.OutputStream _out, org.csapi.dsc.TpDataSessionReportRequest[] _s)
	{
		
		_out.write_long(_s.length);
		for (int i=0; i<_s.length;i++)
		{
			org.csapi.dsc.TpDataSessionReportRequestHelper.write(_out,_s[i]);
		}

	}
}
