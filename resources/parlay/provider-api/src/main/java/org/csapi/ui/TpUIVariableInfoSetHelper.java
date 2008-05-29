package org.csapi.ui;

/**
 *	Generated from IDL definition of alias "TpUIVariableInfoSet"
 *	@author JacORB IDL compiler 
 */

public final class TpUIVariableInfoSetHelper
{
	private static org.omg.CORBA.TypeCode _type = null;

	public static void insert (org.omg.CORBA.Any any, org.csapi.ui.TpUIVariableInfo[] s)
	{
		any.type (type ());
		write (any.create_output_stream (), s);
	}

	public static org.csapi.ui.TpUIVariableInfo[] extract (final org.omg.CORBA.Any any)
	{
		return read (any.create_input_stream ());
	}

	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_alias_tc(org.csapi.ui.TpUIVariableInfoSetHelper.id(), "TpUIVariableInfoSet",org.omg.CORBA.ORB.init().create_sequence_tc(0, org.csapi.ui.TpUIVariableInfoHelper.type()));
		}
		return _type;
	}

	public static String id()
	{
		return "IDL:org/csapi/ui/TpUIVariableInfoSet:1.0";
	}
	public static org.csapi.ui.TpUIVariableInfo[] read (final org.omg.CORBA.portable.InputStream _in)
	{
		org.csapi.ui.TpUIVariableInfo[] _result;
		int _l_result79 = _in.read_long();
		_result = new org.csapi.ui.TpUIVariableInfo[_l_result79];
		for (int i=0;i<_result.length;i++)
		{
			_result[i]=org.csapi.ui.TpUIVariableInfoHelper.read(_in);
		}

		return _result;
	}

	public static void write (final org.omg.CORBA.portable.OutputStream _out, org.csapi.ui.TpUIVariableInfo[] _s)
	{
		
		_out.write_long(_s.length);
		for (int i=0; i<_s.length;i++)
		{
			org.csapi.ui.TpUIVariableInfoHelper.write(_out,_s[i]);
		}

	}
}
