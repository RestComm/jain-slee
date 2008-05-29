package org.csapi.fw;
/**
 *	Generated from IDL definition of enum "TpDomainIDType"
 *	@author JacORB IDL compiler 
 */

public final class TpDomainIDTypeHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.fw.TpDomainIDTypeHelper.id(),"TpDomainIDType",new String[]{"P_FW","P_CLIENT_APPLICATION","P_ENT_OP","P_SERVICE_INSTANCE","P_SERVICE_SUPPLIER"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.fw.TpDomainIDType s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.fw.TpDomainIDType extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/fw/TpDomainIDType:1.0";
	}
	public static TpDomainIDType read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpDomainIDType.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpDomainIDType s)
	{
		out.write_long(s.value());
	}
}
