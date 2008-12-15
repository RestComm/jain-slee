package org.mobicents.sleetests.runtime.sbbcontext.GetService;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.management.ObjectName;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.slee.connection.ExternalActivityHandle;

import org.jboss.jmx.adaptor.rmi.RMIAdaptor;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.ServiceIDImpl;
import org.mobicents.sleetests.container.installService.InstallServiceTest;

import com.opencloud.sleetck.lib.AbstractSleeTCKTest;
import com.opencloud.sleetck.lib.resource.TCKActivityID;
import com.opencloud.sleetck.lib.resource.TCKSbbMessage;
import com.opencloud.sleetck.lib.resource.events.TCKResourceEventX;
import com.opencloud.sleetck.lib.resource.events.TCKResourceEventY;
import com.opencloud.sleetck.lib.resource.testapi.TCKResourceListener;
import com.opencloud.sleetck.lib.resource.testapi.TCKResourceTestInterface;
import com.opencloud.sleetck.lib.testutils.BaseTCKResourceListener;
import com.opencloud.sleetck.lib.testutils.FutureResult;

public class GetServiceTest extends InstallServiceTest {

	public void setUp() throws Exception {
		super.setUp();

		getLog()
				.info(
						"\n========================\nConnecting to resource\n========================\n");
		TCKResourceListener resourceListener = new TestResourceListenerImpl();
		setResourceListener(resourceListener);
		/*
		 * Properties props = new Properties(); try {
		 * props.load(getClass().getResourceAsStream("sipStack.properties")); }
		 * catch (IOException IOE) { logger.info("FAILED TO LOAD:
		 * sipStack.properties"); }
		 */
		// super.service=new ServiceIDImpl();
	}

	protected FutureResult result;

	private class TestResourceListenerImpl extends BaseTCKResourceListener {

		public synchronized void onSbbMessage(TCKSbbMessage message,
				TCKActivityID calledActivity) throws RemoteException {
			Map sbbData = (Map) message.getMessage();
			Boolean sbbPassed = (Boolean) sbbData.get("result");
			String sbbTestMessage = (String) sbbData.get("message");

			getLog().info(
					"Received message from SBB: passed=" + sbbPassed
							+ ", message=" + sbbTestMessage);

			if (sbbPassed.booleanValue()) {
				result.setPassed();
			} else {
				result.setFailed(0, sbbTestMessage);
			}
		}

		public void onException(Exception exception) throws RemoteException {
			getLog().warning("Received exception from SBB or resource:");
			getLog().warning(exception);
			result.setError(exception);
		}
	}

	private TCKActivityID tckActivityID = null;

	private String activityName = null;

	/** Creates a new instance of SleeConnector */

	private ExternalActivityHandle ah = null;

	private String tckRAEntityName = "tck";

	private String serviceActivityClassName = "org.mobicents.slee.runtime.serviceactivity.ServiceActivityImpl";

	private String tckActivityClassName = "com.opencloud.sleetck.lib.resource.impl.TCKActivityImpl";

	private String nullActivityClassName = "org.mobicents.slee.runtime.facilities.NullActivityImpl";

	private String testSbbID = "ActivityManagementMBeanTestSbb#mobicents#0.1";

	private String nullGetClassName = "NullActivityImpl";

	private String serviceGetClassName = "ServiceActivityImpl";

	public void run(FutureResult result) throws Exception {
		this.result = result;

		TCKResourceTestInterface resource = utils().getResourceInterface();
		activityName = utils().getTestParams().getProperty("activityName");
		tckActivityID = resource.createActivity(activityName);

		utils()
				.getLog()
				.info(
						"===[###] Sending X1");
		resource.fireEvent(TCKResourceEventX.X1, TCKResourceEventX.X1,
				tckActivityID, null);
		utils().getLog().info("===[###] Waiting");
		Thread.currentThread().sleep(1000);
		//utils().getLog().info("===[###] Deactivating service");
		//deactivateService();
		//utils().getLog().info("===[###] Waiting");
		//Thread.currentThread().sleep(1000);
		//utils().getLog().info("===[###] Activating service");
		//activateService();
		//utils().getLog().info("===[###] Waiting");
		Thread.currentThread().sleep(5000);
		result.setPassed();

	}

	// =============== HELPER METHOD

	private boolean compareACTables(Object[] fist, Object[] second) {

		if (fist.length != second.length)
			return false;

		for (int i = 0; i < fist.length; i++) {

			Object tmpO = fist[i];
			if (tmpO instanceof Object[]) {
				Object[] firstSub = (Object[]) tmpO;
				Object[] secondSub = (Object[]) second[i];

				for (int j = 0; j < firstSub.length; j++)
					if (!firstSub[j].equals(secondSub[j]))
						return false;
			} else {
				if ((tmpO != null && second[i] == null)
						|| (tmpO == null && second[i] != null)) {
					return false;
				}
				if (tmpO != null)
					if (!tmpO.equals(second[i]))
						return false;
			}

		}

		return true;
	}

}
