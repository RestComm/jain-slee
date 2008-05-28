package org.csapi.pam;


/**
 *	Generated from IDL definition of struct "TpPAMAANotificationData"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMAANotificationDataHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.pam.TpPAMAANotificationDataHelper.id(),"TpPAMAANotificationData",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("Identity", org.csapi.pam.TpPAMFQNameHelper.type(), null),new org.omg.CORBA.StructMember("Agent", org.csapi.pam.TpPAMFQNameHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.pam.TpPAMAANotificationData s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.pam.TpPAMAANotificationData extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/pam/TpPAMAANotificationData:1.0";
	}
	public static org.csapi.pam.TpPAMAANotificationData read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.pam.TpPAMAANotificationData result = new org.csapi.pam.TpPAMAANotificationData();
		result.Identity=in.read_string();
		result.Agent=in.read_string();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.pam.TpPAMAANotificationData s)
	{
		out.write_string(s.Identity);
		out.write_string(s.Agent);
	}
}
