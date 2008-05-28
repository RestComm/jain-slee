package org.csapi.am;

/**
 *	Generated from IDL definition of struct "TpBalance"
 *	@author JacORB IDL compiler 
 */

public final class TpBalance
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpBalance(){}
	public org.csapi.TpAddress UserID;
	public org.csapi.am.TpBalanceQueryError StatusCode;
	public org.csapi.am.TpBalanceInfo BalanceInfo;
	public TpBalance(org.csapi.TpAddress UserID, org.csapi.am.TpBalanceQueryError StatusCode, org.csapi.am.TpBalanceInfo BalanceInfo)
	{
		this.UserID = UserID;
		this.StatusCode = StatusCode;
		this.BalanceInfo = BalanceInfo;
	}
}
