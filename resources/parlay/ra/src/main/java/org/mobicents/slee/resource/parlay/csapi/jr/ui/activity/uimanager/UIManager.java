package org.mobicents.slee.resource.parlay.csapi.jr.ui.activity.uimanager;

import org.csapi.ui.IpAppUI;
import org.csapi.ui.IpAppUICall;
import org.mobicents.csapi.jr.slee.TpServiceIdentifier;
import org.mobicents.csapi.jr.slee.ui.IpUIManagerConnection;
import org.mobicents.csapi.jr.slee.ui.TpUIIdentifier;
import org.mobicents.slee.resource.parlay.csapi.jr.ui.activity.ui.AbstractUI;
import org.mobicents.slee.resource.parlay.csapi.jr.ui.activity.ui.UIGeneric;
import org.mobicents.slee.resource.parlay.csapi.jr.ui.activity.uicall.UICall;
import org.mobicents.slee.resource.parlay.session.ServiceSession;

/**
 * UIManager activity. This interface does not simply extend the org.csapi.cc.mpccs.IpAppMultiPartyCallControlManagerOperations interface, because
 * methods which take CORBA TpXXXIdentifier in the IpAppMultiPartyCallControlManagerOperations (IDL generated) interface 
 * will in this interface have equivalents which take a SLEE TpXXXIdentifier.

 */
 public interface UIManager extends ServiceSession, IpUIManagerConnection {

     
     /**
      * Used to get activity when caller doesnt need to distinguish what type of UI is being handled.
     * @param uiSessionID
     * @return may be either a UIGeneric or a UICall. 
     */
    AbstractUI getAbstractUI(int uiSessionID); 
     
    /**
     * Used to get activity when caller expects a UIGeneric
     * @param uiSessionID
     * @return
     */
     UIGeneric getUIGeneric(int uiSessionID);

    /**
     * Used to get activity when caller expects a UICall
     * @param uiSessionID
     * @return
     */
     UICall getUICall(int uiSessionID);

    /**
     * @param uiSessionID
     * @return
     */
     AbstractUI removeUI(int uiSessionID);

    /**
     * @param uiSessionID
     * @param ui
     */
     void addUI(int uiSessionID, AbstractUI ui);

    /**
     * Return the underlying gateway interface.
     * 
     * @return
     */
    IpAppUI getIpAppUI();

    /**
     * Return the underlying gateway interface.
     * 
     * @return
     */
    IpAppUICall getIpAppUICall();

    /**
     * @return
     */
    TpServiceIdentifier getTpServiceIdentifier();

  
    UIGeneric createUIGeneric(org.csapi.ui.TpUIIdentifier uiReference);

    // following methods based on equivalents in
    // org.csapi.ui.IpAppUIManagerOperations

    void userInteractionAborted(org.csapi.ui.TpUIIdentifier userInteraction);

    void reportNotification(TpUIIdentifier userInteraction,
            org.csapi.ui.TpUIEventInfo eventInfo, int assignmentID);

    void userInteractionNotificationInterrupted();

    void userInteractionNotificationContinued();

    void reportEventNotification(TpUIIdentifier userInteraction,
            org.csapi.ui.TpUIEventNotificationInfo eventNotificationInfo,
            int assignmentID);

}
