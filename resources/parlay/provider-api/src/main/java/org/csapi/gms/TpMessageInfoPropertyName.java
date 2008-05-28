package org.csapi.gms;
/**
 *	Generated from IDL definition of enum "TpMessageInfoPropertyName"
 *	@author JacORB IDL compiler 
 */

public final class TpMessageInfoPropertyName
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _P_MESSAGING_MESSAGE_UNDEFINED = 0;
	public static final TpMessageInfoPropertyName P_MESSAGING_MESSAGE_UNDEFINED = new TpMessageInfoPropertyName(_P_MESSAGING_MESSAGE_UNDEFINED);
	public static final int _P_MESSAGING_MESSAGE_ID = 1;
	public static final TpMessageInfoPropertyName P_MESSAGING_MESSAGE_ID = new TpMessageInfoPropertyName(_P_MESSAGING_MESSAGE_ID);
	public static final int _P_MESSAGING_MESSAGE_SUBJECT = 2;
	public static final TpMessageInfoPropertyName P_MESSAGING_MESSAGE_SUBJECT = new TpMessageInfoPropertyName(_P_MESSAGING_MESSAGE_SUBJECT);
	public static final int _P_MESSAGING_MESSAGE_DATE_SENT = 3;
	public static final TpMessageInfoPropertyName P_MESSAGING_MESSAGE_DATE_SENT = new TpMessageInfoPropertyName(_P_MESSAGING_MESSAGE_DATE_SENT);
	public static final int _P_MESSAGING_MESSAGE_DATE_RECEIVED = 4;
	public static final TpMessageInfoPropertyName P_MESSAGING_MESSAGE_DATE_RECEIVED = new TpMessageInfoPropertyName(_P_MESSAGING_MESSAGE_DATE_RECEIVED);
	public static final int _P_MESSAGING_MESSAGE_DATE_CHANGED = 5;
	public static final TpMessageInfoPropertyName P_MESSAGING_MESSAGE_DATE_CHANGED = new TpMessageInfoPropertyName(_P_MESSAGING_MESSAGE_DATE_CHANGED);
	public static final int _P_MESSAGING_MESSAGE_SENT_FROM = 6;
	public static final TpMessageInfoPropertyName P_MESSAGING_MESSAGE_SENT_FROM = new TpMessageInfoPropertyName(_P_MESSAGING_MESSAGE_SENT_FROM);
	public static final int _P_MESSAGING_MESSAGE_SENT_TO = 7;
	public static final TpMessageInfoPropertyName P_MESSAGING_MESSAGE_SENT_TO = new TpMessageInfoPropertyName(_P_MESSAGING_MESSAGE_SENT_TO);
	public static final int _P_MESSAGING_MESSAGE_CC_TO = 8;
	public static final TpMessageInfoPropertyName P_MESSAGING_MESSAGE_CC_TO = new TpMessageInfoPropertyName(_P_MESSAGING_MESSAGE_CC_TO);
	public static final int _P_MESSAGING_MESSAGE_BCC_TO = 9;
	public static final TpMessageInfoPropertyName P_MESSAGING_MESSAGE_BCC_TO = new TpMessageInfoPropertyName(_P_MESSAGING_MESSAGE_BCC_TO);
	public static final int _P_MESSAGING_MESSAGE_SIZE = 10;
	public static final TpMessageInfoPropertyName P_MESSAGING_MESSAGE_SIZE = new TpMessageInfoPropertyName(_P_MESSAGING_MESSAGE_SIZE);
	public static final int _P_MESSAGING_MESSAGE_PRIORITY = 11;
	public static final TpMessageInfoPropertyName P_MESSAGING_MESSAGE_PRIORITY = new TpMessageInfoPropertyName(_P_MESSAGING_MESSAGE_PRIORITY);
	public static final int _P_MESSAGING_MESSAGE_FORMAT = 12;
	public static final TpMessageInfoPropertyName P_MESSAGING_MESSAGE_FORMAT = new TpMessageInfoPropertyName(_P_MESSAGING_MESSAGE_FORMAT);
	public static final int _P_MESSAGING_MESSAGE_FOLDER = 13;
	public static final TpMessageInfoPropertyName P_MESSAGING_MESSAGE_FOLDER = new TpMessageInfoPropertyName(_P_MESSAGING_MESSAGE_FOLDER);
	public static final int _P_MESSAGING_MESSAGE_STATUS = 14;
	public static final TpMessageInfoPropertyName P_MESSAGING_MESSAGE_STATUS = new TpMessageInfoPropertyName(_P_MESSAGING_MESSAGE_STATUS);
	public int value()
	{
		return value;
	}
	public static TpMessageInfoPropertyName from_int(int value)
	{
		switch (value) {
			case _P_MESSAGING_MESSAGE_UNDEFINED: return P_MESSAGING_MESSAGE_UNDEFINED;
			case _P_MESSAGING_MESSAGE_ID: return P_MESSAGING_MESSAGE_ID;
			case _P_MESSAGING_MESSAGE_SUBJECT: return P_MESSAGING_MESSAGE_SUBJECT;
			case _P_MESSAGING_MESSAGE_DATE_SENT: return P_MESSAGING_MESSAGE_DATE_SENT;
			case _P_MESSAGING_MESSAGE_DATE_RECEIVED: return P_MESSAGING_MESSAGE_DATE_RECEIVED;
			case _P_MESSAGING_MESSAGE_DATE_CHANGED: return P_MESSAGING_MESSAGE_DATE_CHANGED;
			case _P_MESSAGING_MESSAGE_SENT_FROM: return P_MESSAGING_MESSAGE_SENT_FROM;
			case _P_MESSAGING_MESSAGE_SENT_TO: return P_MESSAGING_MESSAGE_SENT_TO;
			case _P_MESSAGING_MESSAGE_CC_TO: return P_MESSAGING_MESSAGE_CC_TO;
			case _P_MESSAGING_MESSAGE_BCC_TO: return P_MESSAGING_MESSAGE_BCC_TO;
			case _P_MESSAGING_MESSAGE_SIZE: return P_MESSAGING_MESSAGE_SIZE;
			case _P_MESSAGING_MESSAGE_PRIORITY: return P_MESSAGING_MESSAGE_PRIORITY;
			case _P_MESSAGING_MESSAGE_FORMAT: return P_MESSAGING_MESSAGE_FORMAT;
			case _P_MESSAGING_MESSAGE_FOLDER: return P_MESSAGING_MESSAGE_FOLDER;
			case _P_MESSAGING_MESSAGE_STATUS: return P_MESSAGING_MESSAGE_STATUS;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpMessageInfoPropertyName(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
