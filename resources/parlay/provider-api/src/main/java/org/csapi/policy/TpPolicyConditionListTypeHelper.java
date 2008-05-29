package org.csapi.policy;
/**
 *	Generated from IDL definition of enum "TpPolicyConditionListType"
 *	@author JacORB IDL compiler 
 */

public final class TpPolicyConditionListTypeHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.policy.TpPolicyConditionListTypeHelper.id(),"TpPolicyConditionListType",new String[]{"P_PM_DNF","P_PM_CNF"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.policy.TpPolicyConditionListType s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.policy.TpPolicyConditionListType extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/policy/TpPolicyConditionListType:1.0";
	}
	public static TpPolicyConditionListType read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpPolicyConditionListType.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpPolicyConditionListType s)
	{
		out.write_long(s.value());
	}
}
