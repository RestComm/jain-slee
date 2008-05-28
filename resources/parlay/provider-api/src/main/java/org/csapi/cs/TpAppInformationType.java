package org.csapi.cs;
/**
 *	Generated from IDL definition of enum "TpAppInformationType"
 *	@author JacORB IDL compiler 
 */

public final class TpAppInformationType
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _P_APP_INF_TIMESTAMP = 0;
	public static final TpAppInformationType P_APP_INF_TIMESTAMP = new TpAppInformationType(_P_APP_INF_TIMESTAMP);
	public int value()
	{
		return value;
	}
	public static TpAppInformationType from_int(int value)
	{
		switch (value) {
			case _P_APP_INF_TIMESTAMP: return P_APP_INF_TIMESTAMP;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpAppInformationType(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
