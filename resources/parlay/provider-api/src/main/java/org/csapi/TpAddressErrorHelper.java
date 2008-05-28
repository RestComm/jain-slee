package org.csapi;
/**
 *	Generated from IDL definition of enum "TpAddressError"
 *	@author JacORB IDL compiler 
 */

public final class TpAddressErrorHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.TpAddressErrorHelper.id(),"TpAddressError",new String[]{"P_ADDRESS_INVALID_UNDEFINED","P_ADDRESS_INVALID_MISSING","P_ADDRESS_INVALID_MISSING_ELEMENT","P_ADDRESS_INVALID_OUT_OF_RANGE","P_ADDRESS_INVALID_INCOMPLETE","P_ADDRESS_INVALID_CANNOT_DECODE"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.TpAddressError s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.TpAddressError extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/TpAddressError:1.0";
	}
	public static TpAddressError read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpAddressError.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpAddressError s)
	{
		out.write_long(s.value());
	}
}
