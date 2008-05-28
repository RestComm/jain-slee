package org.csapi;


/**
 *	Generated from IDL definition of struct "TpStructuredAttributeValue"
 *	@author JacORB IDL compiler 
 */

public final class TpStructuredAttributeValueHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.TpStructuredAttributeValueHelper.id(),"TpStructuredAttributeValue",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("Type", org.csapi.TpStructuredAttributeTypeHelper.type(), null),new org.omg.CORBA.StructMember("Value", org.csapi.TpAnyHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.TpStructuredAttributeValue s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.TpStructuredAttributeValue extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/TpStructuredAttributeValue:1.0";
	}
	public static org.csapi.TpStructuredAttributeValue read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.TpStructuredAttributeValue result = new org.csapi.TpStructuredAttributeValue();
		result.Type=in.read_string();
		result.Value=in.read_any();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.TpStructuredAttributeValue s)
	{
		out.write_string(s.Type);
		out.write_any(s.Value);
	}
}
