package org.csapi;
/**
 *	Generated from IDL definition of enum "TpAddressPlan"
 *	@author JacORB IDL compiler 
 */

public final class TpAddressPlanHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.TpAddressPlanHelper.id(),"TpAddressPlan",new String[]{"P_ADDRESS_PLAN_NOT_PRESENT","P_ADDRESS_PLAN_UNDEFINED","P_ADDRESS_PLAN_IP","P_ADDRESS_PLAN_MULTICAST","P_ADDRESS_PLAN_UNICAST","P_ADDRESS_PLAN_E164","P_ADDRESS_PLAN_AESA","P_ADDRESS_PLAN_URL","P_ADDRESS_PLAN_NSAP","P_ADDRESS_PLAN_SMTP","P_ADDRESS_PLAN_MSMAIL","P_ADDRESS_PLAN_X400","P_ADDRESS_PLAN_SIP","P_ADDRESS_PLAN_ANY","P_ADDRESS_PLAN_NATIONAL"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.TpAddressPlan s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.TpAddressPlan extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/TpAddressPlan:1.0";
	}
	public static TpAddressPlan read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpAddressPlan.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpAddressPlan s)
	{
		out.write_long(s.value());
	}
}
