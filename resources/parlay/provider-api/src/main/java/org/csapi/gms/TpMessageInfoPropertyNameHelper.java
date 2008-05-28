package org.csapi.gms;
/**
 *	Generated from IDL definition of enum "TpMessageInfoPropertyName"
 *	@author JacORB IDL compiler 
 */

public final class TpMessageInfoPropertyNameHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.gms.TpMessageInfoPropertyNameHelper.id(),"TpMessageInfoPropertyName",new String[]{"P_MESSAGING_MESSAGE_UNDEFINED","P_MESSAGING_MESSAGE_ID","P_MESSAGING_MESSAGE_SUBJECT","P_MESSAGING_MESSAGE_DATE_SENT","P_MESSAGING_MESSAGE_DATE_RECEIVED","P_MESSAGING_MESSAGE_DATE_CHANGED","P_MESSAGING_MESSAGE_SENT_FROM","P_MESSAGING_MESSAGE_SENT_TO","P_MESSAGING_MESSAGE_CC_TO","P_MESSAGING_MESSAGE_BCC_TO","P_MESSAGING_MESSAGE_SIZE","P_MESSAGING_MESSAGE_PRIORITY","P_MESSAGING_MESSAGE_FORMAT","P_MESSAGING_MESSAGE_FOLDER","P_MESSAGING_MESSAGE_STATUS"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.gms.TpMessageInfoPropertyName s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.gms.TpMessageInfoPropertyName extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/gms/TpMessageInfoPropertyName:1.0";
	}
	public static TpMessageInfoPropertyName read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpMessageInfoPropertyName.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpMessageInfoPropertyName s)
	{
		out.write_long(s.value());
	}
}
