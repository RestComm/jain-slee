package org.csapi.cc.cccs;


/**
 *	Generated from IDL definition of struct "TpConfSearchCriteria"
 *	@author JacORB IDL compiler 
 */

public final class TpConfSearchCriteriaHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.cc.cccs.TpConfSearchCriteriaHelper.id(),"TpConfSearchCriteria",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("StartSearch", org.csapi.TpDateAndTimeHelper.type(), null),new org.omg.CORBA.StructMember("StopSearch", org.csapi.TpDateAndTimeHelper.type(), null),new org.omg.CORBA.StructMember("RequestedResources", org.csapi.TpInt32Helper.type(), null),new org.omg.CORBA.StructMember("RequestedDuration", org.csapi.TpDurationHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cc.cccs.TpConfSearchCriteria s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cc.cccs.TpConfSearchCriteria extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/cccs/TpConfSearchCriteria:1.0";
	}
	public static org.csapi.cc.cccs.TpConfSearchCriteria read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.cc.cccs.TpConfSearchCriteria result = new org.csapi.cc.cccs.TpConfSearchCriteria();
		result.StartSearch=in.read_string();
		result.StopSearch=in.read_string();
		result.RequestedResources=in.read_long();
		result.RequestedDuration=in.read_long();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.cc.cccs.TpConfSearchCriteria s)
	{
		out.write_string(s.StartSearch);
		out.write_string(s.StopSearch);
		out.write_long(s.RequestedResources);
		out.write_long(s.RequestedDuration);
	}
}
