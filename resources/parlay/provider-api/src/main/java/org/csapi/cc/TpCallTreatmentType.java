package org.csapi.cc;
/**
 *	Generated from IDL definition of enum "TpCallTreatmentType"
 *	@author JacORB IDL compiler 
 */

public final class TpCallTreatmentType
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _P_CALL_TREATMENT_DEFAULT = 0;
	public static final TpCallTreatmentType P_CALL_TREATMENT_DEFAULT = new TpCallTreatmentType(_P_CALL_TREATMENT_DEFAULT);
	public static final int _P_CALL_TREATMENT_RELEASE = 1;
	public static final TpCallTreatmentType P_CALL_TREATMENT_RELEASE = new TpCallTreatmentType(_P_CALL_TREATMENT_RELEASE);
	public static final int _P_CALL_TREATMENT_SIAR = 2;
	public static final TpCallTreatmentType P_CALL_TREATMENT_SIAR = new TpCallTreatmentType(_P_CALL_TREATMENT_SIAR);
	public int value()
	{
		return value;
	}
	public static TpCallTreatmentType from_int(int value)
	{
		switch (value) {
			case _P_CALL_TREATMENT_DEFAULT: return P_CALL_TREATMENT_DEFAULT;
			case _P_CALL_TREATMENT_RELEASE: return P_CALL_TREATMENT_RELEASE;
			case _P_CALL_TREATMENT_SIAR: return P_CALL_TREATMENT_SIAR;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpCallTreatmentType(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
