package org.csapi.pam.provisioning;

/**
 *	Generated from IDL interface "IpPAMIdentityManagement"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpPAMIdentityManagementOperations
	extends org.csapi.IpInterfaceOperations
{
	/* constants */
	/* operations  */
	void createIdentity(java.lang.String identity, java.lang.String[] identityTypes, byte[] authToken) throws org.csapi.pam.P_PAM_IDENTITY_EXISTS,org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_TYPE,org.csapi.pam.P_PAM_INVALID_CREDENTIAL;
	void deleteIdentity(java.lang.String identity, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_IDENTITY,org.csapi.pam.P_PAM_INVALID_CREDENTIAL;
	boolean isIdentity(java.lang.String identity, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_INVALID_CREDENTIAL;
	void createGroupIdentity(java.lang.String identity, java.lang.String[] identityTypes, byte[] authToken) throws org.csapi.pam.P_PAM_IDENTITY_EXISTS,org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_TYPE,org.csapi.pam.P_PAM_INVALID_CREDENTIAL;
	void deleteGroupIdentity(java.lang.String identity, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_IDENTITY,org.csapi.pam.P_PAM_INVALID_CREDENTIAL;
	void addToGroup(java.lang.String group, java.lang.String member, byte[] authToken) throws org.csapi.pam.P_PAM_UNKNOWN_GROUP,org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_IS_CYCLIC,org.csapi.pam.P_PAM_UNKNOWN_MEMBER,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_MEMBER_EXISTS;
	void removeFromGroup(java.lang.String group, java.lang.String identity, byte[] authToken) throws org.csapi.pam.P_PAM_UNKNOWN_GROUP,org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_MEMBER,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_NOT_MEMBER;
	java.lang.String[] listMembers(java.lang.String identity, byte[] authToken) throws org.csapi.pam.P_PAM_UNKNOWN_GROUP,org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_INVALID_CREDENTIAL;
	boolean isGroupIdentity(java.lang.String identity, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_INVALID_CREDENTIAL;
	java.lang.String[] listGroupMembership(java.lang.String identity, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_IDENTITY,org.csapi.pam.P_PAM_INVALID_CREDENTIAL;
	void addAlias(java.lang.String identity, java.lang.String alias, byte[] authToken) throws org.csapi.pam.P_PAM_ALIAS_NOT_UNIQUE,org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_IDENTITY,org.csapi.pam.P_PAM_ALIAS_EXISTS,org.csapi.pam.P_PAM_INVALID_CREDENTIAL;
	void removeAliases(java.lang.String identity, java.lang.String alias, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_IDENTITY,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNASSIGNED_ALIAS;
	java.lang.String[] listAliases(java.lang.String identity, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_IDENTITY,org.csapi.pam.P_PAM_INVALID_CREDENTIAL;
	java.lang.String lookupByAlias(java.lang.String alias, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_ALIAS;
	void associateTypes(java.lang.String identity, java.lang.String[] identityTypes, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_TYPE_ASSOCIATED,org.csapi.pam.P_PAM_UNKNOWN_TYPE,org.csapi.pam.P_PAM_UNKNOWN_IDENTITY,org.csapi.pam.P_PAM_INVALID_CREDENTIAL;
	void disassociateTypes(java.lang.String identity, java.lang.String[] identityTypes, byte[] authToken) throws org.csapi.pam.P_PAM_DISASSOCIATED_TYPE,org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_IDENTITY,org.csapi.pam.P_PAM_INVALID_CREDENTIAL;
	java.lang.String[] listTypesOfIdentity(java.lang.String identity, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_IDENTITY,org.csapi.pam.P_PAM_INVALID_CREDENTIAL;
	boolean hasType(java.lang.String identity, java.lang.String typeName, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_IDENTITY,org.csapi.pam.P_PAM_INVALID_CREDENTIAL;
	org.csapi.pam.TpPAMAttribute[] getIdentityAttributes(java.lang.String identity, java.lang.String identityType, java.lang.String[] attributeNames, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_TYPE,org.csapi.pam.P_PAM_UNKNOWN_IDENTITY,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTE;
	void setIdentityAttributes(java.lang.String identity, java.lang.String identityType, org.csapi.pam.TpPAMAttribute[] attributes, byte[] authToken) throws org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTES,org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_TYPE,org.csapi.pam.P_PAM_UNKNOWN_IDENTITY,org.csapi.pam.P_PAM_INVALID_CREDENTIAL;
}
