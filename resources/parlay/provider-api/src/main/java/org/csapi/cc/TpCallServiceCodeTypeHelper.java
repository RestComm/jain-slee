package org.csapi.cc;
/**
 *	Generated from IDL definition of enum "TpCallServiceCodeType"
 *	@author JacORB IDL compiler 
 */

public final class TpCallServiceCodeTypeHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.cc.TpCallServiceCodeTypeHelper.id(),"TpCallServiceCodeType",new String[]{"P_CALL_SERVICE_CODE_UNDEFINED","P_CALL_SERVICE_CODE_DIGITS","P_CALL_SERVICE_CODE_FACILITY","P_CALL_SERVICE_CODE_U2U","P_CALL_SERVICE_CODE_HOOKFLASH","P_CALL_SERVICE_CODE_RECALL"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cc.TpCallServiceCodeType s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cc.TpCallServiceCodeType extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/TpCallServiceCodeType:1.0";
	}
	public static TpCallServiceCodeType read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpCallServiceCodeType.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpCallServiceCodeType s)
	{
		out.write_long(s.value());
	}
}
