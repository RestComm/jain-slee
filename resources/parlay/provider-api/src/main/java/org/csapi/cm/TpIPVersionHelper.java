package org.csapi.cm;
/**
 *	Generated from IDL definition of enum "TpIPVersion"
 *	@author JacORB IDL compiler 
 */

public final class TpIPVersionHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.cm.TpIPVersionHelper.id(),"TpIPVersion",new String[]{"VERSION_UNKNOWN","VERSION_IPV4","VERSION_IPV6"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cm.TpIPVersion s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cm.TpIPVersion extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cm/TpIPVersion:1.0";
	}
	public static TpIPVersion read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpIPVersion.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpIPVersion s)
	{
		out.write_long(s.value());
	}
}
