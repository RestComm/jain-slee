package org.csapi.cs;


/**
 *	Generated from IDL definition of struct "TpChargingParameter"
 *	@author JacORB IDL compiler 
 */

public final class TpChargingParameterHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.cs.TpChargingParameterHelper.id(),"TpChargingParameter",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("ParameterID", org.csapi.cs.TpChargingParameterIDHelper.type(), null),new org.omg.CORBA.StructMember("ParameterValue", org.csapi.cs.TpChargingParameterValueHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cs.TpChargingParameter s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cs.TpChargingParameter extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cs/TpChargingParameter:1.0";
	}
	public static org.csapi.cs.TpChargingParameter read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.cs.TpChargingParameter result = new org.csapi.cs.TpChargingParameter();
		result.ParameterID=in.read_long();
		result.ParameterValue=org.csapi.cs.TpChargingParameterValueHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.cs.TpChargingParameter s)
	{
		out.write_long(s.ParameterID);
		org.csapi.cs.TpChargingParameterValueHelper.write(out,s.ParameterValue);
	}
}
