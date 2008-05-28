package org.csapi.cs;
/**
 *	Generated from IDL definition of enum "TpSessionEndedCause"
 *	@author JacORB IDL compiler 
 */

public final class TpSessionEndedCauseHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.cs.TpSessionEndedCauseHelper.id(),"TpSessionEndedCause",new String[]{"P_CHS_CAUSE_UNDEFINED","P_CHS_CAUSE_TIMER_EXPIRED"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cs.TpSessionEndedCause s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cs.TpSessionEndedCause extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cs/TpSessionEndedCause:1.0";
	}
	public static TpSessionEndedCause read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpSessionEndedCause.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpSessionEndedCause s)
	{
		out.write_long(s.value());
	}
}
