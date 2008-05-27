package org.csapi.cc;
/**
 *	Generated from IDL definition of enum "TpCallNetworkAccessType"
 *	@author JacORB IDL compiler 
 */

public final class TpCallNetworkAccessTypeHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.cc.TpCallNetworkAccessTypeHelper.id(),"TpCallNetworkAccessType",new String[]{"P_CALL_NETWORK_ACCESS_TYPE_UNKNOWN","P_CALL_NETWORK_ACCESS_TYPE_POT","P_CALL_NETWORK_ACCESS_TYPE_ISDN","P_CALL_NETWORK_ACCESS_TYPE_DIALUPINTERNET","P_CALL_NETWORK_ACCESS_TYPE_XDSL","P_CALL_NETWORK_ACCESS_TYPE_WIRELESS"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cc.TpCallNetworkAccessType s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cc.TpCallNetworkAccessType extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/TpCallNetworkAccessType:1.0";
	}
	public static TpCallNetworkAccessType read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpCallNetworkAccessType.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpCallNetworkAccessType s)
	{
		out.write_long(s.value());
	}
}
