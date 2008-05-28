package org.csapi.cc.mmccs;
/**
 *	Generated from IDL definition of enum "TpMediaStreamEventType"
 *	@author JacORB IDL compiler 
 */

public final class TpMediaStreamEventType
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _P_MEDIA_STREAM_ADDED = 0;
	public static final TpMediaStreamEventType P_MEDIA_STREAM_ADDED = new TpMediaStreamEventType(_P_MEDIA_STREAM_ADDED);
	public static final int _P_MEDIA_STREAM_SUBTRACTED = 1;
	public static final TpMediaStreamEventType P_MEDIA_STREAM_SUBTRACTED = new TpMediaStreamEventType(_P_MEDIA_STREAM_SUBTRACTED);
	public int value()
	{
		return value;
	}
	public static TpMediaStreamEventType from_int(int value)
	{
		switch (value) {
			case _P_MEDIA_STREAM_ADDED: return P_MEDIA_STREAM_ADDED;
			case _P_MEDIA_STREAM_SUBTRACTED: return P_MEDIA_STREAM_SUBTRACTED;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpMediaStreamEventType(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
