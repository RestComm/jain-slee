package org.csapi.fw;
/**
 *	Generated from IDL definition of enum "TpFwUnavailReason"
 *	@author JacORB IDL compiler 
 */

public final class TpFwUnavailReasonHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.fw.TpFwUnavailReasonHelper.id(),"TpFwUnavailReason",new String[]{"FW_UNAVAILABLE_UNDEFINED","FW_UNAVAILABLE_LOCAL_FAILURE","FW_UNAVAILABLE_GATEWAY_FAILURE","FW_UNAVAILABLE_OVERLOADED","FW_UNAVAILABLE_CLOSED","FW_UNAVAILABLE_PROTOCOL_FAILURE"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.fw.TpFwUnavailReason s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.fw.TpFwUnavailReason extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/fw/TpFwUnavailReason:1.0";
	}
	public static TpFwUnavailReason read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpFwUnavailReason.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpFwUnavailReason s)
	{
		out.write_long(s.value());
	}
}
