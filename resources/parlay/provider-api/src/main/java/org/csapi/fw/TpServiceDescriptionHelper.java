package org.csapi.fw;


/**
 *	Generated from IDL definition of struct "TpServiceDescription"
 *	@author JacORB IDL compiler 
 */

public final class TpServiceDescriptionHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.fw.TpServiceDescriptionHelper.id(),"TpServiceDescription",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("ServiceTypeName", org.csapi.fw.TpServiceTypeNameHelper.type(), null),new org.omg.CORBA.StructMember("ServicePropertyList", org.csapi.fw.TpServicePropertyListHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.fw.TpServiceDescription s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.fw.TpServiceDescription extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/fw/TpServiceDescription:1.0";
	}
	public static org.csapi.fw.TpServiceDescription read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.fw.TpServiceDescription result = new org.csapi.fw.TpServiceDescription();
		result.ServiceTypeName=in.read_string();
		result.ServicePropertyList = org.csapi.fw.TpServicePropertyListHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.fw.TpServiceDescription s)
	{
		out.write_string(s.ServiceTypeName);
		org.csapi.fw.TpServicePropertyListHelper.write(out,s.ServicePropertyList);
	}
}
