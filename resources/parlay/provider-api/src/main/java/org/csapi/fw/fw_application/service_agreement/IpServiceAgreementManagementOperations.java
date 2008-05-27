package org.csapi.fw.fw_application.service_agreement;

/**
 *	Generated from IDL interface "IpServiceAgreementManagement"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpServiceAgreementManagementOperations
	extends org.csapi.IpInterfaceOperations
{
	/* constants */
	/* operations  */
	org.csapi.fw.TpSignatureAndServiceMgr signServiceAgreement(java.lang.String serviceToken, java.lang.String agreementText, java.lang.String signingAlgorithm) throws org.csapi.fw.P_INVALID_SIGNING_ALGORITHM,org.csapi.TpCommonExceptions,org.csapi.fw.P_INVALID_AGREEMENT_TEXT,org.csapi.fw.P_ACCESS_DENIED,org.csapi.fw.P_INVALID_SERVICE_TOKEN,org.csapi.fw.P_SERVICE_ACCESS_DENIED;
	void terminateServiceAgreement(java.lang.String serviceToken, java.lang.String terminationText, byte[] digitalSignature) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_INVALID_SIGNATURE,org.csapi.fw.P_ACCESS_DENIED,org.csapi.fw.P_INVALID_SERVICE_TOKEN;
	java.lang.String selectService(java.lang.String serviceID) throws org.csapi.fw.P_INVALID_SERVICE_ID,org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED,org.csapi.fw.P_SERVICE_ACCESS_DENIED;
	void initiateSignServiceAgreement(java.lang.String serviceToken) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_INVALID_SERVICE_TOKEN,org.csapi.fw.P_SERVICE_ACCESS_DENIED;
}
