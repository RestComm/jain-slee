package org.csapi.gms;

/**
 *	Generated from IDL definition of union "TpMessagingEventInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpMessagingEventInfoHelper
{
	private static org.omg.CORBA.TypeCode _type;
	public static void insert (final org.omg.CORBA.Any any, final org.csapi.gms.TpMessagingEventInfo s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.gms.TpMessagingEventInfo extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/gms/TpMessagingEventInfo:1.0";
	}
	public static TpMessagingEventInfo read (org.omg.CORBA.portable.InputStream in)
	{
		TpMessagingEventInfo result = new TpMessagingEventInfo ();
		org.csapi.gms.TpMessagingEventName disc = org.csapi.gms.TpMessagingEventName.from_int(in.read_long());
		switch (disc.value ())
		{
			case org.csapi.gms.TpMessagingEventName._P_EVENT_GMS_NAME_UNDEFINED:
			{
				java.lang.String _var;
				_var=in.read_string();
				result.EventNameUndefined (_var);
				break;
			}
			case org.csapi.gms.TpMessagingEventName._P_EVENT_GMS_NEW_MESSAGE_ARRIVED:
			{
				org.csapi.gms.TpGMSNewMessageArrivedInfo _var;
				_var=org.csapi.gms.TpGMSNewMessageArrivedInfoHelper.read(in);
				result.EventGMSNewMessageArrived (_var);
				break;
			}
		}
		return result;
	}
	public static void write (org.omg.CORBA.portable.OutputStream out, TpMessagingEventInfo s)
	{
		out.write_long (s.discriminator().value ());
		switch (s.discriminator().value ())
		{
			case org.csapi.gms.TpMessagingEventName._P_EVENT_GMS_NAME_UNDEFINED:
			{
				out.write_string(s.EventNameUndefined ());
				break;
			}
			case org.csapi.gms.TpMessagingEventName._P_EVENT_GMS_NEW_MESSAGE_ARRIVED:
			{
				org.csapi.gms.TpGMSNewMessageArrivedInfoHelper.write(out,s.EventGMSNewMessageArrived ());
				break;
			}
		}
	}
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			org.omg.CORBA.UnionMember[] members = new org.omg.CORBA.UnionMember[2];
			org.omg.CORBA.Any label_any;
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.gms.TpMessagingEventNameHelper.insert(label_any, org.csapi.gms.TpMessagingEventName.P_EVENT_GMS_NAME_UNDEFINED);
			members[1] = new org.omg.CORBA.UnionMember ("EventNameUndefined", label_any, org.omg.CORBA.ORB.init().create_string_tc(0),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.gms.TpMessagingEventNameHelper.insert(label_any, org.csapi.gms.TpMessagingEventName.P_EVENT_GMS_NEW_MESSAGE_ARRIVED);
			members[0] = new org.omg.CORBA.UnionMember ("EventGMSNewMessageArrived", label_any, org.csapi.gms.TpGMSNewMessageArrivedInfoHelper.type(),null);
			 _type = org.omg.CORBA.ORB.init().create_union_tc(id(),"TpMessagingEventInfo",org.csapi.gms.TpMessagingEventNameHelper.type(), members);
		}
		return _type;
	}
}
