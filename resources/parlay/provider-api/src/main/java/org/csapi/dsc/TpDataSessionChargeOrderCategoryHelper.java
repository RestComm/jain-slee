package org.csapi.dsc;
/**
 *	Generated from IDL definition of enum "TpDataSessionChargeOrderCategory"
 *	@author JacORB IDL compiler 
 */

public final class TpDataSessionChargeOrderCategoryHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.dsc.TpDataSessionChargeOrderCategoryHelper.id(),"TpDataSessionChargeOrderCategory",new String[]{"P_DATA_SESSION_CHARGE_PER_VOLUME","P_DATA_SESSION_CHARGE_NETWORK"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.dsc.TpDataSessionChargeOrderCategory s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.dsc.TpDataSessionChargeOrderCategory extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/dsc/TpDataSessionChargeOrderCategory:1.0";
	}
	public static TpDataSessionChargeOrderCategory read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpDataSessionChargeOrderCategory.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpDataSessionChargeOrderCategory s)
	{
		out.write_long(s.value());
	}
}
