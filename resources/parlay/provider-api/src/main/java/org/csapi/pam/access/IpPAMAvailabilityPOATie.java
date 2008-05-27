package org.csapi.pam.access;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpPAMAvailability"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpPAMAvailabilityPOATie
	extends IpPAMAvailabilityPOA
{
	private IpPAMAvailabilityOperations _delegate;

	private POA _poa;
	public IpPAMAvailabilityPOATie(IpPAMAvailabilityOperations delegate)
	{
		_delegate = delegate;
	}
	public IpPAMAvailabilityPOATie(IpPAMAvailabilityOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.pam.access.IpPAMAvailability _this()
	{
		return org.csapi.pam.access.IpPAMAvailabilityHelper.narrow(_this_object());
	}
	public org.csapi.pam.access.IpPAMAvailability _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.pam.access.IpPAMAvailabilityHelper.narrow(_this_object(orb));
	}
	public IpPAMAvailabilityOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpPAMAvailabilityOperations delegate)
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
	public org.csapi.pam.TpPAMPreferenceData getPreference(java.lang.String identity, org.csapi.pam.TpPAMContext pamContext, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_IDENTITY,org.csapi.pam.P_PAM_INVALID_CREDENTIAL
	{
		return _delegate.getPreference(identity,pamContext,authToken);
	}

	public org.csapi.pam.TpPAMAvailabilityProfile[] getAvailability(java.lang.String identity, org.csapi.pam.TpPAMContext pamContext, java.lang.String[] attributeNames, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_IDENTITY,org.csapi.pam.P_PAM_INVALID_CREDENTIAL
	{
		return _delegate.getAvailability(identity,pamContext,attributeNames,authToken);
	}

	public void setPreference(java.lang.String identity, org.csapi.pam.TpPAMContext pamContext, java.lang.String operation, org.csapi.pam.TpPAMPreferenceData newPreference, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_IDENTITY,org.csapi.pam.P_PAM_INVALID_CREDENTIAL
	{
_delegate.setPreference(identity,pamContext,operation,newPreference,authToken);
	}

}
