package org.csapi.cm;
/**
 *	Generated from IDL definition of enum "TpIPv4AddType"
 *	@author JacORB IDL compiler 
 */

public final class TpIPv4AddTypeHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.cm.TpIPv4AddTypeHelper.id(),"TpIPv4AddType",new String[]{"IPV4_ADD_CLASS_A","IPV4_ADD_CLASS_B","IPV4_ADD_CLASS_C","IPV4_ADD_CLASS_D","IPV4_ADD_CLASS_E"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cm.TpIPv4AddType s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cm.TpIPv4AddType extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cm/TpIPv4AddType:1.0";
	}
	public static TpIPv4AddType read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpIPv4AddType.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpIPv4AddType s)
	{
		out.write_long(s.value());
	}
}
