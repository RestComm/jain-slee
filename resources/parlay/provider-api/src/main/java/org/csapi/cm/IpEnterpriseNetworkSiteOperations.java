package org.csapi.cm;

/**
 *	Generated from IDL interface "IpEnterpriseNetworkSite"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpEnterpriseNetworkSiteOperations
	extends org.csapi.cm.IpEnterpriseNetworkOperations
{
	/* constants */
	/* operations  */
	java.lang.String[] getSAPList() throws org.csapi.TpCommonExceptions,org.csapi.cm.P_UNKNOWN_SAPS;
	java.lang.String getSiteID() throws org.csapi.TpCommonExceptions,org.csapi.cm.P_UNKNOWN_SITE_ID;
	java.lang.String getSiteLocation() throws org.csapi.TpCommonExceptions,org.csapi.cm.P_UNKNOWN_SITE_LOCATION;
	java.lang.String getSiteDescription() throws org.csapi.TpCommonExceptions,org.csapi.cm.P_UNKNOWN_SITE_DESCRIPTION;
	org.csapi.cm.TpIPSubnet getIPSubnet() throws org.csapi.TpCommonExceptions,org.csapi.cm.P_UNKNOWN_IPSUBNET;
	org.csapi.cm.TpIPSubnet getSAPIPSubnet(java.lang.String sapID) throws org.csapi.TpCommonExceptions,org.csapi.cm.P_UNKNOWN_SAP,org.csapi.cm.P_ILLEGAL_SITE_ID,org.csapi.cm.P_UNKNOWN_IPSUBNET;
}
