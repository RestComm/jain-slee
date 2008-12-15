package org.mobicents.sleetests.du.dependencyRemoval1;

import java.rmi.RemoteException;

import java.util.ArrayList;

import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import com.opencloud.sleetck.lib.AbstractSleeTCKTest;

import com.opencloud.sleetck.lib.resource.TCKActivityID;
import com.opencloud.sleetck.lib.resource.TCKSbbMessage;

import com.opencloud.sleetck.lib.resource.events.TCKResourceEventX;
import com.opencloud.sleetck.lib.resource.events.TCKResourceEventY;
import com.opencloud.sleetck.lib.resource.testapi.TCKResourceListener;
import com.opencloud.sleetck.lib.resource.testapi.TCKResourceTestInterface;

import com.opencloud.sleetck.lib.testutils.BaseTCKResourceListener;
import com.opencloud.sleetck.lib.testutils.FutureResult;

import org.mobicents.slee.container.component.ComponentKey;
import org.mobicents.slee.container.component.DeployableUnitIDImpl;
import org.mobicents.slee.container.component.ResourceAdaptorIDImpl;
import org.mobicents.slee.container.component.ServiceIDImpl;
import org.mobicents.slee.container.management.jmx.SleeCommandInterface;

public class DependencyRemovalTest1 extends AbstractSleeTCKTest {

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

	}

	protected FutureResult result;

	/*
	 * protected void setResultPassed(String msg) throws Exception {
	 * logger.info("Success: " + msg);
	 * 
	 * HashMap sbbData = new HashMap(); sbbData.put("result", Boolean.TRUE);
	 * sbbData.put("message", msg);
	 * TCKSbbUtils.getResourceInterface().sendSbbMessage(sbbData); }
	 * 
	 * protected void setResultFailed(String msg) throws Exception {
	 * logger.info("Failed: " + msg);
	 * 
	 * HashMap sbbData = new HashMap(); sbbData.put("result", Boolean.FALSE);
	 * sbbData.put("message", msg);
	 * TCKSbbUtils.getResourceInterface().sendSbbMessage(sbbData); }
	 */

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

	private String jnpHostURL = "jnp://127.0.0.1:1099";

	private String du1_name = "DU2-DU.jar";

	private String du2_name = "DU3-DU.jar";

	private String service1_id = "Service2#mobicents#0.1";

	private String service2_id = "Service3#mobicents#0.1";

	private ComponentKey s1_id = new ComponentKey(this.service1_id);

	private ServiceIDImpl service_1 = new ServiceIDImpl(s1_id);

	private ComponentKey s2_id = new ComponentKey(this.service2_id);

	private ServiceIDImpl service_2 = new ServiceIDImpl(s2_id);

	private DeployableUnitIDImpl duID1Impl, duID2Impl;

	public void run(FutureResult result) throws Exception {
		this.result = result;

		TCKResourceTestInterface resource = utils().getResourceInterface();
		activityName = utils().getTestParams().getProperty("activityName");
		tckActivityID = resource.createActivity(activityName);

		Properties props = new Properties();
		try {
			getLog().info(" == LOADING PROPS FROM: test.properties ==");
			props.load(getClass().getResourceAsStream("test.properties"));
			jnpHostURL = props.getProperty("jnpHost", jnpHostURL);
			du1_name = props.getProperty("du1.name", du1_name);
			du2_name = props.getProperty("du2.name", du2_name);
			service1_id = props.getProperty("service1.id", service1_id);
			service2_id = props.getProperty("service2.id", service2_id);

			s1_id = new ComponentKey(service1_id);
			service_1 = new ServiceIDImpl(s1_id);

			s2_id = new ComponentKey(service2_id);
			service_2 = new ServiceIDImpl(s2_id);

			getLog().info(" == FINISHED LOADING PROPS ==");
		} catch (Exception IOE) {
			getLog().info("FAILED TO LOAD: test.properties");

		}

		utils()
				.getLog()
				.info(
						"\n===================\nSTARTING DEPLOYMENT IN FEW uS\n===================\nACTIVITY:"
								+ activityName
								+ "\n=======================================");

		String mcHOME = System.getProperty("MOBICENTS_HOME");
		if (mcHOME == null)
			result
					.setError(" == The System Property MOBICENTS_HOME is required, but does not exist!! ==");

		SleeCommandInterface SCI = new SleeCommandInterface(jnpHostURL);
		String dusPATH = "file://" + mcHOME + "/tests/lib/container/";
		getLog().info(" == STARTIGN DEPLOYMENT ==");
		ArrayList errors = new ArrayList(2);
		boolean du1_installed, du2_installed, s1_activated, s2_activated;
		du1_installed = du2_installed = s1_activated = s2_activated = false;
		try {
			Object opResult = null;
			getLog().info(
					" == INSTALLING TEST SERVICE1:" + dusPATH + du1_name
							+ " ==");
			opResult = SCI.invokeOperation("-install", dusPATH + du1_name,
					null, null);
			getLog().info(" == SERVICE INSTALLED:" + opResult + " ==");
			duID1Impl = (DeployableUnitIDImpl) opResult;
			du1_installed = true;
			getLog().info(
					" == INSTALLING TEST SERVICE2:" + dusPATH + du2_name
							+ " ==");
			opResult = SCI.invokeOperation("-install", dusPATH + du2_name,
					null, null);
			getLog().info(" == SERVICE INSTALLED:" + opResult + " ==");
			duID2Impl = (DeployableUnitIDImpl) opResult;
			du2_installed = true;
			getLog().info(" == ACTIVATING SERVICE [" + service_1 + "] ==");
			opResult = SCI.invokeOperation("-activateService", service_1
					.toString(), null, null);
			getLog().info(" == SERVICE ACTIVATED:" + opResult + " ==");
			s1_activated = true;
			getLog().info(" == ACTIVATING SERVICE [" + service_2 + "] ==");
			opResult = SCI.invokeOperation("-activateService", service_2
					.toString(), null, null);
			getLog().info(" == SERVICE ACTIVATED:" + opResult + " ==");
			s2_activated = true;
		} catch (Exception e) {

			e.printStackTrace();
			errors.add(e);
		}


		getLog().info("FIRING X1");
		resource.fireEvent(TCKResourceEventX.X1, TCKResourceEventX.X1,
				tckActivityID, null);
		getLog().info("WAITING FOR 4 RECEIVALS ");
		try {
			Thread.currentThread().sleep(1500);
		} catch (Exception e) {

		}

		getLog().info(" === TRYING TO REMOVE DU1["+du1_name+"] , THIS SHOULD FAIL - DependencyException");
		// TRY TO REMOVE DU1 - Should fail
		Object opResult = null;
		try {
			if (s2_activated) {
				getLog().info(" == DEACTIVATING SERVICE[" + service_1 + "] ==");
				opResult = SCI.invokeOperation("-deactivateService", service_1
						.toString(), null, null);
				s1_activated = !s1_activated;
				getLog().info(" == SERVICE DEACTIVATED:" + opResult + " ==");
			}
			if (du2_installed) {
				getLog().info(" == REMOVING DU[" + du1_name + "] ==");
				opResult = SCI.invokeOperation("-uninstall",
						dusPATH + du1_name, null, null);
				du1_installed = !du1_installed;
				getLog().info(" == SERVICE UNINSTALLED:" + opResult + " ==");
			}
		} catch (Exception e) {
			Throwable cause=e.getCause();
			if(cause==null || !( cause instanceof javax.slee.management.DependencyException))
			{
				//WE SHOULD GET [javax.slee.management.DependencyException: Somebody is referencing a component of this DU -- cannot uninstall it!]
				result.setFailed(0, " DIDNT REECEIVE DEPENDENCY EXCEPTION, GOT ["+cause+"]");
			}else
			{
				getLog().info("== Seems like DU1 has nto been uninstalled, testing Sbb1,2 and 3");
			}
		}

		Object[] message = new Object[4];
		message[0] = duID1Impl;
		message[1] = service1_id;
		message[2] = duID2Impl;
		message[3] = service2_id;
		getLog().info("FIRING X2");
		resource.fireEvent(TCKResourceEventX.X2, message, tckActivityID, null);

		try {
			Thread.currentThread().sleep(2500);
		} catch (Exception e) {

		}

		
		//Now removing DU2
		
		try{
			getLog().info("START REMOVAL OF SERVICES[" + s2_activated + "]");
			if (s2_activated) {
				getLog().info(" == DEACTIVATING SERVICE[" + service_2 + "] ==");
				opResult = SCI.invokeOperation("-deactivateService", service_2
						.toString(), null, null);
				s2_activated = !s2_activated;
				getLog().info(" == SERVICE DEACTIVATED:" + opResult + " ==");
			}
			if (du2_installed) {
				getLog().info(" == REMOVING DU[" + du2_name + "] ==");
				opResult = SCI.invokeOperation("-uninstall",
						dusPATH + du2_name, null, null);
				du2_installed = !du2_installed;
				getLog().info(" == SERVICE UNINSTALLED:" + opResult + " ==");
			}
		}catch(Exception e)
		{
			result.setFailed(0, " == FAILED REMOVING DU2["+e+"] ==");
			return;
		}
		
		getLog().info("== DU2 REMOVED - Testing if Sbb2 is still present ==");
		
		message = new Object[4];
		message[0] = duID1Impl;
		message[1] = service1_id;
		message[2] = duID2Impl;
		message[3] = service2_id;
		getLog().info("FIRING Y1");
		resource.fireEvent(TCKResourceEventY.Y1, message, tckActivityID, null);
		
		try {
			Thread.currentThread().sleep(2500);
		} catch (Exception e) {

		}
		
		try {
			//Object opResult = null;
			getLog().info("START REMOVAL OF SERVICES[" + s2_activated + "]");
			if (s2_activated) {
				getLog().info(" == DEACTIVATING SERVICE[" + service_2 + "] ==");
				opResult = SCI.invokeOperation("-deactivateService", service_2
						.toString(), null, null);
				s2_activated = !s2_activated;
				getLog().info(" == SERVICE DEACTIVATED:" + opResult + " ==");
			}
			if (du2_installed) {
				getLog().info(" == REMOVING DU[" + du2_name + "] ==");
				opResult = SCI.invokeOperation("-uninstall",
						dusPATH + du2_name, null, null);
				du2_installed = !du2_installed;
				getLog().info(" == SERVICE UNINSTALLED:" + opResult + " ==");
			}
			if (s1_activated) {
				getLog().info(" == DEACTIVATING SERVICE[" + service_1 + "] ==");
				opResult = SCI.invokeOperation("-deactivateService", service_1
						.toString(), null, null);
				s1_activated = !s1_activated;
				getLog().info(" == SERVICE DEACTIVATED:" + opResult + " ==");
			}
			if (du1_installed) {
				getLog().info(" == REMOVING DU[" + du1_name + "] ==");
				opResult = SCI.invokeOperation("-uninstall",
						dusPATH + du1_name, null, null);
				du1_installed = !du1_installed;
				getLog().info(" == SERVICE UNINSTALLED:" + opResult + " ==");
			}

		} catch (Exception e) {
			errors.add(e);
		}
		if (!errors.isEmpty()) {
			getLog().info(" == SOME ERRORS OCURED!!: ==");
			StringBuffer sb = new StringBuffer(400);
			StringBuffer stackTraceSb = null;
			Iterator it = errors.iterator();
			int i = 0;
			while (it.hasNext()) {

				Exception ex = (Exception) it.next();
				StackTraceElement[] ste = ex.getStackTrace();
				if (ste != null && ste.length > 0) {
					stackTraceSb = new StringBuffer(1000);
					stackTraceSb.append("\n");
					for (int c = 0; c < ste.length; c++)
						stackTraceSb.append("=>" + ste[c].getClassName()
								+ " -> " + ste[c].getMethodName() + ":"
								+ ste[c].getLineNumber() + "\n");
				}
				sb.append("\n[#" + i++ + "] -> " + ex.getMessage());
				if (stackTraceSb != null)
					sb.append("\nTRACE:\n" + stackTraceSb + "\n");

				stackTraceSb = null;
			}
			getLog().info(" == ERROR INFO: ==\n" + sb);
		}

	}

}
