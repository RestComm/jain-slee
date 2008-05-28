package org.csapi.pam.event;

/**
 *	Generated from IDL interface "IpPAMEventHandler"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpPAMEventHandlerOperations
	extends org.csapi.IpInterfaceOperations
{
	/* constants */
	/* operations  */
	boolean isRegistered(int clientID, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_INVALID_CREDENTIAL;
	int registerAppInterface(org.csapi.pam.event.IpAppPAMEventHandler appInterface, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_INVALID_CREDENTIAL;
	int registerForEvent(int clientID, org.csapi.pam.TpPAMEventInfo[] eventList, int validFor, byte[] authToken) throws org.csapi.pam.P_PAM_NOT_REGISTERED,org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_INVALID_CREDENTIAL;
	void deregisterAppInterface(int clientID, byte[] authToken) throws org.csapi.pam.P_PAM_NOT_REGISTERED,org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_INVALID_CREDENTIAL;
	void deregisterFromEvent(int eventID, byte[] authToken) throws org.csapi.pam.P_PAM_NOT_REGISTERED,org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_INVALID_CREDENTIAL;
}
