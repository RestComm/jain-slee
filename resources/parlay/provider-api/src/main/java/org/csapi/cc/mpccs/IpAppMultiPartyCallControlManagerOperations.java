package org.csapi.cc.mpccs;

/**
 *	Generated from IDL interface "IpAppMultiPartyCallControlManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpAppMultiPartyCallControlManagerOperations
	extends org.csapi.IpInterfaceOperations
{
	/* constants */
	/* operations  */
	org.csapi.cc.mpccs.TpAppMultiPartyCallBack reportNotification(org.csapi.cc.mpccs.TpMultiPartyCallIdentifier callReference, org.csapi.cc.mpccs.TpCallLegIdentifier[] callLegReferenceSet, org.csapi.cc.TpCallNotificationInfo notificationInfo, int assignmentID);
	void callAborted(int callReference);
	void managerInterrupted();
	void managerResumed();
	void callOverloadEncountered(int assignmentID);
	void callOverloadCeased(int assignmentID);
}
