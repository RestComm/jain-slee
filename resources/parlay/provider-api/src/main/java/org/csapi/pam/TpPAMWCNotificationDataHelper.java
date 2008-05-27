package org.csapi.pam;


/**
 *	Generated from IDL definition of struct "TpPAMWCNotificationData"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMWCNotificationDataHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.pam.TpPAMWCNotificationDataHelper.id(),"TpPAMWCNotificationData",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("Event", org.csapi.pam.TpPAMEventNameHelper.type(), null),new org.omg.CORBA.StructMember("ChangeType", org.csapi.pam.TpPAMwatcherChangeTypeHelper.type(), null),new org.omg.CORBA.StructMember("Identity", org.csapi.pam.TpPAMFQNameHelper.type(), null),new org.omg.CORBA.StructMember("Watchers", org.csapi.pam.TpPAMFQNameListHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.pam.TpPAMWCNotificationData s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.pam.TpPAMWCNotificationData extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/pam/TpPAMWCNotificationData:1.0";
	}
	public static org.csapi.pam.TpPAMWCNotificationData read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.pam.TpPAMWCNotificationData result = new org.csapi.pam.TpPAMWCNotificationData();
		result.Event=org.csapi.pam.TpPAMEventNameHelper.read(in);
		result.ChangeType=org.csapi.pam.TpPAMwatcherChangeTypeHelper.read(in);
		result.Identity=in.read_string();
		result.Watchers = org.csapi.pam.TpPAMFQNameListHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.pam.TpPAMWCNotificationData s)
	{
		org.csapi.pam.TpPAMEventNameHelper.write(out,s.Event);
		org.csapi.pam.TpPAMwatcherChangeTypeHelper.write(out,s.ChangeType);
		out.write_string(s.Identity);
		org.csapi.pam.TpPAMFQNameListHelper.write(out,s.Watchers);
	}
}
