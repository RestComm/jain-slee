package org.csapi.am;

/**
 *	Generated from IDL definition of struct "TpTransactionHistory"
 *	@author JacORB IDL compiler 
 */

public final class TpTransactionHistory
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpTransactionHistory(){}
	public int TransactionID;
	public java.lang.String TimeStamp;
	public java.lang.String AdditionalInfo;
	public TpTransactionHistory(int TransactionID, java.lang.String TimeStamp, java.lang.String AdditionalInfo)
	{
		this.TransactionID = TransactionID;
		this.TimeStamp = TimeStamp;
		this.AdditionalInfo = AdditionalInfo;
	}
}
