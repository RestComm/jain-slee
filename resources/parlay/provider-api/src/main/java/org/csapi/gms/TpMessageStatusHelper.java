package org.csapi.gms;
/**
 *	Generated from IDL definition of enum "TpMessageStatus"
 *	@author JacORB IDL compiler 
 */

public final class TpMessageStatusHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.gms.TpMessageStatusHelper.id(),"TpMessageStatus",new String[]{"P_MESSAGING_MESSAGE_STATUS_READ_MESSAGE","P_MESSAGING_MESSAGE_STATUS_UNREAD_MESSAGE","P_MESSAGING_MESSAGE_STATUS_FORWARDED_MESSAGE","P_MESSAGING_MESSAGE_STATUS_REPLIED_TO_MESSAGE","P_MESSAGING_MESSAGE_STATUS_SAVED_OR_UNSENT_MESSAGE","P_MESSAGING_MESSAGE_STATUS_NOTIFICATION_THAT_A_MESSAGE_WAS_DELIVERED","P_MESSAGING_MESSAGE_STATUS_NOTIFICATION_THAT_A_MESSAGE_WAS_READ","P_MESSAGING_MESSAGE_STATUS_NOTIFICATION_THAT_A_MESSAGE_WAS_NOT_DELIVERED","P_MESSAGING_MESSAGE_STATUS_NOTIFICATION_THAT_A_MESSAGE_WAS_NOT_READ"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.gms.TpMessageStatus s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.gms.TpMessageStatus extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/gms/TpMessageStatus:1.0";
	}
	public static TpMessageStatus read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpMessageStatus.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpMessageStatus s)
	{
		out.write_long(s.value());
	}
}
