package org.csapi.pam;
/**
 *	Generated from IDL definition of enum "TpPAMACLDefault"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMACLDefaultHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.pam.TpPAMACLDefaultHelper.id(),"TpPAMACLDefault",new String[]{"PAM_ACCESS_ALLOW","PAM_ACCESS_DENY"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.pam.TpPAMACLDefault s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.pam.TpPAMACLDefault extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/pam/TpPAMACLDefault:1.0";
	}
	public static TpPAMACLDefault read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpPAMACLDefault.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpPAMACLDefault s)
	{
		out.write_long(s.value());
	}
}
