package org.csapi.cc.mmccs;

/**
 *	Generated from IDL interface "IpAppMultiMediaCall"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpAppMultiMediaCallOperations
	extends org.csapi.cc.mpccs.IpAppMultiPartyCallOperations
{
	/* constants */
	/* operations  */
	void superviseVolumeRes(int callSessionID, int report, org.csapi.cc.mmccs.TpCallSuperviseVolume usedVolume);
	void superviseVolumeErr(int callSessionID, org.csapi.cc.TpCallError errorIndication);
}
