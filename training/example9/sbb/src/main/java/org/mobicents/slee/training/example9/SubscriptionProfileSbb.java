package org.mobicents.slee.training.example9;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.slee.ActivityContextInterface;
import javax.slee.CreateException;
import javax.slee.RolledBackContext;
import javax.slee.Sbb;
import javax.slee.SbbContext;
import javax.slee.SbbLocalObject;
import javax.slee.TransactionRolledbackLocalException;
import javax.slee.facilities.Tracer;
import javax.slee.profile.ProfileFacility;
import javax.slee.profile.ProfileID;
import javax.slee.profile.ProfileLocalObject;
import javax.slee.profile.ProfileTable;
import javax.slee.profile.UnrecognizedProfileTableNameException;

import org.mobicents.slee.resource.lab.message.Message;
import org.mobicents.slee.resource.lab.ratype.MessageResourceAdaptorSbbInterface;
import org.mobicents.slee.training.example9.profile.EventControlProfileCMP;
import org.mobicents.slee.training.example9.profile.ProfileCreator;

/**
 * Base SBB offering profile lookup functionality
 * 
 * @author MoBitE info@mobite.co.in
 * 
 */
public abstract class SubscriptionProfileSbb implements Sbb {

	protected static Tracer log = null;

	private MessageResourceAdaptorSbbInterface sbb2ra;

	public void setSbbContext(SbbContext context) {
		this.sbbContext = context;
		this.log = this.sbbContext.getTracer(SubscriptionProfileSbb.class
				.getSimpleName());
		try {

			Context myEnv = (Context) new InitialContext()
					.lookup("java:comp/env");
			profileFacility = (ProfileFacility) myEnv
					.lookup("slee/facilities/profile");
			sbb2ra = (MessageResourceAdaptorSbbInterface) myEnv
					.lookup("slee/resources/raframe/2.0/sbb2ra");
		} catch (Exception ne) {
			log
					.severe(
							"COULD NOT LOCATE RESOURCE IN JNDI: Check JNDI TREE or entity-binding for proper path!!!",
							ne);
		}
	}

	protected EventControlProfileCMP lookup(String activityId) {
		String profileTableName = new String();
		ProfileID profileID = null;
		EventControlProfileCMP eventControlProfileCMP = null;
		try {
			profileTableName = "EventControl";

			// Looking for the ProfileID of the caller (address)
			ProfileTable table = getProfileFacility().getProfileTable(
					profileTableName);
			ProfileLocalObject plo = table.findProfileByAttribute("activityId",
					activityId);
			if (plo != null) {

				eventControlProfileCMP = (EventControlProfileCMP) plo;
			} else {
				Message answer = getMessageResourceAdaptorSbbInterface()
						.getMessageFactory().createMessage("ERROR",
								" specified Activity Id Not Allowed !! ");
				// ... send it using the resource adaptor
				getMessageResourceAdaptorSbbInterface().send(answer);
			}
		} catch (NullPointerException e) {
			log.severe(
					"Exception using the getProfileByIndexedAttribute method",
					e);
		} catch (UnrecognizedProfileTableNameException e) {
			log
					.severe(
							"Exception in getting the Profile Specification in getControllerProfileCMP(profileID):"
									+ "The ProfileID object does not identify a Profile Table created from the Profile Specification",
							e);
		} catch (TransactionRolledbackLocalException e) {
			log.severe(e.getMessage(), e);
		}

		return eventControlProfileCMP;
	}

	public void unsetSbbContext() {
		this.sbbContext = null;
	}

	public void sbbCreate() throws CreateException {

	}

	public void sbbPostCreate() throws CreateException {
	}

	public void sbbRemove() {
	}

	public void sbbPassivate() {
	}

	public void sbbActivate() {
	}

	public void sbbLoad() {
	}

	public void sbbStore() {
	}

	public void sbbExceptionThrown(Exception exception, Object event,
			ActivityContextInterface aci) {
	}

	public void sbbRolledBack(RolledBackContext context) {
	}

	protected final SbbContext getSbbContext() {
		return sbbContext;
	}

	protected final ProfileFacility getProfileFacility() {
		return profileFacility;
	}

	protected final SbbLocalObject getSbbLocalObject() {
		return sbbContext.getSbbLocalObject();
	}

	protected MessageResourceAdaptorSbbInterface getMessageResourceAdaptorSbbInterface() {
		return this.sbb2ra;
	}

	private SbbContext sbbContext;
	private ProfileFacility profileFacility;

}
