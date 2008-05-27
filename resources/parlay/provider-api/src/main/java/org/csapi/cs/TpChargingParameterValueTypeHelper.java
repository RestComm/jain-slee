package org.csapi.cs;
/**
 *	Generated from IDL definition of enum "TpChargingParameterValueType"
 *	@author JacORB IDL compiler 
 */

public final class TpChargingParameterValueTypeHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.cs.TpChargingParameterValueTypeHelper.id(),"TpChargingParameterValueType",new String[]{"P_CHS_PARAMETER_INT32","P_CHS_PARAMETER_FLOAT","P_CHS_PARAMETER_STRING","P_CHS_PARAMETER_BOOLEAN","P_CHS_PARAMETER_OCTETSET"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cs.TpChargingParameterValueType s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cs.TpChargingParameterValueType extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cs/TpChargingParameterValueType:1.0";
	}
	public static TpChargingParameterValueType read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpChargingParameterValueType.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpChargingParameterValueType s)
	{
		out.write_long(s.value());
	}
}
