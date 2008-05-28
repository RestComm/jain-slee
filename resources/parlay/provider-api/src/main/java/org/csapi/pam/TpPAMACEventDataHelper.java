package org.csapi.pam;


/**
 *	Generated from IDL definition of struct "TpPAMACEventData"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMACEventDataHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.pam.TpPAMACEventDataHelper.id(),"TpPAMACEventData",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("AgentType", org.csapi.TpStringListHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.pam.TpPAMACEventData s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.pam.TpPAMACEventData extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/pam/TpPAMACEventData:1.0";
	}
	public static org.csapi.pam.TpPAMACEventData read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.pam.TpPAMACEventData result = new org.csapi.pam.TpPAMACEventData();
		result.AgentType = org.csapi.TpStringListHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.pam.TpPAMACEventData s)
	{
		org.csapi.TpStringListHelper.write(out,s.AgentType);
	}
}
