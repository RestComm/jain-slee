package org.csapi.fw.fw_service.integrity;

/**
 *	Generated from IDL interface "IpFwHeartBeatMgmt"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpFwHeartBeatMgmtOperations
	extends org.csapi.IpInterfaceOperations
{
	/* constants */
	/* operations  */
	void enableHeartBeat(int interval, org.csapi.fw.fw_service.integrity.IpSvcHeartBeat svcInterface) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions;
	void disableHeartBeat() throws org.csapi.TpCommonExceptions;
	void changeInterval(int interval) throws org.csapi.TpCommonExceptions;
}
