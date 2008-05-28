package org.csapi.cc.cccs;
/**
 *	Generated from IDL definition of enum "TpVideoHandlingType"
 *	@author JacORB IDL compiler 
 */

public final class TpVideoHandlingType
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _P_MIXED_VIDEO = 0;
	public static final TpVideoHandlingType P_MIXED_VIDEO = new TpVideoHandlingType(_P_MIXED_VIDEO);
	public static final int _P_SWITCHED_VIDEO_CHAIR_CONTROLLED = 1;
	public static final TpVideoHandlingType P_SWITCHED_VIDEO_CHAIR_CONTROLLED = new TpVideoHandlingType(_P_SWITCHED_VIDEO_CHAIR_CONTROLLED);
	public static final int _P_SWITCHED_VIDEO_VOICE_CONTROLLED = 2;
	public static final TpVideoHandlingType P_SWITCHED_VIDEO_VOICE_CONTROLLED = new TpVideoHandlingType(_P_SWITCHED_VIDEO_VOICE_CONTROLLED);
	public int value()
	{
		return value;
	}
	public static TpVideoHandlingType from_int(int value)
	{
		switch (value) {
			case _P_MIXED_VIDEO: return P_MIXED_VIDEO;
			case _P_SWITCHED_VIDEO_CHAIR_CONTROLLED: return P_SWITCHED_VIDEO_CHAIR_CONTROLLED;
			case _P_SWITCHED_VIDEO_VOICE_CONTROLLED: return P_SWITCHED_VIDEO_VOICE_CONTROLLED;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpVideoHandlingType(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
