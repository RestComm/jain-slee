package org.csapi.pam;

/**
 *	Generated from IDL definition of alias "TpPAMAttributeList"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMAttributeListHelper
{
	private static org.omg.CORBA.TypeCode _type = null;

	public static void insert (org.omg.CORBA.Any any, org.csapi.pam.TpPAMAttribute[] s)
	{
		any.type (type ());
		write (any.create_output_stream (), s);
	}

	public static org.csapi.pam.TpPAMAttribute[] extract (final org.omg.CORBA.Any any)
	{
		return read (any.create_input_stream ());
	}

	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_alias_tc(org.csapi.pam.TpPAMAttributeListHelper.id(), "TpPAMAttributeList",org.omg.CORBA.ORB.init().create_sequence_tc(0, org.csapi.pam.TpPAMAttributeHelper.type()));
		}
		return _type;
	}

	public static String id()
	{
		return "IDL:org/csapi/pam/TpPAMAttributeList:1.0";
	}
	public static org.csapi.pam.TpPAMAttribute[] read (final org.omg.CORBA.portable.InputStream _in)
	{
		org.csapi.pam.TpPAMAttribute[] _result;
		int _l_result69 = _in.read_long();
		_result = new org.csapi.pam.TpPAMAttribute[_l_result69];
		for (int i=0;i<_result.length;i++)
		{
			_result[i]=org.csapi.pam.TpPAMAttributeHelper.read(_in);
		}

		return _result;
	}

	public static void write (final org.omg.CORBA.portable.OutputStream _out, org.csapi.pam.TpPAMAttribute[] _s)
	{
		
		_out.write_long(_s.length);
		for (int i=0; i<_s.length;i++)
		{
			org.csapi.pam.TpPAMAttributeHelper.write(_out,_s[i]);
		}

	}
}
