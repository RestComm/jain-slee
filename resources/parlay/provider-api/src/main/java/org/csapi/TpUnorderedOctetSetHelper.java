package org.csapi;

/**
 *	Generated from IDL definition of alias "TpUnorderedOctetSet"
 *	@author JacORB IDL compiler 
 */

public final class TpUnorderedOctetSetHelper
{
	private static org.omg.CORBA.TypeCode _type = null;

	public static void insert (org.omg.CORBA.Any any, byte[] s)
	{
		any.type (type ());
		write (any.create_output_stream (), s);
	}

	public static byte[] extract (final org.omg.CORBA.Any any)
	{
		return read (any.create_input_stream ());
	}

	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_alias_tc(org.csapi.TpUnorderedOctetSetHelper.id(), "TpUnorderedOctetSet",org.omg.CORBA.ORB.init().create_sequence_tc(0, org.csapi.TpOctetHelper.type()));
		}
		return _type;
	}

	public static String id()
	{
		return "IDL:org/csapi/TpUnorderedOctetSet:1.0";
	}
	public static byte[] read (final org.omg.CORBA.portable.InputStream _in)
	{
		byte[] _result;
		int _l_result63 = _in.read_long();
		_result = new byte[_l_result63];
		for (int i=0;i<_result.length;i++)
		{
			_result[i]=_in.read_octet();
		}

		return _result;
	}

	public static void write (final org.omg.CORBA.portable.OutputStream _out, byte[] _s)
	{
		
		_out.write_long(_s.length);
		for (int i=0; i<_s.length;i++)
		{
			_out.write_octet(_s[i]);
		}

	}
}
