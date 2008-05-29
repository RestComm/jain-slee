package org.csapi.cc;
/**
 *	Generated from IDL definition of enum "TpCallLegAttachMechanism"
 *	@author JacORB IDL compiler 
 */

public final class TpCallLegAttachMechanism
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _P_CALLLEG_ATTACH_IMPLICITLY = 0;
	public static final TpCallLegAttachMechanism P_CALLLEG_ATTACH_IMPLICITLY = new TpCallLegAttachMechanism(_P_CALLLEG_ATTACH_IMPLICITLY);
	public static final int _P_CALLLEG_ATTACH_EXPLICITLY = 1;
	public static final TpCallLegAttachMechanism P_CALLLEG_ATTACH_EXPLICITLY = new TpCallLegAttachMechanism(_P_CALLLEG_ATTACH_EXPLICITLY);
	public int value()
	{
		return value;
	}
	public static TpCallLegAttachMechanism from_int(int value)
	{
		switch (value) {
			case _P_CALLLEG_ATTACH_IMPLICITLY: return P_CALLLEG_ATTACH_IMPLICITLY;
			case _P_CALLLEG_ATTACH_EXPLICITLY: return P_CALLLEG_ATTACH_EXPLICITLY;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpCallLegAttachMechanism(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
