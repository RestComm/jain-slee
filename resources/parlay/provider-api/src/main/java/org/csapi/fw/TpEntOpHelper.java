package org.csapi.fw;


/**
 *	Generated from IDL definition of struct "TpEntOp"
 *	@author JacORB IDL compiler 
 */

public final class TpEntOpHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.fw.TpEntOpHelper.id(),"TpEntOp",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("EntOpID", org.csapi.fw.TpEntOpIDHelper.type(), null),new org.omg.CORBA.StructMember("EntOpProperties", org.csapi.fw.TpEntOpPropertiesHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.fw.TpEntOp s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.fw.TpEntOp extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/fw/TpEntOp:1.0";
	}
	public static org.csapi.fw.TpEntOp read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.fw.TpEntOp result = new org.csapi.fw.TpEntOp();
		result.EntOpID=in.read_string();
		result.EntOpProperties = org.csapi.fw.TpPropertyListHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.fw.TpEntOp s)
	{
		out.write_string(s.EntOpID);
		org.csapi.fw.TpPropertyListHelper.write(out,s.EntOpProperties);
	}
}
