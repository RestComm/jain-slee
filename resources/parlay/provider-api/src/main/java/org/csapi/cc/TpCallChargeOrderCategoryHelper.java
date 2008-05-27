package org.csapi.cc;
/**
 *	Generated from IDL definition of enum "TpCallChargeOrderCategory"
 *	@author JacORB IDL compiler 
 */

public final class TpCallChargeOrderCategoryHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.cc.TpCallChargeOrderCategoryHelper.id(),"TpCallChargeOrderCategory",new String[]{"P_CALL_CHARGE_TRANSPARENT","P_CALL_CHARGE_PREDEFINED_SET"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cc.TpCallChargeOrderCategory s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cc.TpCallChargeOrderCategory extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/TpCallChargeOrderCategory:1.0";
	}
	public static TpCallChargeOrderCategory read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpCallChargeOrderCategory.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpCallChargeOrderCategory s)
	{
		out.write_long(s.value());
	}
}
