package org.csapi.pam;


/**
 *	Generated from IDL definition of exception "P_PAM_UNKNOWN_TYPE"
 *	@author JacORB IDL compiler 
 */

public final class P_PAM_UNKNOWN_TYPEHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_exception_tc(org.csapi.pam.P_PAM_UNKNOWN_TYPEHelper.id(),"P_PAM_UNKNOWN_TYPE",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("ExtraInformation", org.csapi.TpStringHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.pam.P_PAM_UNKNOWN_TYPE s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.pam.P_PAM_UNKNOWN_TYPE extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/pam/P_PAM_UNKNOWN_TYPE:1.0";
	}
	public static org.csapi.pam.P_PAM_UNKNOWN_TYPE read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.pam.P_PAM_UNKNOWN_TYPE result = new org.csapi.pam.P_PAM_UNKNOWN_TYPE();
		if (!in.read_string().equals(id())) throw new org.omg.CORBA.MARSHAL("wrong id");
		result.ExtraInformation=in.read_string();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.pam.P_PAM_UNKNOWN_TYPE s)
	{
		out.write_string(id());
		out.write_string(s.ExtraInformation);
	}
}
