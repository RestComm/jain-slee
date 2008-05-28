package org.csapi.cc;

/**
 *	Generated from IDL definition of union "TpCallAdditionalErrorInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpCallAdditionalErrorInfoHelper
{
	private static org.omg.CORBA.TypeCode _type;
	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cc.TpCallAdditionalErrorInfo s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cc.TpCallAdditionalErrorInfo extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/TpCallAdditionalErrorInfo:1.0";
	}
	public static TpCallAdditionalErrorInfo read (org.omg.CORBA.portable.InputStream in)
	{
		TpCallAdditionalErrorInfo result = new TpCallAdditionalErrorInfo ();
		org.csapi.cc.TpCallErrorType disc = org.csapi.cc.TpCallErrorType.from_int(in.read_long());
		switch (disc.value ())
		{
			case org.csapi.cc.TpCallErrorType._P_CALL_ERROR_INVALID_ADDRESS:
			{
				org.csapi.TpAddressError _var;
				_var=org.csapi.TpAddressErrorHelper.read(in);
				result.CallErrorInvalidAddress (_var);
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
	public static void write (org.omg.CORBA.portable.OutputStream out, TpCallAdditionalErrorInfo s)
	{
		out.write_long (s.discriminator().value ());
		switch (s.discriminator().value ())
		{
			case org.csapi.cc.TpCallErrorType._P_CALL_ERROR_INVALID_ADDRESS:
			{
				org.csapi.TpAddressErrorHelper.write(out,s.CallErrorInvalidAddress ());
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
			org.csapi.cc.TpCallErrorTypeHelper.insert(label_any, org.csapi.cc.TpCallErrorType.P_CALL_ERROR_INVALID_ADDRESS);
			members[1] = new org.omg.CORBA.UnionMember ("CallErrorInvalidAddress", label_any, org.csapi.TpAddressErrorHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			label_any.insert_octet ((byte)0);
			members[0] = new org.omg.CORBA.UnionMember ("Dummy", label_any, org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.from_int(2)),null);
			 _type = org.omg.CORBA.ORB.init().create_union_tc(id(),"TpCallAdditionalErrorInfo",org.csapi.cc.TpCallErrorTypeHelper.type(), members);
		}
		return _type;
	}
}
