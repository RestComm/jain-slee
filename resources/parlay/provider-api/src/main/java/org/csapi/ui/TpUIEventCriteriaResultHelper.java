package org.csapi.ui;


/**
 *	Generated from IDL definition of struct "TpUIEventCriteriaResult"
 *	@author JacORB IDL compiler 
 */

public final class TpUIEventCriteriaResultHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.ui.TpUIEventCriteriaResultHelper.id(),"TpUIEventCriteriaResult",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("EventCriteria", org.csapi.ui.TpUIEventCriteriaHelper.type(), null),new org.omg.CORBA.StructMember("AssignmentID", org.csapi.TpInt32Helper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.ui.TpUIEventCriteriaResult s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.ui.TpUIEventCriteriaResult extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/ui/TpUIEventCriteriaResult:1.0";
	}
	public static org.csapi.ui.TpUIEventCriteriaResult read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.ui.TpUIEventCriteriaResult result = new org.csapi.ui.TpUIEventCriteriaResult();
		result.EventCriteria=org.csapi.ui.TpUIEventCriteriaHelper.read(in);
		result.AssignmentID=in.read_long();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.ui.TpUIEventCriteriaResult s)
	{
		org.csapi.ui.TpUIEventCriteriaHelper.write(out,s.EventCriteria);
		out.write_long(s.AssignmentID);
	}
}
