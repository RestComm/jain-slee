package org.csapi.cc;
/**
 *	Generated from IDL definition of enum "TpCallPartyToChargeType"
 *	@author JacORB IDL compiler 
 */

public final class TpCallPartyToChargeTypeHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.cc.TpCallPartyToChargeTypeHelper.id(),"TpCallPartyToChargeType",new String[]{"P_CALL_PARTY_ORIGINATING","P_CALL_PARTY_DESTINATION","P_CALL_PARTY_SPECIAL"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cc.TpCallPartyToChargeType s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cc.TpCallPartyToChargeType extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/TpCallPartyToChargeType:1.0";
	}
	public static TpCallPartyToChargeType read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpCallPartyToChargeType.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpCallPartyToChargeType s)
	{
		out.write_long(s.value());
	}
}
