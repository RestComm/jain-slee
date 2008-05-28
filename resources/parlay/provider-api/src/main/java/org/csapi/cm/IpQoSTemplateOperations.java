package org.csapi.cm;

/**
 *	Generated from IDL interface "IpQoSTemplate"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpQoSTemplateOperations
	extends org.csapi.IpServiceOperations
{
	/* constants */
	/* operations  */
	java.lang.String getTemplateType() throws org.csapi.TpCommonExceptions,org.csapi.cm.P_UNKNOWN_TEMPLATE_TYPE;
	java.lang.String getDescription() throws org.csapi.cm.P_UNKNOWN_DESCRIPTION,org.csapi.TpCommonExceptions;
	void setSlaID(java.lang.String slaID) throws org.csapi.TpCommonExceptions,org.csapi.cm.P_ILLEGAL_SLA_ID;
	org.csapi.cm.TpPipeQoSInfo getPipeQoSInfo() throws org.csapi.TpCommonExceptions,org.csapi.cm.P_UNKNOWN_PIPEQOSINFO;
	void setPipeQoSInfo(org.csapi.cm.TpPipeQoSInfo pipeQoSInfo) throws org.csapi.cm.P_ILLEGAL_TAG,org.csapi.TpCommonExceptions,org.csapi.cm.P_ILLEGAL_VALUE,org.csapi.cm.P_ILLEGAL_COMBINATION;
	org.csapi.cm.TpValidityInfo getValidityInfo() throws org.csapi.TpCommonExceptions,org.csapi.cm.P_UNKNOWN_VALIDITY_INFO;
	void setValidityInfo(org.csapi.cm.TpValidityInfo validityInfo) throws org.csapi.cm.P_ILLEGAL_TAG,org.csapi.TpCommonExceptions,org.csapi.cm.P_ILLEGAL_VALUE,org.csapi.cm.P_ILLEGAL_COMBINATION;
	void setProvisionedQoSInfo(org.csapi.cm.TpProvisionedQoSInfo provisionedQoSInfo) throws org.csapi.cm.P_ILLEGAL_TAG,org.csapi.TpCommonExceptions,org.csapi.cm.P_ILLEGAL_VALUE,org.csapi.cm.P_ILLEGAL_COMBINATION;
	org.csapi.cm.TpProvisionedQoSInfo getProvisionedQoSInfo() throws org.csapi.TpCommonExceptions,org.csapi.cm.P_UNKNOWN_QOS_INFO;
	org.csapi.cm.TpDsCodepoint getDsCodepoint() throws org.csapi.cm.P_UNKNOWN_DSCODEPOINT,org.csapi.TpCommonExceptions;
}
