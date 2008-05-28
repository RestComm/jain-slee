package org.csapi.cc.mmccs;

/**
 *	Generated from IDL definition of union "TpAppMultiMediaCallBack"
 *	@author JacORB IDL compiler 
 */

public final class TpAppMultiMediaCallBack
	implements org.omg.CORBA.portable.IDLEntity
{
	private org.csapi.cc.mmccs.TpAppMultiMediaCallBackRefType discriminator;
	private org.csapi.cc.mmccs.IpAppMultiMediaCall AppMultiMediaCall;
	private org.csapi.cc.mmccs.IpAppMultiMediaCallLeg AppMultiMediaCallLeg;
	private org.csapi.cc.mmccs.TpAppMultiMediaCallLegCallBack AppMultiMediaCallAndCallLeg;
	private short Dummy;

	public TpAppMultiMediaCallBack ()
	{
	}

	public org.csapi.cc.mmccs.TpAppMultiMediaCallBackRefType discriminator ()
	{
		return discriminator;
	}

	public org.csapi.cc.mmccs.IpAppMultiMediaCall AppMultiMediaCall ()
	{
		if (discriminator != org.csapi.cc.mmccs.TpAppMultiMediaCallBackRefType.P_APP_MULTIMEDIA_CALL_CALLBACK)
			throw new org.omg.CORBA.BAD_OPERATION();
		return AppMultiMediaCall;
	}

	public void AppMultiMediaCall (org.csapi.cc.mmccs.IpAppMultiMediaCall _x)
	{
		discriminator = org.csapi.cc.mmccs.TpAppMultiMediaCallBackRefType.P_APP_MULTIMEDIA_CALL_CALLBACK;
		AppMultiMediaCall = _x;
	}

	public org.csapi.cc.mmccs.IpAppMultiMediaCallLeg AppMultiMediaCallLeg ()
	{
		if (discriminator != org.csapi.cc.mmccs.TpAppMultiMediaCallBackRefType.P_APP_CALL_LEG_CALLBACK)
			throw new org.omg.CORBA.BAD_OPERATION();
		return AppMultiMediaCallLeg;
	}

	public void AppMultiMediaCallLeg (org.csapi.cc.mmccs.IpAppMultiMediaCallLeg _x)
	{
		discriminator = org.csapi.cc.mmccs.TpAppMultiMediaCallBackRefType.P_APP_CALL_LEG_CALLBACK;
		AppMultiMediaCallLeg = _x;
	}

	public org.csapi.cc.mmccs.TpAppMultiMediaCallLegCallBack AppMultiMediaCallAndCallLeg ()
	{
		if (discriminator != org.csapi.cc.mmccs.TpAppMultiMediaCallBackRefType.P_APP_CALL_AND_CALL_LEG_CALLBACK)
			throw new org.omg.CORBA.BAD_OPERATION();
		return AppMultiMediaCallAndCallLeg;
	}

	public void AppMultiMediaCallAndCallLeg (org.csapi.cc.mmccs.TpAppMultiMediaCallLegCallBack _x)
	{
		discriminator = org.csapi.cc.mmccs.TpAppMultiMediaCallBackRefType.P_APP_CALL_AND_CALL_LEG_CALLBACK;
		AppMultiMediaCallAndCallLeg = _x;
	}

	public short Dummy ()
	{
		if (discriminator != org.csapi.cc.mmccs.TpAppMultiMediaCallBackRefType.P_APP_CALLBACK_UNDEFINED)
			throw new org.omg.CORBA.BAD_OPERATION();
		return Dummy;
	}

	public void Dummy (short _x)
	{
		discriminator = org.csapi.cc.mmccs.TpAppMultiMediaCallBackRefType.P_APP_CALLBACK_UNDEFINED;
		Dummy = _x;
	}

	public void Dummy (org.csapi.cc.mmccs.TpAppMultiMediaCallBackRefType _discriminator, short _x)
	{
		if (_discriminator != org.csapi.cc.mmccs.TpAppMultiMediaCallBackRefType.P_APP_CALLBACK_UNDEFINED)
			throw new org.omg.CORBA.BAD_OPERATION();
		discriminator = _discriminator;
		Dummy = _x;
	}

}
