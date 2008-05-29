package org.csapi.cs;

/**
 *	Generated from IDL definition of alias "TpChargingParameterSet"
 *	@author JacORB IDL compiler 
 */

public final class TpChargingParameterSetHelper
{
	private static org.omg.CORBA.TypeCode _type = null;

	public static void insert (org.omg.CORBA.Any any, org.csapi.cs.TpChargingParameter[] s)
	{
		any.type (type ());
		write (any.create_output_stream (), s);
	}

	public static org.csapi.cs.TpChargingParameter[] extract (final org.omg.CORBA.Any any)
	{
		return read (any.create_input_stream ());
	}

	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_alias_tc(org.csapi.cs.TpChargingParameterSetHelper.id(), "TpChargingParameterSet",org.omg.CORBA.ORB.init().create_sequence_tc(0, org.csapi.cs.TpChargingParameterHelper.type()));
		}
		return _type;
	}

	public static String id()
	{
		return "IDL:org/csapi/cs/TpChargingParameterSet:1.0";
	}
	public static org.csapi.cs.TpChargingParameter[] read (final org.omg.CORBA.portable.InputStream _in)
	{
		org.csapi.cs.TpChargingParameter[] _result;
		int _l_result9 = _in.read_long();
		_result = new org.csapi.cs.TpChargingParameter[_l_result9];
		for (int i=0;i<_result.length;i++)
		{
			_result[i]=org.csapi.cs.TpChargingParameterHelper.read(_in);
		}

		return _result;
	}

	public static void write (final org.omg.CORBA.portable.OutputStream _out, org.csapi.cs.TpChargingParameter[] _s)
	{
		
		_out.write_long(_s.length);
		for (int i=0; i<_s.length;i++)
		{
			org.csapi.cs.TpChargingParameterHelper.write(_out,_s[i]);
		}

	}
}
