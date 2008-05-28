package org.csapi.cc;

/**
 *	Generated from IDL definition of union "TpCallAdditionalErrorInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpCallAdditionalErrorInfo
	implements org.omg.CORBA.portable.IDLEntity
{
	private org.csapi.cc.TpCallErrorType discriminator;
	private org.csapi.TpAddressError CallErrorInvalidAddress;
	private short Dummy;

	public TpCallAdditionalErrorInfo ()
	{
	}

	public org.csapi.cc.TpCallErrorType discriminator ()
	{
		return discriminator;
	}

	public org.csapi.TpAddressError CallErrorInvalidAddress ()
	{
		if (discriminator != org.csapi.cc.TpCallErrorType.P_CALL_ERROR_INVALID_ADDRESS)
			throw new org.omg.CORBA.BAD_OPERATION();
		return CallErrorInvalidAddress;
	}

	public void CallErrorInvalidAddress (org.csapi.TpAddressError _x)
	{
		discriminator = org.csapi.cc.TpCallErrorType.P_CALL_ERROR_INVALID_ADDRESS;
		CallErrorInvalidAddress = _x;
	}

	public short Dummy ()
	{
		if (discriminator != org.csapi.cc.TpCallErrorType.P_CALL_ERROR_UNDEFINED && discriminator != org.csapi.cc.TpCallErrorType.P_CALL_ERROR_INVALID_STATE && discriminator != org.csapi.cc.TpCallErrorType.P_CALL_ERROR_RESOURCE_UNAVAILABLE)
			throw new org.omg.CORBA.BAD_OPERATION();
		return Dummy;
	}

	public void Dummy (short _x)
	{
		discriminator = org.csapi.cc.TpCallErrorType.P_CALL_ERROR_UNDEFINED;
		Dummy = _x;
	}

	public void Dummy (org.csapi.cc.TpCallErrorType _discriminator, short _x)
	{
		if (_discriminator != org.csapi.cc.TpCallErrorType.P_CALL_ERROR_UNDEFINED && discriminator != org.csapi.cc.TpCallErrorType.P_CALL_ERROR_INVALID_STATE && discriminator != org.csapi.cc.TpCallErrorType.P_CALL_ERROR_RESOURCE_UNAVAILABLE)
			throw new org.omg.CORBA.BAD_OPERATION();
		discriminator = _discriminator;
		Dummy = _x;
	}

}
