package org.csapi.cc.cccs;


/**
 *	Generated from IDL definition of struct "TpConfSearchResult"
 *	@author JacORB IDL compiler 
 */

public final class TpConfSearchResultHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.cc.cccs.TpConfSearchResultHelper.id(),"TpConfSearchResult",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("MatchFound", org.csapi.TpBooleanHelper.type(), null),new org.omg.CORBA.StructMember("ActualStartTime", org.csapi.TpDateAndTimeHelper.type(), null),new org.omg.CORBA.StructMember("ActualResources", org.csapi.TpInt32Helper.type(), null),new org.omg.CORBA.StructMember("ActualDuration", org.csapi.TpDurationHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cc.cccs.TpConfSearchResult s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cc.cccs.TpConfSearchResult extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/cccs/TpConfSearchResult:1.0";
	}
	public static org.csapi.cc.cccs.TpConfSearchResult read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.cc.cccs.TpConfSearchResult result = new org.csapi.cc.cccs.TpConfSearchResult();
		result.MatchFound=in.read_boolean();
		result.ActualStartTime=in.read_string();
		result.ActualResources=in.read_long();
		result.ActualDuration=in.read_long();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.cc.cccs.TpConfSearchResult s)
	{
		out.write_boolean(s.MatchFound);
		out.write_string(s.ActualStartTime);
		out.write_long(s.ActualResources);
		out.write_long(s.ActualDuration);
	}
}
