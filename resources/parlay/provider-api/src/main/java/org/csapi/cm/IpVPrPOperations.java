package org.csapi.cm;

/**
 *	Generated from IDL interface "IpVPrP"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpVPrPOperations
	extends org.csapi.IpServiceOperations
{
	/* constants */
	/* operations  */
	java.lang.String getVPrPID() throws org.csapi.cm.P_UNKNOWN_VPRP_ID,org.csapi.TpCommonExceptions;
	java.lang.String getSlaID() throws org.csapi.cm.P_UNKNOWN_SLA_ID,org.csapi.TpCommonExceptions;
	org.csapi.cm.TpVprpStatus getStatus() throws org.csapi.TpCommonExceptions,org.csapi.cm.P_UNKNOWN_STATUS;
	org.csapi.cm.TpProvisionedQoSInfo getProvisionedQoSInfo() throws org.csapi.TpCommonExceptions,org.csapi.cm.P_UNKNOWN_QOS_INFO;
	org.csapi.cm.TpValidityInfo getValidityInfo() throws org.csapi.TpCommonExceptions,org.csapi.cm.P_UNKNOWN_VALIDITY_INFO;
	org.csapi.cm.TpPipeQoSInfo getPipeQoSInfo() throws org.csapi.TpCommonExceptions,org.csapi.cm.P_UNKNOWN_PIPEQOSINFO;
	org.csapi.cm.TpDsCodepoint getDsCodepoint() throws org.csapi.cm.P_UNKNOWN_DSCODEPOINT,org.csapi.TpCommonExceptions;
}
