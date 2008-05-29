package org.csapi;


/**
 *	Generated from IDL definition of exception "P_INVALID_NETWORK_STATE"
 *	@author JacORB IDL compiler 
 */

public final class P_INVALID_NETWORK_STATEHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_exception_tc(org.csapi.P_INVALID_NETWORK_STATEHelper.id(),"P_INVALID_NETWORK_STATE",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("ExtraInformation", org.csapi.TpStringHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.P_INVALID_NETWORK_STATE s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.P_INVALID_NETWORK_STATE extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/P_INVALID_NETWORK_STATE:1.0";
	}
	public static org.csapi.P_INVALID_NETWORK_STATE read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.P_INVALID_NETWORK_STATE result = new org.csapi.P_INVALID_NETWORK_STATE();
		if (!in.read_string().equals(id())) throw new org.omg.CORBA.MARSHAL("wrong id");
		result.ExtraInformation=in.read_string();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.P_INVALID_NETWORK_STATE s)
	{
		out.write_string(id());
		out.write_string(s.ExtraInformation);
	}
}
