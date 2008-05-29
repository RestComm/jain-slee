package org.csapi.pam;


/**
 *	Generated from IDL definition of struct "TpPAMAttribute"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMAttributeHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.pam.TpPAMAttributeHelper.id(),"TpPAMAttribute",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("AttributeName", org.csapi.TpStringHelper.type(), null),new org.omg.CORBA.StructMember("AttributeValue", org.csapi.TpAttributeValueHelper.type(), null),new org.omg.CORBA.StructMember("ExpiresIn", org.csapi.pam.TpPAMTimeIntervalHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.pam.TpPAMAttribute s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.pam.TpPAMAttribute extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/pam/TpPAMAttribute:1.0";
	}
	public static org.csapi.pam.TpPAMAttribute read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.pam.TpPAMAttribute result = new org.csapi.pam.TpPAMAttribute();
		result.AttributeName=in.read_string();
		result.AttributeValue=org.csapi.TpAttributeValueHelper.read(in);
		result.ExpiresIn=in.read_longlong();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.pam.TpPAMAttribute s)
	{
		out.write_string(s.AttributeName);
		org.csapi.TpAttributeValueHelper.write(out,s.AttributeValue);
		out.write_longlong(s.ExpiresIn);
	}
}
