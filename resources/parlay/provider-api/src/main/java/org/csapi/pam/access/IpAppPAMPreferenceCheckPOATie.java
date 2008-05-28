package org.csapi.pam.access;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpAppPAMPreferenceCheck"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpAppPAMPreferenceCheckPOATie
	extends IpAppPAMPreferenceCheckPOA
{
	private IpAppPAMPreferenceCheckOperations _delegate;

	private POA _poa;
	public IpAppPAMPreferenceCheckPOATie(IpAppPAMPreferenceCheckOperations delegate)
	{
		_delegate = delegate;
	}
	public IpAppPAMPreferenceCheckPOATie(IpAppPAMPreferenceCheckOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.pam.access.IpAppPAMPreferenceCheck _this()
	{
		return org.csapi.pam.access.IpAppPAMPreferenceCheckHelper.narrow(_this_object());
	}
	public org.csapi.pam.access.IpAppPAMPreferenceCheck _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.pam.access.IpAppPAMPreferenceCheckHelper.narrow(_this_object(orb));
	}
	public IpAppPAMPreferenceCheckOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpAppPAMPreferenceCheckOperations delegate)
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
	public org.csapi.pam.TpPAMAvailabilityProfile[] computeAvailability(java.lang.String identity, org.csapi.pam.TpPAMContext pamContext, java.lang.String[] attributeNames, byte[] authToken)
	{
		return _delegate.computeAvailability(identity,pamContext,attributeNames,authToken);
	}

}
