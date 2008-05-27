package org.csapi.gms;

/**
 *	Generated from IDL definition of alias "TpFolderInfoPropertySet"
 *	@author JacORB IDL compiler 
 */

public final class TpFolderInfoPropertySetHelper
{
	private static org.omg.CORBA.TypeCode _type = null;

	public static void insert (org.omg.CORBA.Any any, org.csapi.gms.TpFolderInfoProperty[] s)
	{
		any.type (type ());
		write (any.create_output_stream (), s);
	}

	public static org.csapi.gms.TpFolderInfoProperty[] extract (final org.omg.CORBA.Any any)
	{
		return read (any.create_input_stream ());
	}

	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_alias_tc(org.csapi.gms.TpFolderInfoPropertySetHelper.id(), "TpFolderInfoPropertySet",org.omg.CORBA.ORB.init().create_sequence_tc(0, org.csapi.gms.TpFolderInfoPropertyHelper.type()));
		}
		return _type;
	}

	public static String id()
	{
		return "IDL:org/csapi/gms/TpFolderInfoPropertySet:1.0";
	}
	public static org.csapi.gms.TpFolderInfoProperty[] read (final org.omg.CORBA.portable.InputStream _in)
	{
		org.csapi.gms.TpFolderInfoProperty[] _result;
		int _l_result37 = _in.read_long();
		_result = new org.csapi.gms.TpFolderInfoProperty[_l_result37];
		for (int i=0;i<_result.length;i++)
		{
			_result[i]=org.csapi.gms.TpFolderInfoPropertyHelper.read(_in);
		}

		return _result;
	}

	public static void write (final org.omg.CORBA.portable.OutputStream _out, org.csapi.gms.TpFolderInfoProperty[] _s)
	{
		
		_out.write_long(_s.length);
		for (int i=0; i<_s.length;i++)
		{
			org.csapi.gms.TpFolderInfoPropertyHelper.write(_out,_s[i]);
		}

	}
}
