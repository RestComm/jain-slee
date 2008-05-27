package org.mobicents.sleetests.container.deployment.cmp;

import com.opencloud.sleetck.lib.AbstractSleeTCKTest;
import com.opencloud.sleetck.lib.testutils.FutureResult;

/**
 * This tests the deployment of a Sbb with a CMP that extends a class that
 * declares another CMP
 */
public class SbbAndSuperClassWithCMPsTest extends AbstractSleeTCKTest {
	
    public void run(FutureResult result) throws Exception {
        this.result = result;
        this.result.setPassed();
    }

    public void setUp() throws Exception {
        super.setUp();
    }

    private FutureResult result;	

}
