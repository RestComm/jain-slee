package org.csapi;

/**
 *	Generated from IDL definition of alias "TpAttributeSet"
 *	@author JacORB IDL compiler 
 */

public final class TpAttributeSetHelper
{
	private static org.omg.CORBA.TypeCode _type = null;

	public static void insert (org.omg.CORBA.Any any, org.csapi.TpAttribute[] s)
	{
		any.type (type ());
		write (any.create_output_stream (), s);
	}

	public static org.csapi.TpAttribute[] extract (final org.omg.CORBA.Any any)
	{
		return read (any.create_input_stream ());
	}

	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_alias_tc(org.csapi.TpAttributeSetHelper.id(), "TpAttributeSet",org.omg.CORBA.ORB.init().create_sequence_tc(0, org.csapi.TpAttributeHelper.type()));
		}
		return _type;
	}

	public static String id()
	{
		return "IDL:org/csapi/TpAttributeSet:1.0";
	}
	public static org.csapi.TpAttribute[] read (final org.omg.CORBA.portable.InputStream _in)
	{
		org.csapi.TpAttribute[] _result;
		int _l_result65 = _in.read_long();
		_result = new org.csapi.TpAttribute[_l_result65];
		for (int i=0;i<_result.length;i++)
		{
			_result[i]=org.csapi.TpAttributeHelper.read(_in);
		}

		return _result;
	}

	public static void write (final org.omg.CORBA.portable.OutputStream _out, org.csapi.TpAttribute[] _s)
	{
		
		_out.write_long(_s.length);
		for (int i=0; i<_s.length;i++)
		{
			org.csapi.TpAttributeHelper.write(_out,_s[i]);
		}

	}
}
