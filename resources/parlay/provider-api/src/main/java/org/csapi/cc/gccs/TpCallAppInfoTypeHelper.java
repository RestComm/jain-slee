package org.csapi.cc.gccs;
/**
 *	Generated from IDL definition of enum "TpCallAppInfoType"
 *	@author JacORB IDL compiler 
 */

public final class TpCallAppInfoTypeHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.cc.gccs.TpCallAppInfoTypeHelper.id(),"TpCallAppInfoType",new String[]{"P_CALL_APP_UNDEFINED","P_CALL_APP_ALERTING_MECHANISM","P_CALL_APP_NETWORK_ACCESS_TYPE","P_CALL_APP_TELE_SERVICE","P_CALL_APP_BEARER_SERVICE","P_CALL_APP_PARTY_CATEGORY","P_CALL_APP_PRESENTATION_ADDRESS","P_CALL_APP_GENERIC_INFO","P_CALL_APP_ADDITIONAL_ADDRESS"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cc.gccs.TpCallAppInfoType s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cc.gccs.TpCallAppInfoType extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/gccs/TpCallAppInfoType:1.0";
	}
	public static TpCallAppInfoType read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpCallAppInfoType.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpCallAppInfoType s)
	{
		out.write_long(s.value());
	}
}
