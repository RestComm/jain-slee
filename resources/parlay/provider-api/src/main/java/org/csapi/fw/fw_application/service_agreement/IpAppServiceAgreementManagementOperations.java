package org.csapi.fw.fw_application.service_agreement;

/**
 *	Generated from IDL interface "IpAppServiceAgreementManagement"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpAppServiceAgreementManagementOperations
	extends org.csapi.IpInterfaceOperations
{
	/* constants */
	/* operations  */
	byte[] signServiceAgreement(java.lang.String serviceToken, java.lang.String agreementText, java.lang.String signingAlgorithm) throws org.csapi.fw.P_INVALID_SIGNING_ALGORITHM,org.csapi.TpCommonExceptions,org.csapi.fw.P_INVALID_AGREEMENT_TEXT,org.csapi.fw.P_INVALID_SERVICE_TOKEN;
	void terminateServiceAgreement(java.lang.String serviceToken, java.lang.String terminationText, byte[] digitalSignature) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_INVALID_SIGNATURE,org.csapi.fw.P_INVALID_SERVICE_TOKEN;
}
