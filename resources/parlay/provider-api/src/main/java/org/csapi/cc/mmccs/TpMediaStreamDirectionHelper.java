package org.csapi.cc.mmccs;
/**
 *	Generated from IDL definition of enum "TpMediaStreamDirection"
 *	@author JacORB IDL compiler 
 */

public final class TpMediaStreamDirectionHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.cc.mmccs.TpMediaStreamDirectionHelper.id(),"TpMediaStreamDirection",new String[]{"P_SEND_ONLY","P_RECEIVE_ONLY","P_SEND_RECEIVE"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cc.mmccs.TpMediaStreamDirection s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cc.mmccs.TpMediaStreamDirection extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/mmccs/TpMediaStreamDirection:1.0";
	}
	public static TpMediaStreamDirection read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpMediaStreamDirection.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpMediaStreamDirection s)
	{
		out.write_long(s.value());
	}
}
