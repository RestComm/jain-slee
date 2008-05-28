package org.csapi.cc.mmccs;

/**
 *	Generated from IDL interface "IpMultiMediaCall"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpMultiMediaCallOperations
	extends org.csapi.cc.mpccs.IpMultiPartyCallOperations
{
	/* constants */
	/* operations  */
	void superviseVolumeReq(int callSessionID, org.csapi.cc.mmccs.TpCallSuperviseVolume volume, int treatment) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID;
}
