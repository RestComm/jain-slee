package org.csapi.cc;

/**
 *	Generated from IDL definition of union "TpCallPartyToChargeAdditionalInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpCallPartyToChargeAdditionalInfoHelper
{
	private static org.omg.CORBA.TypeCode _type;
	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cc.TpCallPartyToChargeAdditionalInfo s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cc.TpCallPartyToChargeAdditionalInfo extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/TpCallPartyToChargeAdditionalInfo:1.0";
	}
	public static TpCallPartyToChargeAdditionalInfo read (org.omg.CORBA.portable.InputStream in)
	{
		TpCallPartyToChargeAdditionalInfo result = new TpCallPartyToChargeAdditionalInfo ();
		org.csapi.cc.TpCallPartyToChargeType disc = org.csapi.cc.TpCallPartyToChargeType.from_int(in.read_long());
		switch (disc.value ())
		{
			case org.csapi.cc.TpCallPartyToChargeType._P_CALL_PARTY_SPECIAL:
			{
				org.csapi.TpAddress _var;
				_var=org.csapi.TpAddressHelper.read(in);
				result.CallPartySpecial (_var);
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
	public static void write (org.omg.CORBA.portable.OutputStream out, TpCallPartyToChargeAdditionalInfo s)
	{
		out.write_long (s.discriminator().value ());
		switch (s.discriminator().value ())
		{
			case org.csapi.cc.TpCallPartyToChargeType._P_CALL_PARTY_SPECIAL:
			{
				org.csapi.TpAddressHelper.write(out,s.CallPartySpecial ());
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
			org.csapi.cc.TpCallPartyToChargeTypeHelper.insert(label_any, org.csapi.cc.TpCallPartyToChargeType.P_CALL_PARTY_SPECIAL);
			members[1] = new org.omg.CORBA.UnionMember ("CallPartySpecial", label_any, org.csapi.TpAddressHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			label_any.insert_octet ((byte)0);
			members[0] = new org.omg.CORBA.UnionMember ("Dummy", label_any, org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.from_int(2)),null);
			 _type = org.omg.CORBA.ORB.init().create_union_tc(id(),"TpCallPartyToChargeAdditionalInfo",org.csapi.cc.TpCallPartyToChargeTypeHelper.type(), members);
		}
		return _type;
	}
}
