package org.csapi.gms;


/**
 *	Generated from IDL definition of exception "P_GMS_NUMBER_NOT_POSITIVE"
 *	@author JacORB IDL compiler 
 */

public final class P_GMS_NUMBER_NOT_POSITIVEHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_exception_tc(org.csapi.gms.P_GMS_NUMBER_NOT_POSITIVEHelper.id(),"P_GMS_NUMBER_NOT_POSITIVE",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("ExtraInformation", org.csapi.TpStringHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.gms.P_GMS_NUMBER_NOT_POSITIVE s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.gms.P_GMS_NUMBER_NOT_POSITIVE extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/gms/P_GMS_NUMBER_NOT_POSITIVE:1.0";
	}
	public static org.csapi.gms.P_GMS_NUMBER_NOT_POSITIVE read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.gms.P_GMS_NUMBER_NOT_POSITIVE result = new org.csapi.gms.P_GMS_NUMBER_NOT_POSITIVE();
		if (!in.read_string().equals(id())) throw new org.omg.CORBA.MARSHAL("wrong id");
		result.ExtraInformation=in.read_string();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.gms.P_GMS_NUMBER_NOT_POSITIVE s)
	{
		out.write_string(id());
		out.write_string(s.ExtraInformation);
	}
}
