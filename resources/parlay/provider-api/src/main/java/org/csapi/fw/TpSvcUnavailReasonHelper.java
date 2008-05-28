package org.csapi.fw;
/**
 *	Generated from IDL definition of enum "TpSvcUnavailReason"
 *	@author JacORB IDL compiler 
 */

public final class TpSvcUnavailReasonHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.fw.TpSvcUnavailReasonHelper.id(),"TpSvcUnavailReason",new String[]{"SERVICE_UNAVAILABLE_UNDEFINED","SERVICE_UNAVAILABLE_LOCAL_FAILURE","SERVICE_UNAVAILABLE_GATEWAY_FAILURE","SERVICE_UNAVAILABLE_OVERLOADED","SERVICE_UNAVAILABLE_CLOSED"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.fw.TpSvcUnavailReason s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.fw.TpSvcUnavailReason extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/fw/TpSvcUnavailReason:1.0";
	}
	public static TpSvcUnavailReason read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpSvcUnavailReason.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpSvcUnavailReason s)
	{
		out.write_long(s.value());
	}
}
