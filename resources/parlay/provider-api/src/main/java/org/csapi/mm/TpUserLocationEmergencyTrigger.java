package org.csapi.mm;
/**
 *	Generated from IDL definition of enum "TpUserLocationEmergencyTrigger"
 *	@author JacORB IDL compiler 
 */

public final class TpUserLocationEmergencyTrigger
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _P_ULE_CALL_ORIGINATION = 0;
	public static final TpUserLocationEmergencyTrigger P_ULE_CALL_ORIGINATION = new TpUserLocationEmergencyTrigger(_P_ULE_CALL_ORIGINATION);
	public static final int _P_ULE_CALL_RELEASE = 1;
	public static final TpUserLocationEmergencyTrigger P_ULE_CALL_RELEASE = new TpUserLocationEmergencyTrigger(_P_ULE_CALL_RELEASE);
	public static final int _P_ULE_LOCATION_REQUEST = 2;
	public static final TpUserLocationEmergencyTrigger P_ULE_LOCATION_REQUEST = new TpUserLocationEmergencyTrigger(_P_ULE_LOCATION_REQUEST);
	public int value()
	{
		return value;
	}
	public static TpUserLocationEmergencyTrigger from_int(int value)
	{
		switch (value) {
			case _P_ULE_CALL_ORIGINATION: return P_ULE_CALL_ORIGINATION;
			case _P_ULE_CALL_RELEASE: return P_ULE_CALL_RELEASE;
			case _P_ULE_LOCATION_REQUEST: return P_ULE_LOCATION_REQUEST;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpUserLocationEmergencyTrigger(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
