package org.csapi.cc;


/**
 *	Generated from IDL definition of struct "TpCallNotificationReportScope"
 *	@author JacORB IDL compiler 
 */

public final class TpCallNotificationReportScopeHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.cc.TpCallNotificationReportScopeHelper.id(),"TpCallNotificationReportScope",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("DestinationAddress", org.csapi.TpAddressHelper.type(), null),new org.omg.CORBA.StructMember("OriginatingAddress", org.csapi.TpAddressHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cc.TpCallNotificationReportScope s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cc.TpCallNotificationReportScope extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/TpCallNotificationReportScope:1.0";
	}
	public static org.csapi.cc.TpCallNotificationReportScope read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.cc.TpCallNotificationReportScope result = new org.csapi.cc.TpCallNotificationReportScope();
		result.DestinationAddress=org.csapi.TpAddressHelper.read(in);
		result.OriginatingAddress=org.csapi.TpAddressHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.cc.TpCallNotificationReportScope s)
	{
		org.csapi.TpAddressHelper.write(out,s.DestinationAddress);
		org.csapi.TpAddressHelper.write(out,s.OriginatingAddress);
	}
}
