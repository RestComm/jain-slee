package org.csapi.fw.fw_service.integrity;

/**
 *	Generated from IDL interface "IpSvcHeartBeatMgmt"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpSvcHeartBeatMgmtOperations
	extends org.csapi.IpInterfaceOperations
{
	/* constants */
	/* operations  */
	void enableSvcHeartBeat(int interval, org.csapi.fw.fw_service.integrity.IpFwHeartBeat fwInterface) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions;
	void disableSvcHeartBeat() throws org.csapi.TpCommonExceptions;
	void changeInterval(int interval) throws org.csapi.TpCommonExceptions;
}
