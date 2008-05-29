package org.csapi.cs;

/**
 *	Generated from IDL definition of struct "TpMerchantAccountID"
 *	@author JacORB IDL compiler 
 */

public final class TpMerchantAccountID
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpMerchantAccountID(){}
	public java.lang.String MerchantID;
	public int AccountID;
	public TpMerchantAccountID(java.lang.String MerchantID, int AccountID)
	{
		this.MerchantID = MerchantID;
		this.AccountID = AccountID;
	}
}
