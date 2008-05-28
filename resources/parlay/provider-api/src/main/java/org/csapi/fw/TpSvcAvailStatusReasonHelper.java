package org.csapi.fw;
/**
 *	Generated from IDL definition of enum "TpSvcAvailStatusReason"
 *	@author JacORB IDL compiler 
 */

public final class TpSvcAvailStatusReasonHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.fw.TpSvcAvailStatusReasonHelper.id(),"TpSvcAvailStatusReason",new String[]{"SVC_UNAVAILABLE_UNDEFINED","SVC_UNAVAILABLE_LOCAL_FAILURE","SVC_UNAVAILABLE_GATEWAY_FAILURE","SVC_UNAVAILABLE_OVERLOADED","SVC_UNAVAILABLE_CLOSED","SVC_UNAVAILABLE_NO_RESPONSE","SVC_UNAVAILABLE_SW_UPGRADE","SVC_AVAILABLE"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.fw.TpSvcAvailStatusReason s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.fw.TpSvcAvailStatusReason extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/fw/TpSvcAvailStatusReason:1.0";
	}
	public static TpSvcAvailStatusReason read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpSvcAvailStatusReason.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpSvcAvailStatusReason s)
	{
		out.write_long(s.value());
	}
}
