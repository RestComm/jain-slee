package org.csapi.pam;

/**
 *	Generated from IDL definition of alias "TpPAMTimeInterval"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMTimeIntervalHelper
{
	private static org.omg.CORBA.TypeCode _type = null;

	public static void insert (org.omg.CORBA.Any any, long s)
	{
		any.type (type ());
		write (any.create_output_stream (), s);
	}

	public static long extract (final org.omg.CORBA.Any any)
	{
		return read (any.create_input_stream ());
	}

	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_alias_tc(org.csapi.pam.TpPAMTimeIntervalHelper.id(), "TpPAMTimeInterval",org.csapi.TpInt64Helper.type());
		}
		return _type;
	}

	public static String id()
	{
		return "IDL:org/csapi/pam/TpPAMTimeInterval:1.0";
	}
	public static long read (final org.omg.CORBA.portable.InputStream _in)
	{
		long _result;
		_result=_in.read_longlong();
		return _result;
	}

	public static void write (final org.omg.CORBA.portable.OutputStream _out, long _s)
	{
		_out.write_longlong(_s);
	}
}
