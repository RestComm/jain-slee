package org.csapi.mm;
/**
 *	Generated from IDL definition of enum "TpLocationTriggerCriteria"
 *	@author JacORB IDL compiler 
 */

public final class TpLocationTriggerCriteriaHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.mm.TpLocationTriggerCriteriaHelper.id(),"TpLocationTriggerCriteria",new String[]{"P_UL_ENTERING_AREA","P_UL_LEAVING_AREA"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.mm.TpLocationTriggerCriteria s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.mm.TpLocationTriggerCriteria extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/mm/TpLocationTriggerCriteria:1.0";
	}
	public static TpLocationTriggerCriteria read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpLocationTriggerCriteria.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpLocationTriggerCriteria s)
	{
		out.write_long(s.value());
	}
}
