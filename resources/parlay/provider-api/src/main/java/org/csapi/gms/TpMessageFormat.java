package org.csapi.gms;
/**
 *	Generated from IDL definition of enum "TpMessageFormat"
 *	@author JacORB IDL compiler 
 */

public final class TpMessageFormat
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _P_MESSAGING_MESSAGE_FORMAT_UNDEFINED = 0;
	public static final TpMessageFormat P_MESSAGING_MESSAGE_FORMAT_UNDEFINED = new TpMessageFormat(_P_MESSAGING_MESSAGE_FORMAT_UNDEFINED);
	public static final int _P_MESSAGING_MESSAGE_FORMAT_TEXT = 1;
	public static final TpMessageFormat P_MESSAGING_MESSAGE_FORMAT_TEXT = new TpMessageFormat(_P_MESSAGING_MESSAGE_FORMAT_TEXT);
	public static final int _P_MESSAGING_MESSAGE_FORMAT_BINARY = 2;
	public static final TpMessageFormat P_MESSAGING_MESSAGE_FORMAT_BINARY = new TpMessageFormat(_P_MESSAGING_MESSAGE_FORMAT_BINARY);
	public static final int _P_MESSAGING_MESSAGE_FORMAT_UUENCODED = 3;
	public static final TpMessageFormat P_MESSAGING_MESSAGE_FORMAT_UUENCODED = new TpMessageFormat(_P_MESSAGING_MESSAGE_FORMAT_UUENCODED);
	public static final int _P_MESSAGING_MESSAGE_FORMAT_MIME = 4;
	public static final TpMessageFormat P_MESSAGING_MESSAGE_FORMAT_MIME = new TpMessageFormat(_P_MESSAGING_MESSAGE_FORMAT_MIME);
	public static final int _P_MESSAGING_MESSAGE_FORMAT_WAVE = 5;
	public static final TpMessageFormat P_MESSAGING_MESSAGE_FORMAT_WAVE = new TpMessageFormat(_P_MESSAGING_MESSAGE_FORMAT_WAVE);
	public static final int _P_MESSAGING_MESSAGE_FORMAT_AU = 6;
	public static final TpMessageFormat P_MESSAGING_MESSAGE_FORMAT_AU = new TpMessageFormat(_P_MESSAGING_MESSAGE_FORMAT_AU);
	public int value()
	{
		return value;
	}
	public static TpMessageFormat from_int(int value)
	{
		switch (value) {
			case _P_MESSAGING_MESSAGE_FORMAT_UNDEFINED: return P_MESSAGING_MESSAGE_FORMAT_UNDEFINED;
			case _P_MESSAGING_MESSAGE_FORMAT_TEXT: return P_MESSAGING_MESSAGE_FORMAT_TEXT;
			case _P_MESSAGING_MESSAGE_FORMAT_BINARY: return P_MESSAGING_MESSAGE_FORMAT_BINARY;
			case _P_MESSAGING_MESSAGE_FORMAT_UUENCODED: return P_MESSAGING_MESSAGE_FORMAT_UUENCODED;
			case _P_MESSAGING_MESSAGE_FORMAT_MIME: return P_MESSAGING_MESSAGE_FORMAT_MIME;
			case _P_MESSAGING_MESSAGE_FORMAT_WAVE: return P_MESSAGING_MESSAGE_FORMAT_WAVE;
			case _P_MESSAGING_MESSAGE_FORMAT_AU: return P_MESSAGING_MESSAGE_FORMAT_AU;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpMessageFormat(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
