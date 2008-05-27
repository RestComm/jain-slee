package org.csapi.mm;


/**
 *	Generated from IDL definition of struct "TpLocationTriggerCamel"
 *	@author JacORB IDL compiler 
 */

public final class TpLocationTriggerCamelHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.mm.TpLocationTriggerCamelHelper.id(),"TpLocationTriggerCamel",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("UpdateInsideVlr", org.csapi.TpBooleanHelper.type(), null),new org.omg.CORBA.StructMember("UpdateOutsideVlr", org.csapi.TpBooleanHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.mm.TpLocationTriggerCamel s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.mm.TpLocationTriggerCamel extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/mm/TpLocationTriggerCamel:1.0";
	}
	public static org.csapi.mm.TpLocationTriggerCamel read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.mm.TpLocationTriggerCamel result = new org.csapi.mm.TpLocationTriggerCamel();
		result.UpdateInsideVlr=in.read_boolean();
		result.UpdateOutsideVlr=in.read_boolean();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.mm.TpLocationTriggerCamel s)
	{
		out.write_boolean(s.UpdateInsideVlr);
		out.write_boolean(s.UpdateOutsideVlr);
	}
}
