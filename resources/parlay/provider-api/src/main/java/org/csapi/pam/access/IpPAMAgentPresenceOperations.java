package org.csapi.pam.access;

/**
 *	Generated from IDL interface "IpPAMAgentPresence"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpPAMAgentPresenceOperations
	extends org.csapi.IpInterfaceOperations
{
	/* constants */
	/* operations  */
	void setAgentPresence(java.lang.String agent, java.lang.String agentType, org.csapi.pam.TpPAMAttribute[] attributes, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_TYPE,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTE,org.csapi.pam.P_PAM_UNKNOWN_AGENT;
	void setCapabilityPresence(java.lang.String agent, java.lang.String capability, org.csapi.pam.TpPAMAttribute[] attributes, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_CAPABILITY,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTE,org.csapi.pam.P_PAM_UNKNOWN_AGENT;
	void setAgentPresenceExpiration(java.lang.String agent, java.lang.String agentType, java.lang.String[] attributeNames, long expiresIn, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_TYPE,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTE,org.csapi.pam.P_PAM_UNKNOWN_AGENT;
	void setCapabilityPresenceExpiration(java.lang.String agent, java.lang.String capability, java.lang.String[] attributeNames, long expiresIn, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_NO_CAPABILITY,org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTE,org.csapi.pam.P_PAM_UNKNOWN_AGENT;
	org.csapi.pam.TpPAMAttribute[] getAgentPresence(java.lang.String agent, java.lang.String agentType, java.lang.String[] attributeNames, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_TYPE,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTE,org.csapi.pam.P_PAM_UNKNOWN_AGENT;
	org.csapi.pam.TpPAMAttribute[] getCapabilityPresence(java.lang.String agent, java.lang.String capability, java.lang.String[] attributeNames, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_CAPABILITY,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTE,org.csapi.pam.P_PAM_UNKNOWN_AGENT;
}
