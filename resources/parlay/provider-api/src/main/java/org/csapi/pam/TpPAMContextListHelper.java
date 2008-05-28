package org.csapi.pam;

/**
 *	Generated from IDL definition of alias "TpPAMContextList"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMContextListHelper
{
	private static org.omg.CORBA.TypeCode _type = null;

	public static void insert (org.omg.CORBA.Any any, org.csapi.pam.TpPAMContext[] s)
	{
		any.type (type ());
		write (any.create_output_stream (), s);
	}

	public static org.csapi.pam.TpPAMContext[] extract (final org.omg.CORBA.Any any)
	{
		return read (any.create_input_stream ());
	}

	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_alias_tc(org.csapi.pam.TpPAMContextListHelper.id(), "TpPAMContextList",org.omg.CORBA.ORB.init().create_sequence_tc(0, org.csapi.pam.TpPAMContextHelper.type()));
		}
		return _type;
	}

	public static String id()
	{
		return "IDL:org/csapi/pam/TpPAMContextList:1.0";
	}
	public static org.csapi.pam.TpPAMContext[] read (final org.omg.CORBA.portable.InputStream _in)
	{
		org.csapi.pam.TpPAMContext[] _result;
		int _l_result72 = _in.read_long();
		_result = new org.csapi.pam.TpPAMContext[_l_result72];
		for (int i=0;i<_result.length;i++)
		{
			_result[i]=org.csapi.pam.TpPAMContextHelper.read(_in);
		}

		return _result;
	}

	public static void write (final org.omg.CORBA.portable.OutputStream _out, org.csapi.pam.TpPAMContext[] _s)
	{
		
		_out.write_long(_s.length);
		for (int i=0; i<_s.length;i++)
		{
			org.csapi.pam.TpPAMContextHelper.write(_out,_s[i]);
		}

	}
}
