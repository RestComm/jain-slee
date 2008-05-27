package org.csapi.fw;

/**
 *	Generated from IDL definition of alias "TpFaultStatsErrorList"
 *	@author JacORB IDL compiler 
 */

public final class TpFaultStatsErrorListHelper
{
	private static org.omg.CORBA.TypeCode _type = null;

	public static void insert (org.omg.CORBA.Any any, org.csapi.fw.TpFaultStatisticsError[] s)
	{
		any.type (type ());
		write (any.create_output_stream (), s);
	}

	public static org.csapi.fw.TpFaultStatisticsError[] extract (final org.omg.CORBA.Any any)
	{
		return read (any.create_input_stream ());
	}

	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_alias_tc(org.csapi.fw.TpFaultStatsErrorListHelper.id(), "TpFaultStatsErrorList",org.omg.CORBA.ORB.init().create_sequence_tc(0, org.csapi.fw.TpFaultStatisticsErrorHelper.type()));
		}
		return _type;
	}

	public static String id()
	{
		return "IDL:org/csapi/fw/TpFaultStatsErrorList:1.0";
	}
	public static org.csapi.fw.TpFaultStatisticsError[] read (final org.omg.CORBA.portable.InputStream _in)
	{
		org.csapi.fw.TpFaultStatisticsError[] _result;
		int _l_result31 = _in.read_long();
		_result = new org.csapi.fw.TpFaultStatisticsError[_l_result31];
		for (int i=0;i<_result.length;i++)
		{
			_result[i]=org.csapi.fw.TpFaultStatisticsErrorHelper.read(_in);
		}

		return _result;
	}

	public static void write (final org.omg.CORBA.portable.OutputStream _out, org.csapi.fw.TpFaultStatisticsError[] _s)
	{
		
		_out.write_long(_s.length);
		for (int i=0; i<_s.length;i++)
		{
			org.csapi.fw.TpFaultStatisticsErrorHelper.write(_out,_s[i]);
		}

	}
}
