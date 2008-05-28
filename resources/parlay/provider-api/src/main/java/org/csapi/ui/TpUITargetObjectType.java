package org.csapi.ui;
/**
 *	Generated from IDL definition of enum "TpUITargetObjectType"
 *	@author JacORB IDL compiler 
 */

public final class TpUITargetObjectType
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _P_UI_TARGET_OBJECT_CALL = 0;
	public static final TpUITargetObjectType P_UI_TARGET_OBJECT_CALL = new TpUITargetObjectType(_P_UI_TARGET_OBJECT_CALL);
	public static final int _P_UI_TARGET_OBJECT_MULTI_PARTY_CALL = 1;
	public static final TpUITargetObjectType P_UI_TARGET_OBJECT_MULTI_PARTY_CALL = new TpUITargetObjectType(_P_UI_TARGET_OBJECT_MULTI_PARTY_CALL);
	public static final int _P_UI_TARGET_OBJECT_CALL_LEG = 2;
	public static final TpUITargetObjectType P_UI_TARGET_OBJECT_CALL_LEG = new TpUITargetObjectType(_P_UI_TARGET_OBJECT_CALL_LEG);
	public int value()
	{
		return value;
	}
	public static TpUITargetObjectType from_int(int value)
	{
		switch (value) {
			case _P_UI_TARGET_OBJECT_CALL: return P_UI_TARGET_OBJECT_CALL;
			case _P_UI_TARGET_OBJECT_MULTI_PARTY_CALL: return P_UI_TARGET_OBJECT_MULTI_PARTY_CALL;
			case _P_UI_TARGET_OBJECT_CALL_LEG: return P_UI_TARGET_OBJECT_CALL_LEG;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpUITargetObjectType(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
