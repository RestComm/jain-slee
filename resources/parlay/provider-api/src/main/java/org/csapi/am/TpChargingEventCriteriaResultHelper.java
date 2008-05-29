package org.csapi.am;


/**
 *	Generated from IDL definition of struct "TpChargingEventCriteriaResult"
 *	@author JacORB IDL compiler 
 */

public final class TpChargingEventCriteriaResultHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.am.TpChargingEventCriteriaResultHelper.id(),"TpChargingEventCriteriaResult",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("ChargingEventCriteria", org.csapi.am.TpChargingEventCriteriaHelper.type(), null),new org.omg.CORBA.StructMember("AssignmentID", org.csapi.TpAssignmentIDHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.am.TpChargingEventCriteriaResult s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.am.TpChargingEventCriteriaResult extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/am/TpChargingEventCriteriaResult:1.0";
	}
	public static org.csapi.am.TpChargingEventCriteriaResult read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.am.TpChargingEventCriteriaResult result = new org.csapi.am.TpChargingEventCriteriaResult();
		result.ChargingEventCriteria=org.csapi.am.TpChargingEventCriteriaHelper.read(in);
		result.AssignmentID=in.read_long();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.am.TpChargingEventCriteriaResult s)
	{
		org.csapi.am.TpChargingEventCriteriaHelper.write(out,s.ChargingEventCriteria);
		out.write_long(s.AssignmentID);
	}
}
