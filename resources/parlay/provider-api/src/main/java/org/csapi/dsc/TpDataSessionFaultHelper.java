package org.csapi.dsc;
/**
 *	Generated from IDL definition of enum "TpDataSessionFault"
 *	@author JacORB IDL compiler 
 */

public final class TpDataSessionFaultHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.dsc.TpDataSessionFaultHelper.id(),"TpDataSessionFault",new String[]{"P_DATA_SESSION_FAULT_UNDEFINED","P_DATA_SESSION_FAULT_USER_ABORTED","P_DATA_SESSION_TIMEOUT_ON_RELEASE","P_DATA_SESSION_TIMEOUT_ON_INTERRUPT"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.dsc.TpDataSessionFault s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.dsc.TpDataSessionFault extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/dsc/TpDataSessionFault:1.0";
	}
	public static TpDataSessionFault read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpDataSessionFault.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpDataSessionFault s)
	{
		out.write_long(s.value());
	}
}
