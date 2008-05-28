package org.csapi.fw.fw_service.service_lifecycle;

/**
 *	Generated from IDL interface "IpServiceInstanceLifecycleManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpServiceInstanceLifecycleManagerOperations
	extends org.csapi.IpInterfaceOperations
{
	/* constants */
	/* operations  */
	org.csapi.IpService createServiceManager(java.lang.String application, org.csapi.fw.TpServiceProperty[] serviceProperties, java.lang.String serviceInstanceID) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_INVALID_PROPERTY;
	void destroyServiceManager(java.lang.String serviceInstance) throws org.csapi.TpCommonExceptions;
}
