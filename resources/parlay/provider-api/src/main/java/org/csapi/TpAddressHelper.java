package org.csapi;


/**
 *	Generated from IDL definition of struct "TpAddress"
 *	@author JacORB IDL compiler 
 */

public final class TpAddressHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.TpAddressHelper.id(),"TpAddress",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("Plan", org.csapi.TpAddressPlanHelper.type(), null),new org.omg.CORBA.StructMember("AddrString", org.csapi.TpStringHelper.type(), null),new org.omg.CORBA.StructMember("Name", org.csapi.TpStringHelper.type(), null),new org.omg.CORBA.StructMember("Presentation", org.csapi.TpAddressPresentationHelper.type(), null),new org.omg.CORBA.StructMember("Screening", org.csapi.TpAddressScreeningHelper.type(), null),new org.omg.CORBA.StructMember("SubAddressString", org.csapi.TpStringHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.TpAddress s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.TpAddress extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/TpAddress:1.0";
	}
	public static org.csapi.TpAddress read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.TpAddress result = new org.csapi.TpAddress();
		result.Plan=org.csapi.TpAddressPlanHelper.read(in);
		result.AddrString=in.read_string();
		result.Name=in.read_string();
		result.Presentation=org.csapi.TpAddressPresentationHelper.read(in);
		result.Screening=org.csapi.TpAddressScreeningHelper.read(in);
		result.SubAddressString=in.read_string();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.TpAddress s)
	{
		org.csapi.TpAddressPlanHelper.write(out,s.Plan);
		out.write_string(s.AddrString);
		out.write_string(s.Name);
		org.csapi.TpAddressPresentationHelper.write(out,s.Presentation);
		org.csapi.TpAddressScreeningHelper.write(out,s.Screening);
		out.write_string(s.SubAddressString);
	}
}
