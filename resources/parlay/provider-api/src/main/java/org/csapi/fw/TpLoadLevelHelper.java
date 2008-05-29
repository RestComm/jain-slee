package org.csapi.fw;
/**
 *	Generated from IDL definition of enum "TpLoadLevel"
 *	@author JacORB IDL compiler 
 */

public final class TpLoadLevelHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.fw.TpLoadLevelHelper.id(),"TpLoadLevel",new String[]{"LOAD_LEVEL_NORMAL","LOAD_LEVEL_OVERLOAD","LOAD_LEVEL_SEVERE_OVERLOAD"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.fw.TpLoadLevel s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.fw.TpLoadLevel extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/fw/TpLoadLevel:1.0";
	}
	public static TpLoadLevel read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpLoadLevel.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpLoadLevel s)
	{
		out.write_long(s.value());
	}
}
