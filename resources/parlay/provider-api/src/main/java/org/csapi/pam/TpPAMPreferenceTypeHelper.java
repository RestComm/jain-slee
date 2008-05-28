package org.csapi.pam;
/**
 *	Generated from IDL definition of enum "TpPAMPreferenceType"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMPreferenceTypeHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.pam.TpPAMPreferenceTypeHelper.id(),"TpPAMPreferenceType",new String[]{"PAM_ACCESS_LIST","PAM_EXTERNAL_CONTROL"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.pam.TpPAMPreferenceType s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.pam.TpPAMPreferenceType extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/pam/TpPAMPreferenceType:1.0";
	}
	public static TpPAMPreferenceType read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpPAMPreferenceType.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpPAMPreferenceType s)
	{
		out.write_long(s.value());
	}
}
