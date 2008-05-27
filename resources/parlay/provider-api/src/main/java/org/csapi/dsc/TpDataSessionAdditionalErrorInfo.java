package org.csapi.dsc;

/**
 *	Generated from IDL definition of union "TpDataSessionAdditionalErrorInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpDataSessionAdditionalErrorInfo
	implements org.omg.CORBA.portable.IDLEntity
{
	private org.csapi.dsc.TpDataSessionErrorType discriminator;
	private org.csapi.TpAddressError DataSessionErrorInvalidAddress;
	private short Dummy;

	public TpDataSessionAdditionalErrorInfo ()
	{
	}

	public org.csapi.dsc.TpDataSessionErrorType discriminator ()
	{
		return discriminator;
	}

	public org.csapi.TpAddressError DataSessionErrorInvalidAddress ()
	{
		if (discriminator != org.csapi.dsc.TpDataSessionErrorType.P_DATA_SESSION_ERROR_INVALID_ADDRESS)
			throw new org.omg.CORBA.BAD_OPERATION();
		return DataSessionErrorInvalidAddress;
	}

	public void DataSessionErrorInvalidAddress (org.csapi.TpAddressError _x)
	{
		discriminator = org.csapi.dsc.TpDataSessionErrorType.P_DATA_SESSION_ERROR_INVALID_ADDRESS;
		DataSessionErrorInvalidAddress = _x;
	}

	public short Dummy ()
	{
		if (discriminator != org.csapi.dsc.TpDataSessionErrorType.P_DATA_SESSION_ERROR_UNDEFINED && discriminator != org.csapi.dsc.TpDataSessionErrorType.P_DATA_SESSION_ERROR_INVALID_STATE)
			throw new org.omg.CORBA.BAD_OPERATION();
		return Dummy;
	}

	public void Dummy (short _x)
	{
		discriminator = org.csapi.dsc.TpDataSessionErrorType.P_DATA_SESSION_ERROR_UNDEFINED;
		Dummy = _x;
	}

	public void Dummy (org.csapi.dsc.TpDataSessionErrorType _discriminator, short _x)
	{
		if (_discriminator != org.csapi.dsc.TpDataSessionErrorType.P_DATA_SESSION_ERROR_UNDEFINED && discriminator != org.csapi.dsc.TpDataSessionErrorType.P_DATA_SESSION_ERROR_INVALID_STATE)
			throw new org.omg.CORBA.BAD_OPERATION();
		discriminator = _discriminator;
		Dummy = _x;
	}

}
