package org.csapi.ui;

/**
 *	Generated from IDL definition of alias "TpUIEventCriteriaResultSet"
 *	@author JacORB IDL compiler 
 */

public final class TpUIEventCriteriaResultSetHelper
{
	private static org.omg.CORBA.TypeCode _type = null;

	public static void insert (org.omg.CORBA.Any any, org.csapi.ui.TpUIEventCriteriaResult[] s)
	{
		any.type (type ());
		write (any.create_output_stream (), s);
	}

	public static org.csapi.ui.TpUIEventCriteriaResult[] extract (final org.omg.CORBA.Any any)
	{
		return read (any.create_input_stream ());
	}

	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_alias_tc(org.csapi.ui.TpUIEventCriteriaResultSetHelper.id(), "TpUIEventCriteriaResultSet",org.omg.CORBA.ORB.init().create_sequence_tc(0, org.csapi.ui.TpUIEventCriteriaResultHelper.type()));
		}
		return _type;
	}

	public static String id()
	{
		return "IDL:org/csapi/ui/TpUIEventCriteriaResultSet:1.0";
	}
	public static org.csapi.ui.TpUIEventCriteriaResult[] read (final org.omg.CORBA.portable.InputStream _in)
	{
		org.csapi.ui.TpUIEventCriteriaResult[] _result;
		int _l_result80 = _in.read_long();
		_result = new org.csapi.ui.TpUIEventCriteriaResult[_l_result80];
		for (int i=0;i<_result.length;i++)
		{
			_result[i]=org.csapi.ui.TpUIEventCriteriaResultHelper.read(_in);
		}

		return _result;
	}

	public static void write (final org.omg.CORBA.portable.OutputStream _out, org.csapi.ui.TpUIEventCriteriaResult[] _s)
	{
		
		_out.write_long(_s.length);
		for (int i=0; i<_s.length;i++)
		{
			org.csapi.ui.TpUIEventCriteriaResultHelper.write(_out,_s[i]);
		}

	}
}
