package org.csapi;
/**
 *	Generated from IDL definition of enum "TpDataSessionQosClass"
 *	@author JacORB IDL compiler 
 */

public final class TpDataSessionQosClass
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _P_DATA_SESSION_QOS_CLASS_CONVERSATIONAL = 0;
	public static final TpDataSessionQosClass P_DATA_SESSION_QOS_CLASS_CONVERSATIONAL = new TpDataSessionQosClass(_P_DATA_SESSION_QOS_CLASS_CONVERSATIONAL);
	public static final int _P_DATA_SESSION_QOS_CLASS_STREAMING = 1;
	public static final TpDataSessionQosClass P_DATA_SESSION_QOS_CLASS_STREAMING = new TpDataSessionQosClass(_P_DATA_SESSION_QOS_CLASS_STREAMING);
	public static final int _P_DATA_SESSION_QOS_CLASS_INTERACTIVE = 2;
	public static final TpDataSessionQosClass P_DATA_SESSION_QOS_CLASS_INTERACTIVE = new TpDataSessionQosClass(_P_DATA_SESSION_QOS_CLASS_INTERACTIVE);
	public static final int _P_DATA_SESSION_QOS_CLASS_BACKGROUND = 3;
	public static final TpDataSessionQosClass P_DATA_SESSION_QOS_CLASS_BACKGROUND = new TpDataSessionQosClass(_P_DATA_SESSION_QOS_CLASS_BACKGROUND);
	public int value()
	{
		return value;
	}
	public static TpDataSessionQosClass from_int(int value)
	{
		switch (value) {
			case _P_DATA_SESSION_QOS_CLASS_CONVERSATIONAL: return P_DATA_SESSION_QOS_CLASS_CONVERSATIONAL;
			case _P_DATA_SESSION_QOS_CLASS_STREAMING: return P_DATA_SESSION_QOS_CLASS_STREAMING;
			case _P_DATA_SESSION_QOS_CLASS_INTERACTIVE: return P_DATA_SESSION_QOS_CLASS_INTERACTIVE;
			case _P_DATA_SESSION_QOS_CLASS_BACKGROUND: return P_DATA_SESSION_QOS_CLASS_BACKGROUND;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpDataSessionQosClass(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
