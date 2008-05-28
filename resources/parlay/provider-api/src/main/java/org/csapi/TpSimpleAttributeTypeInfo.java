package org.csapi;
/**
 *	Generated from IDL definition of enum "TpSimpleAttributeTypeInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpSimpleAttributeTypeInfo
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _P_BOOLEAN = 0;
	public static final TpSimpleAttributeTypeInfo P_BOOLEAN = new TpSimpleAttributeTypeInfo(_P_BOOLEAN);
	public static final int _P_OCTET = 1;
	public static final TpSimpleAttributeTypeInfo P_OCTET = new TpSimpleAttributeTypeInfo(_P_OCTET);
	public static final int _P_CHAR = 2;
	public static final TpSimpleAttributeTypeInfo P_CHAR = new TpSimpleAttributeTypeInfo(_P_CHAR);
	public static final int _P_WCHAR = 3;
	public static final TpSimpleAttributeTypeInfo P_WCHAR = new TpSimpleAttributeTypeInfo(_P_WCHAR);
	public static final int _P_STRING = 4;
	public static final TpSimpleAttributeTypeInfo P_STRING = new TpSimpleAttributeTypeInfo(_P_STRING);
	public static final int _P_WSTRING = 5;
	public static final TpSimpleAttributeTypeInfo P_WSTRING = new TpSimpleAttributeTypeInfo(_P_WSTRING);
	public static final int _P_INT16 = 6;
	public static final TpSimpleAttributeTypeInfo P_INT16 = new TpSimpleAttributeTypeInfo(_P_INT16);
	public static final int _P_UNSIGNED_INT16 = 7;
	public static final TpSimpleAttributeTypeInfo P_UNSIGNED_INT16 = new TpSimpleAttributeTypeInfo(_P_UNSIGNED_INT16);
	public static final int _P_INT32 = 8;
	public static final TpSimpleAttributeTypeInfo P_INT32 = new TpSimpleAttributeTypeInfo(_P_INT32);
	public static final int _P_UNSIGNED_INT32 = 9;
	public static final TpSimpleAttributeTypeInfo P_UNSIGNED_INT32 = new TpSimpleAttributeTypeInfo(_P_UNSIGNED_INT32);
	public static final int _P_INT64 = 10;
	public static final TpSimpleAttributeTypeInfo P_INT64 = new TpSimpleAttributeTypeInfo(_P_INT64);
	public static final int _P_UNSIGNED_INT64 = 11;
	public static final TpSimpleAttributeTypeInfo P_UNSIGNED_INT64 = new TpSimpleAttributeTypeInfo(_P_UNSIGNED_INT64);
	public static final int _P_FLOAT = 12;
	public static final TpSimpleAttributeTypeInfo P_FLOAT = new TpSimpleAttributeTypeInfo(_P_FLOAT);
	public static final int _P_DOUBLE = 13;
	public static final TpSimpleAttributeTypeInfo P_DOUBLE = new TpSimpleAttributeTypeInfo(_P_DOUBLE);
	public static final int _P_FIXED = 14;
	public static final TpSimpleAttributeTypeInfo P_FIXED = new TpSimpleAttributeTypeInfo(_P_FIXED);
	public int value()
	{
		return value;
	}
	public static TpSimpleAttributeTypeInfo from_int(int value)
	{
		switch (value) {
			case _P_BOOLEAN: return P_BOOLEAN;
			case _P_OCTET: return P_OCTET;
			case _P_CHAR: return P_CHAR;
			case _P_WCHAR: return P_WCHAR;
			case _P_STRING: return P_STRING;
			case _P_WSTRING: return P_WSTRING;
			case _P_INT16: return P_INT16;
			case _P_UNSIGNED_INT16: return P_UNSIGNED_INT16;
			case _P_INT32: return P_INT32;
			case _P_UNSIGNED_INT32: return P_UNSIGNED_INT32;
			case _P_INT64: return P_INT64;
			case _P_UNSIGNED_INT64: return P_UNSIGNED_INT64;
			case _P_FLOAT: return P_FLOAT;
			case _P_DOUBLE: return P_DOUBLE;
			case _P_FIXED: return P_FIXED;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpSimpleAttributeTypeInfo(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
