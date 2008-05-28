package org.csapi.fw;


/**
 *	Generated from IDL definition of struct "TpLoadPolicy"
 *	@author JacORB IDL compiler 
 */

public final class TpLoadPolicyHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.fw.TpLoadPolicyHelper.id(),"TpLoadPolicy",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("LoadPolicy", org.csapi.TpStringHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.fw.TpLoadPolicy s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.fw.TpLoadPolicy extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/fw/TpLoadPolicy:1.0";
	}
	public static org.csapi.fw.TpLoadPolicy read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.fw.TpLoadPolicy result = new org.csapi.fw.TpLoadPolicy();
		result.LoadPolicy=in.read_string();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.fw.TpLoadPolicy s)
	{
		out.write_string(s.LoadPolicy);
	}
}
