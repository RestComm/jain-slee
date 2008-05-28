package org.csapi.pam;

/**
 *	Generated from IDL definition of struct "TpPAMErrorInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMErrorInfo
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpPAMErrorInfo(){}
	public org.csapi.pam.TpPAMErrorCause Cause;
	public org.csapi.pam.TpPAMNotificationInfo ErrorData;
	public TpPAMErrorInfo(org.csapi.pam.TpPAMErrorCause Cause, org.csapi.pam.TpPAMNotificationInfo ErrorData)
	{
		this.Cause = Cause;
		this.ErrorData = ErrorData;
	}
}
