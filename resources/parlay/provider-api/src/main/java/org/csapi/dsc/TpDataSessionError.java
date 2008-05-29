package org.csapi.dsc;

/**
 *	Generated from IDL definition of struct "TpDataSessionError"
 *	@author JacORB IDL compiler 
 */

public final class TpDataSessionError
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpDataSessionError(){}
	public java.lang.String ErrorTime;
	public org.csapi.dsc.TpDataSessionErrorType ErrorType;
	public org.csapi.dsc.TpDataSessionAdditionalErrorInfo AdditionalErrorInfo;
	public TpDataSessionError(java.lang.String ErrorTime, org.csapi.dsc.TpDataSessionErrorType ErrorType, org.csapi.dsc.TpDataSessionAdditionalErrorInfo AdditionalErrorInfo)
	{
		this.ErrorTime = ErrorTime;
		this.ErrorType = ErrorType;
		this.AdditionalErrorInfo = AdditionalErrorInfo;
	}
}
