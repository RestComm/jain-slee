package org.csapi.policy;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpPolicyManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpPolicyManagerPOATie
	extends IpPolicyManagerPOA
{
	private IpPolicyManagerOperations _delegate;

	private POA _poa;
	public IpPolicyManagerPOATie(IpPolicyManagerOperations delegate)
	{
		_delegate = delegate;
	}
	public IpPolicyManagerPOATie(IpPolicyManagerOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.policy.IpPolicyManager _this()
	{
		return org.csapi.policy.IpPolicyManagerHelper.narrow(_this_object());
	}
	public org.csapi.policy.IpPolicyManager _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.policy.IpPolicyManagerHelper.narrow(_this_object(orb));
	}
	public IpPolicyManagerOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpPolicyManagerOperations delegate)
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
	public void abortTransaction() throws org.csapi.TpCommonExceptions,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS
	{
_delegate.abortTransaction();
	}

	public void removeRepository(java.lang.String repositoryName) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR
	{
_delegate.removeRepository(repositoryName);
	}

	public org.csapi.policy.IpPolicyDomain getDomain(java.lang.String domainName) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR
	{
		return _delegate.getDomain(domainName);
	}

	public org.csapi.policy.IpPolicyRepository createRepository(java.lang.String repositoryName) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR
	{
		return _delegate.createRepository(repositoryName);
	}

	public int getDomainCount() throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION
	{
		return _delegate.getDomainCount();
	}

	public org.csapi.policy.IpPolicyDomain createDomain(java.lang.String domainName) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR
	{
		return _delegate.createDomain(domainName);
	}

	public org.csapi.policy.IpPolicyRepository getRepository(java.lang.String repositoryName) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR
	{
		return _delegate.getRepository(repositoryName);
	}

	public boolean commitTransaction() throws org.csapi.TpCommonExceptions,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS
	{
		return _delegate.commitTransaction();
	}

	public void startTransaction() throws org.csapi.TpCommonExceptions,org.csapi.policy.P_TRANSACTION_IN_PROCESS,org.csapi.policy.P_ACCESS_VIOLATION
	{
_delegate.startTransaction();
	}

	public void removeDomain(java.lang.String domainName) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR
	{
_delegate.removeDomain(domainName);
	}

	public java.lang.String[] findMatchingDomains(org.csapi.TpAttribute[] matchingAttributes) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION
	{
		return _delegate.findMatchingDomains(matchingAttributes);
	}

	public org.csapi.policy.IpPolicyIterator getRepositoryIterator() throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION
	{
		return _delegate.getRepositoryIterator();
	}

	public int getRepositoryCount() throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION
	{
		return _delegate.getRepositoryCount();
	}

	public org.csapi.policy.IpPolicyIterator getDomainIterator() throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION
	{
		return _delegate.getDomainIterator();
	}

}
