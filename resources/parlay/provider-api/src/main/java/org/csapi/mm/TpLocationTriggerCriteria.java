package org.csapi.mm;
/**
 *	Generated from IDL definition of enum "TpLocationTriggerCriteria"
 *	@author JacORB IDL compiler 
 */

public final class TpLocationTriggerCriteria
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _P_UL_ENTERING_AREA = 0;
	public static final TpLocationTriggerCriteria P_UL_ENTERING_AREA = new TpLocationTriggerCriteria(_P_UL_ENTERING_AREA);
	public static final int _P_UL_LEAVING_AREA = 1;
	public static final TpLocationTriggerCriteria P_UL_LEAVING_AREA = new TpLocationTriggerCriteria(_P_UL_LEAVING_AREA);
	public int value()
	{
		return value;
	}
	public static TpLocationTriggerCriteria from_int(int value)
	{
		switch (value) {
			case _P_UL_ENTERING_AREA: return P_UL_ENTERING_AREA;
			case _P_UL_LEAVING_AREA: return P_UL_LEAVING_AREA;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpLocationTriggerCriteria(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
