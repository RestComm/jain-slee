package org.csapi;


/**
 *	Generated from IDL definition of struct "TpChargeAdviceInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpChargeAdviceInfoHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.TpChargeAdviceInfoHelper.id(),"TpChargeAdviceInfo",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("CurrentCAI", org.csapi.TpCAIElementsHelper.type(), null),new org.omg.CORBA.StructMember("NextCAI", org.csapi.TpCAIElementsHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.TpChargeAdviceInfo s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.TpChargeAdviceInfo extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/TpChargeAdviceInfo:1.0";
	}
	public static org.csapi.TpChargeAdviceInfo read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.TpChargeAdviceInfo result = new org.csapi.TpChargeAdviceInfo();
		result.CurrentCAI=org.csapi.TpCAIElementsHelper.read(in);
		result.NextCAI=org.csapi.TpCAIElementsHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.TpChargeAdviceInfo s)
	{
		org.csapi.TpCAIElementsHelper.write(out,s.CurrentCAI);
		org.csapi.TpCAIElementsHelper.write(out,s.NextCAI);
	}
}
