package org.csapi.cc.gccs;

/**
 *	Generated from IDL definition of union "TpCallAdditionalReportCriteria"
 *	@author JacORB IDL compiler 
 */

public final class TpCallAdditionalReportCriteriaHelper
{
	private static org.omg.CORBA.TypeCode _type;
	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cc.gccs.TpCallAdditionalReportCriteria s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cc.gccs.TpCallAdditionalReportCriteria extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/gccs/TpCallAdditionalReportCriteria:1.0";
	}
	public static TpCallAdditionalReportCriteria read (org.omg.CORBA.portable.InputStream in)
	{
		TpCallAdditionalReportCriteria result = new TpCallAdditionalReportCriteria ();
		org.csapi.cc.gccs.TpCallReportType disc = org.csapi.cc.gccs.TpCallReportType.from_int(in.read_long());
		switch (disc.value ())
		{
			case org.csapi.cc.gccs.TpCallReportType._P_CALL_REPORT_NO_ANSWER:
			{
				int _var;
				_var=in.read_long();
				result.NoAnswerDuration (_var);
				break;
			}
			case org.csapi.cc.gccs.TpCallReportType._P_CALL_REPORT_SERVICE_CODE:
			{
				org.csapi.cc.TpCallServiceCode _var;
				_var=org.csapi.cc.TpCallServiceCodeHelper.read(in);
				result.ServiceCode (_var);
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
	public static void write (org.omg.CORBA.portable.OutputStream out, TpCallAdditionalReportCriteria s)
	{
		out.write_long (s.discriminator().value ());
		switch (s.discriminator().value ())
		{
			case org.csapi.cc.gccs.TpCallReportType._P_CALL_REPORT_NO_ANSWER:
			{
				out.write_long(s.NoAnswerDuration ());
				break;
			}
			case org.csapi.cc.gccs.TpCallReportType._P_CALL_REPORT_SERVICE_CODE:
			{
				org.csapi.cc.TpCallServiceCodeHelper.write(out,s.ServiceCode ());
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
			org.omg.CORBA.UnionMember[] members = new org.omg.CORBA.UnionMember[3];
			org.omg.CORBA.Any label_any;
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.cc.gccs.TpCallReportTypeHelper.insert(label_any, org.csapi.cc.gccs.TpCallReportType.P_CALL_REPORT_NO_ANSWER);
			members[2] = new org.omg.CORBA.UnionMember ("NoAnswerDuration", label_any, org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.from_int(3)),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.cc.gccs.TpCallReportTypeHelper.insert(label_any, org.csapi.cc.gccs.TpCallReportType.P_CALL_REPORT_SERVICE_CODE);
			members[1] = new org.omg.CORBA.UnionMember ("ServiceCode", label_any, org.csapi.cc.TpCallServiceCodeHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			label_any.insert_octet ((byte)0);
			members[0] = new org.omg.CORBA.UnionMember ("Dummy", label_any, org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.from_int(2)),null);
			 _type = org.omg.CORBA.ORB.init().create_union_tc(id(),"TpCallAdditionalReportCriteria",org.csapi.cc.gccs.TpCallReportTypeHelper.type(), members);
		}
		return _type;
	}
}
