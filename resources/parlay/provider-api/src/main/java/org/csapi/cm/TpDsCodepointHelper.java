package org.csapi.cm;


/**
 *	Generated from IDL definition of struct "TpDsCodepoint"
 *	@author JacORB IDL compiler 
 */

public final class TpDsCodepointHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.cm.TpDsCodepointHelper.id(),"TpDsCodepoint",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("match", org.csapi.TpStringHelper.type(), null),new org.omg.CORBA.StructMember("mask", org.csapi.TpStringHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cm.TpDsCodepoint s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cm.TpDsCodepoint extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cm/TpDsCodepoint:1.0";
	}
	public static org.csapi.cm.TpDsCodepoint read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.cm.TpDsCodepoint result = new org.csapi.cm.TpDsCodepoint();
		result.match=in.read_string();
		result.mask=in.read_string();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.cm.TpDsCodepoint s)
	{
		out.write_string(s.match);
		out.write_string(s.mask);
	}
}
