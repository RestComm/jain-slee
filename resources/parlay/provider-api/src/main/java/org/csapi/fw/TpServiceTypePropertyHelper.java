package org.csapi.fw;


/**
 *	Generated from IDL definition of struct "TpServiceTypeProperty"
 *	@author JacORB IDL compiler 
 */

public final class TpServiceTypePropertyHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.fw.TpServiceTypePropertyHelper.id(),"TpServiceTypeProperty",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("ServicePropertyName", org.csapi.fw.TpServicePropertyNameHelper.type(), null),new org.omg.CORBA.StructMember("ServiceTypePropertyMode", org.csapi.fw.TpServiceTypePropertyModeHelper.type(), null),new org.omg.CORBA.StructMember("ServicePropertyTypeName", org.csapi.fw.TpServicePropertyTypeNameHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.fw.TpServiceTypeProperty s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.fw.TpServiceTypeProperty extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/fw/TpServiceTypeProperty:1.0";
	}
	public static org.csapi.fw.TpServiceTypeProperty read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.fw.TpServiceTypeProperty result = new org.csapi.fw.TpServiceTypeProperty();
		result.ServicePropertyName=in.read_string();
		result.ServiceTypePropertyMode=org.csapi.fw.TpServiceTypePropertyModeHelper.read(in);
		result.ServicePropertyTypeName=in.read_string();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.fw.TpServiceTypeProperty s)
	{
		out.write_string(s.ServicePropertyName);
		org.csapi.fw.TpServiceTypePropertyModeHelper.write(out,s.ServiceTypePropertyMode);
		out.write_string(s.ServicePropertyTypeName);
	}
}
