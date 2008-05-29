package org.csapi.fw;

/**
 *	Generated from IDL definition of union "TpLoadStatisticInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpLoadStatisticInfoHelper
{
	private static org.omg.CORBA.TypeCode _type;
	public static void insert (final org.omg.CORBA.Any any, final org.csapi.fw.TpLoadStatisticInfo s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.fw.TpLoadStatisticInfo extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/fw/TpLoadStatisticInfo:1.0";
	}
	public static TpLoadStatisticInfo read (org.omg.CORBA.portable.InputStream in)
	{
		TpLoadStatisticInfo result = new TpLoadStatisticInfo ();
		org.csapi.fw.TpLoadStatisticInfoType disc = org.csapi.fw.TpLoadStatisticInfoType.from_int(in.read_long());
		switch (disc.value ())
		{
			case org.csapi.fw.TpLoadStatisticInfoType._P_LOAD_STATISTICS_VALID:
			{
				org.csapi.fw.TpLoadStatisticData _var;
				_var=org.csapi.fw.TpLoadStatisticDataHelper.read(in);
				result.LoadStatisticData (_var);
				break;
			}
			case org.csapi.fw.TpLoadStatisticInfoType._P_LOAD_STATISTICS_INVALID:
			{
				org.csapi.fw.TpLoadStatisticError _var;
				_var=org.csapi.fw.TpLoadStatisticErrorHelper.read(in);
				result.LoadStatisticError (_var);
				break;
			}
		}
		return result;
	}
	public static void write (org.omg.CORBA.portable.OutputStream out, TpLoadStatisticInfo s)
	{
		out.write_long (s.discriminator().value ());
		switch (s.discriminator().value ())
		{
			case org.csapi.fw.TpLoadStatisticInfoType._P_LOAD_STATISTICS_VALID:
			{
				org.csapi.fw.TpLoadStatisticDataHelper.write(out,s.LoadStatisticData ());
				break;
			}
			case org.csapi.fw.TpLoadStatisticInfoType._P_LOAD_STATISTICS_INVALID:
			{
				org.csapi.fw.TpLoadStatisticErrorHelper.write(out,s.LoadStatisticError ());
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
			org.csapi.fw.TpLoadStatisticInfoTypeHelper.insert(label_any, org.csapi.fw.TpLoadStatisticInfoType.P_LOAD_STATISTICS_VALID);
			members[1] = new org.omg.CORBA.UnionMember ("LoadStatisticData", label_any, org.csapi.fw.TpLoadStatisticDataHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.fw.TpLoadStatisticInfoTypeHelper.insert(label_any, org.csapi.fw.TpLoadStatisticInfoType.P_LOAD_STATISTICS_INVALID);
			members[0] = new org.omg.CORBA.UnionMember ("LoadStatisticError", label_any, org.csapi.fw.TpLoadStatisticErrorHelper.type(),null);
			 _type = org.omg.CORBA.ORB.init().create_union_tc(id(),"TpLoadStatisticInfo",org.csapi.fw.TpLoadStatisticInfoTypeHelper.type(), members);
		}
		return _type;
	}
}
