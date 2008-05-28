package org.csapi.cc;
/**
 *	Generated from IDL definition of enum "TpCallEventType"
 *	@author JacORB IDL compiler 
 */

public final class TpCallEventTypeHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.cc.TpCallEventTypeHelper.id(),"TpCallEventType",new String[]{"P_CALL_EVENT_UNDEFINED","P_CALL_EVENT_ORIGINATING_CALL_ATTEMPT","P_CALL_EVENT_ORIGINATING_CALL_ATTEMPT_AUTHORISED","P_CALL_EVENT_ADDRESS_COLLECTED","P_CALL_EVENT_ADDRESS_ANALYSED","P_CALL_EVENT_ORIGINATING_SERVICE_CODE","P_CALL_EVENT_ORIGINATING_RELEASE","P_CALL_EVENT_TERMINATING_CALL_ATTEMPT","P_CALL_EVENT_TERMINATING_CALL_ATTEMPT_AUTHORISED","P_CALL_EVENT_ALERTING","P_CALL_EVENT_ANSWER","P_CALL_EVENT_TERMINATING_RELEASE","P_CALL_EVENT_REDIRECTED","P_CALL_EVENT_TERMINATING_SERVICE_CODE","P_CALL_EVENT_QUEUED"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cc.TpCallEventType s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cc.TpCallEventType extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/TpCallEventType:1.0";
	}
	public static TpCallEventType read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpCallEventType.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpCallEventType s)
	{
		out.write_long(s.value());
	}
}
