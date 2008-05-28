package org.csapi.policy;

/**
 *	Generated from IDL definition of alias "TpPolicyConditionList"
 *	@author JacORB IDL compiler 
 */

public final class TpPolicyConditionListHelper
{
	private static org.omg.CORBA.TypeCode _type = null;

	public static void insert (org.omg.CORBA.Any any, org.csapi.policy.TpPolicyConditionListElement[] s)
	{
		any.type (type ());
		write (any.create_output_stream (), s);
	}

	public static org.csapi.policy.TpPolicyConditionListElement[] extract (final org.omg.CORBA.Any any)
	{
		return read (any.create_input_stream ());
	}

	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_alias_tc(org.csapi.policy.TpPolicyConditionListHelper.id(), "TpPolicyConditionList",org.omg.CORBA.ORB.init().create_sequence_tc(0, org.csapi.policy.TpPolicyConditionListElementHelper.type()));
		}
		return _type;
	}

	public static String id()
	{
		return "IDL:org/csapi/policy/TpPolicyConditionList:1.0";
	}
	public static org.csapi.policy.TpPolicyConditionListElement[] read (final org.omg.CORBA.portable.InputStream _in)
	{
		org.csapi.policy.TpPolicyConditionListElement[] _result;
		int _l_result77 = _in.read_long();
		_result = new org.csapi.policy.TpPolicyConditionListElement[_l_result77];
		for (int i=0;i<_result.length;i++)
		{
			_result[i]=org.csapi.policy.TpPolicyConditionListElementHelper.read(_in);
		}

		return _result;
	}

	public static void write (final org.omg.CORBA.portable.OutputStream _out, org.csapi.policy.TpPolicyConditionListElement[] _s)
	{
		
		_out.write_long(_s.length);
		for (int i=0; i<_s.length;i++)
		{
			org.csapi.policy.TpPolicyConditionListElementHelper.write(_out,_s[i]);
		}

	}
}
