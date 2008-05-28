package org.csapi.dsc;


/**
 *	Generated from IDL definition of struct "TpDataSessionError"
 *	@author JacORB IDL compiler 
 */

public final class TpDataSessionErrorHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.dsc.TpDataSessionErrorHelper.id(),"TpDataSessionError",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("ErrorTime", org.csapi.TpDateAndTimeHelper.type(), null),new org.omg.CORBA.StructMember("ErrorType", org.csapi.dsc.TpDataSessionErrorTypeHelper.type(), null),new org.omg.CORBA.StructMember("AdditionalErrorInfo", org.csapi.dsc.TpDataSessionAdditionalErrorInfoHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.dsc.TpDataSessionError s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.dsc.TpDataSessionError extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/dsc/TpDataSessionError:1.0";
	}
	public static org.csapi.dsc.TpDataSessionError read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.dsc.TpDataSessionError result = new org.csapi.dsc.TpDataSessionError();
		result.ErrorTime=in.read_string();
		result.ErrorType=org.csapi.dsc.TpDataSessionErrorTypeHelper.read(in);
		result.AdditionalErrorInfo=org.csapi.dsc.TpDataSessionAdditionalErrorInfoHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.dsc.TpDataSessionError s)
	{
		out.write_string(s.ErrorTime);
		org.csapi.dsc.TpDataSessionErrorTypeHelper.write(out,s.ErrorType);
		org.csapi.dsc.TpDataSessionAdditionalErrorInfoHelper.write(out,s.AdditionalErrorInfo);
	}
}
