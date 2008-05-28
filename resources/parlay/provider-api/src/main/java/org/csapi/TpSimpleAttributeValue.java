package org.csapi;

/**
 *	Generated from IDL definition of union "TpSimpleAttributeValue"
 *	@author JacORB IDL compiler 
 */

public final class TpSimpleAttributeValue
	implements org.omg.CORBA.portable.IDLEntity
{
	private org.csapi.TpSimpleAttributeTypeInfo discriminator;
	private boolean BooleanValue;
	private byte OctetValue;
	private char CharValue;
	private char WCharValue;
	private java.lang.String StringValue;
	private java.lang.String WStringValue;
	private short Int16Value;
	private short UnsignedInt16Value;
	private int Int32Value;
	private int UnsignedInt32Value;
	private long Int64Value;
	private long UnsignedInt64Value;
	private float FloatValue;
	private double DoubleValue;
	private float FixedValue;

	public TpSimpleAttributeValue ()
	{
	}

	public org.csapi.TpSimpleAttributeTypeInfo discriminator ()
	{
		return discriminator;
	}

	public boolean BooleanValue ()
	{
		if (discriminator != org.csapi.TpSimpleAttributeTypeInfo.P_BOOLEAN)
			throw new org.omg.CORBA.BAD_OPERATION();
		return BooleanValue;
	}

	public void BooleanValue (boolean _x)
	{
		discriminator = org.csapi.TpSimpleAttributeTypeInfo.P_BOOLEAN;
		BooleanValue = _x;
	}

	public byte OctetValue ()
	{
		if (discriminator != org.csapi.TpSimpleAttributeTypeInfo.P_OCTET)
			throw new org.omg.CORBA.BAD_OPERATION();
		return OctetValue;
	}

	public void OctetValue (byte _x)
	{
		discriminator = org.csapi.TpSimpleAttributeTypeInfo.P_OCTET;
		OctetValue = _x;
	}

	public char CharValue ()
	{
		if (discriminator != org.csapi.TpSimpleAttributeTypeInfo.P_CHAR)
			throw new org.omg.CORBA.BAD_OPERATION();
		return CharValue;
	}

	public void CharValue (char _x)
	{
		discriminator = org.csapi.TpSimpleAttributeTypeInfo.P_CHAR;
		CharValue = _x;
	}

	public char WCharValue ()
	{
		if (discriminator != org.csapi.TpSimpleAttributeTypeInfo.P_WCHAR)
			throw new org.omg.CORBA.BAD_OPERATION();
		return WCharValue;
	}

	public void WCharValue (char _x)
	{
		discriminator = org.csapi.TpSimpleAttributeTypeInfo.P_WCHAR;
		WCharValue = _x;
	}

	public java.lang.String StringValue ()
	{
		if (discriminator != org.csapi.TpSimpleAttributeTypeInfo.P_STRING)
			throw new org.omg.CORBA.BAD_OPERATION();
		return StringValue;
	}

	public void StringValue (java.lang.String _x)
	{
		discriminator = org.csapi.TpSimpleAttributeTypeInfo.P_STRING;
		StringValue = _x;
	}

	public java.lang.String WStringValue ()
	{
		if (discriminator != org.csapi.TpSimpleAttributeTypeInfo.P_WSTRING)
			throw new org.omg.CORBA.BAD_OPERATION();
		return WStringValue;
	}

	public void WStringValue (java.lang.String _x)
	{
		discriminator = org.csapi.TpSimpleAttributeTypeInfo.P_WSTRING;
		WStringValue = _x;
	}

	public short Int16Value ()
	{
		if (discriminator != org.csapi.TpSimpleAttributeTypeInfo.P_INT16)
			throw new org.omg.CORBA.BAD_OPERATION();
		return Int16Value;
	}

	public void Int16Value (short _x)
	{
		discriminator = org.csapi.TpSimpleAttributeTypeInfo.P_INT16;
		Int16Value = _x;
	}

	public short UnsignedInt16Value ()
	{
		if (discriminator != org.csapi.TpSimpleAttributeTypeInfo.P_UNSIGNED_INT16)
			throw new org.omg.CORBA.BAD_OPERATION();
		return UnsignedInt16Value;
	}

	public void UnsignedInt16Value (short _x)
	{
		discriminator = org.csapi.TpSimpleAttributeTypeInfo.P_UNSIGNED_INT16;
		UnsignedInt16Value = _x;
	}

	public int Int32Value ()
	{
		if (discriminator != org.csapi.TpSimpleAttributeTypeInfo.P_INT32)
			throw new org.omg.CORBA.BAD_OPERATION();
		return Int32Value;
	}

	public void Int32Value (int _x)
	{
		discriminator = org.csapi.TpSimpleAttributeTypeInfo.P_INT32;
		Int32Value = _x;
	}

	public int UnsignedInt32Value ()
	{
		if (discriminator != org.csapi.TpSimpleAttributeTypeInfo.P_UNSIGNED_INT32)
			throw new org.omg.CORBA.BAD_OPERATION();
		return UnsignedInt32Value;
	}

	public void UnsignedInt32Value (int _x)
	{
		discriminator = org.csapi.TpSimpleAttributeTypeInfo.P_UNSIGNED_INT32;
		UnsignedInt32Value = _x;
	}

	public long Int64Value ()
	{
		if (discriminator != org.csapi.TpSimpleAttributeTypeInfo.P_INT64)
			throw new org.omg.CORBA.BAD_OPERATION();
		return Int64Value;
	}

	public void Int64Value (long _x)
	{
		discriminator = org.csapi.TpSimpleAttributeTypeInfo.P_INT64;
		Int64Value = _x;
	}

	public long UnsignedInt64Value ()
	{
		if (discriminator != org.csapi.TpSimpleAttributeTypeInfo.P_UNSIGNED_INT64)
			throw new org.omg.CORBA.BAD_OPERATION();
		return UnsignedInt64Value;
	}

	public void UnsignedInt64Value (long _x)
	{
		discriminator = org.csapi.TpSimpleAttributeTypeInfo.P_UNSIGNED_INT64;
		UnsignedInt64Value = _x;
	}

	public float FloatValue ()
	{
		if (discriminator != org.csapi.TpSimpleAttributeTypeInfo.P_FLOAT)
			throw new org.omg.CORBA.BAD_OPERATION();
		return FloatValue;
	}

	public void FloatValue (float _x)
	{
		discriminator = org.csapi.TpSimpleAttributeTypeInfo.P_FLOAT;
		FloatValue = _x;
	}

	public double DoubleValue ()
	{
		if (discriminator != org.csapi.TpSimpleAttributeTypeInfo.P_DOUBLE)
			throw new org.omg.CORBA.BAD_OPERATION();
		return DoubleValue;
	}

	public void DoubleValue (double _x)
	{
		discriminator = org.csapi.TpSimpleAttributeTypeInfo.P_DOUBLE;
		DoubleValue = _x;
	}

	public float FixedValue ()
	{
		if (discriminator != org.csapi.TpSimpleAttributeTypeInfo.P_FIXED)
			throw new org.omg.CORBA.BAD_OPERATION();
		return FixedValue;
	}

	public void FixedValue (float _x)
	{
		discriminator = org.csapi.TpSimpleAttributeTypeInfo.P_FIXED;
		FixedValue = _x;
	}

}
