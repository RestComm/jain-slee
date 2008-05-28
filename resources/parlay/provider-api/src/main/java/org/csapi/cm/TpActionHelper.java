package org.csapi.cm;
/**
 *	Generated from IDL definition of enum "TpAction"
 *	@author JacORB IDL compiler 
 */

public final class TpActionHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.cm.TpActionHelper.id(),"TpAction",new String[]{"DROP","TRANSMIT","RESHAPE","REMARK"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cm.TpAction s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cm.TpAction extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cm/TpAction:1.0";
	}
	public static TpAction read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpAction.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpAction s)
	{
		out.write_long(s.value());
	}
}
