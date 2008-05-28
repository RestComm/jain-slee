package org.csapi;
/**
 *	Generated from IDL definition of enum "TpAddressScreening"
 *	@author JacORB IDL compiler 
 */

public final class TpAddressScreeningHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.TpAddressScreeningHelper.id(),"TpAddressScreening",new String[]{"P_ADDRESS_SCREENING_UNDEFINED","P_ADDRESS_SCREENING_USER_VERIFIED_PASSED","P_ADDRESS_SCREENING_USER_NOT_VERIFIED","P_ADDRESS_SCREENING_USER_VERIFIED_FAILED","P_ADDRESS_SCREENING_NETWORK"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.TpAddressScreening s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.TpAddressScreening extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/TpAddressScreening:1.0";
	}
	public static TpAddressScreening read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpAddressScreening.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpAddressScreening s)
	{
		out.write_long(s.value());
	}
}
