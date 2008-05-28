package org.csapi.cc;
/**
 *	Generated from IDL definition of enum "TpCallTreatmentType"
 *	@author JacORB IDL compiler 
 */

public final class TpCallTreatmentTypeHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.cc.TpCallTreatmentTypeHelper.id(),"TpCallTreatmentType",new String[]{"P_CALL_TREATMENT_DEFAULT","P_CALL_TREATMENT_RELEASE","P_CALL_TREATMENT_SIAR"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cc.TpCallTreatmentType s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cc.TpCallTreatmentType extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/TpCallTreatmentType:1.0";
	}
	public static TpCallTreatmentType read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpCallTreatmentType.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpCallTreatmentType s)
	{
		out.write_long(s.value());
	}
}
