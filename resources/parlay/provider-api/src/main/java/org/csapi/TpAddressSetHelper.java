package org.csapi;

/**
 *	Generated from IDL definition of alias "TpAddressSet"
 *	@author JacORB IDL compiler 
 */

public final class TpAddressSetHelper
{
	private static org.omg.CORBA.TypeCode _type = null;

	public static void insert (org.omg.CORBA.Any any, org.csapi.TpAddress[] s)
	{
		any.type (type ());
		write (any.create_output_stream (), s);
	}

	public static org.csapi.TpAddress[] extract (final org.omg.CORBA.Any any)
	{
		return read (any.create_input_stream ());
	}

	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_alias_tc(org.csapi.TpAddressSetHelper.id(), "TpAddressSet",org.omg.CORBA.ORB.init().create_sequence_tc(0, org.csapi.TpAddressHelper.type()));
		}
		return _type;
	}

	public static String id()
	{
		return "IDL:org/csapi/TpAddressSet:1.0";
	}
	public static org.csapi.TpAddress[] read (final org.omg.CORBA.portable.InputStream _in)
	{
		org.csapi.TpAddress[] _result;
		int _l_result59 = _in.read_long();
		_result = new org.csapi.TpAddress[_l_result59];
		for (int i=0;i<_result.length;i++)
		{
			_result[i]=org.csapi.TpAddressHelper.read(_in);
		}

		return _result;
	}

	public static void write (final org.omg.CORBA.portable.OutputStream _out, org.csapi.TpAddress[] _s)
	{
		
		_out.write_long(_s.length);
		for (int i=0; i<_s.length;i++)
		{
			org.csapi.TpAddressHelper.write(_out,_s[i]);
		}

	}
}
