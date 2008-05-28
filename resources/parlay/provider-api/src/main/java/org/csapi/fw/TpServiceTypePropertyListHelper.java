package org.csapi.fw;

/**
 *	Generated from IDL definition of alias "TpServiceTypePropertyList"
 *	@author JacORB IDL compiler 
 */

public final class TpServiceTypePropertyListHelper
{
	private static org.omg.CORBA.TypeCode _type = null;

	public static void insert (org.omg.CORBA.Any any, org.csapi.fw.TpServiceTypeProperty[] s)
	{
		any.type (type ());
		write (any.create_output_stream (), s);
	}

	public static org.csapi.fw.TpServiceTypeProperty[] extract (final org.omg.CORBA.Any any)
	{
		return read (any.create_input_stream ());
	}

	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_alias_tc(org.csapi.fw.TpServiceTypePropertyListHelper.id(), "TpServiceTypePropertyList",org.omg.CORBA.ORB.init().create_sequence_tc(0, org.csapi.fw.TpServiceTypePropertyHelper.type()));
		}
		return _type;
	}

	public static String id()
	{
		return "IDL:org/csapi/fw/TpServiceTypePropertyList:1.0";
	}
	public static org.csapi.fw.TpServiceTypeProperty[] read (final org.omg.CORBA.portable.InputStream _in)
	{
		org.csapi.fw.TpServiceTypeProperty[] _result;
		int _l_result24 = _in.read_long();
		_result = new org.csapi.fw.TpServiceTypeProperty[_l_result24];
		for (int i=0;i<_result.length;i++)
		{
			_result[i]=org.csapi.fw.TpServiceTypePropertyHelper.read(_in);
		}

		return _result;
	}

	public static void write (final org.omg.CORBA.portable.OutputStream _out, org.csapi.fw.TpServiceTypeProperty[] _s)
	{
		
		_out.write_long(_s.length);
		for (int i=0; i<_s.length;i++)
		{
			org.csapi.fw.TpServiceTypePropertyHelper.write(_out,_s[i]);
		}

	}
}
