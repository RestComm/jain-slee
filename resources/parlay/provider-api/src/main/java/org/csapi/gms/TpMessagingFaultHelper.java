package org.csapi.gms;
/**
 *	Generated from IDL definition of enum "TpMessagingFault"
 *	@author JacORB IDL compiler 
 */

public final class TpMessagingFaultHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.gms.TpMessagingFaultHelper.id(),"TpMessagingFault",new String[]{"P_MESSAGING_FAULT_UNDEFINED"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.gms.TpMessagingFault s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.gms.TpMessagingFault extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/gms/TpMessagingFault:1.0";
	}
	public static TpMessagingFault read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpMessagingFault.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpMessagingFault s)
	{
		out.write_long(s.value());
	}
}
