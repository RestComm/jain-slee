package org.csapi.cs;

/**
 *	Generated from IDL interface "IpChargingSession"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpChargingSessionOperations
	extends org.csapi.IpServiceOperations
{
	/* constants */
	/* operations  */
	void creditAmountReq(int sessionID, org.csapi.cs.TpApplicationDescription applicationDescription, org.csapi.cs.TpChargingPrice amount, boolean closeReservation, int requestNumber) throws org.csapi.P_INVALID_AMOUNT,org.csapi.P_INVALID_CURRENCY,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID,org.csapi.cs.P_INVALID_REQUEST_NUMBER;
	void creditUnitReq(int sessionID, org.csapi.cs.TpApplicationDescription applicationDescription, org.csapi.cs.TpVolume[] volumes, boolean closeReservation, int requestNumber) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID,org.csapi.cs.P_INVALID_REQUEST_NUMBER,org.csapi.cs.P_INVALID_VOLUME;
	void debitAmountReq(int sessionID, org.csapi.cs.TpApplicationDescription applicationDescription, org.csapi.cs.TpChargingPrice amount, boolean closeReservation, int requestNumber) throws org.csapi.P_INVALID_AMOUNT,org.csapi.P_INVALID_CURRENCY,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID,org.csapi.cs.P_INVALID_REQUEST_NUMBER;
	void debitUnitReq(int sessionID, org.csapi.cs.TpApplicationDescription applicationDescription, org.csapi.cs.TpVolume[] volumes, boolean closeReservation, int requestNumber) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID,org.csapi.cs.P_INVALID_REQUEST_NUMBER,org.csapi.cs.P_INVALID_VOLUME;
	void directCreditAmountReq(int sessionID, org.csapi.cs.TpApplicationDescription applicationDescription, org.csapi.cs.TpChargingParameter[] chargingParameters, org.csapi.cs.TpChargingPrice amount, int requestNumber) throws org.csapi.P_INVALID_AMOUNT,org.csapi.P_INVALID_CURRENCY,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID,org.csapi.cs.P_INVALID_REQUEST_NUMBER;
	void directCreditUnitReq(int sessionID, org.csapi.cs.TpApplicationDescription applicationDescription, org.csapi.cs.TpChargingParameter[] chargingParameters, org.csapi.cs.TpVolume[] volumes, int requestNumber) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID,org.csapi.cs.P_INVALID_REQUEST_NUMBER,org.csapi.cs.P_INVALID_VOLUME;
	void directDebitAmountReq(int sessionID, org.csapi.cs.TpApplicationDescription applicationDescription, org.csapi.cs.TpChargingParameter[] chargingParameters, org.csapi.cs.TpChargingPrice amount, int requestNumber) throws org.csapi.P_INVALID_AMOUNT,org.csapi.P_INVALID_CURRENCY,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID,org.csapi.cs.P_INVALID_REQUEST_NUMBER;
	void directDebitUnitReq(int sessionID, org.csapi.cs.TpApplicationDescription applicationDescription, org.csapi.cs.TpChargingParameter[] chargingParameters, org.csapi.cs.TpVolume[] volumes, int requestNumber) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID,org.csapi.cs.P_INVALID_REQUEST_NUMBER,org.csapi.cs.P_INVALID_VOLUME;
	void extendLifeTimeReq(int sessionID) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID;
	org.csapi.cs.TpChargingPrice getAmountLeft(int sessionID) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID;
	int getLifeTimeLeft(int sessionID) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID;
	org.csapi.cs.TpVolume[] getUnitLeft(int sessionID) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID;
	void rateReq(int sessionID, org.csapi.cs.TpChargingParameter[] chargingParameters) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID;
	void release(int sessionID, int requestNumber) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID,org.csapi.cs.P_INVALID_REQUEST_NUMBER;
	void reserveAmountReq(int sessionID, org.csapi.cs.TpApplicationDescription applicationDescription, org.csapi.cs.TpChargingParameter[] chargingParameters, org.csapi.cs.TpChargingPrice preferredAmount, org.csapi.cs.TpChargingPrice minimumAmount, int requestNumber) throws org.csapi.P_INVALID_AMOUNT,org.csapi.P_INVALID_CURRENCY,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID,org.csapi.cs.P_INVALID_REQUEST_NUMBER;
	void reserveUnitReq(int sessionID, org.csapi.cs.TpApplicationDescription applicationDescription, org.csapi.cs.TpChargingParameter[] chargingParameters, org.csapi.cs.TpVolume[] volumes, int requestNumber) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID,org.csapi.cs.P_INVALID_REQUEST_NUMBER,org.csapi.cs.P_INVALID_VOLUME;
}
