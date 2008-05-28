package org.mobicents.slee.resource.parlay.csapi.jr.ui;

import org.csapi.ui.IpAppUIPOA;
import org.csapi.ui.TpUIError;
import org.csapi.ui.TpUIFault;
import org.csapi.ui.TpUIReport;
import org.mobicents.slee.resource.parlay.csapi.jr.ui.activity.uimanager.UIManager;
import org.omg.PortableServer.POA;

import EDU.oswego.cs.dl.util.concurrent.Executor;

/**
 * 
 */
public final class IpAppUIImpl extends IpAppUIPOA {

    private transient POA defaultPOA;

    private final transient IpAppUIOperationsDelegate ipAppUIOperationsDelegate;

    /**
     * @param uiManager
     * @param ipAppUIPOA
     * @param ipAppUIExecutors
     */
    public IpAppUIImpl(final UIManager uiManager, final POA ipAppUIPOA,
            final Executor[] ipAppUIExecutors) {
        super();
        this.defaultPOA = ipAppUIPOA;
        this.ipAppUIOperationsDelegate = new IpAppUIOperationsDelegate(
                uiManager, ipAppUIExecutors);

    }

    /**
     * 
     */
    public  void dispose() {

        defaultPOA = null;

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.omg.PortableServer.Servant#_default_POA()
     */
    public POA _default_POA() {
        return defaultPOA;
    }

  
    public void sendInfoRes(final int userInteractionSessionID, final int assignmentID,
            final TpUIReport response) {
        ipAppUIOperationsDelegate.sendInfoRes(userInteractionSessionID,
                assignmentID, response);
    }

    public void sendInfoErr(final int userInteractionSessionID, final int assignmentID,
            final TpUIError error) {
        ipAppUIOperationsDelegate.sendInfoErr(userInteractionSessionID,
                assignmentID, error);
    }

    public void sendInfoAndCollectRes(final int userInteractionSessionID,
            final int assignmentID, final TpUIReport response, final String collectedInfo) {
        ipAppUIOperationsDelegate
                .sendInfoAndCollectRes(userInteractionSessionID, assignmentID,
                        response, collectedInfo);

    }

    public void sendInfoAndCollectErr(final int userInteractionSessionID,
            final int assignmentID, final TpUIError error) {
        ipAppUIOperationsDelegate.sendInfoAndCollectErr(
                userInteractionSessionID, assignmentID, error);

    }

    public void userInteractionFaultDetected(final int userInteractionSessionID,
            final TpUIFault fault) {
        ipAppUIOperationsDelegate.userInteractionFaultDetected(
                userInteractionSessionID, fault);

    }

}
