package org.csapi.am;

/**
 *	Generated from IDL definition of alias "TpBalanceSet"
 *	@author JacORB IDL compiler 
 */

public final class TpBalanceSetHelper
{
	private static org.omg.CORBA.TypeCode _type = null;

	public static void insert (org.omg.CORBA.Any any, org.csapi.am.TpBalance[] s)
	{
		any.type (type ());
		write (any.create_output_stream (), s);
	}

	public static org.csapi.am.TpBalance[] extract (final org.omg.CORBA.Any any)
	{
		return read (any.create_input_stream ());
	}

	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_alias_tc(org.csapi.am.TpBalanceSetHelper.id(), "TpBalanceSet",org.omg.CORBA.ORB.init().create_sequence_tc(0, org.csapi.am.TpBalanceHelper.type()));
		}
		return _type;
	}

	public static String id()
	{
		return "IDL:org/csapi/am/TpBalanceSet:1.0";
	}
	public static org.csapi.am.TpBalance[] read (final org.omg.CORBA.portable.InputStream _in)
	{
		org.csapi.am.TpBalance[] _result;
		int _l_result0 = _in.read_long();
		_result = new org.csapi.am.TpBalance[_l_result0];
		for (int i=0;i<_result.length;i++)
		{
			_result[i]=org.csapi.am.TpBalanceHelper.read(_in);
		}

		return _result;
	}

	public static void write (final org.omg.CORBA.portable.OutputStream _out, org.csapi.am.TpBalance[] _s)
	{
		
		_out.write_long(_s.length);
		for (int i=0; i<_s.length;i++)
		{
			org.csapi.am.TpBalanceHelper.write(_out,_s[i]);
		}

	}
}
