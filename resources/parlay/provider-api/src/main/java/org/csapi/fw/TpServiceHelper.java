package org.csapi.fw;


/**
 *	Generated from IDL definition of struct "TpService"
 *	@author JacORB IDL compiler 
 */

public final class TpServiceHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.fw.TpServiceHelper.id(),"TpService",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("ServiceID", org.csapi.fw.TpServiceIDHelper.type(), null),new org.omg.CORBA.StructMember("ServiceDescription", org.csapi.fw.TpServiceDescriptionHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.fw.TpService s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.fw.TpService extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/fw/TpService:1.0";
	}
	public static org.csapi.fw.TpService read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.fw.TpService result = new org.csapi.fw.TpService();
		result.ServiceID=in.read_string();
		result.ServiceDescription=org.csapi.fw.TpServiceDescriptionHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.fw.TpService s)
	{
		out.write_string(s.ServiceID);
		org.csapi.fw.TpServiceDescriptionHelper.write(out,s.ServiceDescription);
	}
}
