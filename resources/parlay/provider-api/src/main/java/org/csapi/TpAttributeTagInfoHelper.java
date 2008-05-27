package org.csapi;
/**
 *	Generated from IDL definition of enum "TpAttributeTagInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpAttributeTagInfoHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.TpAttributeTagInfoHelper.id(),"TpAttributeTagInfo",new String[]{"P_SIMPLE_TYPE","P_STRUCTURED_TYPE","P_XML_TYPE"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.TpAttributeTagInfo s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.TpAttributeTagInfo extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/TpAttributeTagInfo:1.0";
	}
	public static TpAttributeTagInfo read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpAttributeTagInfo.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpAttributeTagInfo s)
	{
		out.write_long(s.value());
	}
}
