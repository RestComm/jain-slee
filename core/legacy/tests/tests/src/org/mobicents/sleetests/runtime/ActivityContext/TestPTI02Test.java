package org.mobicents.sleetests.runtime.ActivityContext;
  
import javax.slee.management.DeployableUnitID;

import com.opencloud.sleetck.lib.*;
import com.opencloud.sleetck.lib.testutils.*;
import com.opencloud.sleetck.lib.resource.TCKSbbMessage;
import com.opencloud.sleetck.lib.resource.testapi.TCKResourceListener;
import com.opencloud.sleetck.lib.resource.TCKActivityID;
import com.opencloud.sleetck.lib.resource.testapi.TCKResourceTestInterface;
import com.opencloud.sleetck.lib.resource.events.TCKResourceEventX;

import java.rmi.RemoteException;
import java.util.HashMap;


/**
 * Tests if detaching and then attaching an sbb entity to the AC where the event was received 
 * causes the sbbEntity to receive it again. 
 * @author Eduardo Martins
 *
 */
public class TestPTI02Test implements SleeTCKTest {

    private static final String SERVICE_DU_PATH_PARAM = "serviceDUPath";
    private static final int TEST_ID = 2;

    public void init(SleeTCKTestUtils utils) {
    	this.utils = utils;
    }

    /**
     * Perform the actual test.
     */

    public TCKTestResult run() throws Exception {
    	
    	result = new FutureResult(utils.getLog());
    	
    	TCKResourceTestInterface resource = utils.getResourceInterface();
    	
    	TCKActivityID activityID1 = resource.createActivity("TestPTI01InitialActivity");
    	resource.fireEvent(TCKResourceEventX.X1, TCKResourceEventX.X1, activityID1, null);
    	    	
    	utils.getLog().fine("Event X1 fired, waiting for test pass indicator from SBB.");
    	return result.waitForResultOrFail(utils.getTestTimeout(), "Timeout waiting for SBB to receive ActivityEndEvent", TEST_ID);    	
    }

    /**
     * Do all the pre-run configuration of the test.
     */

    public void setUp() throws Exception {
	utils.getLog().fine("Connecting to resource");
	resourceListener = new TCKResourceListenerImpl();
	utils.getResourceInterface().setResourceListener(resourceListener);
	utils.getLog().fine("Installing and activating service");

	// Install the Deployable Unit.
	String duPath = utils.getTestParams().getProperty(SERVICE_DU_PATH_PARAM);
	DeployableUnitID duID = utils.install(duPath);
	utils.activateServices(duID, true); // Activate the service
    }

    /**
     * Clean up after the test.
     */

    public void tearDown() throws Exception {
	utils.getLog().fine("Disconnecting from resource");
	utils.getResourceInterface().clearActivities();
	utils.getResourceInterface().removeResourceListener();
	utils.getLog().fine("Deactivating and uninstalling service");
	utils.deactivateAllServices();
	utils.uninstallAll();
    }

    private class TCKResourceListenerImpl extends BaseTCKResourceListener {
	public synchronized void onSbbMessage(TCKSbbMessage message, TCKActivityID calledActivity) throws RemoteException {
	    utils.getLog().info("Received message from SBB");

	    HashMap map = (HashMap) message.getMessage();
	    Boolean passed = (Boolean) map.get("Result");
	    String msgString = (String) map.get("Message");

	    if (passed.booleanValue() == true)
		result.setPassed();
	    else
		result.setFailed(TEST_ID, msgString);
	}

	public void onException(Exception e) throws RemoteException {
	    utils.getLog().warning("Received exception from SBB");
	    utils.getLog().warning(e);
	    result.setError(e);
	}
    }

    private SleeTCKTestUtils utils;
    private TCKResourceListener resourceListener;
    private FutureResult result;
}
