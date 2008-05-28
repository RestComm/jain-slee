package org.csapi.policy;


/**
 *	Generated from IDL definition of struct "TpPolicyEvent"
 *	@author JacORB IDL compiler 
 */

public final class TpPolicyEventHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.policy.TpPolicyEventHelper.id(),"TpPolicyEvent",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("EventID", org.csapi.TpInt32Helper.type(), null),new org.omg.CORBA.StructMember("TimeGenerated", org.csapi.TpDateAndTimeHelper.type(), null),new org.omg.CORBA.StructMember("Attributes", org.csapi.TpAttributeSetHelper.type(), null),new org.omg.CORBA.StructMember("EventDefinitionName", org.csapi.TpStringHelper.type(), null),new org.omg.CORBA.StructMember("EventDomainName", org.csapi.TpStringHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.policy.TpPolicyEvent s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.policy.TpPolicyEvent extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/policy/TpPolicyEvent:1.0";
	}
	public static org.csapi.policy.TpPolicyEvent read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.policy.TpPolicyEvent result = new org.csapi.policy.TpPolicyEvent();
		result.EventID=in.read_long();
		result.TimeGenerated=in.read_string();
		result.Attributes = org.csapi.TpAttributeSetHelper.read(in);
		result.EventDefinitionName=in.read_string();
		result.EventDomainName=in.read_string();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.policy.TpPolicyEvent s)
	{
		out.write_long(s.EventID);
		out.write_string(s.TimeGenerated);
		org.csapi.TpAttributeSetHelper.write(out,s.Attributes);
		out.write_string(s.EventDefinitionName);
		out.write_string(s.EventDomainName);
	}
}
