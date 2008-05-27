package org.csapi.pam;


/**
 *	Generated from IDL definition of struct "TpPAMAVCNotificationData"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMAVCNotificationDataHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.pam.TpPAMAVCNotificationDataHelper.id(),"TpPAMAVCNotificationData",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("Identity", org.csapi.pam.TpPAMFQNameHelper.type(), null),new org.omg.CORBA.StructMember("Availability", org.csapi.pam.TpPAMAvailabilityProfileListHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.pam.TpPAMAVCNotificationData s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.pam.TpPAMAVCNotificationData extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/pam/TpPAMAVCNotificationData:1.0";
	}
	public static org.csapi.pam.TpPAMAVCNotificationData read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.pam.TpPAMAVCNotificationData result = new org.csapi.pam.TpPAMAVCNotificationData();
		result.Identity=in.read_string();
		result.Availability = org.csapi.pam.TpPAMAvailabilityProfileListHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.pam.TpPAMAVCNotificationData s)
	{
		out.write_string(s.Identity);
		org.csapi.pam.TpPAMAvailabilityProfileListHelper.write(out,s.Availability);
	}
}
