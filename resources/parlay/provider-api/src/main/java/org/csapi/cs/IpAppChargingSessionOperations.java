package org.csapi.cs;

/**
 *	Generated from IDL interface "IpAppChargingSession"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpAppChargingSessionOperations
	extends org.csapi.IpInterfaceOperations
{
	/* constants */
	/* operations  */
	void creditAmountErr(int sessionID, int requestNumber, org.csapi.cs.TpChargingError error, int requestNumberNextRequest);
	void creditAmountRes(int sessionID, int requestNumber, org.csapi.cs.TpChargingPrice creditedAmount, org.csapi.cs.TpChargingPrice reservedAmountLeft, int requestNumberNextRequest);
	void creditUnitErr(int sessionID, int requestNumber, org.csapi.cs.TpChargingError error, int requestNumberNextRequest);
	void creditUnitRes(int sessionID, int requestNumber, org.csapi.cs.TpVolume[] creditedVolumes, org.csapi.cs.TpVolume[] reservedUnitsLeft, int requestNumberNextRequest);
	void debitAmountErr(int sessionID, int requestNumber, org.csapi.cs.TpChargingError error, int requestNumberNextRequest);
	void debitAmountRes(int sessionID, int requestNumber, org.csapi.cs.TpChargingPrice debitedAmount, org.csapi.cs.TpChargingPrice reservedAmountLeft, int requestNumberNextRequest);
	void debitUnitErr(int sessionID, int requestNumber, org.csapi.cs.TpChargingError error, int requestNumberNextRequest);
	void debitUnitRes(int sessionID, int requestNumber, org.csapi.cs.TpVolume[] debitedVolumes, org.csapi.cs.TpVolume[] reservedUnitsLeft, int requestNumberNextRequest);
	void directCreditAmountErr(int sessionID, int requestNumber, org.csapi.cs.TpChargingError error, int requestNumberNextRequest);
	void directCreditAmountRes(int sessionID, int requestNumber, org.csapi.cs.TpChargingPrice creditedAmount, int requestNumberNextRequest);
	void directCreditUnitErr(int sessionID, int requestNumber, org.csapi.cs.TpChargingError error, int requestNumberNextRequest);
	void directCreditUnitRes(int sessionID, int requestNumber, org.csapi.cs.TpVolume[] creditedVolumes, int requestNumberNextRequest);
	void directDebitAmountErr(int sessionID, int requestNumber, org.csapi.cs.TpChargingError error, int requestNumberNextRequest);
	void directDebitAmountRes(int sessionID, int requestNumber, org.csapi.cs.TpChargingPrice debitedAmount, int requestNumberNextRequest);
	void directDebitUnitErr(int sessionID, int requestNumber, org.csapi.cs.TpChargingError error, int requestNumberNextRequest);
	void directDebitUnitRes(int sessionID, int requestNumber, org.csapi.cs.TpVolume[] debitedVolumes, int requestNumberNextRequest);
	void extendLifeTimeErr(int sessionID, org.csapi.cs.TpChargingError error);
	void extendLifeTimeRes(int sessionID, int sessionTimeLeft);
	void rateErr(int sessionID, org.csapi.cs.TpChargingError error);
	void rateRes(int sessionID, org.csapi.cs.TpPriceVolume[] rates, int validityTimeLeft);
	void reserveAmountErr(int sessionID, int requestNumber, org.csapi.cs.TpChargingError error, int requestNumberNextRequest);
	void reserveAmountRes(int sessionID, int requestNumber, org.csapi.cs.TpChargingPrice reservedAmount, int sessionTimeLeft, int requestNumberNextRequest);
	void reserveUnitErr(int sessionID, int requestNumber, org.csapi.cs.TpChargingError error, int requestNumberNextRequest);
	void reserveUnitRes(int sessionID, int requestNumber, org.csapi.cs.TpVolume[] reservedUnits, int sessionTimeLeft, int requestNumberNextRequest);
	void sessionEnded(int sessionID, org.csapi.cs.TpSessionEndedCause report);
}
