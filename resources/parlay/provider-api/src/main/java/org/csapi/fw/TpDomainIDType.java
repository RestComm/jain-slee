package org.csapi.fw;
/**
 *	Generated from IDL definition of enum "TpDomainIDType"
 *	@author JacORB IDL compiler 
 */

public final class TpDomainIDType
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _P_FW = 0;
	public static final TpDomainIDType P_FW = new TpDomainIDType(_P_FW);
	public static final int _P_CLIENT_APPLICATION = 1;
	public static final TpDomainIDType P_CLIENT_APPLICATION = new TpDomainIDType(_P_CLIENT_APPLICATION);
	public static final int _P_ENT_OP = 2;
	public static final TpDomainIDType P_ENT_OP = new TpDomainIDType(_P_ENT_OP);
	public static final int _P_SERVICE_INSTANCE = 3;
	public static final TpDomainIDType P_SERVICE_INSTANCE = new TpDomainIDType(_P_SERVICE_INSTANCE);
	public static final int _P_SERVICE_SUPPLIER = 4;
	public static final TpDomainIDType P_SERVICE_SUPPLIER = new TpDomainIDType(_P_SERVICE_SUPPLIER);
	public int value()
	{
		return value;
	}
	public static TpDomainIDType from_int(int value)
	{
		switch (value) {
			case _P_FW: return P_FW;
			case _P_CLIENT_APPLICATION: return P_CLIENT_APPLICATION;
			case _P_ENT_OP: return P_ENT_OP;
			case _P_SERVICE_INSTANCE: return P_SERVICE_INSTANCE;
			case _P_SERVICE_SUPPLIER: return P_SERVICE_SUPPLIER;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpDomainIDType(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
