package org.csapi.cc;

/**
 *	Generated from IDL definition of alias "TpCallAppInfoSet"
 *	@author JacORB IDL compiler 
 */

public final class TpCallAppInfoSetHelper
{
	private static org.omg.CORBA.TypeCode _type = null;

	public static void insert (org.omg.CORBA.Any any, org.csapi.cc.TpCallAppInfo[] s)
	{
		any.type (type ());
		write (any.create_output_stream (), s);
	}

	public static org.csapi.cc.TpCallAppInfo[] extract (final org.omg.CORBA.Any any)
	{
		return read (any.create_input_stream ());
	}

	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_alias_tc(org.csapi.cc.TpCallAppInfoSetHelper.id(), "TpCallAppInfoSet",org.omg.CORBA.ORB.init().create_sequence_tc(0, org.csapi.cc.TpCallAppInfoHelper.type()));
		}
		return _type;
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/TpCallAppInfoSet:1.0";
	}
	public static org.csapi.cc.TpCallAppInfo[] read (final org.omg.CORBA.portable.InputStream _in)
	{
		org.csapi.cc.TpCallAppInfo[] _result;
		int _l_result54 = _in.read_long();
		_result = new org.csapi.cc.TpCallAppInfo[_l_result54];
		for (int i=0;i<_result.length;i++)
		{
			_result[i]=org.csapi.cc.TpCallAppInfoHelper.read(_in);
		}

		return _result;
	}

	public static void write (final org.omg.CORBA.portable.OutputStream _out, org.csapi.cc.TpCallAppInfo[] _s)
	{
		
		_out.write_long(_s.length);
		for (int i=0; i<_s.length;i++)
		{
			org.csapi.cc.TpCallAppInfoHelper.write(_out,_s[i]);
		}

	}
}
