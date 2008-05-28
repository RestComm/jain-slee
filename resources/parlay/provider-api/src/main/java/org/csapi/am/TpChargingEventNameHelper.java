package org.csapi.am;
/**
 *	Generated from IDL definition of enum "TpChargingEventName"
 *	@author JacORB IDL compiler 
 */

public final class TpChargingEventNameHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.am.TpChargingEventNameHelper.id(),"TpChargingEventName",new String[]{"P_AM_CHARGING","P_AM_RECHARGING","P_AM_ACCOUNT_LOW","P_AM_ACCOUNT_ZERO","P_AM_ACCOUNT_DISABLED"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.am.TpChargingEventName s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.am.TpChargingEventName extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/am/TpChargingEventName:1.0";
	}
	public static TpChargingEventName read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpChargingEventName.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpChargingEventName s)
	{
		out.write_long(s.value());
	}
}
