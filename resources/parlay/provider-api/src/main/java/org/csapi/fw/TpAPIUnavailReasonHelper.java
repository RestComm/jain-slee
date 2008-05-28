package org.csapi.fw;
/**
 *	Generated from IDL definition of enum "TpAPIUnavailReason"
 *	@author JacORB IDL compiler 
 */

public final class TpAPIUnavailReasonHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.fw.TpAPIUnavailReasonHelper.id(),"TpAPIUnavailReason",new String[]{"API_UNAVAILABLE_UNDEFINED","API_UNAVAILABLE_LOCAL_FAILURE","API_UNAVAILABLE_GATEWAY_FAILURE","API_UNAVAILABLE_OVERLOADED","API_UNAVAILABLE_CLOSED","API_UNAVAILABLE_PROTOCOL_FAILURE"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.fw.TpAPIUnavailReason s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.fw.TpAPIUnavailReason extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/fw/TpAPIUnavailReason:1.0";
	}
	public static TpAPIUnavailReason read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpAPIUnavailReason.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpAPIUnavailReason s)
	{
		out.write_long(s.value());
	}
}
