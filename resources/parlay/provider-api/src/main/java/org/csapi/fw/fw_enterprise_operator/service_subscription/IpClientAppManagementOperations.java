package org.csapi.fw.fw_enterprise_operator.service_subscription;

/**
 *	Generated from IDL interface "IpClientAppManagement"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpClientAppManagementOperations
	extends org.csapi.IpInterfaceOperations
{
	/* constants */
	/* operations  */
	void createClientApp(org.csapi.fw.TpClientAppDescription clientAppDescription) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED,org.csapi.fw.P_INVALID_CLIENT_APP_ID;
	void modifyClientApp(org.csapi.fw.TpClientAppDescription clientAppDescription) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED,org.csapi.fw.P_INVALID_CLIENT_APP_ID;
	void deleteClientApp(java.lang.String clientAppID) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED,org.csapi.fw.P_INVALID_CLIENT_APP_ID;
	void createSAG(org.csapi.fw.TpSag sag, java.lang.String[] clientAppIDs) throws org.csapi.fw.P_INVALID_SAG_ID,org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED,org.csapi.fw.P_INVALID_CLIENT_APP_ID;
	void modifySAG(org.csapi.fw.TpSag sag) throws org.csapi.fw.P_INVALID_SAG_ID,org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED;
	void deleteSAG(java.lang.String sagID) throws org.csapi.fw.P_INVALID_SAG_ID,org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED;
	void addSAGMembers(java.lang.String sagID, java.lang.String[] clientAppIDs) throws org.csapi.fw.P_INVALID_SAG_ID,org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED,org.csapi.fw.P_INVALID_ADDITION_TO_SAG,org.csapi.fw.P_INVALID_CLIENT_APP_ID;
	void removeSAGMembers(java.lang.String sagID, java.lang.String[] clientAppIDList) throws org.csapi.fw.P_INVALID_SAG_ID,org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED,org.csapi.fw.P_INVALID_CLIENT_APP_ID;
	org.csapi.fw.TpAddSagMembersConflict[] requestConflictInfo() throws org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED;
}
