package org.csapi.dsc;


/**
 *	Generated from IDL definition of struct "TpDataSessionReleaseCause"
 *	@author JacORB IDL compiler 
 */

public final class TpDataSessionReleaseCauseHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.dsc.TpDataSessionReleaseCauseHelper.id(),"TpDataSessionReleaseCause",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("Value", org.csapi.TpInt32Helper.type(), null),new org.omg.CORBA.StructMember("Location", org.csapi.TpInt32Helper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.dsc.TpDataSessionReleaseCause s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.dsc.TpDataSessionReleaseCause extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/dsc/TpDataSessionReleaseCause:1.0";
	}
	public static org.csapi.dsc.TpDataSessionReleaseCause read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.dsc.TpDataSessionReleaseCause result = new org.csapi.dsc.TpDataSessionReleaseCause();
		result.Value=in.read_long();
		result.Location=in.read_long();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.dsc.TpDataSessionReleaseCause s)
	{
		out.write_long(s.Value);
		out.write_long(s.Location);
	}
}
