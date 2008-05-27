package org.csapi.cs;

/**
 *	Generated from IDL definition of alias "TpCorrelationType"
 *	@author JacORB IDL compiler 
 */

public final class TpCorrelationTypeHelper
{
	private static org.omg.CORBA.TypeCode _type = null;

	public static void insert (org.omg.CORBA.Any any, int s)
	{
		any.type (type ());
		write (any.create_output_stream (), s);
	}

	public static int extract (final org.omg.CORBA.Any any)
	{
		return read (any.create_input_stream ());
	}

	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_alias_tc(org.csapi.cs.TpCorrelationTypeHelper.id(), "TpCorrelationType",org.csapi.TpInt32Helper.type());
		}
		return _type;
	}

	public static String id()
	{
		return "IDL:org/csapi/cs/TpCorrelationType:1.0";
	}
	public static int read (final org.omg.CORBA.portable.InputStream _in)
	{
		int _result;
		_result=_in.read_long();
		return _result;
	}

	public static void write (final org.omg.CORBA.portable.OutputStream _out, int _s)
	{
		_out.write_long(_s);
	}
}
