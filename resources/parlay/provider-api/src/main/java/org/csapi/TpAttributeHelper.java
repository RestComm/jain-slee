package org.csapi;


/**
 *	Generated from IDL definition of struct "TpAttribute"
 *	@author JacORB IDL compiler 
 */

public final class TpAttributeHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.TpAttributeHelper.id(),"TpAttribute",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("AttributeName", org.csapi.TpStringHelper.type(), null),new org.omg.CORBA.StructMember("AttributeValue", org.csapi.TpAttributeValueHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.TpAttribute s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.TpAttribute extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/TpAttribute:1.0";
	}
	public static org.csapi.TpAttribute read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.TpAttribute result = new org.csapi.TpAttribute();
		result.AttributeName=in.read_string();
		result.AttributeValue=org.csapi.TpAttributeValueHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.TpAttribute s)
	{
		out.write_string(s.AttributeName);
		org.csapi.TpAttributeValueHelper.write(out,s.AttributeValue);
	}
}
