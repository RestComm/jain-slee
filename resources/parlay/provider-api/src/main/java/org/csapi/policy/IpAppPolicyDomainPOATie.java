package org.csapi.policy;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpAppPolicyDomain"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpAppPolicyDomainPOATie
	extends IpAppPolicyDomainPOA
{
	private IpAppPolicyDomainOperations _delegate;

	private POA _poa;
	public IpAppPolicyDomainPOATie(IpAppPolicyDomainOperations delegate)
	{
		_delegate = delegate;
	}
	public IpAppPolicyDomainPOATie(IpAppPolicyDomainOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.policy.IpAppPolicyDomain _this()
	{
		return org.csapi.policy.IpAppPolicyDomainHelper.narrow(_this_object());
	}
	public org.csapi.policy.IpAppPolicyDomain _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.policy.IpAppPolicyDomainHelper.narrow(_this_object(orb));
	}
	public IpAppPolicyDomainOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpAppPolicyDomainOperations delegate)
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
	public void reportNotification(int assignmentID, org.csapi.policy.TpPolicyEvent event)
	{
_delegate.reportNotification(assignmentID,event);
	}

}
