package org.csapi.cm;


/**
 *	Generated from IDL definition of struct "TpNameDescrpTagInt"
 *	@author JacORB IDL compiler 
 */

public final class TpNameDescrpTagIntHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.cm.TpNameDescrpTagIntHelper.id(),"TpNameDescrpTagInt",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("name", org.csapi.TpStringHelper.type(), null),new org.omg.CORBA.StructMember("description", org.csapi.TpStringHelper.type(), null),new org.omg.CORBA.StructMember("tag", org.csapi.cm.TpTagValueHelper.type(), null),new org.omg.CORBA.StructMember("value", org.csapi.TpInt32Helper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cm.TpNameDescrpTagInt s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cm.TpNameDescrpTagInt extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cm/TpNameDescrpTagInt:1.0";
	}
	public static org.csapi.cm.TpNameDescrpTagInt read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.cm.TpNameDescrpTagInt result = new org.csapi.cm.TpNameDescrpTagInt();
		result.name=in.read_string();
		result.description=in.read_string();
		result.tag=org.csapi.cm.TpTagValueHelper.read(in);
		result.value=in.read_long();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.cm.TpNameDescrpTagInt s)
	{
		out.write_string(s.name);
		out.write_string(s.description);
		org.csapi.cm.TpTagValueHelper.write(out,s.tag);
		out.write_long(s.value);
	}
}
