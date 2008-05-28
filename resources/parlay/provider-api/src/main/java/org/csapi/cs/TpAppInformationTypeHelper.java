package org.csapi.cs;
/**
 *	Generated from IDL definition of enum "TpAppInformationType"
 *	@author JacORB IDL compiler 
 */

public final class TpAppInformationTypeHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.cs.TpAppInformationTypeHelper.id(),"TpAppInformationType",new String[]{"P_APP_INF_TIMESTAMP"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cs.TpAppInformationType s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cs.TpAppInformationType extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cs/TpAppInformationType:1.0";
	}
	public static TpAppInformationType read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpAppInformationType.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpAppInformationType s)
	{
		out.write_long(s.value());
	}
}
