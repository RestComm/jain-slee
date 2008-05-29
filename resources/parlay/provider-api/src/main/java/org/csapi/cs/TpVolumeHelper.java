package org.csapi.cs;


/**
 *	Generated from IDL definition of struct "TpVolume"
 *	@author JacORB IDL compiler 
 */

public final class TpVolumeHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.cs.TpVolumeHelper.id(),"TpVolume",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("Amount", org.csapi.cs.TpAmountHelper.type(), null),new org.omg.CORBA.StructMember("Unit", org.csapi.cs.TpUnitIDHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cs.TpVolume s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cs.TpVolume extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cs/TpVolume:1.0";
	}
	public static org.csapi.cs.TpVolume read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.cs.TpVolume result = new org.csapi.cs.TpVolume();
		result.Amount=org.csapi.cs.TpAmountHelper.read(in);
		result.Unit=in.read_long();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.cs.TpVolume s)
	{
		org.csapi.cs.TpAmountHelper.write(out,s.Amount);
		out.write_long(s.Unit);
	}
}
