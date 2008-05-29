package org.csapi;

/**
 *	Generated from IDL definition of alias "TpDouble"
 *	@author JacORB IDL compiler 
 */

public final class TpDoubleHelper
{
	private static org.omg.CORBA.TypeCode _type = null;

	public static void insert (org.omg.CORBA.Any any, double s)
	{
		any.type (type ());
		write (any.create_output_stream (), s);
	}

	public static double extract (final org.omg.CORBA.Any any)
	{
		return read (any.create_input_stream ());
	}

	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_alias_tc(org.csapi.TpDoubleHelper.id(), "TpDouble",org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.from_int(7)));
		}
		return _type;
	}

	public static String id()
	{
		return "IDL:org/csapi/TpDouble:1.0";
	}
	public static double read (final org.omg.CORBA.portable.InputStream _in)
	{
		double _result;
		_result=_in.read_double();
		return _result;
	}

	public static void write (final org.omg.CORBA.portable.OutputStream _out, double _s)
	{
		_out.write_double(_s);
	}
}
