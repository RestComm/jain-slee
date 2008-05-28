package org.csapi.cs;
/**
 *	Generated from IDL definition of enum "TpChargingParameterValueType"
 *	@author JacORB IDL compiler 
 */

public final class TpChargingParameterValueType
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _P_CHS_PARAMETER_INT32 = 0;
	public static final TpChargingParameterValueType P_CHS_PARAMETER_INT32 = new TpChargingParameterValueType(_P_CHS_PARAMETER_INT32);
	public static final int _P_CHS_PARAMETER_FLOAT = 1;
	public static final TpChargingParameterValueType P_CHS_PARAMETER_FLOAT = new TpChargingParameterValueType(_P_CHS_PARAMETER_FLOAT);
	public static final int _P_CHS_PARAMETER_STRING = 2;
	public static final TpChargingParameterValueType P_CHS_PARAMETER_STRING = new TpChargingParameterValueType(_P_CHS_PARAMETER_STRING);
	public static final int _P_CHS_PARAMETER_BOOLEAN = 3;
	public static final TpChargingParameterValueType P_CHS_PARAMETER_BOOLEAN = new TpChargingParameterValueType(_P_CHS_PARAMETER_BOOLEAN);
	public static final int _P_CHS_PARAMETER_OCTETSET = 4;
	public static final TpChargingParameterValueType P_CHS_PARAMETER_OCTETSET = new TpChargingParameterValueType(_P_CHS_PARAMETER_OCTETSET);
	public int value()
	{
		return value;
	}
	public static TpChargingParameterValueType from_int(int value)
	{
		switch (value) {
			case _P_CHS_PARAMETER_INT32: return P_CHS_PARAMETER_INT32;
			case _P_CHS_PARAMETER_FLOAT: return P_CHS_PARAMETER_FLOAT;
			case _P_CHS_PARAMETER_STRING: return P_CHS_PARAMETER_STRING;
			case _P_CHS_PARAMETER_BOOLEAN: return P_CHS_PARAMETER_BOOLEAN;
			case _P_CHS_PARAMETER_OCTETSET: return P_CHS_PARAMETER_OCTETSET;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpChargingParameterValueType(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
