package org.csapi.policy;


/**
 *	Generated from IDL definition of struct "TpPolicyActionListElement"
 *	@author JacORB IDL compiler 
 */

public final class TpPolicyActionListElementHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.policy.TpPolicyActionListElementHelper.id(),"TpPolicyActionListElement",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("Action", org.omg.CORBA.ORB.init().create_interface_tc("IDL:org/csapi/policy/IpPolicyAction:1.0", "IpPolicyAction"), null),new org.omg.CORBA.StructMember("SequenceNumber", org.csapi.TpInt32Helper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.policy.TpPolicyActionListElement s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.policy.TpPolicyActionListElement extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/policy/TpPolicyActionListElement:1.0";
	}
	public static org.csapi.policy.TpPolicyActionListElement read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.policy.TpPolicyActionListElement result = new org.csapi.policy.TpPolicyActionListElement();
		result.Action=org.csapi.policy.IpPolicyActionHelper.read(in);
		result.SequenceNumber=in.read_long();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.policy.TpPolicyActionListElement s)
	{
		org.csapi.policy.IpPolicyActionHelper.write(out,s.Action);
		out.write_long(s.SequenceNumber);
	}
}
