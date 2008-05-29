package org.csapi.fw;


/**
 *	Generated from IDL definition of struct "TpServiceContract"
 *	@author JacORB IDL compiler 
 */

public final class TpServiceContractHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.fw.TpServiceContractHelper.id(),"TpServiceContract",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("ServiceContractID", org.csapi.fw.TpServiceContractIDHelper.type(), null),new org.omg.CORBA.StructMember("ServiceContractDescription", org.csapi.fw.TpServiceContractDescriptionHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.fw.TpServiceContract s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.fw.TpServiceContract extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/fw/TpServiceContract:1.0";
	}
	public static org.csapi.fw.TpServiceContract read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.fw.TpServiceContract result = new org.csapi.fw.TpServiceContract();
		result.ServiceContractID=in.read_string();
		result.ServiceContractDescription=org.csapi.fw.TpServiceContractDescriptionHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.fw.TpServiceContract s)
	{
		out.write_string(s.ServiceContractID);
		org.csapi.fw.TpServiceContractDescriptionHelper.write(out,s.ServiceContractDescription);
	}
}
