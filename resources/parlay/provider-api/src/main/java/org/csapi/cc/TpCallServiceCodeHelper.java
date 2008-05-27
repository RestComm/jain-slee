package org.csapi.cc;


/**
 *	Generated from IDL definition of struct "TpCallServiceCode"
 *	@author JacORB IDL compiler 
 */

public final class TpCallServiceCodeHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.cc.TpCallServiceCodeHelper.id(),"TpCallServiceCode",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("CallServiceCodeType", org.csapi.cc.TpCallServiceCodeTypeHelper.type(), null),new org.omg.CORBA.StructMember("ServiceCodeValue", org.csapi.TpStringHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cc.TpCallServiceCode s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cc.TpCallServiceCode extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/TpCallServiceCode:1.0";
	}
	public static org.csapi.cc.TpCallServiceCode read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.cc.TpCallServiceCode result = new org.csapi.cc.TpCallServiceCode();
		result.CallServiceCodeType=org.csapi.cc.TpCallServiceCodeTypeHelper.read(in);
		result.ServiceCodeValue=in.read_string();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.cc.TpCallServiceCode s)
	{
		org.csapi.cc.TpCallServiceCodeTypeHelper.write(out,s.CallServiceCodeType);
		out.write_string(s.ServiceCodeValue);
	}
}
