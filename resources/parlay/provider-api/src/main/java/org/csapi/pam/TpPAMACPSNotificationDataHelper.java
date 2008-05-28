package org.csapi.pam;


/**
 *	Generated from IDL definition of struct "TpPAMACPSNotificationData"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMACPSNotificationDataHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.pam.TpPAMACPSNotificationDataHelper.id(),"TpPAMACPSNotificationData",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("Agent", org.csapi.pam.TpPAMFQNameHelper.type(), null),new org.omg.CORBA.StructMember("Capability", org.csapi.pam.TpPAMCapabilityHelper.type(), null),new org.omg.CORBA.StructMember("AttributeNames", org.csapi.TpStringListHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.pam.TpPAMACPSNotificationData s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.pam.TpPAMACPSNotificationData extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/pam/TpPAMACPSNotificationData:1.0";
	}
	public static org.csapi.pam.TpPAMACPSNotificationData read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.pam.TpPAMACPSNotificationData result = new org.csapi.pam.TpPAMACPSNotificationData();
		result.Agent=in.read_string();
		result.Capability=in.read_string();
		result.AttributeNames = org.csapi.TpStringListHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.pam.TpPAMACPSNotificationData s)
	{
		out.write_string(s.Agent);
		out.write_string(s.Capability);
		org.csapi.TpStringListHelper.write(out,s.AttributeNames);
	}
}
