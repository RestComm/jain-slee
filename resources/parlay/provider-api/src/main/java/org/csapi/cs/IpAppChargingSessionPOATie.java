package org.csapi.cs;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpAppChargingSession"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpAppChargingSessionPOATie
	extends IpAppChargingSessionPOA
{
	private IpAppChargingSessionOperations _delegate;

	private POA _poa;
	public IpAppChargingSessionPOATie(IpAppChargingSessionOperations delegate)
	{
		_delegate = delegate;
	}
	public IpAppChargingSessionPOATie(IpAppChargingSessionOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.cs.IpAppChargingSession _this()
	{
		return org.csapi.cs.IpAppChargingSessionHelper.narrow(_this_object());
	}
	public org.csapi.cs.IpAppChargingSession _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.cs.IpAppChargingSessionHelper.narrow(_this_object(orb));
	}
	public IpAppChargingSessionOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpAppChargingSessionOperations delegate)
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
	public void reserveAmountRes(int sessionID, int requestNumber, org.csapi.cs.TpChargingPrice reservedAmount, int sessionTimeLeft, int requestNumberNextRequest)
	{
_delegate.reserveAmountRes(sessionID,requestNumber,reservedAmount,sessionTimeLeft,requestNumberNextRequest);
	}

	public void debitUnitErr(int sessionID, int requestNumber, org.csapi.cs.TpChargingError error, int requestNumberNextRequest)
	{
_delegate.debitUnitErr(sessionID,requestNumber,error,requestNumberNextRequest);
	}

	public void creditUnitRes(int sessionID, int requestNumber, org.csapi.cs.TpVolume[] creditedVolumes, org.csapi.cs.TpVolume[] reservedUnitsLeft, int requestNumberNextRequest)
	{
_delegate.creditUnitRes(sessionID,requestNumber,creditedVolumes,reservedUnitsLeft,requestNumberNextRequest);
	}

	public void debitAmountErr(int sessionID, int requestNumber, org.csapi.cs.TpChargingError error, int requestNumberNextRequest)
	{
_delegate.debitAmountErr(sessionID,requestNumber,error,requestNumberNextRequest);
	}

	public void reserveUnitErr(int sessionID, int requestNumber, org.csapi.cs.TpChargingError error, int requestNumberNextRequest)
	{
_delegate.reserveUnitErr(sessionID,requestNumber,error,requestNumberNextRequest);
	}

	public void sessionEnded(int sessionID, org.csapi.cs.TpSessionEndedCause report)
	{
_delegate.sessionEnded(sessionID,report);
	}

	public void extendLifeTimeErr(int sessionID, org.csapi.cs.TpChargingError error)
	{
_delegate.extendLifeTimeErr(sessionID,error);
	}

	public void directDebitUnitRes(int sessionID, int requestNumber, org.csapi.cs.TpVolume[] debitedVolumes, int requestNumberNextRequest)
	{
_delegate.directDebitUnitRes(sessionID,requestNumber,debitedVolumes,requestNumberNextRequest);
	}

	public void creditAmountRes(int sessionID, int requestNumber, org.csapi.cs.TpChargingPrice creditedAmount, org.csapi.cs.TpChargingPrice reservedAmountLeft, int requestNumberNextRequest)
	{
_delegate.creditAmountRes(sessionID,requestNumber,creditedAmount,reservedAmountLeft,requestNumberNextRequest);
	}

	public void directDebitUnitErr(int sessionID, int requestNumber, org.csapi.cs.TpChargingError error, int requestNumberNextRequest)
	{
_delegate.directDebitUnitErr(sessionID,requestNumber,error,requestNumberNextRequest);
	}

	public void creditAmountErr(int sessionID, int requestNumber, org.csapi.cs.TpChargingError error, int requestNumberNextRequest)
	{
_delegate.creditAmountErr(sessionID,requestNumber,error,requestNumberNextRequest);
	}

	public void directCreditAmountErr(int sessionID, int requestNumber, org.csapi.cs.TpChargingError error, int requestNumberNextRequest)
	{
_delegate.directCreditAmountErr(sessionID,requestNumber,error,requestNumberNextRequest);
	}

	public void directCreditAmountRes(int sessionID, int requestNumber, org.csapi.cs.TpChargingPrice creditedAmount, int requestNumberNextRequest)
	{
_delegate.directCreditAmountRes(sessionID,requestNumber,creditedAmount,requestNumberNextRequest);
	}

	public void debitAmountRes(int sessionID, int requestNumber, org.csapi.cs.TpChargingPrice debitedAmount, org.csapi.cs.TpChargingPrice reservedAmountLeft, int requestNumberNextRequest)
	{
_delegate.debitAmountRes(sessionID,requestNumber,debitedAmount,reservedAmountLeft,requestNumberNextRequest);
	}

	public void directCreditUnitErr(int sessionID, int requestNumber, org.csapi.cs.TpChargingError error, int requestNumberNextRequest)
	{
_delegate.directCreditUnitErr(sessionID,requestNumber,error,requestNumberNextRequest);
	}

	public void reserveUnitRes(int sessionID, int requestNumber, org.csapi.cs.TpVolume[] reservedUnits, int sessionTimeLeft, int requestNumberNextRequest)
	{
_delegate.reserveUnitRes(sessionID,requestNumber,reservedUnits,sessionTimeLeft,requestNumberNextRequest);
	}

	public void extendLifeTimeRes(int sessionID, int sessionTimeLeft)
	{
_delegate.extendLifeTimeRes(sessionID,sessionTimeLeft);
	}

	public void creditUnitErr(int sessionID, int requestNumber, org.csapi.cs.TpChargingError error, int requestNumberNextRequest)
	{
_delegate.creditUnitErr(sessionID,requestNumber,error,requestNumberNextRequest);
	}

	public void rateRes(int sessionID, org.csapi.cs.TpPriceVolume[] rates, int validityTimeLeft)
	{
_delegate.rateRes(sessionID,rates,validityTimeLeft);
	}

	public void directCreditUnitRes(int sessionID, int requestNumber, org.csapi.cs.TpVolume[] creditedVolumes, int requestNumberNextRequest)
	{
_delegate.directCreditUnitRes(sessionID,requestNumber,creditedVolumes,requestNumberNextRequest);
	}

	public void rateErr(int sessionID, org.csapi.cs.TpChargingError error)
	{
_delegate.rateErr(sessionID,error);
	}

	public void reserveAmountErr(int sessionID, int requestNumber, org.csapi.cs.TpChargingError error, int requestNumberNextRequest)
	{
_delegate.reserveAmountErr(sessionID,requestNumber,error,requestNumberNextRequest);
	}

	public void directDebitAmountErr(int sessionID, int requestNumber, org.csapi.cs.TpChargingError error, int requestNumberNextRequest)
	{
_delegate.directDebitAmountErr(sessionID,requestNumber,error,requestNumberNextRequest);
	}

	public void directDebitAmountRes(int sessionID, int requestNumber, org.csapi.cs.TpChargingPrice debitedAmount, int requestNumberNextRequest)
	{
_delegate.directDebitAmountRes(sessionID,requestNumber,debitedAmount,requestNumberNextRequest);
	}

	public void debitUnitRes(int sessionID, int requestNumber, org.csapi.cs.TpVolume[] debitedVolumes, org.csapi.cs.TpVolume[] reservedUnitsLeft, int requestNumberNextRequest)
	{
_delegate.debitUnitRes(sessionID,requestNumber,debitedVolumes,reservedUnitsLeft,requestNumberNextRequest);
	}

}
