package org.csapi.policy;
/**
 *	Generated from IDL definition of enum "TpPolicyActionType"
 *	@author JacORB IDL compiler 
 */

public final class TpPolicyActionTypeHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.policy.TpPolicyActionTypeHelper.id(),"TpPolicyActionType",new String[]{"P_PM_EVENT_ACTION","P_PM_EXPRESSION_ACTION"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.policy.TpPolicyActionType s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.policy.TpPolicyActionType extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/policy/TpPolicyActionType:1.0";
	}
	public static TpPolicyActionType read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpPolicyActionType.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpPolicyActionType s)
	{
		out.write_long(s.value());
	}
}
