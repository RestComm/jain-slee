package org.csapi.fw;
/**
 *	Generated from IDL definition of enum "TpInterfaceFault"
 *	@author JacORB IDL compiler 
 */

public final class TpInterfaceFault
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _INTERFACE_FAULT_UNDEFINED = 0;
	public static final TpInterfaceFault INTERFACE_FAULT_UNDEFINED = new TpInterfaceFault(_INTERFACE_FAULT_UNDEFINED);
	public static final int _INTERFACE_FAULT_LOCAL_FAILURE = 1;
	public static final TpInterfaceFault INTERFACE_FAULT_LOCAL_FAILURE = new TpInterfaceFault(_INTERFACE_FAULT_LOCAL_FAILURE);
	public static final int _INTERFACE_FAULT_GATEWAY_FAILURE = 2;
	public static final TpInterfaceFault INTERFACE_FAULT_GATEWAY_FAILURE = new TpInterfaceFault(_INTERFACE_FAULT_GATEWAY_FAILURE);
	public static final int _INTERFACE_FAULT_PROTOCOL_ERROR = 3;
	public static final TpInterfaceFault INTERFACE_FAULT_PROTOCOL_ERROR = new TpInterfaceFault(_INTERFACE_FAULT_PROTOCOL_ERROR);
	public int value()
	{
		return value;
	}
	public static TpInterfaceFault from_int(int value)
	{
		switch (value) {
			case _INTERFACE_FAULT_UNDEFINED: return INTERFACE_FAULT_UNDEFINED;
			case _INTERFACE_FAULT_LOCAL_FAILURE: return INTERFACE_FAULT_LOCAL_FAILURE;
			case _INTERFACE_FAULT_GATEWAY_FAILURE: return INTERFACE_FAULT_GATEWAY_FAILURE;
			case _INTERFACE_FAULT_PROTOCOL_ERROR: return INTERFACE_FAULT_PROTOCOL_ERROR;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpInterfaceFault(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
