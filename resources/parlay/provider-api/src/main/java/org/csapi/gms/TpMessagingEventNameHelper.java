package org.csapi.gms;
/**
 *	Generated from IDL definition of enum "TpMessagingEventName"
 *	@author JacORB IDL compiler 
 */

public final class TpMessagingEventNameHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.gms.TpMessagingEventNameHelper.id(),"TpMessagingEventName",new String[]{"P_EVENT_GMS_NAME_UNDEFINED","P_EVENT_GMS_NEW_MESSAGE_ARRIVED"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.gms.TpMessagingEventName s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.gms.TpMessagingEventName extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/gms/TpMessagingEventName:1.0";
	}
	public static TpMessagingEventName read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpMessagingEventName.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpMessagingEventName s)
	{
		out.write_long(s.value());
	}
}
