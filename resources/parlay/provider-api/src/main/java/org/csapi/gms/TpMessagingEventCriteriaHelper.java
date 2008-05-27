package org.csapi.gms;

/**
 *	Generated from IDL definition of union "TpMessagingEventCriteria"
 *	@author JacORB IDL compiler 
 */

public final class TpMessagingEventCriteriaHelper
{
	private static org.omg.CORBA.TypeCode _type;
	public static void insert (final org.omg.CORBA.Any any, final org.csapi.gms.TpMessagingEventCriteria s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.gms.TpMessagingEventCriteria extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/gms/TpMessagingEventCriteria:1.0";
	}
	public static TpMessagingEventCriteria read (org.omg.CORBA.portable.InputStream in)
	{
		TpMessagingEventCriteria result = new TpMessagingEventCriteria ();
		org.csapi.gms.TpMessagingEventName disc = org.csapi.gms.TpMessagingEventName.from_int(in.read_long());
		switch (disc.value ())
		{
			case org.csapi.gms.TpMessagingEventName._P_EVENT_GMS_NEW_MESSAGE_ARRIVED:
			{
				org.csapi.gms.TpGMSNewMessageArrivedCriteria _var;
				_var=org.csapi.gms.TpGMSNewMessageArrivedCriteriaHelper.read(in);
				result.EventGMSNewMessageArrived (_var);
				break;
			}
			default:
			{
				short _var;
				_var=in.read_short();
				result.Dummy (_var);
			}
		}
		return result;
	}
	public static void write (org.omg.CORBA.portable.OutputStream out, TpMessagingEventCriteria s)
	{
		out.write_long (s.discriminator().value ());
		switch (s.discriminator().value ())
		{
			case org.csapi.gms.TpMessagingEventName._P_EVENT_GMS_NEW_MESSAGE_ARRIVED:
			{
				org.csapi.gms.TpGMSNewMessageArrivedCriteriaHelper.write(out,s.EventGMSNewMessageArrived ());
				break;
			}
			default:
			{
				out.write_short(s.Dummy ());
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
			org.csapi.gms.TpMessagingEventNameHelper.insert(label_any, org.csapi.gms.TpMessagingEventName.P_EVENT_GMS_NEW_MESSAGE_ARRIVED);
			members[1] = new org.omg.CORBA.UnionMember ("EventGMSNewMessageArrived", label_any, org.csapi.gms.TpGMSNewMessageArrivedCriteriaHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			label_any.insert_octet ((byte)0);
			members[0] = new org.omg.CORBA.UnionMember ("Dummy", label_any, org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.from_int(2)),null);
			 _type = org.omg.CORBA.ORB.init().create_union_tc(id(),"TpMessagingEventCriteria",org.csapi.gms.TpMessagingEventNameHelper.type(), members);
		}
		return _type;
	}
}
