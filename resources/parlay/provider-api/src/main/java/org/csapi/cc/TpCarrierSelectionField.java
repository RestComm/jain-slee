package org.csapi.cc;
/**
 *	Generated from IDL definition of enum "TpCarrierSelectionField"
 *	@author JacORB IDL compiler 
 */

public final class TpCarrierSelectionField
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _P_CIC_UNDEFINED = 0;
	public static final TpCarrierSelectionField P_CIC_UNDEFINED = new TpCarrierSelectionField(_P_CIC_UNDEFINED);
	public static final int _P_CIC_NO_INPUT = 1;
	public static final TpCarrierSelectionField P_CIC_NO_INPUT = new TpCarrierSelectionField(_P_CIC_NO_INPUT);
	public static final int _P_CIC_INPUT = 2;
	public static final TpCarrierSelectionField P_CIC_INPUT = new TpCarrierSelectionField(_P_CIC_INPUT);
	public static final int _P_CIC_UNDETERMINED = 3;
	public static final TpCarrierSelectionField P_CIC_UNDETERMINED = new TpCarrierSelectionField(_P_CIC_UNDETERMINED);
	public static final int _P_CIC_NOT_PRESCRIBED = 4;
	public static final TpCarrierSelectionField P_CIC_NOT_PRESCRIBED = new TpCarrierSelectionField(_P_CIC_NOT_PRESCRIBED);
	public int value()
	{
		return value;
	}
	public static TpCarrierSelectionField from_int(int value)
	{
		switch (value) {
			case _P_CIC_UNDEFINED: return P_CIC_UNDEFINED;
			case _P_CIC_NO_INPUT: return P_CIC_NO_INPUT;
			case _P_CIC_INPUT: return P_CIC_INPUT;
			case _P_CIC_UNDETERMINED: return P_CIC_UNDETERMINED;
			case _P_CIC_NOT_PRESCRIBED: return P_CIC_NOT_PRESCRIBED;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpCarrierSelectionField(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
