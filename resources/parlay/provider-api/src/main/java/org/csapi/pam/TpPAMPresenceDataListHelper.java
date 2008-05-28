package org.csapi.pam;

/**
 *	Generated from IDL definition of alias "TpPAMPresenceDataList"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMPresenceDataListHelper
{
	private static org.omg.CORBA.TypeCode _type = null;

	public static void insert (org.omg.CORBA.Any any, org.csapi.pam.TpPAMPresenceData[] s)
	{
		any.type (type ());
		write (any.create_output_stream (), s);
	}

	public static org.csapi.pam.TpPAMPresenceData[] extract (final org.omg.CORBA.Any any)
	{
		return read (any.create_input_stream ());
	}

	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_alias_tc(org.csapi.pam.TpPAMPresenceDataListHelper.id(), "TpPAMPresenceDataList",org.omg.CORBA.ORB.init().create_sequence_tc(0, org.csapi.pam.TpPAMPresenceDataHelper.type()));
		}
		return _type;
	}

	public static String id()
	{
		return "IDL:org/csapi/pam/TpPAMPresenceDataList:1.0";
	}
	public static org.csapi.pam.TpPAMPresenceData[] read (final org.omg.CORBA.portable.InputStream _in)
	{
		org.csapi.pam.TpPAMPresenceData[] _result;
		int _l_result70 = _in.read_long();
		_result = new org.csapi.pam.TpPAMPresenceData[_l_result70];
		for (int i=0;i<_result.length;i++)
		{
			_result[i]=org.csapi.pam.TpPAMPresenceDataHelper.read(_in);
		}

		return _result;
	}

	public static void write (final org.omg.CORBA.portable.OutputStream _out, org.csapi.pam.TpPAMPresenceData[] _s)
	{
		
		_out.write_long(_s.length);
		for (int i=0; i<_s.length;i++)
		{
			org.csapi.pam.TpPAMPresenceDataHelper.write(_out,_s[i]);
		}

	}
}
