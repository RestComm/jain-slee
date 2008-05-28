package org.csapi.cs;


/**
 *	Generated from IDL definition of struct "TpCorrelationID"
 *	@author JacORB IDL compiler 
 */

public final class TpCorrelationIDHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.cs.TpCorrelationIDHelper.id(),"TpCorrelationID",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("CorrelationID", org.csapi.TpSessionIDHelper.type(), null),new org.omg.CORBA.StructMember("CorrelationType", org.csapi.cs.TpCorrelationTypeHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cs.TpCorrelationID s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cs.TpCorrelationID extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cs/TpCorrelationID:1.0";
	}
	public static org.csapi.cs.TpCorrelationID read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.cs.TpCorrelationID result = new org.csapi.cs.TpCorrelationID();
		result.CorrelationID=in.read_long();
		result.CorrelationType=in.read_long();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.cs.TpCorrelationID s)
	{
		out.write_long(s.CorrelationID);
		out.write_long(s.CorrelationType);
	}
}
