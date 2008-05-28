package org.csapi.cc.gccs;
/**
 *	Generated from IDL definition of enum "TpCallFault"
 *	@author JacORB IDL compiler 
 */

public final class TpCallFaultHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.cc.gccs.TpCallFaultHelper.id(),"TpCallFault",new String[]{"P_CALL_FAULT_UNDEFINED","P_CALL_TIMEOUT_ON_RELEASE","P_CALL_TIMEOUT_ON_INTERRUPT"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cc.gccs.TpCallFault s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cc.gccs.TpCallFault extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/gccs/TpCallFault:1.0";
	}
	public static TpCallFault read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpCallFault.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpCallFault s)
	{
		out.write_long(s.value());
	}
}
