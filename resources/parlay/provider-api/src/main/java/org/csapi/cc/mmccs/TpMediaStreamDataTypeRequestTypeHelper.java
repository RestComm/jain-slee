package org.csapi.cc.mmccs;
/**
 *	Generated from IDL definition of enum "TpMediaStreamDataTypeRequestType"
 *	@author JacORB IDL compiler 
 */

public final class TpMediaStreamDataTypeRequestTypeHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.cc.mmccs.TpMediaStreamDataTypeRequestTypeHelper.id(),"TpMediaStreamDataTypeRequestType",new String[]{"P_AUDIO_CAPABILITIES","P_VIDEO_CAPABILITIES","P_DATA_CAPABILITIES"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cc.mmccs.TpMediaStreamDataTypeRequestType s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cc.mmccs.TpMediaStreamDataTypeRequestType extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/mmccs/TpMediaStreamDataTypeRequestType:1.0";
	}
	public static TpMediaStreamDataTypeRequestType read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpMediaStreamDataTypeRequestType.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpMediaStreamDataTypeRequestType s)
	{
		out.write_long(s.value());
	}
}
