package org.mobicents.sleetests.javax.slee.resource.ResourceAdaptorTypeDescriptor;

import com.opencloud.sleetck.lib.SleeTCKTest;
import com.opencloud.sleetck.lib.SleeTCKTestUtils;
import com.opencloud.sleetck.lib.TCKTestResult;
import com.opencloud.sleetck.lib.testutils.jmx.DeploymentMBeanProxy;

import javax.slee.ComponentID;
import javax.slee.EventTypeID;
import javax.slee.management.DeployableUnitDescriptor;
import javax.slee.management.DeployableUnitID;
import javax.slee.resource.ResourceAdaptorTypeDescriptor;
import javax.slee.resource.ResourceAdaptorTypeID;

public class Test4443Test implements SleeTCKTest {

    private static final String SERVICE_DU_PATH_PARAM = "DUPath";
    private static final int TEST_ID = 4443;

    public void init(SleeTCKTestUtils utils) {
    	this.utils = utils;
    }

    /**
     * Perform the actual test.
     */
    public TCKTestResult run() throws Exception {

	DeploymentMBeanProxy duProxy = utils.getDeploymentMBeanProxy();
	DeployableUnitDescriptor duDesc = duProxy.getDescriptor(duID);
	ComponentID components[] = duDesc.getComponents();

	for (int i = 0; i < components.length; i++) {
	    if (components[i] instanceof ResourceAdaptorTypeID) {
		ResourceAdaptorTypeDescriptor desc = (ResourceAdaptorTypeDescriptor) duProxy.getDescriptor(components[i]);

		EventTypeID events[] = desc.getEventTypes();
		if (events.length == 1)
		    return TCKTestResult.passed();

		return TCKTestResult.failed(TEST_ID, "ResourceAdaptorTypeDescriptor.getEventTypes() returned incorrect number of event types.");
	    }
	}

	return TCKTestResult.error("Unable to find installed resource adaptor type descriptor.");
    }

    /**
     * Do all the pre-run configuration of the test.
     */
    public void setUp() throws Exception {
    	String duPath = utils.getTestParams().getProperty(SERVICE_DU_PATH_PARAM);
    	duID = utils.install(duPath);
    }

    /**
     * Clean up after the test.
     */
    public void tearDown() throws Exception {
    	utils.getLog().fine("Disconnecting from resource");
        utils.getResourceInterface().clearActivities();
        utils.getLog().fine("Deactivating and uninstalling service");
        utils.deactivateAllServices();
        utils.uninstallAll();
    }

    private SleeTCKTestUtils utils;
    private DeployableUnitID duID;
}
