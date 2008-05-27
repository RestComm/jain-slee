package org.csapi.cc;


/**
 *	Generated from IDL definition of struct "TpCallError"
 *	@author JacORB IDL compiler 
 */

public final class TpCallErrorHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.cc.TpCallErrorHelper.id(),"TpCallError",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("ErrorTime", org.csapi.TpDateAndTimeHelper.type(), null),new org.omg.CORBA.StructMember("ErrorType", org.csapi.cc.TpCallErrorTypeHelper.type(), null),new org.omg.CORBA.StructMember("AdditionalErorInfo", org.csapi.cc.TpCallAdditionalErrorInfoHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cc.TpCallError s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cc.TpCallError extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/TpCallError:1.0";
	}
	public static org.csapi.cc.TpCallError read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.cc.TpCallError result = new org.csapi.cc.TpCallError();
		result.ErrorTime=in.read_string();
		result.ErrorType=org.csapi.cc.TpCallErrorTypeHelper.read(in);
		result.AdditionalErorInfo=org.csapi.cc.TpCallAdditionalErrorInfoHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.cc.TpCallError s)
	{
		out.write_string(s.ErrorTime);
		org.csapi.cc.TpCallErrorTypeHelper.write(out,s.ErrorType);
		org.csapi.cc.TpCallAdditionalErrorInfoHelper.write(out,s.AdditionalErorInfo);
	}
}
