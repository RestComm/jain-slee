package org.csapi.cc.gccs;


/**
 *	Generated from IDL definition of struct "TpCallReleaseCause"
 *	@author JacORB IDL compiler 
 */

public final class TpCallReleaseCauseHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.cc.gccs.TpCallReleaseCauseHelper.id(),"TpCallReleaseCause",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("Value", org.csapi.TpInt32Helper.type(), null),new org.omg.CORBA.StructMember("Location", org.csapi.TpInt32Helper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cc.gccs.TpCallReleaseCause s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cc.gccs.TpCallReleaseCause extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/gccs/TpCallReleaseCause:1.0";
	}
	public static org.csapi.cc.gccs.TpCallReleaseCause read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.cc.gccs.TpCallReleaseCause result = new org.csapi.cc.gccs.TpCallReleaseCause();
		result.Value=in.read_long();
		result.Location=in.read_long();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.cc.gccs.TpCallReleaseCause s)
	{
		out.write_long(s.Value);
		out.write_long(s.Location);
	}
}
