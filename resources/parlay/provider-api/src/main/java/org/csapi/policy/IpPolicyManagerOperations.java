package org.csapi.policy;

/**
 *	Generated from IDL interface "IpPolicyManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpPolicyManagerOperations
	extends org.csapi.IpInterfaceOperations
{
	/* constants */
	/* operations  */
	org.csapi.policy.IpPolicyDomain createDomain(java.lang.String domainName) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR;
	org.csapi.policy.IpPolicyDomain getDomain(java.lang.String domainName) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR;
	void removeDomain(java.lang.String domainName) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR;
	int getDomainCount() throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION;
	org.csapi.policy.IpPolicyIterator getDomainIterator() throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION;
	java.lang.String[] findMatchingDomains(org.csapi.TpAttribute[] matchingAttributes) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION;
	org.csapi.policy.IpPolicyRepository createRepository(java.lang.String repositoryName) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR;
	org.csapi.policy.IpPolicyRepository getRepository(java.lang.String repositoryName) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR;
	void removeRepository(java.lang.String repositoryName) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR;
	int getRepositoryCount() throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION;
	org.csapi.policy.IpPolicyIterator getRepositoryIterator() throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION;
	void startTransaction() throws org.csapi.TpCommonExceptions,org.csapi.policy.P_TRANSACTION_IN_PROCESS,org.csapi.policy.P_ACCESS_VIOLATION;
	boolean commitTransaction() throws org.csapi.TpCommonExceptions,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS;
	void abortTransaction() throws org.csapi.TpCommonExceptions,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS;
}
