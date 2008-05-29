package org.csapi.fw;
/**
 *	Generated from IDL definition of enum "TpSubjectType"
 *	@author JacORB IDL compiler 
 */

public final class TpSubjectType
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _P_SUBJECT_UNDEFINED = 0;
	public static final TpSubjectType P_SUBJECT_UNDEFINED = new TpSubjectType(_P_SUBJECT_UNDEFINED);
	public static final int _P_SUBJECT_CLIENT_APP = 1;
	public static final TpSubjectType P_SUBJECT_CLIENT_APP = new TpSubjectType(_P_SUBJECT_CLIENT_APP);
	public static final int _P_SUBJECT_FW = 2;
	public static final TpSubjectType P_SUBJECT_FW = new TpSubjectType(_P_SUBJECT_FW);
	public int value()
	{
		return value;
	}
	public static TpSubjectType from_int(int value)
	{
		switch (value) {
			case _P_SUBJECT_UNDEFINED: return P_SUBJECT_UNDEFINED;
			case _P_SUBJECT_CLIENT_APP: return P_SUBJECT_CLIENT_APP;
			case _P_SUBJECT_FW: return P_SUBJECT_FW;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpSubjectType(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
