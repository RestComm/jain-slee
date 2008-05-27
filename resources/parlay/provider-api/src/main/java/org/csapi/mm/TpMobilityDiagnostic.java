package org.csapi.mm;
/**
 *	Generated from IDL definition of enum "TpMobilityDiagnostic"
 *	@author JacORB IDL compiler 
 */

public final class TpMobilityDiagnostic
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _P_M_NO_INFORMATION = 0;
	public static final TpMobilityDiagnostic P_M_NO_INFORMATION = new TpMobilityDiagnostic(_P_M_NO_INFORMATION);
	public static final int _P_M_APPL_NOT_IN_PRIV_EXCEPT_LST = 1;
	public static final TpMobilityDiagnostic P_M_APPL_NOT_IN_PRIV_EXCEPT_LST = new TpMobilityDiagnostic(_P_M_APPL_NOT_IN_PRIV_EXCEPT_LST);
	public static final int _P_M_CALL_TO_USER_NOT_SETUP = 2;
	public static final TpMobilityDiagnostic P_M_CALL_TO_USER_NOT_SETUP = new TpMobilityDiagnostic(_P_M_CALL_TO_USER_NOT_SETUP);
	public static final int _P_M_PRIVACY_OVERRIDE_NOT_APPLIC = 3;
	public static final TpMobilityDiagnostic P_M_PRIVACY_OVERRIDE_NOT_APPLIC = new TpMobilityDiagnostic(_P_M_PRIVACY_OVERRIDE_NOT_APPLIC);
	public static final int _P_M_DISALL_BY_LOCAL_REGULAT_REQ = 4;
	public static final TpMobilityDiagnostic P_M_DISALL_BY_LOCAL_REGULAT_REQ = new TpMobilityDiagnostic(_P_M_DISALL_BY_LOCAL_REGULAT_REQ);
	public static final int _P_M_CONGESTION = 5;
	public static final TpMobilityDiagnostic P_M_CONGESTION = new TpMobilityDiagnostic(_P_M_CONGESTION);
	public static final int _P_M_INSUFFICIENT_RESOURCES = 6;
	public static final TpMobilityDiagnostic P_M_INSUFFICIENT_RESOURCES = new TpMobilityDiagnostic(_P_M_INSUFFICIENT_RESOURCES);
	public static final int _P_M_INSUFFICIENT_MEAS_DATA = 7;
	public static final TpMobilityDiagnostic P_M_INSUFFICIENT_MEAS_DATA = new TpMobilityDiagnostic(_P_M_INSUFFICIENT_MEAS_DATA);
	public static final int _P_M_INCONSISTENT_MEAS_DATA = 8;
	public static final TpMobilityDiagnostic P_M_INCONSISTENT_MEAS_DATA = new TpMobilityDiagnostic(_P_M_INCONSISTENT_MEAS_DATA);
	public static final int _P_M_LOC_PROC_NOT_COMPLETED = 9;
	public static final TpMobilityDiagnostic P_M_LOC_PROC_NOT_COMPLETED = new TpMobilityDiagnostic(_P_M_LOC_PROC_NOT_COMPLETED);
	public static final int _P_M_LOC_PROC_NOT_SUPP_BY_USER = 10;
	public static final TpMobilityDiagnostic P_M_LOC_PROC_NOT_SUPP_BY_USER = new TpMobilityDiagnostic(_P_M_LOC_PROC_NOT_SUPP_BY_USER);
	public static final int _P_M_QOS_NOT_ATTAINABLE = 11;
	public static final TpMobilityDiagnostic P_M_QOS_NOT_ATTAINABLE = new TpMobilityDiagnostic(_P_M_QOS_NOT_ATTAINABLE);
	public int value()
	{
		return value;
	}
	public static TpMobilityDiagnostic from_int(int value)
	{
		switch (value) {
			case _P_M_NO_INFORMATION: return P_M_NO_INFORMATION;
			case _P_M_APPL_NOT_IN_PRIV_EXCEPT_LST: return P_M_APPL_NOT_IN_PRIV_EXCEPT_LST;
			case _P_M_CALL_TO_USER_NOT_SETUP: return P_M_CALL_TO_USER_NOT_SETUP;
			case _P_M_PRIVACY_OVERRIDE_NOT_APPLIC: return P_M_PRIVACY_OVERRIDE_NOT_APPLIC;
			case _P_M_DISALL_BY_LOCAL_REGULAT_REQ: return P_M_DISALL_BY_LOCAL_REGULAT_REQ;
			case _P_M_CONGESTION: return P_M_CONGESTION;
			case _P_M_INSUFFICIENT_RESOURCES: return P_M_INSUFFICIENT_RESOURCES;
			case _P_M_INSUFFICIENT_MEAS_DATA: return P_M_INSUFFICIENT_MEAS_DATA;
			case _P_M_INCONSISTENT_MEAS_DATA: return P_M_INCONSISTENT_MEAS_DATA;
			case _P_M_LOC_PROC_NOT_COMPLETED: return P_M_LOC_PROC_NOT_COMPLETED;
			case _P_M_LOC_PROC_NOT_SUPP_BY_USER: return P_M_LOC_PROC_NOT_SUPP_BY_USER;
			case _P_M_QOS_NOT_ATTAINABLE: return P_M_QOS_NOT_ATTAINABLE;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpMobilityDiagnostic(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
