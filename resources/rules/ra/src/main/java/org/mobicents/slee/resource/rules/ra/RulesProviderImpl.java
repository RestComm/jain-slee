package org.mobicents.slee.resource.rules.ra;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.slee.facilities.FacilityException;

import org.apache.log4j.Logger;
import org.drools.RuleBase;
import org.drools.WorkingMemory;
//import org.mobicents.slee.management.rules.RulesScanner;
import org.mobicents.slee.resource.rules.ratype.RulesProvider;
import org.mobicents.slee.resource.rules.ratype.RulesSession;

public class RulesProviderImpl implements RulesProvider {

	private static Logger logger = Logger.getLogger(RulesProviderImpl.class);

	public static final String RULES_BASE_JNDI_PREFIX = "java:rulebase/";

	private RulesResourceAdaptor ra;

	public RulesProviderImpl(RulesResourceAdaptor ra) {
		logger
				.debug("Constructor RulesResourceAdaptorSbbInterfaceImpl(RulesResourceAdaptor ra) called.");
		this.ra = ra;
	}

	public RulesSession getNewRulesSession(String drlFile) {
		// RulesSession session = new RulesSessionImpl();

		System.out
				.println("getNewRulesSession() of RulesProviderImpl called fact = :  drlFile = "
						+ drlFile);

		RulesSession activity = null;

		try {

			WorkingMemory wm = null;
			String rulebaseJndi;
			RuleBase rb = null;
			if (activity == null) {

				Context ctx = new InitialContext();

				rulebaseJndi = RULES_BASE_JNDI_PREFIX + drlFile;

				// Do the lookup of RuleBase that RulesScannerMBean would have
				// already registered with JNDI
				try {
					rb = (RuleBase) ctx.lookup(rulebaseJndi);

				} catch (NameNotFoundException ex) {
					logger.error("Lookup of RuleBase failed " + rulebaseJndi,
							ex);
					throw new RuntimeException("Lookup of RuleBase failed "
							+ rulebaseJndi, ex);

				}

				wm = rb.newWorkingMemory(false);

				activity = new RulesSessionImpl(drlFile, wm);

				RulesActivityHandle handle = new RulesActivityHandle(activity
						.getId());

				//Note We are not using the listener now
				CallWorkingMemoryListener listener = new CallWorkingMemoryListener(
						handle, this.ra.getBootstrapContext());

				wm.addEventListener(listener);

				this.ra.addActivity(handle, activity);

				this.ra.getSleeEndpoint().activityStarted(handle);

			}
			return activity;

		} catch (FacilityException fe) {
			logger.error("Caught a FacilityException: ");
			fe.printStackTrace();
			throw new RuntimeException(
					"RAFrameResourceAdapter.onEvent(): FacilityException caught. ",
					fe);
		} catch (Exception e) {
			logger.error("Caught an UnrecognizedEventException: ");
			e.printStackTrace();
			throw new RuntimeException(
					"RAFrameResourceAdaptor.onEvent(): UnrecognizedEventException caught.",
					e);
		}
	}

}
