package org.csapi.gms;
/**
 *	Generated from IDL definition of enum "TpMessagePriority"
 *	@author JacORB IDL compiler 
 */

public final class TpMessagePriorityHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.gms.TpMessagePriorityHelper.id(),"TpMessagePriority",new String[]{"P_MESSAGING_MESSAGE_PRIORITY_UNDEFINED","P_MESSAGING_MESSAGE_PRIORITY_HIGH","P_MESSAGING_MESSAGE_PRIORITY_LOW"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.gms.TpMessagePriority s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.gms.TpMessagePriority extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/gms/TpMessagePriority:1.0";
	}
	public static TpMessagePriority read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpMessagePriority.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpMessagePriority s)
	{
		out.write_long(s.value());
	}
}
