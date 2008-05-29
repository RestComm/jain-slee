package org.csapi.ui;


/**
 *	Generated from IDL definition of struct "TpUIMessageCriteria"
 *	@author JacORB IDL compiler 
 */

public final class TpUIMessageCriteriaHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.ui.TpUIMessageCriteriaHelper.id(),"TpUIMessageCriteria",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("EndSequence", org.csapi.TpStringHelper.type(), null),new org.omg.CORBA.StructMember("MaxMessageTime", org.csapi.TpDurationHelper.type(), null),new org.omg.CORBA.StructMember("MaxMessageSize", org.csapi.TpInt32Helper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.ui.TpUIMessageCriteria s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.ui.TpUIMessageCriteria extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/ui/TpUIMessageCriteria:1.0";
	}
	public static org.csapi.ui.TpUIMessageCriteria read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.ui.TpUIMessageCriteria result = new org.csapi.ui.TpUIMessageCriteria();
		result.EndSequence=in.read_string();
		result.MaxMessageTime=in.read_long();
		result.MaxMessageSize=in.read_long();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.ui.TpUIMessageCriteria s)
	{
		out.write_string(s.EndSequence);
		out.write_long(s.MaxMessageTime);
		out.write_long(s.MaxMessageSize);
	}
}
