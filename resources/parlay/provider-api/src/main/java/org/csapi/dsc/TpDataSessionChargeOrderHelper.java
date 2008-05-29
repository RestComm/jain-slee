package org.csapi.dsc;

/**
 *	Generated from IDL definition of union "TpDataSessionChargeOrder"
 *	@author JacORB IDL compiler 
 */

public final class TpDataSessionChargeOrderHelper
{
	private static org.omg.CORBA.TypeCode _type;
	public static void insert (final org.omg.CORBA.Any any, final org.csapi.dsc.TpDataSessionChargeOrder s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.dsc.TpDataSessionChargeOrder extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/dsc/TpDataSessionChargeOrder:1.0";
	}
	public static TpDataSessionChargeOrder read (org.omg.CORBA.portable.InputStream in)
	{
		TpDataSessionChargeOrder result = new TpDataSessionChargeOrder ();
		org.csapi.dsc.TpDataSessionChargeOrderCategory disc = org.csapi.dsc.TpDataSessionChargeOrderCategory.from_int(in.read_long());
		switch (disc.value ())
		{
			case org.csapi.dsc.TpDataSessionChargeOrderCategory._P_DATA_SESSION_CHARGE_PER_VOLUME:
			{
				org.csapi.dsc.TpChargePerVolume _var;
				_var=org.csapi.dsc.TpChargePerVolumeHelper.read(in);
				result.ChargePerVolume (_var);
				break;
			}
			case org.csapi.dsc.TpDataSessionChargeOrderCategory._P_DATA_SESSION_CHARGE_NETWORK:
			{
				java.lang.String _var;
				_var=in.read_string();
				result.NetworkCharge (_var);
				break;
			}
		}
		return result;
	}
	public static void write (org.omg.CORBA.portable.OutputStream out, TpDataSessionChargeOrder s)
	{
		out.write_long (s.discriminator().value ());
		switch (s.discriminator().value ())
		{
			case org.csapi.dsc.TpDataSessionChargeOrderCategory._P_DATA_SESSION_CHARGE_PER_VOLUME:
			{
				org.csapi.dsc.TpChargePerVolumeHelper.write(out,s.ChargePerVolume ());
				break;
			}
			case org.csapi.dsc.TpDataSessionChargeOrderCategory._P_DATA_SESSION_CHARGE_NETWORK:
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
			org.omg.CORBA.UnionMember[] members = new org.omg.CORBA.UnionMember[2];
			org.omg.CORBA.Any label_any;
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.dsc.TpDataSessionChargeOrderCategoryHelper.insert(label_any, org.csapi.dsc.TpDataSessionChargeOrderCategory.P_DATA_SESSION_CHARGE_PER_VOLUME);
			members[1] = new org.omg.CORBA.UnionMember ("ChargePerVolume", label_any, org.csapi.dsc.TpChargePerVolumeHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.dsc.TpDataSessionChargeOrderCategoryHelper.insert(label_any, org.csapi.dsc.TpDataSessionChargeOrderCategory.P_DATA_SESSION_CHARGE_NETWORK);
			members[0] = new org.omg.CORBA.UnionMember ("NetworkCharge", label_any, org.omg.CORBA.ORB.init().create_string_tc(0),null);
			 _type = org.omg.CORBA.ORB.init().create_union_tc(id(),"TpDataSessionChargeOrder",org.csapi.dsc.TpDataSessionChargeOrderCategoryHelper.type(), members);
		}
		return _type;
	}
}
