package org.csapi.fw.fw_application.service_agreement;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpAppServiceAgreementManagement"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpAppServiceAgreementManagementPOATie
	extends IpAppServiceAgreementManagementPOA
{
	private IpAppServiceAgreementManagementOperations _delegate;

	private POA _poa;
	public IpAppServiceAgreementManagementPOATie(IpAppServiceAgreementManagementOperations delegate)
	{
		_delegate = delegate;
	}
	public IpAppServiceAgreementManagementPOATie(IpAppServiceAgreementManagementOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.fw.fw_application.service_agreement.IpAppServiceAgreementManagement _this()
	{
		return org.csapi.fw.fw_application.service_agreement.IpAppServiceAgreementManagementHelper.narrow(_this_object());
	}
	public org.csapi.fw.fw_application.service_agreement.IpAppServiceAgreementManagement _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.fw.fw_application.service_agreement.IpAppServiceAgreementManagementHelper.narrow(_this_object(orb));
	}
	public IpAppServiceAgreementManagementOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpAppServiceAgreementManagementOperations delegate)
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
	public byte[] signServiceAgreement(java.lang.String serviceToken, java.lang.String agreementText, java.lang.String signingAlgorithm) throws org.csapi.fw.P_INVALID_SIGNING_ALGORITHM,org.csapi.TpCommonExceptions,org.csapi.fw.P_INVALID_AGREEMENT_TEXT,org.csapi.fw.P_INVALID_SERVICE_TOKEN
	{
		return _delegate.signServiceAgreement(serviceToken,agreementText,signingAlgorithm);
	}

	public void terminateServiceAgreement(java.lang.String serviceToken, java.lang.String terminationText, byte[] digitalSignature) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_INVALID_SIGNATURE,org.csapi.fw.P_INVALID_SERVICE_TOKEN
	{
_delegate.terminateServiceAgreement(serviceToken,terminationText,digitalSignature);
	}

}
