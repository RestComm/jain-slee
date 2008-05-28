package org.csapi.fw;


/**
 *	Generated from IDL definition of struct "TpLoadThreshold"
 *	@author JacORB IDL compiler 
 */

public final class TpLoadThresholdHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.fw.TpLoadThresholdHelper.id(),"TpLoadThreshold",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("LoadThreshold", org.csapi.TpFloatHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.fw.TpLoadThreshold s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.fw.TpLoadThreshold extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/fw/TpLoadThreshold:1.0";
	}
	public static org.csapi.fw.TpLoadThreshold read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.fw.TpLoadThreshold result = new org.csapi.fw.TpLoadThreshold();
		result.LoadThreshold=in.read_float();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.fw.TpLoadThreshold s)
	{
		out.write_float(s.LoadThreshold);
	}
}
