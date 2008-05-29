package org.csapi.gms;
/**
 *	Generated from IDL definition of enum "TpMessageFormat"
 *	@author JacORB IDL compiler 
 */

public final class TpMessageFormatHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.gms.TpMessageFormatHelper.id(),"TpMessageFormat",new String[]{"P_MESSAGING_MESSAGE_FORMAT_UNDEFINED","P_MESSAGING_MESSAGE_FORMAT_TEXT","P_MESSAGING_MESSAGE_FORMAT_BINARY","P_MESSAGING_MESSAGE_FORMAT_UUENCODED","P_MESSAGING_MESSAGE_FORMAT_MIME","P_MESSAGING_MESSAGE_FORMAT_WAVE","P_MESSAGING_MESSAGE_FORMAT_AU"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.gms.TpMessageFormat s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.gms.TpMessageFormat extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/gms/TpMessageFormat:1.0";
	}
	public static TpMessageFormat read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpMessageFormat.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpMessageFormat s)
	{
		out.write_long(s.value());
	}
}
