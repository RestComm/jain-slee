package org.csapi;

/**
 *	Generated from IDL definition of union "TpAoCOrder"
 *	@author JacORB IDL compiler 
 */

public final class TpAoCOrderHelper
{
	private static org.omg.CORBA.TypeCode _type;
	public static void insert (final org.omg.CORBA.Any any, final org.csapi.TpAoCOrder s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.TpAoCOrder extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/TpAoCOrder:1.0";
	}
	public static TpAoCOrder read (org.omg.CORBA.portable.InputStream in)
	{
		TpAoCOrder result = new TpAoCOrder ();
		org.csapi.TpCallAoCOrderCategory disc = org.csapi.TpCallAoCOrderCategory.from_int(in.read_long());
		switch (disc.value ())
		{
			case org.csapi.TpCallAoCOrderCategory._P_CHARGE_ADVICE_INFO:
			{
				org.csapi.TpChargeAdviceInfo _var;
				_var=org.csapi.TpChargeAdviceInfoHelper.read(in);
				result.ChargeAdviceInfo (_var);
				break;
			}
			case org.csapi.TpCallAoCOrderCategory._P_CHARGE_PER_TIME:
			{
				org.csapi.TpChargePerTime _var;
				_var=org.csapi.TpChargePerTimeHelper.read(in);
				result.ChargePerTime (_var);
				break;
			}
			case org.csapi.TpCallAoCOrderCategory._P_CHARGE_NETWORK:
			{
				java.lang.String _var;
				_var=in.read_string();
				result.NetworkCharge (_var);
				break;
			}
		}
		return result;
	}
	public static void write (org.omg.CORBA.portable.OutputStream out, TpAoCOrder s)
	{
		out.write_long (s.discriminator().value ());
		switch (s.discriminator().value ())
		{
			case org.csapi.TpCallAoCOrderCategory._P_CHARGE_ADVICE_INFO:
			{
				org.csapi.TpChargeAdviceInfoHelper.write(out,s.ChargeAdviceInfo ());
				break;
			}
			case org.csapi.TpCallAoCOrderCategory._P_CHARGE_PER_TIME:
			{
				org.csapi.TpChargePerTimeHelper.write(out,s.ChargePerTime ());
				break;
			}
			case org.csapi.TpCallAoCOrderCategory._P_CHARGE_NETWORK:
			{
				out.write_string(s.NetworkCharge ());
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
			org.csapi.TpCallAoCOrderCategoryHelper.insert(label_any, org.csapi.TpCallAoCOrderCategory.P_CHARGE_ADVICE_INFO);
			members[2] = new org.omg.CORBA.UnionMember ("ChargeAdviceInfo", label_any, org.csapi.TpChargeAdviceInfoHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.TpCallAoCOrderCategoryHelper.insert(label_any, org.csapi.TpCallAoCOrderCategory.P_CHARGE_PER_TIME);
			members[1] = new org.omg.CORBA.UnionMember ("ChargePerTime", label_any, org.csapi.TpChargePerTimeHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.TpCallAoCOrderCategoryHelper.insert(label_any, org.csapi.TpCallAoCOrderCategory.P_CHARGE_NETWORK);
			members[0] = new org.omg.CORBA.UnionMember ("NetworkCharge", label_any, org.omg.CORBA.ORB.init().create_string_tc(0),null);
			 _type = org.omg.CORBA.ORB.init().create_union_tc(id(),"TpAoCOrder",org.csapi.TpCallAoCOrderCategoryHelper.type(), members);
		}
		return _type;
	}
}
