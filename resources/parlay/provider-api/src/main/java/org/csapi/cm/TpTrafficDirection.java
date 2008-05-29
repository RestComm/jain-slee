package org.csapi.cm;
/**
 *	Generated from IDL definition of enum "TpTrafficDirection"
 *	@author JacORB IDL compiler 
 */

public final class TpTrafficDirection
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _UNIDIRECTIONAL = 0;
	public static final TpTrafficDirection UNIDIRECTIONAL = new TpTrafficDirection(_UNIDIRECTIONAL);
	public static final int _BIDIRECTIONAL = 1;
	public static final TpTrafficDirection BIDIRECTIONAL = new TpTrafficDirection(_BIDIRECTIONAL);
	public int value()
	{
		return value;
	}
	public static TpTrafficDirection from_int(int value)
	{
		switch (value) {
			case _UNIDIRECTIONAL: return UNIDIRECTIONAL;
			case _BIDIRECTIONAL: return BIDIRECTIONAL;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpTrafficDirection(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
