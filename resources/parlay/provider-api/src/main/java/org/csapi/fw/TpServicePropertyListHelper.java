package org.csapi.fw;

/**
 *	Generated from IDL definition of alias "TpServicePropertyList"
 *	@author JacORB IDL compiler 
 */

public final class TpServicePropertyListHelper
{
	private static org.omg.CORBA.TypeCode _type = null;

	public static void insert (org.omg.CORBA.Any any, org.csapi.fw.TpServiceProperty[] s)
	{
		any.type (type ());
		write (any.create_output_stream (), s);
	}

	public static org.csapi.fw.TpServiceProperty[] extract (final org.omg.CORBA.Any any)
	{
		return read (any.create_input_stream ());
	}

	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_alias_tc(org.csapi.fw.TpServicePropertyListHelper.id(), "TpServicePropertyList",org.omg.CORBA.ORB.init().create_sequence_tc(0, org.csapi.fw.TpServicePropertyHelper.type()));
		}
		return _type;
	}

	public static String id()
	{
		return "IDL:org/csapi/fw/TpServicePropertyList:1.0";
	}
	public static org.csapi.fw.TpServiceProperty[] read (final org.omg.CORBA.portable.InputStream _in)
	{
		org.csapi.fw.TpServiceProperty[] _result;
		int _l_result21 = _in.read_long();
		_result = new org.csapi.fw.TpServiceProperty[_l_result21];
		for (int i=0;i<_result.length;i++)
		{
			_result[i]=org.csapi.fw.TpServicePropertyHelper.read(_in);
		}

		return _result;
	}

	public static void write (final org.omg.CORBA.portable.OutputStream _out, org.csapi.fw.TpServiceProperty[] _s)
	{
		
		_out.write_long(_s.length);
		for (int i=0; i<_s.length;i++)
		{
			org.csapi.fw.TpServicePropertyHelper.write(_out,_s[i]);
		}

	}
}
