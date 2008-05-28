package org.csapi.policy;


/**
 *	Generated from IDL definition of struct "TpPolicyConditionListElement"
 *	@author JacORB IDL compiler 
 */

public final class TpPolicyConditionListElementHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.policy.TpPolicyConditionListElementHelper.id(),"TpPolicyConditionListElement",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("Condition", org.omg.CORBA.ORB.init().create_interface_tc("IDL:org/csapi/policy/IpPolicyCondition:1.0", "IpPolicyCondition"), null),new org.omg.CORBA.StructMember("GroupNumber", org.csapi.TpInt32Helper.type(), null),new org.omg.CORBA.StructMember("Negated", org.csapi.TpBooleanHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.policy.TpPolicyConditionListElement s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.policy.TpPolicyConditionListElement extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/policy/TpPolicyConditionListElement:1.0";
	}
	public static org.csapi.policy.TpPolicyConditionListElement read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.policy.TpPolicyConditionListElement result = new org.csapi.policy.TpPolicyConditionListElement();
		result.Condition=org.csapi.policy.IpPolicyConditionHelper.read(in);
		result.GroupNumber=in.read_long();
		result.Negated=in.read_boolean();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.policy.TpPolicyConditionListElement s)
	{
		org.csapi.policy.IpPolicyConditionHelper.write(out,s.Condition);
		out.write_long(s.GroupNumber);
		out.write_boolean(s.Negated);
	}
}
