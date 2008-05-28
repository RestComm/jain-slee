package org.csapi.gms;
/**
 *	Generated from IDL definition of enum "TpMailboxInfoPropertyName"
 *	@author JacORB IDL compiler 
 */

public final class TpMailboxInfoPropertyNameHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.gms.TpMailboxInfoPropertyNameHelper.id(),"TpMailboxInfoPropertyName",new String[]{"P_MESSAGING_MAILBOX_UNDEFINED","P_MESSAGING_MAILBOX_ID","P_MESSAGING_MAILBOX_OWNER","P_MESSAGING_MAILBOX_FOLDER","P_MESSAGING_MAILBOX_DATE_CREATED","P_MESSAGING_MAILBOX_DATE_CHANGED"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.gms.TpMailboxInfoPropertyName s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.gms.TpMailboxInfoPropertyName extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/gms/TpMailboxInfoPropertyName:1.0";
	}
	public static TpMailboxInfoPropertyName read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpMailboxInfoPropertyName.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpMailboxInfoPropertyName s)
	{
		out.write_long(s.value());
	}
}
