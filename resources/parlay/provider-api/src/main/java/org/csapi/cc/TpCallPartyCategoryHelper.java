package org.csapi.cc;
/**
 *	Generated from IDL definition of enum "TpCallPartyCategory"
 *	@author JacORB IDL compiler 
 */

public final class TpCallPartyCategoryHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.cc.TpCallPartyCategoryHelper.id(),"TpCallPartyCategory",new String[]{"P_CALL_PARTY_CATEGORY_UNKNOWN","P_CALL_PARTY_CATEGORY_OPERATOR_F","P_CALL_PARTY_CATEGORY_OPERATOR_E","P_CALL_PARTY_CATEGORY_OPERATOR_G","P_CALL_PARTY_CATEGORY_OPERATOR_R","P_CALL_PARTY_CATEGORY_OPERATOR_S","P_CALL_PARTY_CATEGORY_ORDINARY_SUB","P_CALL_PARTY_CATEGORY_PRIORITY_SUB","P_CALL_PARTY_CATEGORY_DATA_CALL","P_CALL_PARTY_CATEGORY_TEST_CALL","P_CALL_PARTY_CATEGORY_PAYPHONE"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cc.TpCallPartyCategory s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cc.TpCallPartyCategory extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/TpCallPartyCategory:1.0";
	}
	public static TpCallPartyCategory read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpCallPartyCategory.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpCallPartyCategory s)
	{
		out.write_long(s.value());
	}
}
