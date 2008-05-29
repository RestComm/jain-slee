package org.csapi.fw.fw_application.discovery;

/**
 *	Generated from IDL interface "IpServiceDiscovery"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpServiceDiscoveryOperations
	extends org.csapi.IpInterfaceOperations
{
	/* constants */
	/* operations  */
	java.lang.String[] listServiceTypes() throws org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED;
	org.csapi.fw.TpServiceTypeDescription describeServiceType(java.lang.String name) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_ILLEGAL_SERVICE_TYPE,org.csapi.fw.P_ACCESS_DENIED,org.csapi.fw.P_UNKNOWN_SERVICE_TYPE;
	org.csapi.fw.TpService[] discoverService(java.lang.String serviceTypeName, org.csapi.fw.TpServiceProperty[] desiredPropertyList, int max) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_ILLEGAL_SERVICE_TYPE,org.csapi.fw.P_ACCESS_DENIED,org.csapi.fw.P_INVALID_PROPERTY,org.csapi.fw.P_UNKNOWN_SERVICE_TYPE;
	org.csapi.fw.TpService[] listSubscribedServices() throws org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED;
}
