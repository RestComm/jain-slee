package org.csapi;

/**
 *	Generated from IDL definition of alias "TpFloat"
 *	@author JacORB IDL compiler 
 */

public final class TpFloatHelper
{
	private static org.omg.CORBA.TypeCode _type = null;

	public static void insert (org.omg.CORBA.Any any, float s)
	{
		any.type (type ());
		write (any.create_output_stream (), s);
	}

	public static float extract (final org.omg.CORBA.Any any)
	{
		return read (any.create_input_stream ());
	}

	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_alias_tc(org.csapi.TpFloatHelper.id(), "TpFloat",org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.from_int(6)));
		}
		return _type;
	}

	public static String id()
	{
		return "IDL:org/csapi/TpFloat:1.0";
	}
	public static float read (final org.omg.CORBA.portable.InputStream _in)
	{
		float _result;
		_result=_in.read_float();
		return _result;
	}

	public static void write (final org.omg.CORBA.portable.OutputStream _out, float _s)
	{
		_out.write_float(_s);
	}
}
