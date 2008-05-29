package org.csapi.cc;


/**
 *	Generated from IDL definition of struct "TpNotificationRequestedSetEntry"
 *	@author JacORB IDL compiler 
 */

public final class TpNotificationRequestedSetEntryHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.cc.TpNotificationRequestedSetEntryHelper.id(),"TpNotificationRequestedSetEntry",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("NotificationRequestSet", org.csapi.cc.TpNotificationRequestedSetHelper.type(), null),new org.omg.CORBA.StructMember("Final", org.csapi.TpBooleanHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cc.TpNotificationRequestedSetEntry s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cc.TpNotificationRequestedSetEntry extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/TpNotificationRequestedSetEntry:1.0";
	}
	public static org.csapi.cc.TpNotificationRequestedSetEntry read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.cc.TpNotificationRequestedSetEntry result = new org.csapi.cc.TpNotificationRequestedSetEntry();
		result.NotificationRequestSet = org.csapi.cc.TpNotificationRequestedSetHelper.read(in);
		result.Final=in.read_boolean();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.cc.TpNotificationRequestedSetEntry s)
	{
		org.csapi.cc.TpNotificationRequestedSetHelper.write(out,s.NotificationRequestSet);
		out.write_boolean(s.Final);
	}
}
