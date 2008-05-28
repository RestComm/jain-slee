package org.csapi.cs;


/**
 *	Generated from IDL definition of struct "TpAmount"
 *	@author JacORB IDL compiler 
 */

public final class TpAmountHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.cs.TpAmountHelper.id(),"TpAmount",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("Number", org.csapi.TpInt32Helper.type(), null),new org.omg.CORBA.StructMember("Exponent", org.csapi.TpInt32Helper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cs.TpAmount s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cs.TpAmount extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cs/TpAmount:1.0";
	}
	public static org.csapi.cs.TpAmount read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.cs.TpAmount result = new org.csapi.cs.TpAmount();
		result.Number=in.read_long();
		result.Exponent=in.read_long();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.cs.TpAmount s)
	{
		out.write_long(s.Number);
		out.write_long(s.Exponent);
	}
}
