package org.csapi.pam;
/**
 *	Generated from IDL definition of enum "TpPAMErrorCause"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMErrorCauseHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.pam.TpPAMErrorCauseHelper.id(),"TpPAMErrorCause",new String[]{"P_PAM_CAUSE_UNDEFINED","P_PAM_CAUSE_INVALID_ADDRESS","P_PAM_CAUSE_SYSTEM_FAILURE","P_PAM_CAUSE_INFO_UNAVAILABLE","P_PAM_CAUSE_EVENT_REGISTRATION_CANCELLED"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.pam.TpPAMErrorCause s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.pam.TpPAMErrorCause extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/pam/TpPAMErrorCause:1.0";
	}
	public static TpPAMErrorCause read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpPAMErrorCause.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpPAMErrorCause s)
	{
		out.write_long(s.value());
	}
}
