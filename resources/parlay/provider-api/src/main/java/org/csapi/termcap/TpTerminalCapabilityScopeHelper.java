package org.csapi.termcap;


/**
 *	Generated from IDL definition of struct "TpTerminalCapabilityScope"
 *	@author JacORB IDL compiler 
 */

public final class TpTerminalCapabilityScopeHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.termcap.TpTerminalCapabilityScopeHelper.id(),"TpTerminalCapabilityScope",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("ScopeType", org.csapi.termcap.TpTerminalCapabilityScopeTypeHelper.type(), null),new org.omg.CORBA.StructMember("Scope", org.csapi.TpStringHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.termcap.TpTerminalCapabilityScope s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.termcap.TpTerminalCapabilityScope extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/termcap/TpTerminalCapabilityScope:1.0";
	}
	public static org.csapi.termcap.TpTerminalCapabilityScope read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.termcap.TpTerminalCapabilityScope result = new org.csapi.termcap.TpTerminalCapabilityScope();
		result.ScopeType=org.csapi.termcap.TpTerminalCapabilityScopeTypeHelper.read(in);
		result.Scope=in.read_string();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.termcap.TpTerminalCapabilityScope s)
	{
		org.csapi.termcap.TpTerminalCapabilityScopeTypeHelper.write(out,s.ScopeType);
		out.write_string(s.Scope);
	}
}
