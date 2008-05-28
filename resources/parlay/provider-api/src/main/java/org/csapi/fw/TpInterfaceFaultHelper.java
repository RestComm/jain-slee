package org.csapi.fw;
/**
 *	Generated from IDL definition of enum "TpInterfaceFault"
 *	@author JacORB IDL compiler 
 */

public final class TpInterfaceFaultHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.fw.TpInterfaceFaultHelper.id(),"TpInterfaceFault",new String[]{"INTERFACE_FAULT_UNDEFINED","INTERFACE_FAULT_LOCAL_FAILURE","INTERFACE_FAULT_GATEWAY_FAILURE","INTERFACE_FAULT_PROTOCOL_ERROR"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.fw.TpInterfaceFault s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.fw.TpInterfaceFault extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/fw/TpInterfaceFault:1.0";
	}
	public static TpInterfaceFault read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpInterfaceFault.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpInterfaceFault s)
	{
		out.write_long(s.value());
	}
}
