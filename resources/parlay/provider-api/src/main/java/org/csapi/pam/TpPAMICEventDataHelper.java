package org.csapi.pam;


/**
 *	Generated from IDL definition of struct "TpPAMICEventData"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMICEventDataHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.pam.TpPAMICEventDataHelper.id(),"TpPAMICEventData",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("IdentityType", org.csapi.TpStringListHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.pam.TpPAMICEventData s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.pam.TpPAMICEventData extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/pam/TpPAMICEventData:1.0";
	}
	public static org.csapi.pam.TpPAMICEventData read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.pam.TpPAMICEventData result = new org.csapi.pam.TpPAMICEventData();
		result.IdentityType = org.csapi.TpStringListHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.pam.TpPAMICEventData s)
	{
		org.csapi.TpStringListHelper.write(out,s.IdentityType);
	}
}
