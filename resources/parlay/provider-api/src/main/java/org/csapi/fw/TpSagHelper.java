package org.csapi.fw;


/**
 *	Generated from IDL definition of struct "TpSag"
 *	@author JacORB IDL compiler 
 */

public final class TpSagHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.fw.TpSagHelper.id(),"TpSag",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("SagID", org.csapi.fw.TpSagIDHelper.type(), null),new org.omg.CORBA.StructMember("SagDescription", org.csapi.fw.TpSagDescriptionHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.fw.TpSag s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.fw.TpSag extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/fw/TpSag:1.0";
	}
	public static org.csapi.fw.TpSag read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.fw.TpSag result = new org.csapi.fw.TpSag();
		result.SagID=in.read_string();
		result.SagDescription=in.read_string();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.fw.TpSag s)
	{
		out.write_string(s.SagID);
		out.write_string(s.SagDescription);
	}
}
