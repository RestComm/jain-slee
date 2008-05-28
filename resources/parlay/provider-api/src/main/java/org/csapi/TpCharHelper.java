package org.csapi;

/**
 *	Generated from IDL definition of alias "TpChar"
 *	@author JacORB IDL compiler 
 */

public final class TpCharHelper
{
	private static org.omg.CORBA.TypeCode _type = null;

	public static void insert (org.omg.CORBA.Any any, char s)
	{
		any.type (type ());
		write (any.create_output_stream (), s);
	}

	public static char extract (final org.omg.CORBA.Any any)
	{
		return read (any.create_input_stream ());
	}

	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_alias_tc(org.csapi.TpCharHelper.id(), "TpChar",org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.from_int(9)));
		}
		return _type;
	}

	public static String id()
	{
		return "IDL:org/csapi/TpChar:1.0";
	}
	public static char read (final org.omg.CORBA.portable.InputStream _in)
	{
		char _result;
		_result=_in.read_char();
		return _result;
	}

	public static void write (final org.omg.CORBA.portable.OutputStream _out, char _s)
	{
		_out.write_char(_s);
	}
}
