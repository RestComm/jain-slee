package org.csapi.fw;


/**
 *	Generated from IDL definition of struct "TpServiceTypeDescription"
 *	@author JacORB IDL compiler 
 */

public final class TpServiceTypeDescriptionHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.fw.TpServiceTypeDescriptionHelper.id(),"TpServiceTypeDescription",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("ServiceTypePropertyList", org.csapi.fw.TpServiceTypePropertyListHelper.type(), null),new org.omg.CORBA.StructMember("ServiceTypeNameList", org.csapi.fw.TpServiceTypeNameListHelper.type(), null),new org.omg.CORBA.StructMember("AvailableOrUnavailable", org.csapi.TpBooleanHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.fw.TpServiceTypeDescription s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.fw.TpServiceTypeDescription extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/fw/TpServiceTypeDescription:1.0";
	}
	public static org.csapi.fw.TpServiceTypeDescription read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.fw.TpServiceTypeDescription result = new org.csapi.fw.TpServiceTypeDescription();
		result.ServiceTypePropertyList = org.csapi.fw.TpServiceTypePropertyListHelper.read(in);
		result.ServiceTypeNameList = org.csapi.fw.TpServiceTypeNameListHelper.read(in);
		result.AvailableOrUnavailable=in.read_boolean();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.fw.TpServiceTypeDescription s)
	{
		org.csapi.fw.TpServiceTypePropertyListHelper.write(out,s.ServiceTypePropertyList);
		org.csapi.fw.TpServiceTypeNameListHelper.write(out,s.ServiceTypeNameList);
		out.write_boolean(s.AvailableOrUnavailable);
	}
}
