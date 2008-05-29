package org.csapi.fw.fw_application.service_agreement;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpServiceAgreementManagement"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpServiceAgreementManagementPOATie
	extends IpServiceAgreementManagementPOA
{
	private IpServiceAgreementManagementOperations _delegate;

	private POA _poa;
	public IpServiceAgreementManagementPOATie(IpServiceAgreementManagementOperations delegate)
	{
		_delegate = delegate;
	}
	public IpServiceAgreementManagementPOATie(IpServiceAgreementManagementOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.fw.fw_application.service_agreement.IpServiceAgreementManagement _this()
	{
		return org.csapi.fw.fw_application.service_agreement.IpServiceAgreementManagementHelper.narrow(_this_object());
	}
	public org.csapi.fw.fw_application.service_agreement.IpServiceAgreementManagement _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.fw.fw_application.service_agreement.IpServiceAgreementManagementHelper.narrow(_this_object(orb));
	}
	public IpServiceAgreementManagementOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpServiceAgreementManagementOperations delegate)
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
	public org.csapi.fw.TpSignatureAndServiceMgr signServiceAgreement(java.lang.String serviceToken, java.lang.String agreementText, java.lang.String signingAlgorithm) throws org.csapi.fw.P_INVALID_SIGNING_ALGORITHM,org.csapi.TpCommonExceptions,org.csapi.fw.P_INVALID_AGREEMENT_TEXT,org.csapi.fw.P_ACCESS_DENIED,org.csapi.fw.P_INVALID_SERVICE_TOKEN,org.csapi.fw.P_SERVICE_ACCESS_DENIED
	{
		return _delegate.signServiceAgreement(serviceToken,agreementText,signingAlgorithm);
	}

	public void initiateSignServiceAgreement(java.lang.String serviceToken) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_INVALID_SERVICE_TOKEN,org.csapi.fw.P_SERVICE_ACCESS_DENIED
	{
_delegate.initiateSignServiceAgreement(serviceToken);
	}

	public java.lang.String selectService(java.lang.String serviceID) throws org.csapi.fw.P_INVALID_SERVICE_ID,org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED,org.csapi.fw.P_SERVICE_ACCESS_DENIED
	{
		return _delegate.selectService(serviceID);
	}

	public void terminateServiceAgreement(java.lang.String serviceToken, java.lang.String terminationText, byte[] digitalSignature) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_INVALID_SIGNATURE,org.csapi.fw.P_ACCESS_DENIED,org.csapi.fw.P_INVALID_SERVICE_TOKEN
	{
_delegate.terminateServiceAgreement(serviceToken,terminationText,digitalSignature);
	}

}
