package org.csapi.fw.fw_service.service_registration;

/**
 *	Generated from IDL interface "IpFwServiceRegistration"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpFwServiceRegistrationOperations
	extends org.csapi.IpInterfaceOperations
{
	/* constants */
	/* operations  */
	java.lang.String registerService(java.lang.String serviceTypeName, org.csapi.fw.TpServiceProperty[] servicePropertyList) throws org.csapi.fw.P_SERVICE_TYPE_UNAVAILABLE,org.csapi.fw.P_MISSING_MANDATORY_PROPERTY,org.csapi.TpCommonExceptions,org.csapi.fw.P_ILLEGAL_SERVICE_TYPE,org.csapi.fw.P_UNKNOWN_SERVICE_TYPE,org.csapi.fw.P_DUPLICATE_PROPERTY_NAME,org.csapi.fw.P_PROPERTY_TYPE_MISMATCH;
	void announceServiceAvailability(java.lang.String serviceID, org.csapi.fw.fw_service.service_lifecycle.IpServiceInstanceLifecycleManager serviceInstanceLifecycleManagerRef) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions,org.csapi.fw.P_UNKNOWN_SERVICE_ID,org.csapi.fw.P_ILLEGAL_SERVICE_ID;
	void unregisterService(java.lang.String serviceID) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_UNKNOWN_SERVICE_ID,org.csapi.fw.P_ILLEGAL_SERVICE_ID;
	org.csapi.fw.TpServiceDescription describeService(java.lang.String serviceID) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_UNKNOWN_SERVICE_ID,org.csapi.fw.P_ILLEGAL_SERVICE_ID;
	void unannounceService(java.lang.String serviceID) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_UNKNOWN_SERVICE_ID,org.csapi.fw.P_ILLEGAL_SERVICE_ID;
}
