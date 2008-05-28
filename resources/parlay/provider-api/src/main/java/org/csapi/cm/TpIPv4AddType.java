package org.csapi.cm;
/**
 *	Generated from IDL definition of enum "TpIPv4AddType"
 *	@author JacORB IDL compiler 
 */

public final class TpIPv4AddType
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _IPV4_ADD_CLASS_A = 0;
	public static final TpIPv4AddType IPV4_ADD_CLASS_A = new TpIPv4AddType(_IPV4_ADD_CLASS_A);
	public static final int _IPV4_ADD_CLASS_B = 1;
	public static final TpIPv4AddType IPV4_ADD_CLASS_B = new TpIPv4AddType(_IPV4_ADD_CLASS_B);
	public static final int _IPV4_ADD_CLASS_C = 2;
	public static final TpIPv4AddType IPV4_ADD_CLASS_C = new TpIPv4AddType(_IPV4_ADD_CLASS_C);
	public static final int _IPV4_ADD_CLASS_D = 3;
	public static final TpIPv4AddType IPV4_ADD_CLASS_D = new TpIPv4AddType(_IPV4_ADD_CLASS_D);
	public static final int _IPV4_ADD_CLASS_E = 4;
	public static final TpIPv4AddType IPV4_ADD_CLASS_E = new TpIPv4AddType(_IPV4_ADD_CLASS_E);
	public int value()
	{
		return value;
	}
	public static TpIPv4AddType from_int(int value)
	{
		switch (value) {
			case _IPV4_ADD_CLASS_A: return IPV4_ADD_CLASS_A;
			case _IPV4_ADD_CLASS_B: return IPV4_ADD_CLASS_B;
			case _IPV4_ADD_CLASS_C: return IPV4_ADD_CLASS_C;
			case _IPV4_ADD_CLASS_D: return IPV4_ADD_CLASS_D;
			case _IPV4_ADD_CLASS_E: return IPV4_ADD_CLASS_E;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpIPv4AddType(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
