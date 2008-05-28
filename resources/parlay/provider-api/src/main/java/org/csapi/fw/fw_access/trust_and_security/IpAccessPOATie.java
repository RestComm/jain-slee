package org.csapi.fw.fw_access.trust_and_security;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpAccess"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpAccessPOATie
	extends IpAccessPOA
{
	private IpAccessOperations _delegate;

	private POA _poa;
	public IpAccessPOATie(IpAccessOperations delegate)
	{
		_delegate = delegate;
	}
	public IpAccessPOATie(IpAccessOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.fw.fw_access.trust_and_security.IpAccess _this()
	{
		return org.csapi.fw.fw_access.trust_and_security.IpAccessHelper.narrow(_this_object());
	}
	public org.csapi.fw.fw_access.trust_and_security.IpAccess _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.fw.fw_access.trust_and_security.IpAccessHelper.narrow(_this_object(orb));
	}
	public IpAccessOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpAccessOperations delegate)
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
	public void releaseInterface(java.lang.String interfaceName) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED,org.csapi.P_INVALID_INTERFACE_NAME
	{
_delegate.releaseInterface(interfaceName);
	}

	public org.csapi.IpInterface obtainInterfaceWithCallback(java.lang.String interfaceName, org.csapi.IpInterface clientInterface) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED,org.csapi.P_INVALID_INTERFACE_NAME
	{
		return _delegate.obtainInterfaceWithCallback(interfaceName,clientInterface);
	}

	public void terminateAccess(java.lang.String terminationText, byte[] digitalSignature) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_INVALID_SIGNATURE
	{
_delegate.terminateAccess(terminationText,digitalSignature);
	}

	public void endAccess(org.csapi.fw.TpProperty[] endAccessProperties) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED,org.csapi.fw.P_INVALID_PROPERTY
	{
_delegate.endAccess(endAccessProperties);
	}

	public void relinquishInterface(java.lang.String interfaceName, java.lang.String terminationText, byte[] digitalSignature) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_INVALID_SIGNATURE,org.csapi.P_INVALID_INTERFACE_NAME
	{
_delegate.relinquishInterface(interfaceName,terminationText,digitalSignature);
	}

	public java.lang.String[] listInterfaces() throws org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED
	{
		return _delegate.listInterfaces();
	}

	public org.csapi.IpInterface obtainInterface(java.lang.String interfaceName) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED,org.csapi.P_INVALID_INTERFACE_NAME
	{
		return _delegate.obtainInterface(interfaceName);
	}

	public java.lang.String selectSigningAlgorithm(java.lang.String signingAlgorithmCaps) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED,org.csapi.fw.P_NO_ACCEPTABLE_SIGNING_ALGORITHM
	{
		return _delegate.selectSigningAlgorithm(signingAlgorithmCaps);
	}

}
