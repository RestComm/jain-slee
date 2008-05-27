package org.csapi.am;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpAppAccountManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpAppAccountManagerPOATie
	extends IpAppAccountManagerPOA
{
	private IpAppAccountManagerOperations _delegate;

	private POA _poa;
	public IpAppAccountManagerPOATie(IpAppAccountManagerOperations delegate)
	{
		_delegate = delegate;
	}
	public IpAppAccountManagerPOATie(IpAppAccountManagerOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.am.IpAppAccountManager _this()
	{
		return org.csapi.am.IpAppAccountManagerHelper.narrow(_this_object());
	}
	public org.csapi.am.IpAppAccountManager _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.am.IpAppAccountManagerHelper.narrow(_this_object(orb));
	}
	public IpAppAccountManagerOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpAppAccountManagerOperations delegate)
	{
		_delegate = delegate;
	}
	public POA _default_POA()
	{
		if (_poa != null)
		{
			return _poa;
		}
		else
		{
			return super._default_POA();
		}
	}
	public void retrieveTransactionHistoryErr(int retrievalID, org.csapi.am.TpTransactionHistoryStatus transactionHistoryError)
	{
_delegate.retrieveTransactionHistoryErr(retrievalID,transactionHistoryError);
	}

	public void queryBalanceRes(int queryId, org.csapi.am.TpBalance[] balances)
	{
_delegate.queryBalanceRes(queryId,balances);
	}

	public void retrieveTransactionHistoryRes(int retrievalID, org.csapi.am.TpTransactionHistory[] transactionHistory)
	{
_delegate.retrieveTransactionHistoryRes(retrievalID,transactionHistory);
	}

	public void queryBalanceErr(int queryId, org.csapi.am.TpBalanceQueryError cause)
	{
_delegate.queryBalanceErr(queryId,cause);
	}

	public void reportNotification(org.csapi.am.TpChargingEventInfo chargingEventInfo, int assignmentId)
	{
_delegate.reportNotification(chargingEventInfo,assignmentId);
	}

}
