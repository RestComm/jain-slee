package org.csapi.ui;

/**
 *	Generated from IDL definition of union "TpUITargetObject"
 *	@author JacORB IDL compiler 
 */

public final class TpUITargetObject
	implements org.omg.CORBA.portable.IDLEntity
{
	private org.csapi.ui.TpUITargetObjectType discriminator;
	private org.csapi.cc.gccs.TpCallIdentifier Call;
	private org.csapi.cc.mpccs.TpMultiPartyCallIdentifier MultiPartyCall;
	private org.csapi.cc.mpccs.TpCallLegIdentifier CallLeg;

	public TpUITargetObject ()
	{
	}

	public org.csapi.ui.TpUITargetObjectType discriminator ()
	{
		return discriminator;
	}

	public org.csapi.cc.gccs.TpCallIdentifier Call ()
	{
		if (discriminator != org.csapi.ui.TpUITargetObjectType.P_UI_TARGET_OBJECT_CALL)
			throw new org.omg.CORBA.BAD_OPERATION();
		return Call;
	}

	public void Call (org.csapi.cc.gccs.TpCallIdentifier _x)
	{
		discriminator = org.csapi.ui.TpUITargetObjectType.P_UI_TARGET_OBJECT_CALL;
		Call = _x;
	}

	public org.csapi.cc.mpccs.TpMultiPartyCallIdentifier MultiPartyCall ()
	{
		if (discriminator != org.csapi.ui.TpUITargetObjectType.P_UI_TARGET_OBJECT_MULTI_PARTY_CALL)
			throw new org.omg.CORBA.BAD_OPERATION();
		return MultiPartyCall;
	}

	public void MultiPartyCall (org.csapi.cc.mpccs.TpMultiPartyCallIdentifier _x)
	{
		discriminator = org.csapi.ui.TpUITargetObjectType.P_UI_TARGET_OBJECT_MULTI_PARTY_CALL;
		MultiPartyCall = _x;
	}

	public org.csapi.cc.mpccs.TpCallLegIdentifier CallLeg ()
	{
		if (discriminator != org.csapi.ui.TpUITargetObjectType.P_UI_TARGET_OBJECT_CALL_LEG)
			throw new org.omg.CORBA.BAD_OPERATION();
		return CallLeg;
	}

	public void CallLeg (org.csapi.cc.mpccs.TpCallLegIdentifier _x)
	{
		discriminator = org.csapi.ui.TpUITargetObjectType.P_UI_TARGET_OBJECT_CALL_LEG;
		CallLeg = _x;
	}

}
