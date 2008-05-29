package org.csapi.dsc;


/**
 *	Generated from IDL definition of struct "TpDataSessionIdentifier"
 *	@author JacORB IDL compiler 
 */

public final class TpDataSessionIdentifierHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.dsc.TpDataSessionIdentifierHelper.id(),"TpDataSessionIdentifier",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("DataSessionReference", org.omg.CORBA.ORB.init().create_interface_tc("IDL:org/csapi/dsc/IpDataSession:1.0", "IpDataSession"), null),new org.omg.CORBA.StructMember("DataSessionID", org.csapi.TpSessionIDHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.dsc.TpDataSessionIdentifier s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.dsc.TpDataSessionIdentifier extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/dsc/TpDataSessionIdentifier:1.0";
	}
	public static org.csapi.dsc.TpDataSessionIdentifier read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.dsc.TpDataSessionIdentifier result = new org.csapi.dsc.TpDataSessionIdentifier();
		result.DataSessionReference=org.csapi.dsc.IpDataSessionHelper.read(in);
		result.DataSessionID=in.read_long();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.dsc.TpDataSessionIdentifier s)
	{
		org.csapi.dsc.IpDataSessionHelper.write(out,s.DataSessionReference);
		out.write_long(s.DataSessionID);
	}
}
