package org.csapi.mm;
/**
 *	Generated from IDL definition of enum "TpLocationResponseIndicator"
 *	@author JacORB IDL compiler 
 */

public final class TpLocationResponseIndicatorHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.mm.TpLocationResponseIndicatorHelper.id(),"TpLocationResponseIndicator",new String[]{"P_M_NO_DELAY","P_M_LOW_DELAY","P_M_DELAY_TOLERANT","P_M_USE_TIMER_VALUE"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.mm.TpLocationResponseIndicator s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.mm.TpLocationResponseIndicator extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/mm/TpLocationResponseIndicator:1.0";
	}
	public static TpLocationResponseIndicator read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpLocationResponseIndicator.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpLocationResponseIndicator s)
	{
		out.write_long(s.value());
	}
}
