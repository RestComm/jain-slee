package org.csapi.cc.mmccs;
/**
 *	Generated from IDL definition of enum "TpAppMultiMediaCallBackRefType"
 *	@author JacORB IDL compiler 
 */

public final class TpAppMultiMediaCallBackRefType
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _P_APP_CALLBACK_UNDEFINED = 0;
	public static final TpAppMultiMediaCallBackRefType P_APP_CALLBACK_UNDEFINED = new TpAppMultiMediaCallBackRefType(_P_APP_CALLBACK_UNDEFINED);
	public static final int _P_APP_MULTIMEDIA_CALL_CALLBACK = 1;
	public static final TpAppMultiMediaCallBackRefType P_APP_MULTIMEDIA_CALL_CALLBACK = new TpAppMultiMediaCallBackRefType(_P_APP_MULTIMEDIA_CALL_CALLBACK);
	public static final int _P_APP_CALL_LEG_CALLBACK = 2;
	public static final TpAppMultiMediaCallBackRefType P_APP_CALL_LEG_CALLBACK = new TpAppMultiMediaCallBackRefType(_P_APP_CALL_LEG_CALLBACK);
	public static final int _P_APP_CALL_AND_CALL_LEG_CALLBACK = 3;
	public static final TpAppMultiMediaCallBackRefType P_APP_CALL_AND_CALL_LEG_CALLBACK = new TpAppMultiMediaCallBackRefType(_P_APP_CALL_AND_CALL_LEG_CALLBACK);
	public int value()
	{
		return value;
	}
	public static TpAppMultiMediaCallBackRefType from_int(int value)
	{
		switch (value) {
			case _P_APP_CALLBACK_UNDEFINED: return P_APP_CALLBACK_UNDEFINED;
			case _P_APP_MULTIMEDIA_CALL_CALLBACK: return P_APP_MULTIMEDIA_CALL_CALLBACK;
			case _P_APP_CALL_LEG_CALLBACK: return P_APP_CALL_LEG_CALLBACK;
			case _P_APP_CALL_AND_CALL_LEG_CALLBACK: return P_APP_CALL_AND_CALL_LEG_CALLBACK;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpAppMultiMediaCallBackRefType(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
