package org.csapi.pam;


/**
 *	Generated from IDL definition of struct "TpPAMIPSNotificationData"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMIPSNotificationDataHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.pam.TpPAMIPSNotificationDataHelper.id(),"TpPAMIPSNotificationData",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("Identity", org.csapi.pam.TpPAMFQNameHelper.type(), null),new org.omg.CORBA.StructMember("Attributes", org.csapi.pam.TpPAMPresenceDataListHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.pam.TpPAMIPSNotificationData s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.pam.TpPAMIPSNotificationData extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/pam/TpPAMIPSNotificationData:1.0";
	}
	public static org.csapi.pam.TpPAMIPSNotificationData read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.pam.TpPAMIPSNotificationData result = new org.csapi.pam.TpPAMIPSNotificationData();
		result.Identity=in.read_string();
		result.Attributes = org.csapi.pam.TpPAMPresenceDataListHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.pam.TpPAMIPSNotificationData s)
	{
		out.write_string(s.Identity);
		org.csapi.pam.TpPAMPresenceDataListHelper.write(out,s.Attributes);
	}
}
