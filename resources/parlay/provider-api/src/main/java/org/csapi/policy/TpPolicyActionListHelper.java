package org.csapi.policy;

/**
 *	Generated from IDL definition of alias "TpPolicyActionList"
 *	@author JacORB IDL compiler 
 */

public final class TpPolicyActionListHelper
{
	private static org.omg.CORBA.TypeCode _type = null;

	public static void insert (org.omg.CORBA.Any any, org.csapi.policy.TpPolicyActionListElement[] s)
	{
		any.type (type ());
		write (any.create_output_stream (), s);
	}

	public static org.csapi.policy.TpPolicyActionListElement[] extract (final org.omg.CORBA.Any any)
	{
		return read (any.create_input_stream ());
	}

	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_alias_tc(org.csapi.policy.TpPolicyActionListHelper.id(), "TpPolicyActionList",org.omg.CORBA.ORB.init().create_sequence_tc(0, org.csapi.policy.TpPolicyActionListElementHelper.type()));
		}
		return _type;
	}

	public static String id()
	{
		return "IDL:org/csapi/policy/TpPolicyActionList:1.0";
	}
	public static org.csapi.policy.TpPolicyActionListElement[] read (final org.omg.CORBA.portable.InputStream _in)
	{
		org.csapi.policy.TpPolicyActionListElement[] _result;
		int _l_result78 = _in.read_long();
		_result = new org.csapi.policy.TpPolicyActionListElement[_l_result78];
		for (int i=0;i<_result.length;i++)
		{
			_result[i]=org.csapi.policy.TpPolicyActionListElementHelper.read(_in);
		}

		return _result;
	}

	public static void write (final org.omg.CORBA.portable.OutputStream _out, org.csapi.policy.TpPolicyActionListElement[] _s)
	{
		
		_out.write_long(_s.length);
		for (int i=0; i<_s.length;i++)
		{
			org.csapi.policy.TpPolicyActionListElementHelper.write(_out,_s[i]);
		}

	}
}
