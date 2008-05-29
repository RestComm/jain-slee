package org.csapi.cc.mmccs;
/**
 *	Generated from IDL definition of enum "TpMediaStreamDirection"
 *	@author JacORB IDL compiler 
 */

public final class TpMediaStreamDirection
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _P_SEND_ONLY = 0;
	public static final TpMediaStreamDirection P_SEND_ONLY = new TpMediaStreamDirection(_P_SEND_ONLY);
	public static final int _P_RECEIVE_ONLY = 1;
	public static final TpMediaStreamDirection P_RECEIVE_ONLY = new TpMediaStreamDirection(_P_RECEIVE_ONLY);
	public static final int _P_SEND_RECEIVE = 2;
	public static final TpMediaStreamDirection P_SEND_RECEIVE = new TpMediaStreamDirection(_P_SEND_RECEIVE);
	public int value()
	{
		return value;
	}
	public static TpMediaStreamDirection from_int(int value)
	{
		switch (value) {
			case _P_SEND_ONLY: return P_SEND_ONLY;
			case _P_RECEIVE_ONLY: return P_RECEIVE_ONLY;
			case _P_SEND_RECEIVE: return P_SEND_RECEIVE;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpMediaStreamDirection(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
