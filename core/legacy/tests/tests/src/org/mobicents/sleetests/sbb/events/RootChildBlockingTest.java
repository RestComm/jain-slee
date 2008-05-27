package org.mobicents.sleetests.sbb.events;

import java.rmi.RemoteException;
import java.util.Map;

import com.opencloud.sleetck.lib.AbstractSleeTCKTest;
import com.opencloud.sleetck.lib.DescriptionKeys;
import com.opencloud.sleetck.lib.resource.TCKActivityID;
import com.opencloud.sleetck.lib.resource.TCKSbbMessage;
import com.opencloud.sleetck.lib.resource.events.TCKResourceEventX;
import com.opencloud.sleetck.lib.resource.testapi.TCKResourceListener;
import com.opencloud.sleetck.lib.resource.testapi.TCKResourceTestInterface;
import com.opencloud.sleetck.lib.testutils.BaseTCKResourceListener;
import com.opencloud.sleetck.lib.testutils.FutureResult;

public class RootChildBlockingTest extends AbstractSleeTCKTest {
	
    public void run(FutureResult result) throws Exception {
        this.result = result;

        TCKResourceTestInterface resource = utils().getResourceInterface();
        String activityName = utils().getTestParams().getProperty("activityName");
        TCKActivityID activityID = resource.createActivity(activityName);
        
        int time_to_wait = Integer.parseInt(utils().getTestParams().getProperty("time_to_wait"));


        utils().getLog().fine("Firing TCKResourceEventX.X1 on activity " + activityName);
        resource.fireEvent(TCKResourceEventX.X1, TCKResourceEventX.X1, activityID, null);

        utils().getLog().fine("Sleeping for " + time_to_wait + " ms");
        Thread.sleep(time_to_wait);
        
        utils().getLog().fine("Firing TCKResourceEventX.X2 on activity " + activityName);
        resource.fireEvent(TCKResourceEventX.X2, TCKResourceEventX.X2, activityID, null);
    }

    public void setUp() throws Exception {
        getLog().fine("Connecting to resource");
        TCKResourceListener resourceListener = new TCKResourceListenerImpl();
        setResourceListener(resourceListener);

        setupService(DescriptionKeys.SERVICE_DU_PATH_PARAM, true);
    }


    private class TCKResourceListenerImpl extends BaseTCKResourceListener {

    	public synchronized void onSbbMessage(TCKSbbMessage message, TCKActivityID calledActivity) throws RemoteException {
            Map sbbData = (Map)message.getMessage();
            Boolean sbbPassed = (Boolean)sbbData.get("result");
            String sbbTestMessage = (String)sbbData.get("message");

            getLog().info("Received message from SBB: passed=" + sbbPassed + ", message=" + sbbTestMessage);

            if (sbbPassed.booleanValue()) {
                result.setPassed();
            }
            else {
                result.setFailed(0, sbbTestMessage);
            }
        }

        public void onException(Exception exception) throws RemoteException {
            getLog().warning("Received exception from SBB or resource:");
            getLog().warning(exception);
            result.setError(exception);
        }
    }

    private FutureResult result;	

}
