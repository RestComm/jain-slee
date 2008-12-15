package org.mobicents.sleetests.container.management.jmx.startStopSequence;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;

import javax.management.BadAttributeValueExpException;
import javax.management.ObjectName;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.slee.ServiceID;
import javax.slee.connection.ExternalActivityHandle;

import org.jboss.jmx.adaptor.rmi.RMIAdaptor;
import org.mobicents.sleetests.container.installService.InstallServiceTest;

import com.opencloud.sleetck.lib.resource.TCKActivityID;
import com.opencloud.sleetck.lib.resource.TCKSbbMessage;
import com.opencloud.sleetck.lib.resource.testapi.TCKResourceListener;
import com.opencloud.sleetck.lib.testutils.BaseTCKResourceListener;
import com.opencloud.sleetck.lib.testutils.FutureResult;

/**
 * Test Star/Stop sequence - test, deploys additional RA, does 2xFullStart/Stop,
 * 1. - RA is active, Service active - here service will receive start event
 * again 2. - RA is inactive, Service inactive
 * 
 * Test schema:<br>
 * 1. deploy<br>
 * 2. deploy dummy ra<br>
 * 3. deploy dummy service<br>
 * 4. remember state<br>
 * 5. stop/start<br>
 * 6. check if state is consistent<br>
 * 7. stop dummy service<br>
 * 8. remember state<br>
 * 9. stop/start<br>
 * 10. check if state is consistent<br>
 * 11. stop dummy ra<br>
 * 12. remember state<br>
 * 13. stop/start<br>
 * 14. check if state is consistent<br>
 * 15. finito<br>
 * 
 * @category TODO: Add profiles?, Should we also check for DUs?
 * @author baranowb
 * 
 */
public class StartStopSequenceTest extends InstallServiceTest {

	private ObjectName sleeMgmtON = null;
	private ObjectName resourceMgmtON = null;
	private ObjectName servicesMgmtON = null;
	private InitialContext ctx = null;
	private RMIAdaptor rmiserver = null;

	// Awful, but makes it easier to understand to which part held content is
	// refering.
	private HashSet<ServiceID> _4_state_services,  _6_state_services,
			 _8_state_services,  _10_state_services,
			 _12_state_services, 
			_14_state_services;

	private HashSet<String> _4_state_ras,_6_state_ras,_8_state_ras,_10_state_ras,_12_state_ras, _14_state_ras;
	public void setUp() throws Exception {
		super.setUp();

		_4_state_services = new HashSet<ServiceID>(5);
		_4_state_ras = new HashSet<String>(5);
		_8_state_services = new HashSet<ServiceID>(5);
		_8_state_ras = new HashSet<String>(5);
		_10_state_services = new HashSet<ServiceID>(5);
		_10_state_ras = new HashSet<String>(5);
		_12_state_services = new HashSet<ServiceID>(5);
		_12_state_ras = new HashSet<String>(5);
		_14_state_services = new HashSet<ServiceID>(5);
		_14_state_ras = new HashSet<String>(5);

		getLog()
				.info(
						"\n========================\nConnecting to resource\n========================\n");
		TCKResourceListener resourceListener = new TestResourceListenerImpl();
		setResourceListener(resourceListener);
		sleeMgmtON = new ObjectName("slee:service=SleeManagement");

		Hashtable env = new Hashtable();
		env.put(Context.PROVIDER_URL, "jnp://127.0.0.1:1099");
		env.put(Context.INITIAL_CONTEXT_FACTORY,
				"org.jnp.interfaces.NamingContextFactory");
		env.put(Context.URL_PKG_PREFIXES, "org.jnp.interfaces");

		ctx = new InitialContext(env);
		rmiserver = (RMIAdaptor) ctx.lookup("jmx/rmi/RMIAdaptor");

		this.resourceMgmtON = (ObjectName) rmiserver.invoke(sleeMgmtON,
				"getResourceManagementMBean", new Object[] {}, new String[] {});
		this.servicesMgmtON = (ObjectName) rmiserver.invoke(sleeMgmtON,
				"getServiceManagementMBean", new Object[] {}, new String[] {});

		/*
		 * Properties props = new Properties(); try {
		 * props.load(getClass().getResourceAsStream("sipStack.properties")); }
		 * catch (IOException IOE) { logger.info("FAILED TO LOAD:
		 * sipStack.properties"); }
		 */

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
		HashSet<ServiceID> tmpActiveServices = new HashSet<ServiceID>();
		HashSet<String> tmpActiveRas = new HashSet<String>();

		try {
			installAll();
			getLog().info("------------> 1");
			// ---------------------------------------
			// ---> here we are after #3, time for #4
			// ---------------------------------------

			// Services active:
			ServiceID[] activeServicesIDs = (ServiceID[]) rmiserver.invoke(
					servicesMgmtON, "getServices",
					new Object[] { javax.slee.management.ServiceState.ACTIVE },
					new String[] { "javax.slee.management.ServiceState" });
			// We shoudl have two of them, if not its and error ;[
			if (activeServicesIDs.length != 2) {
				result
						.setError("Services count on start does not match, should be [2] and is["
								+ activeServicesIDs.length
								+ "]:"
								+ Arrays.toString(activeServicesIDs));
				throw new BadAttributeValueExpException(
						"Services count on start does not match, should be [2] and is["
								+ activeServicesIDs.length + "]:"
								+ Arrays.toString(activeServicesIDs));

			}

			for (ServiceID serviceID : activeServicesIDs)
				_4_state_services.add(serviceID);

			String[] activeRaEntities = (String[]) rmiserver
					.invoke(
							resourceMgmtON,
							"getResourceAdaptorEntities",
							new Object[] { javax.slee.management.ResourceAdaptorEntityState.ACTIVE },
							new String[] { "javax.slee.management.ResourceAdaptorEntityState" });

			if (activeRaEntities.length != 2) {
				result
						.setError("Ra Entities count on start does not match, should be [2] and is["
								+ activeServicesIDs.length
								+ "]:"
								+ Arrays.toString(activeRaEntities));
				throw new BadAttributeValueExpException(
						"Ra Entities count on start does not match, should be [2] and is["
								+ activeServicesIDs.length + "]:"
								+ Arrays.toString(activeRaEntities));

			}
			for (String entityName : activeRaEntities)
				_4_state_ras.add(entityName);
			getLog().info("------------> 2");
			// ---------------------------------------
			// ---> #5
			// ---------------------------------------

			rmiserver.invoke(sleeMgmtON, "stop", new Object[] {},
					new String[] {});
			
			//Thread.currentThread().wait(1000);
			for(long l=0;l<1000000000l;l++)
			{}

			rmiserver.invoke(sleeMgmtON, "start", new Object[] {},
					new String[] {});
			//Thread.currentThread().wait(1000);
			for(long l=0;l<1000000000l;l++)
			{}
			getLog().info("------------> 3");
			// ---------------------------------------
			// ---> #6 - check state
			// ---------------------------------------

			activeServicesIDs = (ServiceID[]) rmiserver.invoke(servicesMgmtON,
					"getServices",
					new Object[] { javax.slee.management.ServiceState.ACTIVE },
					new String[] { "javax.slee.management.ServiceState" });
			// We shoudl have two of them, if not its and error ;[
			if (activeServicesIDs.length != 2) {
				result
						.setFailed(6,"Services count does not match, should be [2] and is["
								+ activeServicesIDs.length
								+ "]:"
								+ Arrays.toString(activeServicesIDs));
				throw new BadAttributeValueExpException(
						"Services count  does not match, should be [2] and is["
								+ activeServicesIDs.length + "]:"
								+ Arrays.toString(activeServicesIDs));

			}

			for (ServiceID serviceID : activeServicesIDs)
				tmpActiveServices.add(serviceID);

			activeRaEntities = (String[]) rmiserver
					.invoke(
							resourceMgmtON,
							"getResourceAdaptorEntities",
							new Object[] { javax.slee.management.ResourceAdaptorEntityState.ACTIVE },
							new String[] { "javax.slee.management.ResourceAdaptorEntityState" });

			if (activeRaEntities.length != 2) {
				result
						.setFailed(6,"Ra Entities count on after stop/start does not match, should be [2] and is["
								+ activeServicesIDs.length
								+ "]:"
								+ Arrays.toString(activeRaEntities));
				throw new BadAttributeValueExpException(
						"Ra Entities count on after stop/start does not match, should be [2] and is["
								+ activeRaEntities.length + "]:"
								+ Arrays.toString(activeServicesIDs));

			}

			for (String entityName : activeRaEntities)
				tmpActiveRas.add(entityName);

			if (!tmpActiveServices.containsAll(_4_state_services)
					|| !_4_state_services.containsAll(tmpActiveServices)) {
				result.setFailed(6,
						"Active services does not match services active before stop/start Before["
								+ _4_state_services + "] After["
								+ tmpActiveServices + "]");
				throw new IllegalStateException(
						"Active services does not match services active before stop/start Before["
								+ _4_state_services + "] After["
								+ tmpActiveServices + "]");
			}
			if (!tmpActiveRas.containsAll(_4_state_ras)
					|| !_4_state_ras.containsAll(tmpActiveRas)) {
				result.setFailed(6,
						"Active ra entities does not match entities active before stop/start Before["
								+ _4_state_ras + "] After[" + tmpActiveRas
								+ "]");
				throw new IllegalStateException(
						"Active ra entities does not match entities active before stop/start Before["
								+ _4_state_ras + "] After[" + tmpActiveRas
								+ "]");
			}
			getLog().info("------------> 4");
			// ---------------------------------------
			// ---> #7
			// ---------------------------------------

			deactivateService();

			getLog().info("------------> 5");
			// ---------------------------------------
			// ---> #8 remember
			// ---------------------------------------

			activeServicesIDs = (ServiceID[]) rmiserver.invoke(servicesMgmtON,
					"getServices",
					new Object[] { javax.slee.management.ServiceState.ACTIVE },
					new String[] { "javax.slee.management.ServiceState" });
			// We shoudl have two of them, if not its and error ;[
			if (activeServicesIDs.length != 1) {
				result
						.setFailed(8,"Services count on start does not match, should be [1] and is["
								+ activeServicesIDs.length
								+ "]:"
								+ Arrays.toString(activeServicesIDs));
				throw new BadAttributeValueExpException(
						"Services count on start does not match, should be [1] and is["
								+ activeServicesIDs.length + "]:"
								+ Arrays.toString(activeServicesIDs));

			}

			for (ServiceID serviceID : activeServicesIDs)
				_8_state_services.add(serviceID);

			activeRaEntities = (String[]) rmiserver
					.invoke(
							resourceMgmtON,
							"getResourceAdaptorEntities",
							new Object[] { javax.slee.management.ResourceAdaptorEntityState.ACTIVE },
							new String[] { "javax.slee.management.ResourceAdaptorEntityState" });

			if (activeRaEntities.length != 2) {
				result
						.setFailed(8,"Ra Entities count on start does not match, should be [2] and is["
								+ activeServicesIDs.length
								+ "]:"
								+ Arrays.toString(activeRaEntities));
				throw new BadAttributeValueExpException(
						"Ra Entities count on start does not match, should be [2] and is["
								+ activeServicesIDs.length + "]:"
								+ Arrays.toString(activeRaEntities));

			}
			for (String entityName : activeRaEntities)
				_8_state_ras.add(entityName);
			getLog().info("------------> 6");
			// ---------------------------------------
			// ---> #9 stop/start
			// ---------------------------------------

			rmiserver.invoke(sleeMgmtON, "stop", new Object[] {},
					new String[] {});
			
			//Thread.currentThread().wait(1000);
			for(long l=0;l<1000000000l;l++)
			{}

			rmiserver.invoke(sleeMgmtON, "start", new Object[] {},
					new String[] {});
			//Thread.currentThread().wait(1000);
			for(long l=0;l<1000000000l;l++)
			{}
			getLog().info("------------> 7");
			// ---------------------------------------
			// ---> #10 - check state
			// ---------------------------------------
			tmpActiveServices.clear();
			tmpActiveRas.clear();
			activeServicesIDs = (ServiceID[]) rmiserver.invoke(servicesMgmtON,
					"getServices",
					new Object[] { javax.slee.management.ServiceState.ACTIVE },
					new String[] { "javax.slee.management.ServiceState" });
			// We shoudl have two of them, if not its and error ;[
			if (activeServicesIDs.length != 1) {
				result
						.setFailed(10,"Services count on start does not match, should be [1] and is["
								+ activeServicesIDs.length
								+ "]:"
								+ Arrays.toString(activeServicesIDs));
				throw new BadAttributeValueExpException(
						"Services count on start does not match, should be [1] and is["
								+ activeServicesIDs.length + "]:"
								+ Arrays.toString(activeServicesIDs));

			}

			for (ServiceID serviceID : activeServicesIDs)
				tmpActiveServices.add(serviceID);

			activeRaEntities = (String[]) rmiserver
					.invoke(
							resourceMgmtON,
							"getResourceAdaptorEntities",
							new Object[] { javax.slee.management.ResourceAdaptorEntityState.ACTIVE },
							new String[] { "javax.slee.management.ResourceAdaptorEntityState" });

			if (activeRaEntities.length != 2) {
				result
						.setFailed(10,"Ra Entities count on after stop/start does not match, should be [2] and is["
								+ activeServicesIDs.length
								+ "]:"
								+ Arrays.toString(activeRaEntities));
				throw new BadAttributeValueExpException(
						"Ra Entities count on after stop/start does not match, should be [2] and is["
								+ activeRaEntities.length + "]:"
								+ Arrays.toString(activeServicesIDs));

			}

			for (String entityName : activeRaEntities)
				tmpActiveRas.add(entityName);

			if (!tmpActiveServices.containsAll(_8_state_services)
					|| !_8_state_services.containsAll(tmpActiveServices)) {
				result.setFailed(10,
						"Active services does not match services active before stop/start Before["
								+ _8_state_services + "] After["
								+ tmpActiveServices + "]");
				throw new IllegalStateException(
						"Active services does not match services active before stop/start Before["
								+ _8_state_services + "] After["
								+ tmpActiveServices + "]");
			}
			if (!tmpActiveRas.containsAll(_8_state_ras)
					|| !_8_state_ras.containsAll(tmpActiveRas)) {
				result.setFailed(10,
						"Active ra entities does not match entities active before stop/start Before["
								+ _8_state_ras + "] After[" + tmpActiveRas
								+ "]");
				throw new IllegalStateException(
						"Active ra entities does not match entities active before stop/start Before["
								+ _8_state_ras + "] After[" + tmpActiveRas
								+ "]");
			}
			getLog().info("------------> 8");
			// ---------------------------------------
			// ---> #11 - deactivate ra
			// ---------------------------------------
			deactivateRaEntity();
			getLog().info("------------> 9");
			// ---------------------------------------
			// ---> #12. remember state
			// ---------------------------------------

			activeServicesIDs = (ServiceID[]) rmiserver.invoke(servicesMgmtON,
					"getServices",
					new Object[] { javax.slee.management.ServiceState.ACTIVE },
					new String[] { "javax.slee.management.ServiceState" });
			// We shoudl have two of them, if not its and error ;[
			if (activeServicesIDs.length != 1) {
				result
						.setFailed(12,"Services count  does not match, should be [2] and is["
								+ activeServicesIDs.length
								+ "]:"
								+ Arrays.toString(activeServicesIDs));
				throw new BadAttributeValueExpException(
						"Services count  does not match, should be [1] and is["
								+ activeServicesIDs.length + "]:"
								+ Arrays.toString(activeServicesIDs));

			}

			for (ServiceID serviceID : activeServicesIDs)
				_12_state_services.add(serviceID);

			activeRaEntities = (String[]) rmiserver
					.invoke(
							resourceMgmtON,
							"getResourceAdaptorEntities",
							new Object[] { javax.slee.management.ResourceAdaptorEntityState.ACTIVE },
							new String[] { "javax.slee.management.ResourceAdaptorEntityState" });

			if (activeRaEntities.length != 1) {
				result
						.setFailed(12,"Ra Entities count does not match, should be [1] and is["
								+ activeServicesIDs.length
								+ "]:"
								+ Arrays.toString(activeRaEntities));
				throw new BadAttributeValueExpException(
						"Ra Entities count  does not match, should be [1] and is["
								+ activeServicesIDs.length + "]:"
								+ Arrays.toString(activeRaEntities));

			}
			for (String entityName : activeRaEntities)
				_12_state_ras.add(entityName);
			getLog().info("------------> 10");
			// ---------------------------------------
			// ---> #13. stop/start
			// ---------------------------------------
			
			rmiserver.invoke(sleeMgmtON, "stop", new Object[] {},
					new String[] {});
			
			//Thread.currentThread().wait(1000);
			for(long l=0;l<1000000000l;l++)
			{}

			rmiserver.invoke(sleeMgmtON, "start", new Object[] {},
					new String[] {});
			//Thread.currentThread().wait(1000);
			for(long l=0;l<1000000000l;l++)
			{}
			getLog().info("------------> 11");
			// ---------------------------------------
			// ---> #14. check if state is consistent
			// ---------------------------------------
			
			tmpActiveServices.clear();
			tmpActiveRas.clear();
			activeServicesIDs = (ServiceID[]) rmiserver.invoke(servicesMgmtON,
					"getServices",
					new Object[] { javax.slee.management.ServiceState.ACTIVE },
					new String[] { "javax.slee.management.ServiceState" });
			// We shoudl have two of them, if not its and error ;[
			if (activeServicesIDs.length != 1) {
				result
						.setFailed(14,"Services count  does not match, should be [1] and is["
								+ activeServicesIDs.length
								+ "]:"
								+ Arrays.toString(activeServicesIDs));
				throw new BadAttributeValueExpException(
						"Services count  does not match, should be [1] and is["
								+ activeServicesIDs.length + "]:"
								+ Arrays.toString(activeServicesIDs));

			}

			for (ServiceID serviceID : activeServicesIDs)
				tmpActiveServices.add(serviceID);

			activeRaEntities = (String[]) rmiserver
					.invoke(
							resourceMgmtON,
							"getResourceAdaptorEntities",
							new Object[] { javax.slee.management.ResourceAdaptorEntityState.ACTIVE },
							new String[] { "javax.slee.management.ResourceAdaptorEntityState" });

			if (activeRaEntities.length != 1) {
				result
						.setFailed(14,"Ra Entities count on after stop/start does not match, should be [1] and is["
								+ activeServicesIDs.length
								+ "]:"
								+ Arrays.toString(activeRaEntities));
				throw new BadAttributeValueExpException(
						"Ra Entities count on after stop/start does not match, should be [1] and is["
								+ activeRaEntities.length + "]:"
								+ Arrays.toString(activeServicesIDs));

			}

			for (String entityName : activeRaEntities)
				tmpActiveRas.add(entityName);

			if (!tmpActiveServices.containsAll(_12_state_services)
					|| !_12_state_services.containsAll(tmpActiveServices)) {
				result.setFailed(14,
						"Active services does not match services active before stop/start Before["
								+ _8_state_services + "] After["
								+ tmpActiveServices + "]");
				throw new IllegalStateException(
						"Active services does not match services active before stop/start Before["
								+ _8_state_services + "] After["
								+ tmpActiveServices + "]");
			}
			if (!tmpActiveRas.containsAll(_12_state_ras)
					|| !_12_state_ras.containsAll(tmpActiveRas)) {
				result.setFailed(14,
						"Active ra entities does not match entities active before stop/start Before["
								+ _12_state_ras + "] After[" + tmpActiveRas
								+ "]");
				throw new IllegalStateException(
						"Active ra entities does not match entities active before stop/start Before["
								+ _12_state_ras + "] After[" + tmpActiveRas
								+ "]");
			}
			getLog().info("------------> 12");
			// ---------------------------------------
			// ---> #15. finito
			// ---------------------------------------
			
			result.setPassed();
			getLog().info("------------> 13");
			
			
		} catch (Exception e) {
			
			result.setError(e);
			return;
		}finally
		{
			try {
				uninstallAll();
			} catch (Exception ee) {
				ee.printStackTrace();
			}
		}

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
