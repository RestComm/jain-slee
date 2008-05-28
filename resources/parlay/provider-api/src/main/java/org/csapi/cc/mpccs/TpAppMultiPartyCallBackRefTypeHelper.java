package org.csapi.cc.mpccs;
/**
 *	Generated from IDL definition of enum "TpAppMultiPartyCallBackRefType"
 *	@author JacORB IDL compiler 
 */

public final class TpAppMultiPartyCallBackRefTypeHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.cc.mpccs.TpAppMultiPartyCallBackRefTypeHelper.id(),"TpAppMultiPartyCallBackRefType",new String[]{"P_APP_CALLBACK_UNDEFINED","P_APP_MULTIPARTY_CALL_CALLBACK","P_APP_CALL_LEG_CALLBACK","P_APP_CALL_AND_CALL_LEG_CALLBACK"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cc.mpccs.TpAppMultiPartyCallBackRefType s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cc.mpccs.TpAppMultiPartyCallBackRefType extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/mpccs/TpAppMultiPartyCallBackRefType:1.0";
	}
	public static TpAppMultiPartyCallBackRefType read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpAppMultiPartyCallBackRefType.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpAppMultiPartyCallBackRefType s)
	{
		out.write_long(s.value());
	}
}
