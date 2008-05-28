package org.csapi.cm;

/**
 *	Generated from IDL interface "IpEnterpriseNetwork"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpEnterpriseNetworkOperations
	extends org.csapi.IpServiceOperations
{
	/* constants */
	/* operations  */
	java.lang.String[] getSiteList() throws org.csapi.cm.P_UNKNOWN_SITES,org.csapi.TpCommonExceptions;
	org.csapi.IpInterface getVPrN() throws org.csapi.TpCommonExceptions,org.csapi.cm.P_UNKNOWN_VPRN;
	org.csapi.IpInterface getSite(java.lang.String siteID) throws org.csapi.TpCommonExceptions,org.csapi.cm.P_ILLEGAL_SITE_ID,org.csapi.cm.P_UNKNOWN_SITE_ID;
}
