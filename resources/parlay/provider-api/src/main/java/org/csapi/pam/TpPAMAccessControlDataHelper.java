package org.csapi.pam;


/**
 *	Generated from IDL definition of struct "TpPAMAccessControlData"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMAccessControlDataHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.pam.TpPAMAccessControlDataHelper.id(),"TpPAMAccessControlData",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("DefaultPolicy", org.csapi.pam.TpPAMACLDefaultHelper.type(), null),new org.omg.CORBA.StructMember("AllowList", org.csapi.pam.TpPAMFQNameListHelper.type(), null),new org.omg.CORBA.StructMember("DenyList", org.csapi.pam.TpPAMFQNameListHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.pam.TpPAMAccessControlData s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.pam.TpPAMAccessControlData extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/pam/TpPAMAccessControlData:1.0";
	}
	public static org.csapi.pam.TpPAMAccessControlData read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.pam.TpPAMAccessControlData result = new org.csapi.pam.TpPAMAccessControlData();
		result.DefaultPolicy=org.csapi.pam.TpPAMACLDefaultHelper.read(in);
		result.AllowList = org.csapi.pam.TpPAMFQNameListHelper.read(in);
		result.DenyList = org.csapi.pam.TpPAMFQNameListHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.pam.TpPAMAccessControlData s)
	{
		org.csapi.pam.TpPAMACLDefaultHelper.write(out,s.DefaultPolicy);
		org.csapi.pam.TpPAMFQNameListHelper.write(out,s.AllowList);
		org.csapi.pam.TpPAMFQNameListHelper.write(out,s.DenyList);
	}
}
