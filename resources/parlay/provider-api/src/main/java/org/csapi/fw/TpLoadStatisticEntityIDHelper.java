package org.csapi.fw;

/**
 *	Generated from IDL definition of union "TpLoadStatisticEntityID"
 *	@author JacORB IDL compiler 
 */

public final class TpLoadStatisticEntityIDHelper
{
	private static org.omg.CORBA.TypeCode _type;
	public static void insert (final org.omg.CORBA.Any any, final org.csapi.fw.TpLoadStatisticEntityID s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.fw.TpLoadStatisticEntityID extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/fw/TpLoadStatisticEntityID:1.0";
	}
	public static TpLoadStatisticEntityID read (org.omg.CORBA.portable.InputStream in)
	{
		TpLoadStatisticEntityID result = new TpLoadStatisticEntityID ();
		org.csapi.fw.TpLoadStatisticEntityType disc = org.csapi.fw.TpLoadStatisticEntityType.from_int(in.read_long());
		switch (disc.value ())
		{
			case org.csapi.fw.TpLoadStatisticEntityType._P_LOAD_STATISTICS_FW_TYPE:
			{
				java.lang.String _var;
				_var=in.read_string();
				result.FrameworkID (_var);
				break;
			}
			case org.csapi.fw.TpLoadStatisticEntityType._P_LOAD_STATISTICS_SVC_TYPE:
			{
				java.lang.String _var;
				_var=in.read_string();
				result.ServiceID (_var);
				break;
			}
			case org.csapi.fw.TpLoadStatisticEntityType._P_LOAD_STATISTICS_APP_TYPE:
			{
				java.lang.String _var;
				_var=in.read_string();
				result.ClientAppID (_var);
				break;
			}
		}
		return result;
	}
	public static void write (org.omg.CORBA.portable.OutputStream out, TpLoadStatisticEntityID s)
	{
		out.write_long (s.discriminator().value ());
		switch (s.discriminator().value ())
		{
			case org.csapi.fw.TpLoadStatisticEntityType._P_LOAD_STATISTICS_FW_TYPE:
			{
				out.write_string(s.FrameworkID ());
				break;
			}
			case org.csapi.fw.TpLoadStatisticEntityType._P_LOAD_STATISTICS_SVC_TYPE:
			{
				out.write_string(s.ServiceID ());
				break;
			}
			case org.csapi.fw.TpLoadStatisticEntityType._P_LOAD_STATISTICS_APP_TYPE:
			{
				out.write_string(s.ClientAppID ());
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
			org.csapi.fw.TpLoadStatisticEntityTypeHelper.insert(label_any, org.csapi.fw.TpLoadStatisticEntityType.P_LOAD_STATISTICS_FW_TYPE);
			members[2] = new org.omg.CORBA.UnionMember ("FrameworkID", label_any, org.omg.CORBA.ORB.init().create_string_tc(0),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.fw.TpLoadStatisticEntityTypeHelper.insert(label_any, org.csapi.fw.TpLoadStatisticEntityType.P_LOAD_STATISTICS_SVC_TYPE);
			members[1] = new org.omg.CORBA.UnionMember ("ServiceID", label_any, org.omg.CORBA.ORB.init().create_string_tc(0),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.fw.TpLoadStatisticEntityTypeHelper.insert(label_any, org.csapi.fw.TpLoadStatisticEntityType.P_LOAD_STATISTICS_APP_TYPE);
			members[0] = new org.omg.CORBA.UnionMember ("ClientAppID", label_any, org.omg.CORBA.ORB.init().create_string_tc(0),null);
			 _type = org.omg.CORBA.ORB.init().create_union_tc(id(),"TpLoadStatisticEntityID",org.csapi.fw.TpLoadStatisticEntityTypeHelper.type(), members);
		}
		return _type;
	}
}
