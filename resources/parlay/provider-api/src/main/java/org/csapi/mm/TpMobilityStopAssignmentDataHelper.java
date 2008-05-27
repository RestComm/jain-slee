package org.csapi.mm;


/**
 *	Generated from IDL definition of struct "TpMobilityStopAssignmentData"
 *	@author JacORB IDL compiler 
 */

public final class TpMobilityStopAssignmentDataHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.mm.TpMobilityStopAssignmentDataHelper.id(),"TpMobilityStopAssignmentData",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("AssignmentId", org.csapi.TpAssignmentIDHelper.type(), null),new org.omg.CORBA.StructMember("StopScope", org.csapi.mm.TpMobilityStopScopeHelper.type(), null),new org.omg.CORBA.StructMember("Users", org.csapi.TpAddressSetHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.mm.TpMobilityStopAssignmentData s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.mm.TpMobilityStopAssignmentData extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/mm/TpMobilityStopAssignmentData:1.0";
	}
	public static org.csapi.mm.TpMobilityStopAssignmentData read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.mm.TpMobilityStopAssignmentData result = new org.csapi.mm.TpMobilityStopAssignmentData();
		result.AssignmentId=in.read_long();
		result.StopScope=org.csapi.mm.TpMobilityStopScopeHelper.read(in);
		result.Users = org.csapi.TpAddressSetHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.mm.TpMobilityStopAssignmentData s)
	{
		out.write_long(s.AssignmentId);
		org.csapi.mm.TpMobilityStopScopeHelper.write(out,s.StopScope);
		org.csapi.TpAddressSetHelper.write(out,s.Users);
	}
}
