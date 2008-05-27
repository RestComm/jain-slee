package org.csapi.cs;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpChargingSession"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpChargingSessionPOATie
	extends IpChargingSessionPOA
{
	private IpChargingSessionOperations _delegate;

	private POA _poa;
	public IpChargingSessionPOATie(IpChargingSessionOperations delegate)
	{
		_delegate = delegate;
	}
	public IpChargingSessionPOATie(IpChargingSessionOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.cs.IpChargingSession _this()
	{
		return org.csapi.cs.IpChargingSessionHelper.narrow(_this_object());
	}
	public org.csapi.cs.IpChargingSession _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.cs.IpChargingSessionHelper.narrow(_this_object(orb));
	}
	public IpChargingSessionOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpChargingSessionOperations delegate)
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
	public void directCreditUnitReq(int sessionID, org.csapi.cs.TpApplicationDescription applicationDescription, org.csapi.cs.TpChargingParameter[] chargingParameters, org.csapi.cs.TpVolume[] volumes, int requestNumber) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID,org.csapi.cs.P_INVALID_REQUEST_NUMBER,org.csapi.cs.P_INVALID_VOLUME
	{
_delegate.directCreditUnitReq(sessionID,applicationDescription,chargingParameters,volumes,requestNumber);
	}

	public void directDebitUnitReq(int sessionID, org.csapi.cs.TpApplicationDescription applicationDescription, org.csapi.cs.TpChargingParameter[] chargingParameters, org.csapi.cs.TpVolume[] volumes, int requestNumber) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID,org.csapi.cs.P_INVALID_REQUEST_NUMBER,org.csapi.cs.P_INVALID_VOLUME
	{
_delegate.directDebitUnitReq(sessionID,applicationDescription,chargingParameters,volumes,requestNumber);
	}

	public void setCallback(org.csapi.IpInterface appInterface) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions
	{
_delegate.setCallback(appInterface);
	}

	public org.csapi.cs.TpVolume[] getUnitLeft(int sessionID) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
		return _delegate.getUnitLeft(sessionID);
	}

	public void extendLifeTimeReq(int sessionID) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
_delegate.extendLifeTimeReq(sessionID);
	}

	public void creditAmountReq(int sessionID, org.csapi.cs.TpApplicationDescription applicationDescription, org.csapi.cs.TpChargingPrice amount, boolean closeReservation, int requestNumber) throws org.csapi.P_INVALID_AMOUNT,org.csapi.P_INVALID_CURRENCY,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID,org.csapi.cs.P_INVALID_REQUEST_NUMBER
	{
_delegate.creditAmountReq(sessionID,applicationDescription,amount,closeReservation,requestNumber);
	}

	public void debitAmountReq(int sessionID, org.csapi.cs.TpApplicationDescription applicationDescription, org.csapi.cs.TpChargingPrice amount, boolean closeReservation, int requestNumber) throws org.csapi.P_INVALID_AMOUNT,org.csapi.P_INVALID_CURRENCY,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID,org.csapi.cs.P_INVALID_REQUEST_NUMBER
	{
_delegate.debitAmountReq(sessionID,applicationDescription,amount,closeReservation,requestNumber);
	}

	public void setCallbackWithSessionID(org.csapi.IpInterface appInterface, int sessionID) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
_delegate.setCallbackWithSessionID(appInterface,sessionID);
	}

	public void directCreditAmountReq(int sessionID, org.csapi.cs.TpApplicationDescription applicationDescription, org.csapi.cs.TpChargingParameter[] chargingParameters, org.csapi.cs.TpChargingPrice amount, int requestNumber) throws org.csapi.P_INVALID_AMOUNT,org.csapi.P_INVALID_CURRENCY,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID,org.csapi.cs.P_INVALID_REQUEST_NUMBER
	{
_delegate.directCreditAmountReq(sessionID,applicationDescription,chargingParameters,amount,requestNumber);
	}

	public void rateReq(int sessionID, org.csapi.cs.TpChargingParameter[] chargingParameters) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
_delegate.rateReq(sessionID,chargingParameters);
	}

	public void directDebitAmountReq(int sessionID, org.csapi.cs.TpApplicationDescription applicationDescription, org.csapi.cs.TpChargingParameter[] chargingParameters, org.csapi.cs.TpChargingPrice amount, int requestNumber) throws org.csapi.P_INVALID_AMOUNT,org.csapi.P_INVALID_CURRENCY,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID,org.csapi.cs.P_INVALID_REQUEST_NUMBER
	{
_delegate.directDebitAmountReq(sessionID,applicationDescription,chargingParameters,amount,requestNumber);
	}

	public void release(int sessionID, int requestNumber) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID,org.csapi.cs.P_INVALID_REQUEST_NUMBER
	{
_delegate.release(sessionID,requestNumber);
	}

	public void creditUnitReq(int sessionID, org.csapi.cs.TpApplicationDescription applicationDescription, org.csapi.cs.TpVolume[] volumes, boolean closeReservation, int requestNumber) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID,org.csapi.cs.P_INVALID_REQUEST_NUMBER,org.csapi.cs.P_INVALID_VOLUME
	{
_delegate.creditUnitReq(sessionID,applicationDescription,volumes,closeReservation,requestNumber);
	}

	public void reserveUnitReq(int sessionID, org.csapi.cs.TpApplicationDescription applicationDescription, org.csapi.cs.TpChargingParameter[] chargingParameters, org.csapi.cs.TpVolume[] volumes, int requestNumber) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID,org.csapi.cs.P_INVALID_REQUEST_NUMBER,org.csapi.cs.P_INVALID_VOLUME
	{
_delegate.reserveUnitReq(sessionID,applicationDescription,chargingParameters,volumes,requestNumber);
	}

	public void debitUnitReq(int sessionID, org.csapi.cs.TpApplicationDescription applicationDescription, org.csapi.cs.TpVolume[] volumes, boolean closeReservation, int requestNumber) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID,org.csapi.cs.P_INVALID_REQUEST_NUMBER,org.csapi.cs.P_INVALID_VOLUME
	{
_delegate.debitUnitReq(sessionID,applicationDescription,volumes,closeReservation,requestNumber);
	}

	public int getLifeTimeLeft(int sessionID) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
		return _delegate.getLifeTimeLeft(sessionID);
	}

	public org.csapi.cs.TpChargingPrice getAmountLeft(int sessionID) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
		return _delegate.getAmountLeft(sessionID);
	}

	public void reserveAmountReq(int sessionID, org.csapi.cs.TpApplicationDescription applicationDescription, org.csapi.cs.TpChargingParameter[] chargingParameters, org.csapi.cs.TpChargingPrice preferredAmount, org.csapi.cs.TpChargingPrice minimumAmount, int requestNumber) throws org.csapi.P_INVALID_AMOUNT,org.csapi.P_INVALID_CURRENCY,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID,org.csapi.cs.P_INVALID_REQUEST_NUMBER
	{
_delegate.reserveAmountReq(sessionID,applicationDescription,chargingParameters,preferredAmount,minimumAmount,requestNumber);
	}

}
