package org.csapi.pam.provisioning;

/**
 *	Generated from IDL interface "IpPAMIdentityTypeManagement"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpPAMIdentityTypeManagementOperations
	extends org.csapi.IpInterfaceOperations
{
	/* constants */
	/* operations  */
	void createIdentityAttribute(org.csapi.pam.TpPAMAttributeDef pAttribute, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_ATTRIBUTE_EXISTS,org.csapi.pam.P_PAM_INVALID_CREDENTIAL;
	void deleteIdentityAttribute(java.lang.String attributeName, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTE;
	org.csapi.pam.TpPAMAttributeDef getIdentityAttributeDefinition(java.lang.String attributeName, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTE;
	java.lang.String[] listAllIdentityAttributes(byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_INVALID_CREDENTIAL;
	void createIdentityType(java.lang.String typeName, java.lang.String[] attributeNames, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTE,org.csapi.pam.P_PAM_TYPE_EXISTS;
	void deleteIdentityType(java.lang.String typeName, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_TYPE,org.csapi.pam.P_PAM_INVALID_CREDENTIAL;
	java.lang.String[] listIdentityTypes(byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_INVALID_CREDENTIAL;
	void addIdentityTypeAttributes(java.lang.String typeName, java.lang.String[] attributeNames, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_ATTRIBUTE_EXISTS,org.csapi.pam.P_PAM_UNKNOWN_TYPE,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTE;
	void removeIdentityTypeAttributes(java.lang.String typeName, java.lang.String[] attributeNames, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_TYPE,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTE;
	java.lang.String[] listIdentityTypeAttributes(java.lang.String typeName, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_TYPE,org.csapi.pam.P_PAM_INVALID_CREDENTIAL;
}
