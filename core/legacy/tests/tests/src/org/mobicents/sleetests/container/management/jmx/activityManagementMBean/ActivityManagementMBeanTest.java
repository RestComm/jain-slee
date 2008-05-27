package org.mobicents.sleetests.container.management.jmx.activityManagementMBean;

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

import com.opencloud.sleetck.lib.AbstractSleeTCKTest;
import com.opencloud.sleetck.lib.resource.TCKActivityID;
import com.opencloud.sleetck.lib.resource.TCKSbbMessage;
import com.opencloud.sleetck.lib.resource.events.TCKResourceEventX;
import com.opencloud.sleetck.lib.resource.events.TCKResourceEventY;
import com.opencloud.sleetck.lib.resource.testapi.TCKResourceListener;
import com.opencloud.sleetck.lib.resource.testapi.TCKResourceTestInterface;
import com.opencloud.sleetck.lib.testutils.BaseTCKResourceListener;
import com.opencloud.sleetck.lib.testutils.FutureResult;

public class ActivityManagementMBeanTest extends AbstractSleeTCKTest {

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
						"===[###] Sending X1 - Triggering aci numbe check and null aci creation");
		resource.fireEvent(TCKResourceEventX.X1, TCKResourceEventX.X1,
				tckActivityID, null);
		Thread.currentThread().sleep(1000);
		utils()
				.getLog()
				.info(
						"===[###] Sending X2 - Triggering after creation aci number check and scheduling end of two of null aci.");
		resource.fireEvent(TCKResourceEventX.X2, TCKResourceEventX.X2,
				tckActivityID, null);
		Thread.currentThread().sleep(1000);
		utils()
				.getLog()
				.info(
						"===[###] Sending X3 - Triggering after removal aci number check and MBean endActivity on alst null aci");
		resource.fireEvent(TCKResourceEventX.X3, TCKResourceEventX.X3,
				tckActivityID, null);
		Thread.currentThread().sleep(1000);

		
		utils()
		.getLog()
		.info(
				"===[###] Sending Y1 - This will ensure that sbb tests have passed, otherwise we cant continue...");
		resource.fireEvent(TCKResourceEventY.Y1, TCKResourceEventY.Y1,
		tckActivityID, null);
		Thread.currentThread().sleep(2000);
		
		if(result.isSet())
			return;
		
		// Here we should have 3 activities:
		// 1. Service
		// 2. Tck
		// 3. Null with name - it will have name bound to it - and will have
		// common sbb with tck activity

		try {

			Hashtable env = new Hashtable();
			env.put(Context.PROVIDER_URL, "jnp://127.0.0.1:1099");
			env.put(Context.INITIAL_CONTEXT_FACTORY,
					"org.jnp.interfaces.NamingContextFactory");
			env.put(Context.URL_PKG_PREFIXES, "org.jnp.interfaces");

			InitialContext ctx = new InitialContext(env);
			RMIAdaptor rmiserver = (RMIAdaptor) ctx
					.lookup("jmx/rmi/RMIAdaptor");

			ObjectName mbeanName = new ObjectName(
					ActivityManagementMBeanTestSbb._MBEAN_O_NAME);
			// Iterator it = getNullAcisForTest().iterator();
			// ActivityContextInterface naci = (ActivityContextInterface) it
			// .next();
			// it.remove();
			SleeContainer container = SleeContainer.lookupFromJndi();

			utils().getLog().info("===[###] Invoking list ac on MBean");

			Object[] o = (Object[]) rmiserver.invoke(mbeanName,
					"listActivityContexts", new Object[] { new Boolean(true) },
					(String[]) new String[] { "boolean" });

			utils().getLog().info(
					"===[###] Invocation successful, number of AC[" + o.length
							+ "], should be [3]");

			if (o.length != 3) {

				result
						.setFailed(
								0,
								"Number of returned ACIs doesnt match estimated value of 3 - possible something went wrong, see console log!!!");
				return;
			}
			//
			// []
			// 0| AC_ID - String
			// 1| ACTIVITY_CLASS - String
			// 2| LAST_ACCESS_TIME - Stirng - long number
			// 3| RA - Ra Link if any, can be null
			// 4| SBB_ATTACHMENTS - String[]
			// 5| NAMES_BOUND_TO - String[] - can be null
			// 6| TIMERS_ATTACHED - String[] - can be null
			// 7| DATA_PROPERTIES - String[] - can be null

			// Now we have to look for common sbb:
			HashMap sbbeIDToAcID = new HashMap();
			HashSet factoriesSet = new HashSet();

			for (int i = 0; i < o.length; i++) {

				Object[] tmp = (Object[]) o[i];
				String[] s = (String[]) tmp[4];
				if (s == null) {
					// WRONG!!!
					utils()
							.getLog()
							.info(
									"---[!!!] AC["
											+ tmp[0]
											+ "] has null sbb attachemnt set, this shouldnt happen, may be indication of error");
					result
							.setError("AC["
									+ tmp[0]
									+ "] has null sbb attachemnt set, this shouldnt happen, may be indication of error");
					return;
				} else {
					for (int j = 0; j < s.length; j++) {
						if (!sbbeIDToAcID.containsKey(s[j]))
							sbbeIDToAcID.put(s[j], new HashSet());

						Set set = (Set) sbbeIDToAcID.get(s[j]);
						set.add(tmp[0]);
					}
				}

				// This is for next test.
				if (tmp[3] == null) {
					factoriesSet.add(tmp[1]);
				} else {
					factoriesSet.add(tmp[3]);
				}

			}

			Iterator it = sbbeIDToAcID.keySet().iterator();

			// size() should return 1 in each case
			Set serviceIDs = new HashSet();
			Set nullIDs = new HashSet();
			Set tckIDs = new HashSet();
			Set unknownIDs = new HashSet();// - except this one- this should
			// have size==0

			while (it.hasNext()) {

				Object key = it.next();
				Set set = (Set) sbbeIDToAcID.get(key);
				
				if (set.size() == 1) {
					// this is sbbEid thats attached to ServiceImpl activity
					// Atlest it should be, lets look for it in table
					for (int i = 0; i < o.length; i++) {
						Object[] tmp = (Object[]) o[i];
						if (tmp[1].equals(serviceActivityClassName)) {

							serviceIDs.add(tmp[0]);

						} else if (tmp[1].equals(tckActivityClassName)) {
							tckIDs.add(tmp[0]);
						} else if (tmp[1].equals(nullActivityClassName)) {
							nullIDs.add(tmp[0]);

						} else {
							unknownIDs.add(tmp[0]);
						}
					}
				}
			}

			if (serviceIDs.size() != 1) {

				result.setFailed(0,
						"---[!!!] Number of service ac is incorrect["
								+ serviceIDs.size() + "] should be [1]");
				return;
			}

			if (nullIDs.size() != 1) {
				result.setFailed(0, "---[!!!] Number of null ac is incorrect["
						+ nullIDs.size() + "] should be [1]");
				return;
			}

			if (tckIDs.size() != 1) {
				result.setFailed(0, "---[!!!] Number of tck ac is incorrect["
						+ tckIDs.size() + "] should be [1]");
				return;
			}

			if (unknownIDs.size() > 0) {
				result.setFailed(0,
						"---[!!!] Number of unknown ac is incorrect["
								+ unknownIDs.size() + "] should be [1]");
				return;
			}
			utils()
					.getLog()
					.info(
							"===[###] Finished checking AC count and type, success. Listing AC factories check start.");

			// At this point it should be clear that we have here 3 cells :)
			String[] o_perFactory = (String[]) rmiserver.invoke(mbeanName,
					"listActivityContextsFactories", null, null);

			// factoriesSet has to contain all of o_perFactory
			HashSet passedFactories = new HashSet();
			for (int k = 0; k < o_perFactory.length; k++)
				passedFactories.add(o_perFactory[k]);

			if (passedFactories.size() != factoriesSet.size()) {
				result
						.setFailed(
								0,
								"---[!!!] Number of ac facotries retrieved from listActivityCOntexts["
										+ factoriesSet.size()
										+ "] doesnt match one retrieved with listActivityContextsFactories ["
										+ passedFactories.size() + "]");
				return;

			}

			if ((!passedFactories.containsAll(factoriesSet))
					|| (!factoriesSet.containsAll(passedFactories))) {

				result
						.setFailed(
								0,
								"---[!!!] Names of ac facotries retrieved from listActivityCOntexts["
										+ factoriesSet
										+ "] doesnt match one retrieved with listActivityContextsFactories ["
										+ passedFactories + "]");
				return;

			}

			utils()
					.getLog()
					.info(
							"===[###] Finished checking AC factories coutn and type, success. Retrieve acID by activity type start.");

			// Wwe shoudl have 1 of each type
			Object[] nullACIDs = (Object[]) rmiserver.invoke(mbeanName,
					"retrieveActivityContextIDByActivityType",
					new Object[] { nullActivityClassName },
					(String[]) new String[] { "java.lang.String" });

			Object[] serviceACIDs = (Object[]) rmiserver.invoke(mbeanName,
					"retrieveActivityContextIDByActivityType",
					new Object[] { serviceActivityClassName },
					(String[]) new String[] { "java.lang.String" });

			Object[] tckACIDs = (Object[]) rmiserver.invoke(mbeanName,
					"retrieveActivityContextIDByActivityType",
					new Object[] { tckActivityClassName },
					(String[]) new String[] { "java.lang.String" });

			if ((tckACIDs.length != 1) || (serviceACIDs.length != 1)
					|| (nullACIDs.length != 1)) {
				result
						.setFailed(
								0,
								"---[!!!] Number of acIDs returned by retrieveActivityContextIDByActivityType doesnt match, should be [1] and it is nullIDs["
										+ tckACIDs.length
										+ "] tckIDS["
										+ serviceACIDs.length
										+ "] serviceIDS["
										+ nullACIDs.length + "]");
				return;
			}

			it = tckIDs.iterator(); // we now that there will be [1] element,
			// otherwise we would be here
			Object idValue = it.next();
			if (!tckACIDs[0].equals(idValue)) {
				result
						.setFailed(
								0,
								"---[!!!] TCK ac ID returned by retrieveActivityContextIDByActivityType["
										+ tckACIDs[0]
										+ "] doesnt match one returned by listActivityContexts["
										+ idValue + "]");
				return;
			}

			it = nullIDs.iterator(); // we now that there will be [1]
			// element, otherwise we would be here
			idValue = it.next();
			if (!nullACIDs[0].equals(idValue)) {
				result
						.setFailed(
								0,
								"---[!!!] NULL ac ID returned by retrieveActivityContextIDByActivityType["
										+ nullACIDs[0]
										+ "] doesnt match one returned by listActivityContexts["
										+ idValue + "]");
				return;
			}

			it = serviceIDs.iterator(); // we now that there will be [1]
			// element, otherwise we would be here
			idValue = it.next();
			if (!serviceACIDs[0].equals(idValue)) {
				result
						.setFailed(
								0,
								"---[!!!] Service ac ID returned by retrieveActivityContextIDByActivityType["
										+ serviceACIDs[0]
										+ "] doesnt match one returned by listActivityContexts["
										+ idValue + "]");
				return;
			}

			utils()
					.getLog()
					.info(
							"===[###] Finished checking retrieveActivityContextIDByActivityType, success. Retrieve acID by ResourceAdaptorEntityName type start.");

			// we should get only one - for tck
			Object[] idsByRaEntity = (Object[]) rmiserver.invoke(mbeanName,
					"retrieveActivityContextIDByResourceAdaptorEntityName",
					new Object[] { tckRAEntityName },
					(String[]) new String[] { "java.lang.String" });

			if (idsByRaEntity.length != 1) {

				result
						.setFailed(
								0,
								"---[!!!] TCK ac ID[] returned by retrieveActivityContextIDByResourceAdaptorEntityNamelength is incorrect, shoudl be [1] and it is ["
										+ idsByRaEntity.length + "]");
				return;

			}

			if (!idsByRaEntity[0].equals(tckACIDs[0])) {

				result
						.setFailed(
								0,
								"---[!!!] TCK ac ID returned by retrieveActivityContextIDByResourceAdaptorEntityNamelength["
										+ idsByRaEntity[0]
										+ "] doesn match one returned by retrieveActivityContextIDByActivityType["
										+ tckACIDs[0] + "]");
				return;

			}

			
			utils()
			.getLog()
			.info(
					"===[###] Finished checking retrieveActivityContextIDByResourceAdaptorEntityName, success. Retrieve acID by SbbID start.");
			Object[] idsBbySbbID = (Object[]) rmiserver.invoke(mbeanName,
					"retrieveActivityContextIDBySbbID",
					new Object[] { testSbbID },
					(String[]) new String[] { "java.lang.String" });

			if (idsBbySbbID.length != 3) {

				result
						.setFailed(
								0,
								"---[!!!] Number of acIDs returned by retrieveActivityContextIDBySbbID is incorrect, should be [3] and it is ["
										+ idsBbySbbID.length + "]");
				return;

			}

			utils()
			.getLog()
			.info(
					"===[###] Finished checking retrieveActivityContextIDBySbbID, success. Retrieve acID by SbbEID start.");
			
			String sbbEID = null;
			
			
			
			
			
			it = sbbeIDToAcID.keySet().iterator();

			while (it.hasNext()) {
				Object key = it.next();
				HashSet h = (HashSet) sbbeIDToAcID.get(key);
				if (h.size() == 2) {
					sbbEID = (String) key;
					break;
				}
			}

			if (sbbEID != null) {
				Object[] idsBySbbEID = (Object[]) rmiserver.invoke(mbeanName,
						"retrieveActivityContextIDBySbbEntityID",
						new Object[] { sbbEID },
						(String[]) new String[] { "java.lang.String" });

				
				
				if (idsBySbbEID.length != 2) {

					result
							.setFailed(
									0,
									"---[!!!] Number of acIDs returned by retrieveActivityContextIDBySbbEntityID is incorrect, should be [2] and it is ["
											+ idsBySbbEID.length + "]");
					return;

				}

			}else
			{
				result
				.setFailed(
						0,
						"---[!!!] Couldnt find SbbE with 3 ac atached!!!");
				return;
			}

			
			utils()
			.getLog()
			.info(
					"===[###] Finished checking retrieveActivityContextIDBySbbEntityID["+sbbEID+"], success. Check on retrieveActivityContextDetails start.");
			
			
			for (int i = 0; i < o.length; i++) {
				Object[] acArray = (Object[]) o[i];
				Object[] secindArray = (Object[]) rmiserver.invoke(mbeanName,
						"retrieveActivityContextDetails",
						new Object[] { acArray[0] },
						(String[]) new String[] { "java.lang.String" });

				if (!compareACTables(acArray, secindArray)) {
					result.setFailed(0, "FAILED");
				}
			}
			
			
			
			
			
			
			Map m=(Map) rmiserver.invoke(mbeanName,	"retrieveNamesToActivityContextIDMappings",	null,null);

			//Here we should have only one name - "_NULL_3_NAME_FOR_LINGERER"
			
			if(m.size()!=1)
			{
				result
				.setFailed(
						0,
						"---[!!!] Size of bindings doesnt correspond to expected [1] !!! ["+m.keySet().size()+"]["+m.keySet()+"]");
				return;
			}
			else
			{
				if(!m.keySet().contains("_NULL_3_NAME_FOR_LINGERER"))
				{
					result
					.setFailed(
							0,
							"---[!!!] Name of binding doesnt corespond to expected name [_NULL_3_NAME_FOR_LINGERER] !!! ["+m.keySet().size()+"]["+m.keySet()+"]");
					return;
				}
			}
			
			
			//7.10.5.1 Implicitly ending a NullActivity object
			//The condition that implicitly ends a NullActivity object is as follows:
			//· No SBB entities are attached to the Activity Context of the NullActivity object, and
			//· No SLEE Facilities reference the Activity Context of the NullActivity object, and
			//· No events remain to be delivered on the Activity Context of the NullActivity object.
			//SO WE have to end this activity
			
			rmiserver.invoke(mbeanName, "endActivity", new Object[] { m.values().iterator().next() },
					(String[]) new String[] { "java.lang.String" });
			
			
			
			
			utils().getLog().info("===[###] Finished checking name bindings["+m.keySet().size()+"]["+m.keySet()+"]");
			result.setPassed();
			
			
			
		} catch (Exception e) {

			e.printStackTrace();
			result.setError(e);
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
