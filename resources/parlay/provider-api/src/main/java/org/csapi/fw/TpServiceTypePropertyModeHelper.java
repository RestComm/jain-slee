package org.csapi.fw;
/**
 *	Generated from IDL definition of enum "TpServiceTypePropertyMode"
 *	@author JacORB IDL compiler 
 */

public final class TpServiceTypePropertyModeHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.fw.TpServiceTypePropertyModeHelper.id(),"TpServiceTypePropertyMode",new String[]{"NORMAL","MANDATORY","READONLY","MANDATORY_READONLY"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.fw.TpServiceTypePropertyMode s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.fw.TpServiceTypePropertyMode extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/fw/TpServiceTypePropertyMode:1.0";
	}
	public static TpServiceTypePropertyMode read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpServiceTypePropertyMode.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpServiceTypePropertyMode s)
	{
		out.write_long(s.value());
	}
}
