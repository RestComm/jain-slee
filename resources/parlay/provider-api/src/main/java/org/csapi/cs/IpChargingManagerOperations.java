package org.csapi.cs;

/**
 *	Generated from IDL interface "IpChargingManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpChargingManagerOperations
	extends org.csapi.IpServiceOperations
{
	/* constants */
	/* operations  */
	org.csapi.cs.TpChargingSessionID createChargingSession(org.csapi.cs.IpAppChargingSession appChargingSession, java.lang.String sessionDescription, org.csapi.cs.TpMerchantAccountID merchantAccount, org.csapi.TpAddress user, org.csapi.cs.TpCorrelationID correlationID) throws org.csapi.TpCommonExceptions,org.csapi.cs.P_INVALID_ACCOUNT,org.csapi.cs.P_INVALID_USER;
	org.csapi.cs.TpChargingSessionID createSplitChargingSession(org.csapi.cs.IpAppChargingSession appChargingSession, java.lang.String sessionDescription, org.csapi.cs.TpMerchantAccountID merchantAccount, org.csapi.TpAddress[] users, org.csapi.cs.TpCorrelationID correlationID) throws org.csapi.TpCommonExceptions,org.csapi.cs.P_INVALID_ACCOUNT,org.csapi.cs.P_INVALID_USER;
}
