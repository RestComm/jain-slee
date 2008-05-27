package org.csapi.cc.gccs;


/**
 *	Generated from IDL definition of struct "TpCallEventCriteriaResult"
 *	@author JacORB IDL compiler 
 */

public final class TpCallEventCriteriaResultHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.cc.gccs.TpCallEventCriteriaResultHelper.id(),"TpCallEventCriteriaResult",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("CallEventCriteria", org.csapi.cc.gccs.TpCallEventCriteriaHelper.type(), null),new org.omg.CORBA.StructMember("AssignmentID", org.csapi.TpInt32Helper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cc.gccs.TpCallEventCriteriaResult s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cc.gccs.TpCallEventCriteriaResult extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/gccs/TpCallEventCriteriaResult:1.0";
	}
	public static org.csapi.cc.gccs.TpCallEventCriteriaResult read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.cc.gccs.TpCallEventCriteriaResult result = new org.csapi.cc.gccs.TpCallEventCriteriaResult();
		result.CallEventCriteria=org.csapi.cc.gccs.TpCallEventCriteriaHelper.read(in);
		result.AssignmentID=in.read_long();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.cc.gccs.TpCallEventCriteriaResult s)
	{
		org.csapi.cc.gccs.TpCallEventCriteriaHelper.write(out,s.CallEventCriteria);
		out.write_long(s.AssignmentID);
	}
}
