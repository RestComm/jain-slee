package org.csapi.cc.mmccs;
/**
 *	Generated from IDL definition of enum "TpMediaStreamEventType"
 *	@author JacORB IDL compiler 
 */

public final class TpMediaStreamEventTypeHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.cc.mmccs.TpMediaStreamEventTypeHelper.id(),"TpMediaStreamEventType",new String[]{"P_MEDIA_STREAM_ADDED","P_MEDIA_STREAM_SUBTRACTED"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cc.mmccs.TpMediaStreamEventType s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cc.mmccs.TpMediaStreamEventType extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/mmccs/TpMediaStreamEventType:1.0";
	}
	public static TpMediaStreamEventType read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpMediaStreamEventType.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpMediaStreamEventType s)
	{
		out.write_long(s.value());
	}
}
