package org.csapi.fw;

/**
 *	Generated from IDL definition of alias "TpAddSagMembersConflictList"
 *	@author JacORB IDL compiler 
 */

public final class TpAddSagMembersConflictListHelper
{
	private static org.omg.CORBA.TypeCode _type = null;

	public static void insert (org.omg.CORBA.Any any, org.csapi.fw.TpAddSagMembersConflict[] s)
	{
		any.type (type ());
		write (any.create_output_stream (), s);
	}

	public static org.csapi.fw.TpAddSagMembersConflict[] extract (final org.omg.CORBA.Any any)
	{
		return read (any.create_input_stream ());
	}

	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_alias_tc(org.csapi.fw.TpAddSagMembersConflictListHelper.id(), "TpAddSagMembersConflictList",org.omg.CORBA.ORB.init().create_sequence_tc(0, org.csapi.fw.TpAddSagMembersConflictHelper.type()));
		}
		return _type;
	}

	public static String id()
	{
		return "IDL:org/csapi/fw/TpAddSagMembersConflictList:1.0";
	}
	public static org.csapi.fw.TpAddSagMembersConflict[] read (final org.omg.CORBA.portable.InputStream _in)
	{
		org.csapi.fw.TpAddSagMembersConflict[] _result;
		int _l_result29 = _in.read_long();
		_result = new org.csapi.fw.TpAddSagMembersConflict[_l_result29];
		for (int i=0;i<_result.length;i++)
		{
			_result[i]=org.csapi.fw.TpAddSagMembersConflictHelper.read(_in);
		}

		return _result;
	}

	public static void write (final org.omg.CORBA.portable.OutputStream _out, org.csapi.fw.TpAddSagMembersConflict[] _s)
	{
		
		_out.write_long(_s.length);
		for (int i=0; i<_s.length;i++)
		{
			org.csapi.fw.TpAddSagMembersConflictHelper.write(_out,_s[i]);
		}

	}
}
