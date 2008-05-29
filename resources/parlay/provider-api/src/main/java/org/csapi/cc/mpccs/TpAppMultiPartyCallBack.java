package org.csapi.cc.mpccs;

/**
 *	Generated from IDL definition of union "TpAppMultiPartyCallBack"
 *	@author JacORB IDL compiler 
 */

public final class TpAppMultiPartyCallBack
	implements org.omg.CORBA.portable.IDLEntity
{
	private org.csapi.cc.mpccs.TpAppMultiPartyCallBackRefType discriminator;
	private org.csapi.cc.mpccs.IpAppMultiPartyCall AppMultiPartyCall;
	private org.csapi.cc.mpccs.IpAppCallLeg AppCallLeg;
	private org.csapi.cc.mpccs.TpAppCallLegCallBack AppMultiPartyCallAndCallLeg;
	private short Dummy;

	public TpAppMultiPartyCallBack ()
	{
	}

	public org.csapi.cc.mpccs.TpAppMultiPartyCallBackRefType discriminator ()
	{
		return discriminator;
	}

	public org.csapi.cc.mpccs.IpAppMultiPartyCall AppMultiPartyCall ()
	{
		if (discriminator != org.csapi.cc.mpccs.TpAppMultiPartyCallBackRefType.P_APP_MULTIPARTY_CALL_CALLBACK)
			throw new org.omg.CORBA.BAD_OPERATION();
		return AppMultiPartyCall;
	}

	public void AppMultiPartyCall (org.csapi.cc.mpccs.IpAppMultiPartyCall _x)
	{
		discriminator = org.csapi.cc.mpccs.TpAppMultiPartyCallBackRefType.P_APP_MULTIPARTY_CALL_CALLBACK;
		AppMultiPartyCall = _x;
	}

	public org.csapi.cc.mpccs.IpAppCallLeg AppCallLeg ()
	{
		if (discriminator != org.csapi.cc.mpccs.TpAppMultiPartyCallBackRefType.P_APP_CALL_LEG_CALLBACK)
			throw new org.omg.CORBA.BAD_OPERATION();
		return AppCallLeg;
	}

	public void AppCallLeg (org.csapi.cc.mpccs.IpAppCallLeg _x)
	{
		discriminator = org.csapi.cc.mpccs.TpAppMultiPartyCallBackRefType.P_APP_CALL_LEG_CALLBACK;
		AppCallLeg = _x;
	}

	public org.csapi.cc.mpccs.TpAppCallLegCallBack AppMultiPartyCallAndCallLeg ()
	{
		if (discriminator != org.csapi.cc.mpccs.TpAppMultiPartyCallBackRefType.P_APP_CALL_AND_CALL_LEG_CALLBACK)
			throw new org.omg.CORBA.BAD_OPERATION();
		return AppMultiPartyCallAndCallLeg;
	}

	public void AppMultiPartyCallAndCallLeg (org.csapi.cc.mpccs.TpAppCallLegCallBack _x)
	{
		discriminator = org.csapi.cc.mpccs.TpAppMultiPartyCallBackRefType.P_APP_CALL_AND_CALL_LEG_CALLBACK;
		AppMultiPartyCallAndCallLeg = _x;
	}

	public short Dummy ()
	{
		if (discriminator != org.csapi.cc.mpccs.TpAppMultiPartyCallBackRefType.P_APP_CALLBACK_UNDEFINED)
			throw new org.omg.CORBA.BAD_OPERATION();
		return Dummy;
	}

	public void Dummy (short _x)
	{
		discriminator = org.csapi.cc.mpccs.TpAppMultiPartyCallBackRefType.P_APP_CALLBACK_UNDEFINED;
		Dummy = _x;
	}

	public void Dummy (org.csapi.cc.mpccs.TpAppMultiPartyCallBackRefType _discriminator, short _x)
	{
		if (_discriminator != org.csapi.cc.mpccs.TpAppMultiPartyCallBackRefType.P_APP_CALLBACK_UNDEFINED)
			throw new org.omg.CORBA.BAD_OPERATION();
		discriminator = _discriminator;
		Dummy = _x;
	}

}
