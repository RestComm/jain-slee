package org.csapi.mm;

/**
 *	Generated from IDL definition of alias "TpLocationTriggerSet"
 *	@author JacORB IDL compiler 
 */

public final class TpLocationTriggerSetHelper
{
	private static org.omg.CORBA.TypeCode _type = null;

	public static void insert (org.omg.CORBA.Any any, org.csapi.mm.TpLocationTrigger[] s)
	{
		any.type (type ());
		write (any.create_output_stream (), s);
	}

	public static org.csapi.mm.TpLocationTrigger[] extract (final org.omg.CORBA.Any any)
	{
		return read (any.create_input_stream ());
	}

	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_alias_tc(org.csapi.mm.TpLocationTriggerSetHelper.id(), "TpLocationTriggerSet",org.omg.CORBA.ORB.init().create_sequence_tc(0, org.csapi.mm.TpLocationTriggerHelper.type()));
		}
		return _type;
	}

	public static String id()
	{
		return "IDL:org/csapi/mm/TpLocationTriggerSet:1.0";
	}
	public static org.csapi.mm.TpLocationTrigger[] read (final org.omg.CORBA.portable.InputStream _in)
	{
		org.csapi.mm.TpLocationTrigger[] _result;
		int _l_result44 = _in.read_long();
		_result = new org.csapi.mm.TpLocationTrigger[_l_result44];
		for (int i=0;i<_result.length;i++)
		{
			_result[i]=org.csapi.mm.TpLocationTriggerHelper.read(_in);
		}

		return _result;
	}

	public static void write (final org.omg.CORBA.portable.OutputStream _out, org.csapi.mm.TpLocationTrigger[] _s)
	{
		
		_out.write_long(_s.length);
		for (int i=0; i<_s.length;i++)
		{
			org.csapi.mm.TpLocationTriggerHelper.write(_out,_s[i]);
		}

	}
}
