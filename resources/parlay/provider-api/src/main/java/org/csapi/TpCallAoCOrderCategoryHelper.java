package org.csapi;
/**
 *	Generated from IDL definition of enum "TpCallAoCOrderCategory"
 *	@author JacORB IDL compiler 
 */

public final class TpCallAoCOrderCategoryHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.TpCallAoCOrderCategoryHelper.id(),"TpCallAoCOrderCategory",new String[]{"P_CHARGE_ADVICE_INFO","P_CHARGE_PER_TIME","P_CHARGE_NETWORK"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.TpCallAoCOrderCategory s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.TpCallAoCOrderCategory extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/TpCallAoCOrderCategory:1.0";
	}
	public static TpCallAoCOrderCategory read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpCallAoCOrderCategory.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpCallAoCOrderCategory s)
	{
		out.write_long(s.value());
	}
}
