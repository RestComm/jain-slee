package org.csapi.ui;


/**
 *	Generated from IDL definition of struct "TpUICollectCriteria"
 *	@author JacORB IDL compiler 
 */

public final class TpUICollectCriteriaHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.ui.TpUICollectCriteriaHelper.id(),"TpUICollectCriteria",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("MinLength", org.csapi.TpInt32Helper.type(), null),new org.omg.CORBA.StructMember("MaxLength", org.csapi.TpInt32Helper.type(), null),new org.omg.CORBA.StructMember("EndSequence", org.csapi.TpStringHelper.type(), null),new org.omg.CORBA.StructMember("StartTimeout", org.csapi.TpDurationHelper.type(), null),new org.omg.CORBA.StructMember("InterCharTimeout", org.csapi.TpDurationHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.ui.TpUICollectCriteria s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.ui.TpUICollectCriteria extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/ui/TpUICollectCriteria:1.0";
	}
	public static org.csapi.ui.TpUICollectCriteria read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.ui.TpUICollectCriteria result = new org.csapi.ui.TpUICollectCriteria();
		result.MinLength=in.read_long();
		result.MaxLength=in.read_long();
		result.EndSequence=in.read_string();
		result.StartTimeout=in.read_long();
		result.InterCharTimeout=in.read_long();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.ui.TpUICollectCriteria s)
	{
		out.write_long(s.MinLength);
		out.write_long(s.MaxLength);
		out.write_string(s.EndSequence);
		out.write_long(s.StartTimeout);
		out.write_long(s.InterCharTimeout);
	}
}
