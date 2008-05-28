package org.csapi.fw;

/**
 *	Generated from IDL definition of alias "TpEndAccessProperties"
 *	@author JacORB IDL compiler 
 */

public final class TpEndAccessPropertiesHelper
{
	private static org.omg.CORBA.TypeCode _type = null;

	public static void insert (org.omg.CORBA.Any any, org.csapi.fw.TpProperty[] s)
	{
		any.type (type ());
		write (any.create_output_stream (), s);
	}

	public static org.csapi.fw.TpProperty[] extract (final org.omg.CORBA.Any any)
	{
		return read (any.create_input_stream ());
	}

	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_alias_tc(org.csapi.fw.TpEndAccessPropertiesHelper.id(), "TpEndAccessProperties",org.csapi.fw.TpPropertyListHelper.type());
		}
		return _type;
	}

	public static String id()
	{
		return "IDL:org/csapi/fw/TpEndAccessProperties:1.0";
	}
	public static org.csapi.fw.TpProperty[] read (final org.omg.CORBA.portable.InputStream _in)
	{
		org.csapi.fw.TpProperty[] _result;
		_result = org.csapi.fw.TpPropertyListHelper.read(_in);
		return _result;
	}

	public static void write (final org.omg.CORBA.portable.OutputStream _out, org.csapi.fw.TpProperty[] _s)
	{
		org.csapi.fw.TpPropertyListHelper.write(_out,_s);
	}
}
