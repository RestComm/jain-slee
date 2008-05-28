package org.csapi.gms;

/**
 *	Generated from IDL definition of alias "TpMessageInfoPropertySet"
 *	@author JacORB IDL compiler 
 */

public final class TpMessageInfoPropertySetHelper
{
	private static org.omg.CORBA.TypeCode _type = null;

	public static void insert (org.omg.CORBA.Any any, org.csapi.gms.TpMessageInfoProperty[] s)
	{
		any.type (type ());
		write (any.create_output_stream (), s);
	}

	public static org.csapi.gms.TpMessageInfoProperty[] extract (final org.omg.CORBA.Any any)
	{
		return read (any.create_input_stream ());
	}

	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_alias_tc(org.csapi.gms.TpMessageInfoPropertySetHelper.id(), "TpMessageInfoPropertySet",org.omg.CORBA.ORB.init().create_sequence_tc(0, org.csapi.gms.TpMessageInfoPropertyHelper.type()));
		}
		return _type;
	}

	public static String id()
	{
		return "IDL:org/csapi/gms/TpMessageInfoPropertySet:1.0";
	}
	public static org.csapi.gms.TpMessageInfoProperty[] read (final org.omg.CORBA.portable.InputStream _in)
	{
		org.csapi.gms.TpMessageInfoProperty[] _result;
		int _l_result35 = _in.read_long();
		_result = new org.csapi.gms.TpMessageInfoProperty[_l_result35];
		for (int i=0;i<_result.length;i++)
		{
			_result[i]=org.csapi.gms.TpMessageInfoPropertyHelper.read(_in);
		}

		return _result;
	}

	public static void write (final org.omg.CORBA.portable.OutputStream _out, org.csapi.gms.TpMessageInfoProperty[] _s)
	{
		
		_out.write_long(_s.length);
		for (int i=0; i<_s.length;i++)
		{
			org.csapi.gms.TpMessageInfoPropertyHelper.write(_out,_s[i]);
		}

	}
}
