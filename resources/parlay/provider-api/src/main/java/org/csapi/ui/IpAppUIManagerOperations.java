package org.csapi.ui;

/**
 *	Generated from IDL interface "IpAppUIManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpAppUIManagerOperations
	extends org.csapi.IpInterfaceOperations
{
	/* constants */
	/* operations  */
	void userInteractionAborted(org.csapi.ui.TpUIIdentifier userInteraction);
	org.csapi.ui.IpAppUI reportNotification(org.csapi.ui.TpUIIdentifier userInteraction, org.csapi.ui.TpUIEventInfo eventInfo, int assignmentID);
	void userInteractionNotificationInterrupted();
	void userInteractionNotificationContinued();
	org.csapi.ui.IpAppUI reportEventNotification(org.csapi.ui.TpUIIdentifier userInteraction, org.csapi.ui.TpUIEventNotificationInfo eventNotificationInfo, int assignmentID);
}
