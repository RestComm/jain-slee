package org.csapi.cc;

/**
 *	Generated from IDL definition of alias "TpCallEventRequestSet"
 *	@author JacORB IDL compiler 
 */

public final class TpCallEventRequestSetHelper
{
	private static org.omg.CORBA.TypeCode _type = null;

	public static void insert (org.omg.CORBA.Any any, org.csapi.cc.TpCallEventRequest[] s)
	{
		any.type (type ());
		write (any.create_output_stream (), s);
	}

	public static org.csapi.cc.TpCallEventRequest[] extract (final org.omg.CORBA.Any any)
	{
		return read (any.create_input_stream ());
	}

	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_alias_tc(org.csapi.cc.TpCallEventRequestSetHelper.id(), "TpCallEventRequestSet",org.omg.CORBA.ORB.init().create_sequence_tc(0, org.csapi.cc.TpCallEventRequestHelper.type()));
		}
		return _type;
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/TpCallEventRequestSet:1.0";
	}
	public static org.csapi.cc.TpCallEventRequest[] read (final org.omg.CORBA.portable.InputStream _in)
	{
		org.csapi.cc.TpCallEventRequest[] _result;
		int _l_result51 = _in.read_long();
		_result = new org.csapi.cc.TpCallEventRequest[_l_result51];
		for (int i=0;i<_result.length;i++)
		{
			_result[i]=org.csapi.cc.TpCallEventRequestHelper.read(_in);
		}

		return _result;
	}

	public static void write (final org.omg.CORBA.portable.OutputStream _out, org.csapi.cc.TpCallEventRequest[] _s)
	{
		
		_out.write_long(_s.length);
		for (int i=0; i<_s.length;i++)
		{
			org.csapi.cc.TpCallEventRequestHelper.write(_out,_s[i]);
		}

	}
}
