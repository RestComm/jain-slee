package org.csapi.dsc;

/**
 *	Generated from IDL definition of union "TpDataSessionAdditionalReportInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpDataSessionAdditionalReportInfoHelper
{
	private static org.omg.CORBA.TypeCode _type;
	public static void insert (final org.omg.CORBA.Any any, final org.csapi.dsc.TpDataSessionAdditionalReportInfo s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.dsc.TpDataSessionAdditionalReportInfo extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/dsc/TpDataSessionAdditionalReportInfo:1.0";
	}
	public static TpDataSessionAdditionalReportInfo read (org.omg.CORBA.portable.InputStream in)
	{
		TpDataSessionAdditionalReportInfo result = new TpDataSessionAdditionalReportInfo ();
		org.csapi.dsc.TpDataSessionReportType disc = org.csapi.dsc.TpDataSessionReportType.from_int(in.read_long());
		switch (disc.value ())
		{
			case org.csapi.dsc.TpDataSessionReportType._P_DATA_SESSION_REPORT_DISCONNECT:
			{
				org.csapi.dsc.TpDataSessionReleaseCause _var;
				_var=org.csapi.dsc.TpDataSessionReleaseCauseHelper.read(in);
				result.DataSessionDisconnect (_var);
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
	public static void write (org.omg.CORBA.portable.OutputStream out, TpDataSessionAdditionalReportInfo s)
	{
		out.write_long (s.discriminator().value ());
		switch (s.discriminator().value ())
		{
			case org.csapi.dsc.TpDataSessionReportType._P_DATA_SESSION_REPORT_DISCONNECT:
			{
				org.csapi.dsc.TpDataSessionReleaseCauseHelper.write(out,s.DataSessionDisconnect ());
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
			org.csapi.dsc.TpDataSessionReportTypeHelper.insert(label_any, org.csapi.dsc.TpDataSessionReportType.P_DATA_SESSION_REPORT_DISCONNECT);
			members[1] = new org.omg.CORBA.UnionMember ("DataSessionDisconnect", label_any, org.csapi.dsc.TpDataSessionReleaseCauseHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			label_any.insert_octet ((byte)0);
			members[0] = new org.omg.CORBA.UnionMember ("Dummy", label_any, org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.from_int(2)),null);
			 _type = org.omg.CORBA.ORB.init().create_union_tc(id(),"TpDataSessionAdditionalReportInfo",org.csapi.dsc.TpDataSessionReportTypeHelper.type(), members);
		}
		return _type;
	}
}
