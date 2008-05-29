package org.csapi.gms;

/**
 *	Generated from IDL definition of alias "TpMailboxInfoPropertySet"
 *	@author JacORB IDL compiler 
 */

public final class TpMailboxInfoPropertySetHelper
{
	private static org.omg.CORBA.TypeCode _type = null;

	public static void insert (org.omg.CORBA.Any any, org.csapi.gms.TpMailboxInfoProperty[] s)
	{
		any.type (type ());
		write (any.create_output_stream (), s);
	}

	public static org.csapi.gms.TpMailboxInfoProperty[] extract (final org.omg.CORBA.Any any)
	{
		return read (any.create_input_stream ());
	}

	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_alias_tc(org.csapi.gms.TpMailboxInfoPropertySetHelper.id(), "TpMailboxInfoPropertySet",org.omg.CORBA.ORB.init().create_sequence_tc(0, org.csapi.gms.TpMailboxInfoPropertyHelper.type()));
		}
		return _type;
	}

	public static String id()
	{
		return "IDL:org/csapi/gms/TpMailboxInfoPropertySet:1.0";
	}
	public static org.csapi.gms.TpMailboxInfoProperty[] read (final org.omg.CORBA.portable.InputStream _in)
	{
		org.csapi.gms.TpMailboxInfoProperty[] _result;
		int _l_result36 = _in.read_long();
		_result = new org.csapi.gms.TpMailboxInfoProperty[_l_result36];
		for (int i=0;i<_result.length;i++)
		{
			_result[i]=org.csapi.gms.TpMailboxInfoPropertyHelper.read(_in);
		}

		return _result;
	}

	public static void write (final org.omg.CORBA.portable.OutputStream _out, org.csapi.gms.TpMailboxInfoProperty[] _s)
	{
		
		_out.write_long(_s.length);
		for (int i=0; i<_s.length;i++)
		{
			org.csapi.gms.TpMailboxInfoPropertyHelper.write(_out,_s[i]);
		}

	}
}
