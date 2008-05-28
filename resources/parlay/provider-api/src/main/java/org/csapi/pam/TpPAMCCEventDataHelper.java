package org.csapi.pam;


/**
 *	Generated from IDL definition of struct "TpPAMCCEventData"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMCCEventDataHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.pam.TpPAMCCEventDataHelper.id(),"TpPAMCCEventData",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("IdentityName", org.csapi.pam.TpPAMFQNameListHelper.type(), null),new org.omg.CORBA.StructMember("IdentityType", org.csapi.TpStringListHelper.type(), null),new org.omg.CORBA.StructMember("Capabilities", org.csapi.pam.TpPAMCapabilityListHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.pam.TpPAMCCEventData s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.pam.TpPAMCCEventData extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/pam/TpPAMCCEventData:1.0";
	}
	public static org.csapi.pam.TpPAMCCEventData read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.pam.TpPAMCCEventData result = new org.csapi.pam.TpPAMCCEventData();
		result.IdentityName = org.csapi.pam.TpPAMFQNameListHelper.read(in);
		result.IdentityType = org.csapi.TpStringListHelper.read(in);
		result.Capabilities = org.csapi.pam.TpPAMCapabilityListHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.pam.TpPAMCCEventData s)
	{
		org.csapi.pam.TpPAMFQNameListHelper.write(out,s.IdentityName);
		org.csapi.TpStringListHelper.write(out,s.IdentityType);
		org.csapi.pam.TpPAMCapabilityListHelper.write(out,s.Capabilities);
	}
}
