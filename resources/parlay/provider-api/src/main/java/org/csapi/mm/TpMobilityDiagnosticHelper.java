package org.csapi.mm;
/**
 *	Generated from IDL definition of enum "TpMobilityDiagnostic"
 *	@author JacORB IDL compiler 
 */

public final class TpMobilityDiagnosticHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.mm.TpMobilityDiagnosticHelper.id(),"TpMobilityDiagnostic",new String[]{"P_M_NO_INFORMATION","P_M_APPL_NOT_IN_PRIV_EXCEPT_LST","P_M_CALL_TO_USER_NOT_SETUP","P_M_PRIVACY_OVERRIDE_NOT_APPLIC","P_M_DISALL_BY_LOCAL_REGULAT_REQ","P_M_CONGESTION","P_M_INSUFFICIENT_RESOURCES","P_M_INSUFFICIENT_MEAS_DATA","P_M_INCONSISTENT_MEAS_DATA","P_M_LOC_PROC_NOT_COMPLETED","P_M_LOC_PROC_NOT_SUPP_BY_USER","P_M_QOS_NOT_ATTAINABLE"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.mm.TpMobilityDiagnostic s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.mm.TpMobilityDiagnostic extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/mm/TpMobilityDiagnostic:1.0";
	}
	public static TpMobilityDiagnostic read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpMobilityDiagnostic.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpMobilityDiagnostic s)
	{
		out.write_long(s.value());
	}
}
