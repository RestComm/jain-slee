package org.csapi.cm;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpEnterpriseNetwork"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpEnterpriseNetworkPOATie
	extends IpEnterpriseNetworkPOA
{
	private IpEnterpriseNetworkOperations _delegate;

	private POA _poa;
	public IpEnterpriseNetworkPOATie(IpEnterpriseNetworkOperations delegate)
	{
		_delegate = delegate;
	}
	public IpEnterpriseNetworkPOATie(IpEnterpriseNetworkOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.cm.IpEnterpriseNetwork _this()
	{
		return org.csapi.cm.IpEnterpriseNetworkHelper.narrow(_this_object());
	}
	public org.csapi.cm.IpEnterpriseNetwork _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.cm.IpEnterpriseNetworkHelper.narrow(_this_object(orb));
	}
	public IpEnterpriseNetworkOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpEnterpriseNetworkOperations delegate)
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
	public void setCallbackWithSessionID(org.csapi.IpInterface appInterface, int sessionID) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
_delegate.setCallbackWithSessionID(appInterface,sessionID);
	}

	public org.csapi.IpInterface getSite(java.lang.String siteID) throws org.csapi.TpCommonExceptions,org.csapi.cm.P_ILLEGAL_SITE_ID,org.csapi.cm.P_UNKNOWN_SITE_ID
	{
		return _delegate.getSite(siteID);
	}

	public java.lang.String[] getSiteList() throws org.csapi.cm.P_UNKNOWN_SITES,org.csapi.TpCommonExceptions
	{
		return _delegate.getSiteList();
	}

	public void setCallback(org.csapi.IpInterface appInterface) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions
	{
_delegate.setCallback(appInterface);
	}

	public org.csapi.IpInterface getVPrN() throws org.csapi.TpCommonExceptions,org.csapi.cm.P_UNKNOWN_VPRN
	{
		return _delegate.getVPrN();
	}

}
