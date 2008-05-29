package org.csapi.ui;


/**
 *	Generated from IDL definition of struct "TpUIEventCriteria"
 *	@author JacORB IDL compiler 
 */

public final class TpUIEventCriteriaHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.ui.TpUIEventCriteriaHelper.id(),"TpUIEventCriteria",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("OriginatingAddress", org.csapi.TpAddressRangeHelper.type(), null),new org.omg.CORBA.StructMember("DestinationAddress", org.csapi.TpAddressRangeHelper.type(), null),new org.omg.CORBA.StructMember("ServiceCode", org.csapi.TpStringHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.ui.TpUIEventCriteria s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.ui.TpUIEventCriteria extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/ui/TpUIEventCriteria:1.0";
	}
	public static org.csapi.ui.TpUIEventCriteria read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.ui.TpUIEventCriteria result = new org.csapi.ui.TpUIEventCriteria();
		result.OriginatingAddress=org.csapi.TpAddressRangeHelper.read(in);
		result.DestinationAddress=org.csapi.TpAddressRangeHelper.read(in);
		result.ServiceCode=in.read_string();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.ui.TpUIEventCriteria s)
	{
		org.csapi.TpAddressRangeHelper.write(out,s.OriginatingAddress);
		org.csapi.TpAddressRangeHelper.write(out,s.DestinationAddress);
		out.write_string(s.ServiceCode);
	}
}
