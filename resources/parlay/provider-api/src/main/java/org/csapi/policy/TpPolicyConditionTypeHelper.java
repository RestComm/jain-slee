package org.csapi.policy;
/**
 *	Generated from IDL definition of enum "TpPolicyConditionType"
 *	@author JacORB IDL compiler 
 */

public final class TpPolicyConditionTypeHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.policy.TpPolicyConditionTypeHelper.id(),"TpPolicyConditionType",new String[]{"P_PM_TIME_PERIOD_CONDITION","P_PM_EVENT_CONDITION","P_PM_EXPRESSION_CONDITION"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.policy.TpPolicyConditionType s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.policy.TpPolicyConditionType extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/policy/TpPolicyConditionType:1.0";
	}
	public static TpPolicyConditionType read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpPolicyConditionType.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpPolicyConditionType s)
	{
		out.write_long(s.value());
	}
}
