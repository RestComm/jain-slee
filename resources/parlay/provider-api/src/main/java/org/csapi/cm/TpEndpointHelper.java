package org.csapi.cm;


/**
 *	Generated from IDL definition of struct "TpEndpoint"
 *	@author JacORB IDL compiler 
 */

public final class TpEndpointHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.cm.TpEndpointHelper.id(),"TpEndpoint",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("type", org.csapi.cm.TpSiteOrSapHelper.type(), null),new org.omg.CORBA.StructMember("id", org.csapi.TpStringHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cm.TpEndpoint s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cm.TpEndpoint extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cm/TpEndpoint:1.0";
	}
	public static org.csapi.cm.TpEndpoint read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.cm.TpEndpoint result = new org.csapi.cm.TpEndpoint();
		result.type=org.csapi.cm.TpSiteOrSapHelper.read(in);
		result.id=in.read_string();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.cm.TpEndpoint s)
	{
		org.csapi.cm.TpSiteOrSapHelper.write(out,s.type);
		out.write_string(s.id);
	}
}
