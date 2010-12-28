/***************************************************
 *                                                 *
 *  Mobicents: The Open Source VoIP Platform       *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/
package org.mobicents.slee.enabler.sip;

import javax.slee.facilities.TimerID;

public interface SubscriptionClientChildActivityContextInterface extends javax.slee.ActivityContextInterface {

	public void setExpiresTimerID(TimerID tid);

	public TimerID getExpiresTimerID();
}
