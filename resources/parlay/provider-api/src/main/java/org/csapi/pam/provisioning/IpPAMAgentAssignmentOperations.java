package org.csapi.pam.provisioning;

/**
 *	Generated from IDL interface "IpPAMAgentAssignment"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpPAMAgentAssignmentOperations
	extends org.csapi.IpInterfaceOperations
{
	/* constants */
	/* operations  */
	void assignAgent(java.lang.String identity, java.lang.String agentName, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_IDENTITY,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_AGENT;
	void unassignAgent(java.lang.String identity, java.lang.String agentName, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_IDENTITY,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_ASSIGNMENT,org.csapi.pam.P_PAM_UNKNOWN_AGENT;
	java.lang.String[] listAssignedAgents(java.lang.String identity, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_IDENTITY,org.csapi.pam.P_PAM_INVALID_CREDENTIAL;
	java.lang.String[] listAssociatedIdentitiesOfAgent(java.lang.String agentName, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_AGENT;
	java.lang.String[] listAssignedAgentsByCapability(java.lang.String identity, java.lang.String capability, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_IDENTITY,org.csapi.pam.P_PAM_INVALID_CREDENTIAL;
	java.lang.String[] listCapabilitiesOfIdentity(java.lang.String identity, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_IDENTITY,org.csapi.pam.P_PAM_INVALID_CREDENTIAL;
	boolean isIdentityCapableOf(java.lang.String identity, java.lang.String capability, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_IDENTITY,org.csapi.pam.P_PAM_INVALID_CREDENTIAL;
}
