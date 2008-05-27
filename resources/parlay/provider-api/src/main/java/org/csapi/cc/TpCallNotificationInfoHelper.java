package org.csapi.cc;


/**
 *	Generated from IDL definition of struct "TpCallNotificationInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpCallNotificationInfoHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.cc.TpCallNotificationInfoHelper.id(),"TpCallNotificationInfo",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("CallNotificationReportScope", org.csapi.cc.TpCallNotificationReportScopeHelper.type(), null),new org.omg.CORBA.StructMember("CallAppInfo", org.csapi.cc.TpCallAppInfoSetHelper.type(), null),new org.omg.CORBA.StructMember("CallEventInfo", org.csapi.cc.TpCallEventInfoHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cc.TpCallNotificationInfo s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cc.TpCallNotificationInfo extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/TpCallNotificationInfo:1.0";
	}
	public static org.csapi.cc.TpCallNotificationInfo read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.cc.TpCallNotificationInfo result = new org.csapi.cc.TpCallNotificationInfo();
		result.CallNotificationReportScope=org.csapi.cc.TpCallNotificationReportScopeHelper.read(in);
		result.CallAppInfo = org.csapi.cc.TpCallAppInfoSetHelper.read(in);
		result.CallEventInfo=org.csapi.cc.TpCallEventInfoHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.cc.TpCallNotificationInfo s)
	{
		org.csapi.cc.TpCallNotificationReportScopeHelper.write(out,s.CallNotificationReportScope);
		org.csapi.cc.TpCallAppInfoSetHelper.write(out,s.CallAppInfo);
		org.csapi.cc.TpCallEventInfoHelper.write(out,s.CallEventInfo);
	}
}
