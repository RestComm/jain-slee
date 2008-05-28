package org.csapi.fw.fw_application.integrity;

/**
 *	Generated from IDL interface "IpAppHeartBeatMgmt"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpAppHeartBeatMgmtOperations
	extends org.csapi.IpInterfaceOperations
{
	/* constants */
	/* operations  */
	void enableAppHeartBeat(int interval, org.csapi.fw.fw_application.integrity.IpHeartBeat fwInterface);
	void disableAppHeartBeat();
	void changeInterval(int interval);
}
