package org.csapi.pam;
/**
 *	Generated from IDL definition of enum "TpPAMContextName"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMContextNameHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.pam.TpPAMContextNameHelper.id(),"TpPAMContextName",new String[]{"PAM_CONTEXT_ANY","PAM_CONTEXT_COMMUNICATION"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.pam.TpPAMContextName s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.pam.TpPAMContextName extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/pam/TpPAMContextName:1.0";
	}
	public static TpPAMContextName read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpPAMContextName.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpPAMContextName s)
	{
		out.write_long(s.value());
	}
}
