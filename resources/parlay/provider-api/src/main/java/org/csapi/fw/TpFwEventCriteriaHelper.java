package org.csapi.fw;

/**
 *	Generated from IDL definition of union "TpFwEventCriteria"
 *	@author JacORB IDL compiler 
 */

public final class TpFwEventCriteriaHelper
{
	private static org.omg.CORBA.TypeCode _type;
	public static void insert (final org.omg.CORBA.Any any, final org.csapi.fw.TpFwEventCriteria s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.fw.TpFwEventCriteria extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/fw/TpFwEventCriteria:1.0";
	}
	public static TpFwEventCriteria read (org.omg.CORBA.portable.InputStream in)
	{
		TpFwEventCriteria result = new TpFwEventCriteria ();
		org.csapi.fw.TpFwEventName disc = org.csapi.fw.TpFwEventName.from_int(in.read_long());
		switch (disc.value ())
		{
			case org.csapi.fw.TpFwEventName._P_EVENT_FW_NAME_UNDEFINED:
			{
				java.lang.String _var;
				_var=in.read_string();
				result.EventNameUndefined (_var);
				break;
			}
			case org.csapi.fw.TpFwEventName._P_EVENT_FW_SERVICE_AVAILABLE:
			{
				java.lang.String[] _var;
				_var = org.csapi.fw.TpServiceTypeNameListHelper.read(in);
				result.ServiceTypeNameList (_var);
				break;
			}
			case org.csapi.fw.TpFwEventName._P_EVENT_FW_SERVICE_UNAVAILABLE:
			{
				java.lang.String[] _var;
				_var = org.csapi.fw.TpServiceTypeNameListHelper.read(in);
				result.UnavailableServiceTypeNameList (_var);
				break;
			}
		}
		return result;
	}
	public static void write (org.omg.CORBA.portable.OutputStream out, TpFwEventCriteria s)
	{
		out.write_long (s.discriminator().value ());
		switch (s.discriminator().value ())
		{
			case org.csapi.fw.TpFwEventName._P_EVENT_FW_NAME_UNDEFINED:
			{
				out.write_string(s.EventNameUndefined ());
				break;
			}
			case org.csapi.fw.TpFwEventName._P_EVENT_FW_SERVICE_AVAILABLE:
			{
				org.csapi.fw.TpServiceTypeNameListHelper.write(out,s.ServiceTypeNameList ());
				break;
			}
			case org.csapi.fw.TpFwEventName._P_EVENT_FW_SERVICE_UNAVAILABLE:
			{
				org.csapi.fw.TpServiceTypeNameListHelper.write(out,s.UnavailableServiceTypeNameList ());
				break;
			}
		}
	}
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			org.omg.CORBA.UnionMember[] members = new org.omg.CORBA.UnionMember[3];
			org.omg.CORBA.Any label_any;
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.fw.TpFwEventNameHelper.insert(label_any, org.csapi.fw.TpFwEventName.P_EVENT_FW_NAME_UNDEFINED);
			members[2] = new org.omg.CORBA.UnionMember ("EventNameUndefined", label_any, org.omg.CORBA.ORB.init().create_string_tc(0),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.fw.TpFwEventNameHelper.insert(label_any, org.csapi.fw.TpFwEventName.P_EVENT_FW_SERVICE_AVAILABLE);
			members[1] = new org.omg.CORBA.UnionMember ("ServiceTypeNameList", label_any, org.omg.CORBA.ORB.init().create_sequence_tc(0, org.csapi.fw.TpServiceTypeNameHelper.type()),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.fw.TpFwEventNameHelper.insert(label_any, org.csapi.fw.TpFwEventName.P_EVENT_FW_SERVICE_UNAVAILABLE);
			members[0] = new org.omg.CORBA.UnionMember ("UnavailableServiceTypeNameList", label_any, org.omg.CORBA.ORB.init().create_sequence_tc(0, org.csapi.fw.TpServiceTypeNameHelper.type()),null);
			 _type = org.omg.CORBA.ORB.init().create_union_tc(id(),"TpFwEventCriteria",org.csapi.fw.TpFwEventNameHelper.type(), members);
		}
		return _type;
	}
}
