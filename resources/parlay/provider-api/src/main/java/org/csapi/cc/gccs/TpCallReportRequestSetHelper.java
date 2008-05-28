package org.csapi.cc.gccs;

/**
 *	Generated from IDL definition of alias "TpCallReportRequestSet"
 *	@author JacORB IDL compiler 
 */

public final class TpCallReportRequestSetHelper
{
	private static org.omg.CORBA.TypeCode _type = null;

	public static void insert (org.omg.CORBA.Any any, org.csapi.cc.gccs.TpCallReportRequest[] s)
	{
		any.type (type ());
		write (any.create_output_stream (), s);
	}

	public static org.csapi.cc.gccs.TpCallReportRequest[] extract (final org.omg.CORBA.Any any)
	{
		return read (any.create_input_stream ());
	}

	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_alias_tc(org.csapi.cc.gccs.TpCallReportRequestSetHelper.id(), "TpCallReportRequestSet",org.omg.CORBA.ORB.init().create_sequence_tc(0, org.csapi.cc.gccs.TpCallReportRequestHelper.type()));
		}
		return _type;
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/gccs/TpCallReportRequestSet:1.0";
	}
	public static org.csapi.cc.gccs.TpCallReportRequest[] read (final org.omg.CORBA.portable.InputStream _in)
	{
		org.csapi.cc.gccs.TpCallReportRequest[] _result;
		int _l_result34 = _in.read_long();
		_result = new org.csapi.cc.gccs.TpCallReportRequest[_l_result34];
		for (int i=0;i<_result.length;i++)
		{
			_result[i]=org.csapi.cc.gccs.TpCallReportRequestHelper.read(_in);
		}

		return _result;
	}

	public static void write (final org.omg.CORBA.portable.OutputStream _out, org.csapi.cc.gccs.TpCallReportRequest[] _s)
	{
		
		_out.write_long(_s.length);
		for (int i=0; i<_s.length;i++)
		{
			org.csapi.cc.gccs.TpCallReportRequestHelper.write(_out,_s[i]);
		}

	}
}
