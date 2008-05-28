package org.csapi;

/**
 *	Generated from IDL definition of alias "TpBoolean"
 *	@author JacORB IDL compiler 
 */

public final class TpBooleanHelper
{
	private static org.omg.CORBA.TypeCode _type = null;

	public static void insert (org.omg.CORBA.Any any, boolean s)
	{
		any.type (type ());
		write (any.create_output_stream (), s);
	}

	public static boolean extract (final org.omg.CORBA.Any any)
	{
		return read (any.create_input_stream ());
	}

	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_alias_tc(org.csapi.TpBooleanHelper.id(), "TpBoolean",org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.from_int(8)));
		}
		return _type;
	}

	public static String id()
	{
		return "IDL:org/csapi/TpBoolean:1.0";
	}
	public static boolean read (final org.omg.CORBA.portable.InputStream _in)
	{
		boolean _result;
		_result=_in.read_boolean();
		return _result;
	}

	public static void write (final org.omg.CORBA.portable.OutputStream _out, boolean _s)
	{
		_out.write_boolean(_s);
	}
}
