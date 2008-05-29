package org.csapi.gms;
/**
 *	Generated from IDL definition of enum "TpFolderInfoPropertyName"
 *	@author JacORB IDL compiler 
 */

public final class TpFolderInfoPropertyName
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _P_MESSAGING_FOLDER_UNDEFINED = 0;
	public static final TpFolderInfoPropertyName P_MESSAGING_FOLDER_UNDEFINED = new TpFolderInfoPropertyName(_P_MESSAGING_FOLDER_UNDEFINED);
	public static final int _P_MESSAGING_FOLDER_ID = 1;
	public static final TpFolderInfoPropertyName P_MESSAGING_FOLDER_ID = new TpFolderInfoPropertyName(_P_MESSAGING_FOLDER_ID);
	public static final int _P_MESSAGING_FOLDER_MESSAGE = 2;
	public static final TpFolderInfoPropertyName P_MESSAGING_FOLDER_MESSAGE = new TpFolderInfoPropertyName(_P_MESSAGING_FOLDER_MESSAGE);
	public static final int _P_MESSAGING_FOLDER_SUBFOLDER = 3;
	public static final TpFolderInfoPropertyName P_MESSAGING_FOLDER_SUBFOLDER = new TpFolderInfoPropertyName(_P_MESSAGING_FOLDER_SUBFOLDER);
	public static final int _P_MESSAGING_FOLDER_DATE_CREATED = 4;
	public static final TpFolderInfoPropertyName P_MESSAGING_FOLDER_DATE_CREATED = new TpFolderInfoPropertyName(_P_MESSAGING_FOLDER_DATE_CREATED);
	public static final int _P_MESSAGING_FOLDER_DATE_CHANGED = 5;
	public static final TpFolderInfoPropertyName P_MESSAGING_FOLDER_DATE_CHANGED = new TpFolderInfoPropertyName(_P_MESSAGING_FOLDER_DATE_CHANGED);
	public int value()
	{
		return value;
	}
	public static TpFolderInfoPropertyName from_int(int value)
	{
		switch (value) {
			case _P_MESSAGING_FOLDER_UNDEFINED: return P_MESSAGING_FOLDER_UNDEFINED;
			case _P_MESSAGING_FOLDER_ID: return P_MESSAGING_FOLDER_ID;
			case _P_MESSAGING_FOLDER_MESSAGE: return P_MESSAGING_FOLDER_MESSAGE;
			case _P_MESSAGING_FOLDER_SUBFOLDER: return P_MESSAGING_FOLDER_SUBFOLDER;
			case _P_MESSAGING_FOLDER_DATE_CREATED: return P_MESSAGING_FOLDER_DATE_CREATED;
			case _P_MESSAGING_FOLDER_DATE_CHANGED: return P_MESSAGING_FOLDER_DATE_CHANGED;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpFolderInfoPropertyName(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
