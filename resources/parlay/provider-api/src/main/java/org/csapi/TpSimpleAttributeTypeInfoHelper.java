package org.csapi;
/**
 *	Generated from IDL definition of enum "TpSimpleAttributeTypeInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpSimpleAttributeTypeInfoHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.TpSimpleAttributeTypeInfoHelper.id(),"TpSimpleAttributeTypeInfo",new String[]{"P_BOOLEAN","P_OCTET","P_CHAR","P_WCHAR","P_STRING","P_WSTRING","P_INT16","P_UNSIGNED_INT16","P_INT32","P_UNSIGNED_INT32","P_INT64","P_UNSIGNED_INT64","P_FLOAT","P_DOUBLE","P_FIXED"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.TpSimpleAttributeTypeInfo s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.TpSimpleAttributeTypeInfo extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/TpSimpleAttributeTypeInfo:1.0";
	}
	public static TpSimpleAttributeTypeInfo read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpSimpleAttributeTypeInfo.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpSimpleAttributeTypeInfo s)
	{
		out.write_long(s.value());
	}
}
