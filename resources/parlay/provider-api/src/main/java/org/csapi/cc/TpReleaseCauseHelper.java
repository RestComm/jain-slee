package org.csapi.cc;
/**
 *	Generated from IDL definition of enum "TpReleaseCause"
 *	@author JacORB IDL compiler 
 */

public final class TpReleaseCauseHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.cc.TpReleaseCauseHelper.id(),"TpReleaseCause",new String[]{"P_UNDEFINED","P_USER_NOT_AVAILABLE","P_BUSY","P_NO_ANSWER","P_NOT_REACHABLE","P_ROUTING_FAILURE","P_PREMATURE_DISCONNECT","P_DISCONNECTED","P_CALL_RESTRICTED","P_UNAVAILABLE_RESOURCE","P_GENERAL_FAILURE","P_TIMER_EXPIRY","P_UNSUPPORTED_MEDIA"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cc.TpReleaseCause s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cc.TpReleaseCause extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/TpReleaseCause:1.0";
	}
	public static TpReleaseCause read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpReleaseCause.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpReleaseCause s)
	{
		out.write_long(s.value());
	}
}
