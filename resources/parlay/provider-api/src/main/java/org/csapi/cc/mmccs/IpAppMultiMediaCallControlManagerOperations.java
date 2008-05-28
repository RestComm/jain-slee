package org.csapi.cc.mmccs;

/**
 *	Generated from IDL interface "IpAppMultiMediaCallControlManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpAppMultiMediaCallControlManagerOperations
	extends org.csapi.cc.mpccs.IpAppMultiPartyCallControlManagerOperations
{
	/* constants */
	/* operations  */
	org.csapi.cc.mmccs.TpAppMultiMediaCallBack reportMediaNotification(org.csapi.cc.mmccs.TpMultiMediaCallIdentifier callReference, org.csapi.cc.mmccs.TpMultiMediaCallLegIdentifier[] callLegReferenceSet, org.csapi.cc.mmccs.TpMediaStream[] mediaStreams, org.csapi.cc.mmccs.TpMediaStreamEventType type, int assignmentID);
}
