package org.csapi.cc.gccs;

/**
 *	Generated from IDL definition of union "TpCallAdditionalReportInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpCallAdditionalReportInfoHelper
{
	private static org.omg.CORBA.TypeCode _type;
	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cc.gccs.TpCallAdditionalReportInfo s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cc.gccs.TpCallAdditionalReportInfo extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/gccs/TpCallAdditionalReportInfo:1.0";
	}
	public static TpCallAdditionalReportInfo read (org.omg.CORBA.portable.InputStream in)
	{
		TpCallAdditionalReportInfo result = new TpCallAdditionalReportInfo ();
		org.csapi.cc.gccs.TpCallReportType disc = org.csapi.cc.gccs.TpCallReportType.from_int(in.read_long());
		switch (disc.value ())
		{
			case org.csapi.cc.gccs.TpCallReportType._P_CALL_REPORT_BUSY:
			{
				org.csapi.cc.gccs.TpCallReleaseCause _var;
				_var=org.csapi.cc.gccs.TpCallReleaseCauseHelper.read(in);
				result.Busy (_var);
				break;
			}
			case org.csapi.cc.gccs.TpCallReportType._P_CALL_REPORT_DISCONNECT:
			{
				org.csapi.cc.gccs.TpCallReleaseCause _var;
				_var=org.csapi.cc.gccs.TpCallReleaseCauseHelper.read(in);
				result.CallDisconnect (_var);
				break;
			}
			case org.csapi.cc.gccs.TpCallReportType._P_CALL_REPORT_REDIRECTED:
			{
				org.csapi.TpAddress _var;
				_var=org.csapi.TpAddressHelper.read(in);
				result.ForwardAddress (_var);
				break;
			}
			case org.csapi.cc.gccs.TpCallReportType._P_CALL_REPORT_SERVICE_CODE:
			{
				org.csapi.cc.TpCallServiceCode _var;
				_var=org.csapi.cc.TpCallServiceCodeHelper.read(in);
				result.ServiceCode (_var);
				break;
			}
			case org.csapi.cc.gccs.TpCallReportType._P_CALL_REPORT_ROUTING_FAILURE:
			{
				org.csapi.cc.gccs.TpCallReleaseCause _var;
				_var=org.csapi.cc.gccs.TpCallReleaseCauseHelper.read(in);
				result.RoutingFailure (_var);
				break;
			}
			case org.csapi.cc.gccs.TpCallReportType._P_CALL_REPORT_QUEUED:
			{
				java.lang.String _var;
				_var=in.read_string();
				result.QueueStatus (_var);
				break;
			}
			case org.csapi.cc.gccs.TpCallReportType._P_CALL_REPORT_NOT_REACHABLE:
			{
				org.csapi.cc.gccs.TpCallReleaseCause _var;
				_var=org.csapi.cc.gccs.TpCallReleaseCauseHelper.read(in);
				result.NotReachable (_var);
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
	public static void write (org.omg.CORBA.portable.OutputStream out, TpCallAdditionalReportInfo s)
	{
		out.write_long (s.discriminator().value ());
		switch (s.discriminator().value ())
		{
			case org.csapi.cc.gccs.TpCallReportType._P_CALL_REPORT_BUSY:
			{
				org.csapi.cc.gccs.TpCallReleaseCauseHelper.write(out,s.Busy ());
				break;
			}
			case org.csapi.cc.gccs.TpCallReportType._P_CALL_REPORT_DISCONNECT:
			{
				org.csapi.cc.gccs.TpCallReleaseCauseHelper.write(out,s.CallDisconnect ());
				break;
			}
			case org.csapi.cc.gccs.TpCallReportType._P_CALL_REPORT_REDIRECTED:
			{
				org.csapi.TpAddressHelper.write(out,s.ForwardAddress ());
				break;
			}
			case org.csapi.cc.gccs.TpCallReportType._P_CALL_REPORT_SERVICE_CODE:
			{
				org.csapi.cc.TpCallServiceCodeHelper.write(out,s.ServiceCode ());
				break;
			}
			case org.csapi.cc.gccs.TpCallReportType._P_CALL_REPORT_ROUTING_FAILURE:
			{
				org.csapi.cc.gccs.TpCallReleaseCauseHelper.write(out,s.RoutingFailure ());
				break;
			}
			case org.csapi.cc.gccs.TpCallReportType._P_CALL_REPORT_QUEUED:
			{
				out.write_string(s.QueueStatus ());
				break;
			}
			case org.csapi.cc.gccs.TpCallReportType._P_CALL_REPORT_NOT_REACHABLE:
			{
				org.csapi.cc.gccs.TpCallReleaseCauseHelper.write(out,s.NotReachable ());
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
			org.omg.CORBA.UnionMember[] members = new org.omg.CORBA.UnionMember[8];
			org.omg.CORBA.Any label_any;
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.cc.gccs.TpCallReportTypeHelper.insert(label_any, org.csapi.cc.gccs.TpCallReportType.P_CALL_REPORT_BUSY);
			members[7] = new org.omg.CORBA.UnionMember ("Busy", label_any, org.csapi.cc.gccs.TpCallReleaseCauseHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.cc.gccs.TpCallReportTypeHelper.insert(label_any, org.csapi.cc.gccs.TpCallReportType.P_CALL_REPORT_DISCONNECT);
			members[6] = new org.omg.CORBA.UnionMember ("CallDisconnect", label_any, org.csapi.cc.gccs.TpCallReleaseCauseHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.cc.gccs.TpCallReportTypeHelper.insert(label_any, org.csapi.cc.gccs.TpCallReportType.P_CALL_REPORT_REDIRECTED);
			members[5] = new org.omg.CORBA.UnionMember ("ForwardAddress", label_any, org.csapi.TpAddressHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.cc.gccs.TpCallReportTypeHelper.insert(label_any, org.csapi.cc.gccs.TpCallReportType.P_CALL_REPORT_SERVICE_CODE);
			members[4] = new org.omg.CORBA.UnionMember ("ServiceCode", label_any, org.csapi.cc.TpCallServiceCodeHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.cc.gccs.TpCallReportTypeHelper.insert(label_any, org.csapi.cc.gccs.TpCallReportType.P_CALL_REPORT_ROUTING_FAILURE);
			members[3] = new org.omg.CORBA.UnionMember ("RoutingFailure", label_any, org.csapi.cc.gccs.TpCallReleaseCauseHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.cc.gccs.TpCallReportTypeHelper.insert(label_any, org.csapi.cc.gccs.TpCallReportType.P_CALL_REPORT_QUEUED);
			members[2] = new org.omg.CORBA.UnionMember ("QueueStatus", label_any, org.omg.CORBA.ORB.init().create_string_tc(0),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.cc.gccs.TpCallReportTypeHelper.insert(label_any, org.csapi.cc.gccs.TpCallReportType.P_CALL_REPORT_NOT_REACHABLE);
			members[1] = new org.omg.CORBA.UnionMember ("NotReachable", label_any, org.csapi.cc.gccs.TpCallReleaseCauseHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			label_any.insert_octet ((byte)0);
			members[0] = new org.omg.CORBA.UnionMember ("Dummy", label_any, org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.from_int(2)),null);
			 _type = org.omg.CORBA.ORB.init().create_union_tc(id(),"TpCallAdditionalReportInfo",org.csapi.cc.gccs.TpCallReportTypeHelper.type(), members);
		}
		return _type;
	}
}
