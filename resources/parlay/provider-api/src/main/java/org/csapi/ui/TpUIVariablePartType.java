package org.csapi.ui;
/**
 *	Generated from IDL definition of enum "TpUIVariablePartType"
 *	@author JacORB IDL compiler 
 */

public final class TpUIVariablePartType
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _P_UI_VARIABLE_PART_INT = 0;
	public static final TpUIVariablePartType P_UI_VARIABLE_PART_INT = new TpUIVariablePartType(_P_UI_VARIABLE_PART_INT);
	public static final int _P_UI_VARIABLE_PART_ADDRESS = 1;
	public static final TpUIVariablePartType P_UI_VARIABLE_PART_ADDRESS = new TpUIVariablePartType(_P_UI_VARIABLE_PART_ADDRESS);
	public static final int _P_UI_VARIABLE_PART_TIME = 2;
	public static final TpUIVariablePartType P_UI_VARIABLE_PART_TIME = new TpUIVariablePartType(_P_UI_VARIABLE_PART_TIME);
	public static final int _P_UI_VARIABLE_PART_DATE = 3;
	public static final TpUIVariablePartType P_UI_VARIABLE_PART_DATE = new TpUIVariablePartType(_P_UI_VARIABLE_PART_DATE);
	public static final int _P_UI_VARIABLE_PART_PRICE = 4;
	public static final TpUIVariablePartType P_UI_VARIABLE_PART_PRICE = new TpUIVariablePartType(_P_UI_VARIABLE_PART_PRICE);
	public int value()
	{
		return value;
	}
	public static TpUIVariablePartType from_int(int value)
	{
		switch (value) {
			case _P_UI_VARIABLE_PART_INT: return P_UI_VARIABLE_PART_INT;
			case _P_UI_VARIABLE_PART_ADDRESS: return P_UI_VARIABLE_PART_ADDRESS;
			case _P_UI_VARIABLE_PART_TIME: return P_UI_VARIABLE_PART_TIME;
			case _P_UI_VARIABLE_PART_DATE: return P_UI_VARIABLE_PART_DATE;
			case _P_UI_VARIABLE_PART_PRICE: return P_UI_VARIABLE_PART_PRICE;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpUIVariablePartType(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
