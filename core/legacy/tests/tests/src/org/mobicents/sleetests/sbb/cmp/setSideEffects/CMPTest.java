package org.mobicents.sleetests.sbb.cmp.setSideEffects;

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

/**
 * 
 * Coverage for bug 81: https://mobicents.dev.java.net/issues/show_bug.cgi?id=81
	When trying to set a CMP field within a call of a local interface method all CMP
	fields are initialized even if there are already values stored in other CMP
	fields. This is cause by the fact that in a local interface method call when
	calling a CMP setter the CMP fields are not retrieved from TreeCache. There is a
	workaround to call getter method before. If you do so CMP fields are retrieved
	from TreeCache and writing to CMP fields does not cause initializing of all CMP
	fields.
	
	I added a TCK formatted test case, which simulates this behaviour in following way:
	
	A root SBB receives an initial event and during processing of this event it
	creates a child SBB and attaches it to the AC of this initial event. When child
	SBB processes this event afterwards it sets CMP field CMP1 to 41. Later on root
	SBB receives a second event on the same AC, which causes root SBB in event
	handling method for this event to call local interface method methodX1()of child
	SBB. Depending on environment entry in SBB descriptor of child SBB methodX1
	first calls getCMP2 or not for workaround. Afterwards methodX1 calls setCMP2 and
	getCMP1. Dependent on call of getCMP2 before setCMP2 the result of getCMP1 is
	either the correct one (41) or 0. If getCMP2 is called the correct value of CMP1
	is returned from getCMP1, otherwise 0 is returned.
*/
public class CMPTest extends AbstractSleeTCKTest {
	
    public void run(FutureResult result) throws Exception {
        this.result = result;

        TCKResourceTestInterface resource = utils().getResourceInterface();
        String activityName = utils().getTestParams().getProperty("activityName");
        TCKActivityID activityID = resource.createActivity(activityName);
        
        utils().getLog().fine("Firing TCKResourceEventX.X1 on activity " + activityName);
        resource.fireEvent(TCKResourceEventX.X1, TCKResourceEventX.X1, activityID, null);

        utils().getLog().fine("Sleeping for 50 ms");
        Thread.sleep(50);
        
        utils().getLog().fine("Firing TCKResourceEventX.X2 on activity " + activityName);
        resource.fireEvent(TCKResourceEventX.X2, TCKResourceEventX.X2, activityID, null);
    }

    public void setUp() throws Exception {
        super.setUp();
        getLog().fine("Connecting to resource");
        TCKResourceListener resourceListener = new TestResourceListenerImpl();
        setResourceListener(resourceListener);
    }

    private class TestResourceListenerImpl extends BaseTCKResourceListener {

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
