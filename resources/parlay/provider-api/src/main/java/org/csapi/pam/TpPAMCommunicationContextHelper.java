package org.csapi.pam;


/**
 *	Generated from IDL definition of struct "TpPAMCommunicationContext"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMCommunicationContextHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.pam.TpPAMCommunicationContextHelper.id(),"TpPAMCommunicationContext",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("CommunicationCapability", org.csapi.pam.TpPAMCapabilityHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.pam.TpPAMCommunicationContext s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.pam.TpPAMCommunicationContext extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/pam/TpPAMCommunicationContext:1.0";
	}
	public static org.csapi.pam.TpPAMCommunicationContext read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.pam.TpPAMCommunicationContext result = new org.csapi.pam.TpPAMCommunicationContext();
		result.CommunicationCapability=in.read_string();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.pam.TpPAMCommunicationContext s)
	{
		out.write_string(s.CommunicationCapability);
	}
}
