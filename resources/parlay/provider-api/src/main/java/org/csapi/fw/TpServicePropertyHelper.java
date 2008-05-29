package org.csapi.fw;


/**
 *	Generated from IDL definition of struct "TpServiceProperty"
 *	@author JacORB IDL compiler 
 */

public final class TpServicePropertyHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.fw.TpServicePropertyHelper.id(),"TpServiceProperty",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("ServicePropertyName", org.csapi.fw.TpServicePropertyNameHelper.type(), null),new org.omg.CORBA.StructMember("ServicePropertyValueList", org.csapi.fw.TpServicePropertyValueListHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.fw.TpServiceProperty s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.fw.TpServiceProperty extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/fw/TpServiceProperty:1.0";
	}
	public static org.csapi.fw.TpServiceProperty read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.fw.TpServiceProperty result = new org.csapi.fw.TpServiceProperty();
		result.ServicePropertyName=in.read_string();
		result.ServicePropertyValueList = org.csapi.fw.TpServicePropertyValueListHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.fw.TpServiceProperty s)
	{
		out.write_string(s.ServicePropertyName);
		org.csapi.fw.TpServicePropertyValueListHelper.write(out,s.ServicePropertyValueList);
	}
}
