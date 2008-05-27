package org.csapi.dsc;

/**
 *	Generated from IDL definition of union "TpDataSessionAdditionalErrorInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpDataSessionAdditionalErrorInfoHelper
{
	private static org.omg.CORBA.TypeCode _type;
	public static void insert (final org.omg.CORBA.Any any, final org.csapi.dsc.TpDataSessionAdditionalErrorInfo s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.dsc.TpDataSessionAdditionalErrorInfo extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/dsc/TpDataSessionAdditionalErrorInfo:1.0";
	}
	public static TpDataSessionAdditionalErrorInfo read (org.omg.CORBA.portable.InputStream in)
	{
		TpDataSessionAdditionalErrorInfo result = new TpDataSessionAdditionalErrorInfo ();
		org.csapi.dsc.TpDataSessionErrorType disc = org.csapi.dsc.TpDataSessionErrorType.from_int(in.read_long());
		switch (disc.value ())
		{
			case org.csapi.dsc.TpDataSessionErrorType._P_DATA_SESSION_ERROR_INVALID_ADDRESS:
			{
				org.csapi.TpAddressError _var;
				_var=org.csapi.TpAddressErrorHelper.read(in);
				result.DataSessionErrorInvalidAddress (_var);
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
	public static void write (org.omg.CORBA.portable.OutputStream out, TpDataSessionAdditionalErrorInfo s)
	{
		out.write_long (s.discriminator().value ());
		switch (s.discriminator().value ())
		{
			case org.csapi.dsc.TpDataSessionErrorType._P_DATA_SESSION_ERROR_INVALID_ADDRESS:
			{
				org.csapi.TpAddressErrorHelper.write(out,s.DataSessionErrorInvalidAddress ());
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
			org.csapi.dsc.TpDataSessionErrorTypeHelper.insert(label_any, org.csapi.dsc.TpDataSessionErrorType.P_DATA_SESSION_ERROR_INVALID_ADDRESS);
			members[1] = new org.omg.CORBA.UnionMember ("DataSessionErrorInvalidAddress", label_any, org.csapi.TpAddressErrorHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			label_any.insert_octet ((byte)0);
			members[0] = new org.omg.CORBA.UnionMember ("Dummy", label_any, org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.from_int(2)),null);
			 _type = org.omg.CORBA.ORB.init().create_union_tc(id(),"TpDataSessionAdditionalErrorInfo",org.csapi.dsc.TpDataSessionErrorTypeHelper.type(), members);
		}
		return _type;
	}
}
