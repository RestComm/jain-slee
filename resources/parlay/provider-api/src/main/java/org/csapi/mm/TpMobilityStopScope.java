package org.csapi.mm;
/**
 *	Generated from IDL definition of enum "TpMobilityStopScope"
 *	@author JacORB IDL compiler 
 */

public final class TpMobilityStopScope
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _P_M_ALL_IN_ASSIGNMENT = 0;
	public static final TpMobilityStopScope P_M_ALL_IN_ASSIGNMENT = new TpMobilityStopScope(_P_M_ALL_IN_ASSIGNMENT);
	public static final int _P_M_SPECIFIED_USERS = 1;
	public static final TpMobilityStopScope P_M_SPECIFIED_USERS = new TpMobilityStopScope(_P_M_SPECIFIED_USERS);
	public int value()
	{
		return value;
	}
	public static TpMobilityStopScope from_int(int value)
	{
		switch (value) {
			case _P_M_ALL_IN_ASSIGNMENT: return P_M_ALL_IN_ASSIGNMENT;
			case _P_M_SPECIFIED_USERS: return P_M_SPECIFIED_USERS;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpMobilityStopScope(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
