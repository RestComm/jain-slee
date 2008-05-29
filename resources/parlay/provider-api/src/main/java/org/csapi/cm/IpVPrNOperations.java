package org.csapi.cm;

/**
 *	Generated from IDL interface "IpVPrN"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpVPrNOperations
	extends org.csapi.IpServiceOperations
{
	/* constants */
	/* operations  */
	java.lang.String[] getVPrPList() throws org.csapi.TpCommonExceptions,org.csapi.cm.P_UNKNOWN_VPRP;
	org.csapi.IpInterface getVPrP(java.lang.String vPrPID) throws org.csapi.cm.P_UNKNOWN_VPRP_ID,org.csapi.TpCommonExceptions,org.csapi.cm.P_ILLEGAL_VPRPID;
	org.csapi.IpInterface createVPrP(org.csapi.IpInterface templateRef) throws org.csapi.TpCommonExceptions,org.csapi.cm.P_UNKNOWN_INTERFACE,org.csapi.cm.P_ILLEGAL_REF_VALUE;
	void deleteVPrP(java.lang.String vPrPID) throws org.csapi.cm.P_UNKNOWN_VPRP_ID,org.csapi.cm.P_CANT_DELETE_VPRP,org.csapi.TpCommonExceptions;
}
