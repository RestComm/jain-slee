package org.csapi.dsc;


/**
 *	Generated from IDL definition of struct "TpDataSessionEventCriteriaResult"
 *	@author JacORB IDL compiler 
 */

public final class TpDataSessionEventCriteriaResultHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.dsc.TpDataSessionEventCriteriaResultHelper.id(),"TpDataSessionEventCriteriaResult",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("EventCriteria", org.csapi.dsc.TpDataSessionEventCriteriaHelper.type(), null),new org.omg.CORBA.StructMember("AssignmentID", org.csapi.TpAssignmentIDHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.dsc.TpDataSessionEventCriteriaResult s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.dsc.TpDataSessionEventCriteriaResult extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/dsc/TpDataSessionEventCriteriaResult:1.0";
	}
	public static org.csapi.dsc.TpDataSessionEventCriteriaResult read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.dsc.TpDataSessionEventCriteriaResult result = new org.csapi.dsc.TpDataSessionEventCriteriaResult();
		result.EventCriteria=org.csapi.dsc.TpDataSessionEventCriteriaHelper.read(in);
		result.AssignmentID=in.read_long();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.dsc.TpDataSessionEventCriteriaResult s)
	{
		org.csapi.dsc.TpDataSessionEventCriteriaHelper.write(out,s.EventCriteria);
		out.write_long(s.AssignmentID);
	}
}
