package org.csapi;
/**
 *	Generated from IDL definition of enum "TpDataSessionQosClass"
 *	@author JacORB IDL compiler 
 */

public final class TpDataSessionQosClassHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.TpDataSessionQosClassHelper.id(),"TpDataSessionQosClass",new String[]{"P_DATA_SESSION_QOS_CLASS_CONVERSATIONAL","P_DATA_SESSION_QOS_CLASS_STREAMING","P_DATA_SESSION_QOS_CLASS_INTERACTIVE","P_DATA_SESSION_QOS_CLASS_BACKGROUND"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.TpDataSessionQosClass s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.TpDataSessionQosClass extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/TpDataSessionQosClass:1.0";
	}
	public static TpDataSessionQosClass read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpDataSessionQosClass.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpDataSessionQosClass s)
	{
		out.write_long(s.value());
	}
}
