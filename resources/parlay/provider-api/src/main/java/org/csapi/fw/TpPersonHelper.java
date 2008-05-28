package org.csapi.fw;


/**
 *	Generated from IDL definition of struct "TpPerson"
 *	@author JacORB IDL compiler 
 */

public final class TpPersonHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.fw.TpPersonHelper.id(),"TpPerson",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("PersonName", org.csapi.fw.TpPersonNameHelper.type(), null),new org.omg.CORBA.StructMember("PostalAddress", org.csapi.fw.TpPostalAddressHelper.type(), null),new org.omg.CORBA.StructMember("TelephoneNumber", org.csapi.fw.TpTelephoneNumberHelper.type(), null),new org.omg.CORBA.StructMember("Email", org.csapi.fw.TpEmailHelper.type(), null),new org.omg.CORBA.StructMember("HomePage", org.csapi.fw.TpHomePageHelper.type(), null),new org.omg.CORBA.StructMember("PersonProperties", org.csapi.fw.TpPersonPropertiesHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.fw.TpPerson s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.fw.TpPerson extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/fw/TpPerson:1.0";
	}
	public static org.csapi.fw.TpPerson read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.fw.TpPerson result = new org.csapi.fw.TpPerson();
		result.PersonName=in.read_string();
		result.PostalAddress=in.read_string();
		result.TelephoneNumber=in.read_string();
		result.Email=in.read_string();
		result.HomePage=in.read_string();
		result.PersonProperties = org.csapi.fw.TpPropertyListHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.fw.TpPerson s)
	{
		out.write_string(s.PersonName);
		out.write_string(s.PostalAddress);
		out.write_string(s.TelephoneNumber);
		out.write_string(s.Email);
		out.write_string(s.HomePage);
		org.csapi.fw.TpPropertyListHelper.write(out,s.PersonProperties);
	}
}
