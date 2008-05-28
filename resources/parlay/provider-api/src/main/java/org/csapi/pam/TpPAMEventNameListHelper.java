package org.csapi.pam;

/**
 *	Generated from IDL definition of alias "TpPAMEventNameList"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMEventNameListHelper
{
	private static org.omg.CORBA.TypeCode _type = null;

	public static void insert (org.omg.CORBA.Any any, org.csapi.pam.TpPAMEventName[] s)
	{
		any.type (type ());
		write (any.create_output_stream (), s);
	}

	public static org.csapi.pam.TpPAMEventName[] extract (final org.omg.CORBA.Any any)
	{
		return read (any.create_input_stream ());
	}

	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_alias_tc(org.csapi.pam.TpPAMEventNameListHelper.id(), "TpPAMEventNameList",org.omg.CORBA.ORB.init().create_sequence_tc(0, org.csapi.pam.TpPAMEventNameHelper.type()));
		}
		return _type;
	}

	public static String id()
	{
		return "IDL:org/csapi/pam/TpPAMEventNameList:1.0";
	}
	public static org.csapi.pam.TpPAMEventName[] read (final org.omg.CORBA.portable.InputStream _in)
	{
		org.csapi.pam.TpPAMEventName[] _result;
		int _l_result73 = _in.read_long();
		_result = new org.csapi.pam.TpPAMEventName[_l_result73];
		for (int i=0;i<_result.length;i++)
		{
			_result[i]=org.csapi.pam.TpPAMEventNameHelper.read(_in);
		}

		return _result;
	}

	public static void write (final org.omg.CORBA.portable.OutputStream _out, org.csapi.pam.TpPAMEventName[] _s)
	{
		
		_out.write_long(_s.length);
		for (int i=0; i<_s.length;i++)
		{
			org.csapi.pam.TpPAMEventNameHelper.write(_out,_s[i]);
		}

	}
}
