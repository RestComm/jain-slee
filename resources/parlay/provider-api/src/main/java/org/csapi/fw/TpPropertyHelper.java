package org.csapi.fw;


/**
 *	Generated from IDL definition of struct "TpProperty"
 *	@author JacORB IDL compiler 
 */

public final class TpPropertyHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.fw.TpPropertyHelper.id(),"TpProperty",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("PropertyName", org.csapi.fw.TpPropertyNameHelper.type(), null),new org.omg.CORBA.StructMember("PropertyValue", org.csapi.fw.TpPropertyValueHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.fw.TpProperty s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.fw.TpProperty extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/fw/TpProperty:1.0";
	}
	public static org.csapi.fw.TpProperty read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.fw.TpProperty result = new org.csapi.fw.TpProperty();
		result.PropertyName=in.read_string();
		result.PropertyValue=in.read_string();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.fw.TpProperty s)
	{
		out.write_string(s.PropertyName);
		out.write_string(s.PropertyValue);
	}
}
