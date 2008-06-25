package org.mobicents.sleetests.container.deployment.classgeneration;

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

public class SbbLocalObjectImplGenerationTest extends AbstractSleeTCKTest {
	
	private static final int TEST_ID = 1;
	
    public void run(FutureResult result) throws Exception {
        this.result = result;

        TCKResourceTestInterface resource = utils().getResourceInterface();
        
        TCKActivityID activityID = resource.createActivity("SbbLocalObjectImplGenerationTest.activity");
        
        utils().getLog().fine("Firing TCKResourceEventX.X1");
        resource.fireEvent(TCKResourceEventX.X1, TCKResourceEventX.X1, activityID, null);
        
        result.waitForResultOrFail(5000,"Timeout waiting for SBB result", TEST_ID);
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

