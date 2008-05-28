package org.csapi.cc.gccs;
/**
 *	Generated from IDL definition of enum "TpCallNotificationType"
 *	@author JacORB IDL compiler 
 */

public final class TpCallNotificationTypeHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.cc.gccs.TpCallNotificationTypeHelper.id(),"TpCallNotificationType",new String[]{"P_ORIGINATING","P_TERMINATING"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cc.gccs.TpCallNotificationType s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cc.gccs.TpCallNotificationType extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/gccs/TpCallNotificationType:1.0";
	}
	public static TpCallNotificationType read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpCallNotificationType.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpCallNotificationType s)
	{
		out.write_long(s.value());
	}
}
