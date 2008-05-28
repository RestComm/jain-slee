package org.csapi;
/**
 *	Generated from IDL definition of enum "TpAddressPresentation"
 *	@author JacORB IDL compiler 
 */

public final class TpAddressPresentationHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.TpAddressPresentationHelper.id(),"TpAddressPresentation",new String[]{"P_ADDRESS_PRESENTATION_UNDEFINED","P_ADDRESS_PRESENTATION_ALLOWED","P_ADDRESS_PRESENTATION_RESTRICTED","P_ADDRESS_PRESENTATION_ADDRESS_NOT_AVAILABLE"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.TpAddressPresentation s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.TpAddressPresentation extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/TpAddressPresentation:1.0";
	}
	public static TpAddressPresentation read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpAddressPresentation.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpAddressPresentation s)
	{
		out.write_long(s.value());
	}
}
