package org.csapi.gms;
/**
 *	Generated from IDL definition of enum "TpMessagingFault"
 *	@author JacORB IDL compiler 
 */

public final class TpMessagingFault
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _P_MESSAGING_FAULT_UNDEFINED = 0;
	public static final TpMessagingFault P_MESSAGING_FAULT_UNDEFINED = new TpMessagingFault(_P_MESSAGING_FAULT_UNDEFINED);
	public int value()
	{
		return value;
	}
	public static TpMessagingFault from_int(int value)
	{
		switch (value) {
			case _P_MESSAGING_FAULT_UNDEFINED: return P_MESSAGING_FAULT_UNDEFINED;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpMessagingFault(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
