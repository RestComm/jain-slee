package org.csapi.cc.mmccs;
/**
 *	Generated from IDL definition of enum "TpMediaStreamDataTypeRequestType"
 *	@author JacORB IDL compiler 
 */

public final class TpMediaStreamDataTypeRequestType
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _P_AUDIO_CAPABILITIES = 0;
	public static final TpMediaStreamDataTypeRequestType P_AUDIO_CAPABILITIES = new TpMediaStreamDataTypeRequestType(_P_AUDIO_CAPABILITIES);
	public static final int _P_VIDEO_CAPABILITIES = 1;
	public static final TpMediaStreamDataTypeRequestType P_VIDEO_CAPABILITIES = new TpMediaStreamDataTypeRequestType(_P_VIDEO_CAPABILITIES);
	public static final int _P_DATA_CAPABILITIES = 2;
	public static final TpMediaStreamDataTypeRequestType P_DATA_CAPABILITIES = new TpMediaStreamDataTypeRequestType(_P_DATA_CAPABILITIES);
	public int value()
	{
		return value;
	}
	public static TpMediaStreamDataTypeRequestType from_int(int value)
	{
		switch (value) {
			case _P_AUDIO_CAPABILITIES: return P_AUDIO_CAPABILITIES;
			case _P_VIDEO_CAPABILITIES: return P_VIDEO_CAPABILITIES;
			case _P_DATA_CAPABILITIES: return P_DATA_CAPABILITIES;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpMediaStreamDataTypeRequestType(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
