package org.csapi.cc.gccs;

/**
 *	Generated from IDL interface "IpAppCallControlManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpAppCallControlManagerOperations
	extends org.csapi.IpInterfaceOperations
{
	/* constants */
	/* operations  */
	void callAborted(int callReference);
	org.csapi.cc.gccs.IpAppCall callEventNotify(org.csapi.cc.gccs.TpCallIdentifier callReference, org.csapi.cc.gccs.TpCallEventInfo eventInfo, int assignmentID);
	void callNotificationInterrupted();
	void callNotificationContinued();
	void callOverloadEncountered(int assignmentID);
	void callOverloadCeased(int assignmentID);
}
