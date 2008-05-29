package org.csapi.fw;
/**
 *	Generated from IDL definition of enum "TpAppAvailStatusReason"
 *	@author JacORB IDL compiler 
 */

public final class TpAppAvailStatusReasonHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.fw.TpAppAvailStatusReasonHelper.id(),"TpAppAvailStatusReason",new String[]{"APP_UNAVAILABLE_UNDEFINED","APP_UNAVAILABLE_LOCAL_FAILURE","APP_UNAVAILABLE_REMOTE_FAILURE","APP_UNAVAILABLE_OVERLOADED","APP_UNAVAILABLE_CLOSED","APP_UNAVAILABLE_NO_RESPONSE","APP_UNAVAILABLE_SW_UPGRADE","APP_AVAILABLE"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.fw.TpAppAvailStatusReason s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.fw.TpAppAvailStatusReason extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/fw/TpAppAvailStatusReason:1.0";
	}
	public static TpAppAvailStatusReason read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpAppAvailStatusReason.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpAppAvailStatusReason s)
	{
		out.write_long(s.value());
	}
}
