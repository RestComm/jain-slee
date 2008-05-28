package org.csapi.cm;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpEnterpriseNetworkSite"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpEnterpriseNetworkSitePOATie
	extends IpEnterpriseNetworkSitePOA
{
	private IpEnterpriseNetworkSiteOperations _delegate;

	private POA _poa;
	public IpEnterpriseNetworkSitePOATie(IpEnterpriseNetworkSiteOperations delegate)
	{
		_delegate = delegate;
	}
	public IpEnterpriseNetworkSitePOATie(IpEnterpriseNetworkSiteOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.cm.IpEnterpriseNetworkSite _this()
	{
		return org.csapi.cm.IpEnterpriseNetworkSiteHelper.narrow(_this_object());
	}
	public org.csapi.cm.IpEnterpriseNetworkSite _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.cm.IpEnterpriseNetworkSiteHelper.narrow(_this_object(orb));
	}
	public IpEnterpriseNetworkSiteOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpEnterpriseNetworkSiteOperations delegate)
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
	public void setCallback(org.csapi.IpInterface appInterface) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions
	{
_delegate.setCallback(appInterface);
	}

	public org.csapi.cm.TpIPSubnet getSAPIPSubnet(java.lang.String sapID) throws org.csapi.TpCommonExceptions,org.csapi.cm.P_UNKNOWN_SAP,org.csapi.cm.P_ILLEGAL_SITE_ID,org.csapi.cm.P_UNKNOWN_IPSUBNET
	{
		return _delegate.getSAPIPSubnet(sapID);
	}

	public java.lang.String getSiteLocation() throws org.csapi.TpCommonExceptions,org.csapi.cm.P_UNKNOWN_SITE_LOCATION
	{
		return _delegate.getSiteLocation();
	}

	public org.csapi.IpInterface getVPrN() throws org.csapi.TpCommonExceptions,org.csapi.cm.P_UNKNOWN_VPRN
	{
		return _delegate.getVPrN();
	}

	public org.csapi.cm.TpIPSubnet getIPSubnet() throws org.csapi.TpCommonExceptions,org.csapi.cm.P_UNKNOWN_IPSUBNET
	{
		return _delegate.getIPSubnet();
	}

	public java.lang.String[] getSiteList() throws org.csapi.cm.P_UNKNOWN_SITES,org.csapi.TpCommonExceptions
	{
		return _delegate.getSiteList();
	}

	public java.lang.String getSiteID() throws org.csapi.TpCommonExceptions,org.csapi.cm.P_UNKNOWN_SITE_ID
	{
		return _delegate.getSiteID();
	}

	public org.csapi.IpInterface getSite(java.lang.String siteID) throws org.csapi.TpCommonExceptions,org.csapi.cm.P_ILLEGAL_SITE_ID,org.csapi.cm.P_UNKNOWN_SITE_ID
	{
		return _delegate.getSite(siteID);
	}

	public java.lang.String[] getSAPList() throws org.csapi.TpCommonExceptions,org.csapi.cm.P_UNKNOWN_SAPS
	{
		return _delegate.getSAPList();
	}

	public java.lang.String getSiteDescription() throws org.csapi.TpCommonExceptions,org.csapi.cm.P_UNKNOWN_SITE_DESCRIPTION
	{
		return _delegate.getSiteDescription();
	}

	public void setCallbackWithSessionID(org.csapi.IpInterface appInterface, int sessionID) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
_delegate.setCallbackWithSessionID(appInterface,sessionID);
	}

}
