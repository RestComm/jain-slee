package org.csapi.fw.fw_application.integrity;

/**
 *	Generated from IDL interface "IpHeartBeatMgmt"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpHeartBeatMgmtOperations
	extends org.csapi.IpInterfaceOperations
{
	/* constants */
	/* operations  */
	void enableHeartBeat(int interval, org.csapi.fw.fw_application.integrity.IpAppHeartBeat appInterface) throws org.csapi.TpCommonExceptions;
	void disableHeartBeat() throws org.csapi.TpCommonExceptions;
	void changeInterval(int interval) throws org.csapi.TpCommonExceptions;
}
