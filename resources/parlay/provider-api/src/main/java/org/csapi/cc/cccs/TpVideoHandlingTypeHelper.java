package org.csapi.cc.cccs;
/**
 *	Generated from IDL definition of enum "TpVideoHandlingType"
 *	@author JacORB IDL compiler 
 */

public final class TpVideoHandlingTypeHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.cc.cccs.TpVideoHandlingTypeHelper.id(),"TpVideoHandlingType",new String[]{"P_MIXED_VIDEO","P_SWITCHED_VIDEO_CHAIR_CONTROLLED","P_SWITCHED_VIDEO_VOICE_CONTROLLED"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cc.cccs.TpVideoHandlingType s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cc.cccs.TpVideoHandlingType extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/cccs/TpVideoHandlingType:1.0";
	}
	public static TpVideoHandlingType read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpVideoHandlingType.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpVideoHandlingType s)
	{
		out.write_long(s.value());
	}
}
