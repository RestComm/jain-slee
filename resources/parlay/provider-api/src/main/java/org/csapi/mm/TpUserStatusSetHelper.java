package org.csapi.mm;

/**
 *	Generated from IDL definition of alias "TpUserStatusSet"
 *	@author JacORB IDL compiler 
 */

public final class TpUserStatusSetHelper
{
	private static org.omg.CORBA.TypeCode _type = null;

	public static void insert (org.omg.CORBA.Any any, org.csapi.mm.TpUserStatus[] s)
	{
		any.type (type ());
		write (any.create_output_stream (), s);
	}

	public static org.csapi.mm.TpUserStatus[] extract (final org.omg.CORBA.Any any)
	{
		return read (any.create_input_stream ());
	}

	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_alias_tc(org.csapi.mm.TpUserStatusSetHelper.id(), "TpUserStatusSet",org.omg.CORBA.ORB.init().create_sequence_tc(0, org.csapi.mm.TpUserStatusHelper.type()));
		}
		return _type;
	}

	public static String id()
	{
		return "IDL:org/csapi/mm/TpUserStatusSet:1.0";
	}
	public static org.csapi.mm.TpUserStatus[] read (final org.omg.CORBA.portable.InputStream _in)
	{
		org.csapi.mm.TpUserStatus[] _result;
		int _l_result49 = _in.read_long();
		_result = new org.csapi.mm.TpUserStatus[_l_result49];
		for (int i=0;i<_result.length;i++)
		{
			_result[i]=org.csapi.mm.TpUserStatusHelper.read(_in);
		}

		return _result;
	}

	public static void write (final org.omg.CORBA.portable.OutputStream _out, org.csapi.mm.TpUserStatus[] _s)
	{
		
		_out.write_long(_s.length);
		for (int i=0; i<_s.length;i++)
		{
			org.csapi.mm.TpUserStatusHelper.write(_out,_s[i]);
		}

	}
}
