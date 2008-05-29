package org.csapi.dsc;

/**
 *	Generated from IDL definition of alias "TpDataSessionEventCriteriaResultSet"
 *	@author JacORB IDL compiler 
 */

public final class TpDataSessionEventCriteriaResultSetHelper
{
	private static org.omg.CORBA.TypeCode _type = null;

	public static void insert (org.omg.CORBA.Any any, org.csapi.dsc.TpDataSessionEventCriteriaResult[] s)
	{
		any.type (type ());
		write (any.create_output_stream (), s);
	}

	public static org.csapi.dsc.TpDataSessionEventCriteriaResult[] extract (final org.omg.CORBA.Any any)
	{
		return read (any.create_input_stream ());
	}

	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_alias_tc(org.csapi.dsc.TpDataSessionEventCriteriaResultSetHelper.id(), "TpDataSessionEventCriteriaResultSet",org.omg.CORBA.ORB.init().create_sequence_tc(0, org.csapi.dsc.TpDataSessionEventCriteriaResultHelper.type()));
		}
		return _type;
	}

	public static String id()
	{
		return "IDL:org/csapi/dsc/TpDataSessionEventCriteriaResultSet:1.0";
	}
	public static org.csapi.dsc.TpDataSessionEventCriteriaResult[] read (final org.omg.CORBA.portable.InputStream _in)
	{
		org.csapi.dsc.TpDataSessionEventCriteriaResult[] _result;
		int _l_result11 = _in.read_long();
		_result = new org.csapi.dsc.TpDataSessionEventCriteriaResult[_l_result11];
		for (int i=0;i<_result.length;i++)
		{
			_result[i]=org.csapi.dsc.TpDataSessionEventCriteriaResultHelper.read(_in);
		}

		return _result;
	}

	public static void write (final org.omg.CORBA.portable.OutputStream _out, org.csapi.dsc.TpDataSessionEventCriteriaResult[] _s)
	{
		
		_out.write_long(_s.length);
		for (int i=0; i<_s.length;i++)
		{
			org.csapi.dsc.TpDataSessionEventCriteriaResultHelper.write(_out,_s[i]);
		}

	}
}
