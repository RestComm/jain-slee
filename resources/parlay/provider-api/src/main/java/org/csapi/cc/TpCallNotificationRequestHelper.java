package org.csapi.cc;


/**
 *	Generated from IDL definition of struct "TpCallNotificationRequest"
 *	@author JacORB IDL compiler 
 */

public final class TpCallNotificationRequestHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.cc.TpCallNotificationRequestHelper.id(),"TpCallNotificationRequest",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("CallNotificationScope", org.csapi.cc.TpCallNotificationScopeHelper.type(), null),new org.omg.CORBA.StructMember("CallEventsRequested", org.csapi.cc.TpCallEventRequestSetHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cc.TpCallNotificationRequest s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cc.TpCallNotificationRequest extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/TpCallNotificationRequest:1.0";
	}
	public static org.csapi.cc.TpCallNotificationRequest read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.cc.TpCallNotificationRequest result = new org.csapi.cc.TpCallNotificationRequest();
		result.CallNotificationScope=org.csapi.cc.TpCallNotificationScopeHelper.read(in);
		result.CallEventsRequested = org.csapi.cc.TpCallEventRequestSetHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.cc.TpCallNotificationRequest s)
	{
		org.csapi.cc.TpCallNotificationScopeHelper.write(out,s.CallNotificationScope);
		org.csapi.cc.TpCallEventRequestSetHelper.write(out,s.CallEventsRequested);
	}
}
