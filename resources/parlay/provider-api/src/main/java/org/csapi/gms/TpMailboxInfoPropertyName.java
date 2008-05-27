package org.csapi.gms;
/**
 *	Generated from IDL definition of enum "TpMailboxInfoPropertyName"
 *	@author JacORB IDL compiler 
 */

public final class TpMailboxInfoPropertyName
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _P_MESSAGING_MAILBOX_UNDEFINED = 0;
	public static final TpMailboxInfoPropertyName P_MESSAGING_MAILBOX_UNDEFINED = new TpMailboxInfoPropertyName(_P_MESSAGING_MAILBOX_UNDEFINED);
	public static final int _P_MESSAGING_MAILBOX_ID = 1;
	public static final TpMailboxInfoPropertyName P_MESSAGING_MAILBOX_ID = new TpMailboxInfoPropertyName(_P_MESSAGING_MAILBOX_ID);
	public static final int _P_MESSAGING_MAILBOX_OWNER = 2;
	public static final TpMailboxInfoPropertyName P_MESSAGING_MAILBOX_OWNER = new TpMailboxInfoPropertyName(_P_MESSAGING_MAILBOX_OWNER);
	public static final int _P_MESSAGING_MAILBOX_FOLDER = 3;
	public static final TpMailboxInfoPropertyName P_MESSAGING_MAILBOX_FOLDER = new TpMailboxInfoPropertyName(_P_MESSAGING_MAILBOX_FOLDER);
	public static final int _P_MESSAGING_MAILBOX_DATE_CREATED = 4;
	public static final TpMailboxInfoPropertyName P_MESSAGING_MAILBOX_DATE_CREATED = new TpMailboxInfoPropertyName(_P_MESSAGING_MAILBOX_DATE_CREATED);
	public static final int _P_MESSAGING_MAILBOX_DATE_CHANGED = 5;
	public static final TpMailboxInfoPropertyName P_MESSAGING_MAILBOX_DATE_CHANGED = new TpMailboxInfoPropertyName(_P_MESSAGING_MAILBOX_DATE_CHANGED);
	public int value()
	{
		return value;
	}
	public static TpMailboxInfoPropertyName from_int(int value)
	{
		switch (value) {
			case _P_MESSAGING_MAILBOX_UNDEFINED: return P_MESSAGING_MAILBOX_UNDEFINED;
			case _P_MESSAGING_MAILBOX_ID: return P_MESSAGING_MAILBOX_ID;
			case _P_MESSAGING_MAILBOX_OWNER: return P_MESSAGING_MAILBOX_OWNER;
			case _P_MESSAGING_MAILBOX_FOLDER: return P_MESSAGING_MAILBOX_FOLDER;
			case _P_MESSAGING_MAILBOX_DATE_CREATED: return P_MESSAGING_MAILBOX_DATE_CREATED;
			case _P_MESSAGING_MAILBOX_DATE_CHANGED: return P_MESSAGING_MAILBOX_DATE_CHANGED;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpMailboxInfoPropertyName(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
