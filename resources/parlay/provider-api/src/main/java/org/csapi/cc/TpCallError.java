package org.csapi.cc;

/**
 *	Generated from IDL definition of struct "TpCallError"
 *	@author JacORB IDL compiler 
 */

public final class TpCallError
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpCallError(){}
	public java.lang.String ErrorTime;
	public org.csapi.cc.TpCallErrorType ErrorType;
	public org.csapi.cc.TpCallAdditionalErrorInfo AdditionalErorInfo;
	public TpCallError(java.lang.String ErrorTime, org.csapi.cc.TpCallErrorType ErrorType, org.csapi.cc.TpCallAdditionalErrorInfo AdditionalErorInfo)
	{
		this.ErrorTime = ErrorTime;
		this.ErrorType = ErrorType;
		this.AdditionalErorInfo = AdditionalErorInfo;
	}
}
