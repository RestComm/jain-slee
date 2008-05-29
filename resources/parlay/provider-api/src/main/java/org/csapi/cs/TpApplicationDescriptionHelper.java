package org.csapi.cs;


/**
 *	Generated from IDL definition of struct "TpApplicationDescription"
 *	@author JacORB IDL compiler 
 */

public final class TpApplicationDescriptionHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.cs.TpApplicationDescriptionHelper.id(),"TpApplicationDescription",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("Text", org.csapi.TpStringHelper.type(), null),new org.omg.CORBA.StructMember("AppInformation", org.csapi.cs.TpAppInformationSetHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cs.TpApplicationDescription s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cs.TpApplicationDescription extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cs/TpApplicationDescription:1.0";
	}
	public static org.csapi.cs.TpApplicationDescription read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.cs.TpApplicationDescription result = new org.csapi.cs.TpApplicationDescription();
		result.Text=in.read_string();
		result.AppInformation = org.csapi.cs.TpAppInformationSetHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.cs.TpApplicationDescription s)
	{
		out.write_string(s.Text);
		org.csapi.cs.TpAppInformationSetHelper.write(out,s.AppInformation);
	}
}
