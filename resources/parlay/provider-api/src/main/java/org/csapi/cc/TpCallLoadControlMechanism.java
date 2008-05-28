package org.csapi.cc;

/**
 *	Generated from IDL definition of union "TpCallLoadControlMechanism"
 *	@author JacORB IDL compiler 
 */

public final class TpCallLoadControlMechanism
	implements org.omg.CORBA.portable.IDLEntity
{
	private org.csapi.cc.TpCallLoadControlMechanismType discriminator;
	private int CallLoadControlPerInterval;

	public TpCallLoadControlMechanism ()
	{
	}

	public org.csapi.cc.TpCallLoadControlMechanismType discriminator ()
	{
		return discriminator;
	}

	public int CallLoadControlPerInterval ()
	{
		if (discriminator != org.csapi.cc.TpCallLoadControlMechanismType.P_CALL_LOAD_CONTROL_PER_INTERVAL)
			throw new org.omg.CORBA.BAD_OPERATION();
		return CallLoadControlPerInterval;
	}

	public void CallLoadControlPerInterval (int _x)
	{
		discriminator = org.csapi.cc.TpCallLoadControlMechanismType.P_CALL_LOAD_CONTROL_PER_INTERVAL;
		CallLoadControlPerInterval = _x;
	}

}
