package org.csapi.am;

/**
 *	Generated from IDL interface "IpAppAccountManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpAppAccountManagerOperations
	extends org.csapi.IpInterfaceOperations
{
	/* constants */
	/* operations  */
	void reportNotification(org.csapi.am.TpChargingEventInfo chargingEventInfo, int assignmentId);
	void queryBalanceRes(int queryId, org.csapi.am.TpBalance[] balances);
	void queryBalanceErr(int queryId, org.csapi.am.TpBalanceQueryError cause);
	void retrieveTransactionHistoryRes(int retrievalID, org.csapi.am.TpTransactionHistory[] transactionHistory);
	void retrieveTransactionHistoryErr(int retrievalID, org.csapi.am.TpTransactionHistoryStatus transactionHistoryError);
}
