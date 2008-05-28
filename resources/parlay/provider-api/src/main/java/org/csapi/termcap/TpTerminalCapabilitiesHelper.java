package org.csapi.termcap;


/**
 *	Generated from IDL definition of struct "TpTerminalCapabilities"
 *	@author JacORB IDL compiler 
 */

public final class TpTerminalCapabilitiesHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.termcap.TpTerminalCapabilitiesHelper.id(),"TpTerminalCapabilities",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("TerminalCapabilities", org.csapi.TpStringHelper.type(), null),new org.omg.CORBA.StructMember("StatusCode", org.csapi.TpBooleanHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.termcap.TpTerminalCapabilities s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.termcap.TpTerminalCapabilities extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/termcap/TpTerminalCapabilities:1.0";
	}
	public static org.csapi.termcap.TpTerminalCapabilities read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.termcap.TpTerminalCapabilities result = new org.csapi.termcap.TpTerminalCapabilities();
		result.TerminalCapabilities=in.read_string();
		result.StatusCode=in.read_boolean();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.termcap.TpTerminalCapabilities s)
	{
		out.write_string(s.TerminalCapabilities);
		out.write_boolean(s.StatusCode);
	}
}
