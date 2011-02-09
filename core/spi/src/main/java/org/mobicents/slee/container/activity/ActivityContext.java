/**
 * 
 */
package org.mobicents.slee.container.activity;

import java.util.Map;
import java.util.Set;

import javax.slee.Address;
import javax.slee.EventTypeID;
import javax.slee.SLEEException;
import javax.slee.ServiceID;
import javax.slee.facilities.TimerID;
import javax.slee.resource.ActivityIsEndingException;

import org.mobicents.slee.container.event.EventProcessingFailedCallback;
import org.mobicents.slee.container.event.EventProcessingSucceedCallback;
import org.mobicents.slee.container.event.ReferencesHandler;
import org.mobicents.slee.container.event.EventUnreferencedCallback;
import org.mobicents.slee.container.sbbentity.SbbEntityID;

/**
 * @author martins
 * 
 */
public interface ActivityContext {

	/**
	 * 
	 */
	public void activityEnded();

	/**
	 * add a naming binding to this activity context.
	 * 
	 * @param aciName
	 *            - new name binding to be added.
	 * 
	 */
	public void addNameBinding(String aciName);

	/**
	 * attach an sbb entity to this AC.
	 * 
	 * @param sbbEntity
	 *            -- sbb entity to attach.
	 * @return true if the SBB Entity is attached successfully, otherwise when
	 *         the SBB Entitiy has already been attached before, return false
	 */

	public boolean attachSbbEntity(SbbEntityID sbbEntityId);

	/**
	 * attach the given timer to the current activity context.
	 * 
	 * @param timerID
	 *            -- timer id to attach.
	 * 
	 */
	public boolean attachTimer(TimerID timerID);

	/**
	 * Detach the sbb entity
	 * 
	 * @param sbbEntityId
	 */
	public void detachSbbEntity(SbbEntityID sbbEntityId)
			throws javax.slee.TransactionRequiredLocalException;

	/**
	 * Detach timer
	 * 
	 * @param timerID
	 * 
	 */
	public boolean detachTimer(TimerID timerID);

	/**
	 * Ends the activity context.
	 */
	public void endActivity();

	/**
	 * Fires an event in the activity context.
	 * 
	 * @param eventTypeId
	 * @param event
	 * @param address
	 * @param serviceID
	 * @param succeedCallback
	 * @param failedCallback
	 * @param unreferencedCallback
	 * @throws ActivityIsEndingException
	 * @throws SLEEException
	 */
	public void fireEvent(EventTypeID eventTypeId, Object event,
			Address address, ServiceID serviceID,
			EventProcessingSucceedCallback succeedCallback,
			EventProcessingFailedCallback failedCallback,
			EventUnreferencedCallback unreferencedCallback)
			throws ActivityIsEndingException, SLEEException;

	/**
	 * Fires an event in the activity context.
	 * 
	 * @param eventTypeId
	 * @param event
	 * @param address
	 * @param serviceID
	 * @param referencesHandler
	 * @throws ActivityIsEndingException
	 * @throws SLEEException
	 */
	public void fireEvent(EventTypeID eventTypeId, Object event,
			Address address, ServiceID serviceID,
			ReferencesHandler referencesHandler)
			throws ActivityIsEndingException, SLEEException;

	/**
	 * Retrieves the handle of this ac
	 * 
	 * @return
	 */
	public ActivityContextHandle getActivityContextHandle();

	/**
	 * @return
	 */
	public ActivityContextInterface getActivityContextInterface();

	/**
	 * Fetches set of attached timers.
	 * 
	 * @return Set containing TimerID objects representing timers attached to
	 *         this ac.
	 */
	public Set<TimerID> getAttachedTimers();

	/**
	 * Get the shared data for the ACI.
	 * 
	 * @param name
	 *            -- name we want to look up
	 * @return the shared data for the ACI
	 * 
	 */
	public Object getDataAttribute(String key);

	@SuppressWarnings("rawtypes")
	public Map getDataAttributes();

	public LocalActivityContext getLocalActivityContext();

	/**
	 * Fetches set of names given to this ac
	 * 
	 * @return Set containing String objects that are names of this ac
	 */
	public Set<String> getNamingBindings();

	public Set<SbbEntityID> getSbbAttachmentSet();

	/**
	 * get an ordered copy of the set of SBBs attached to this ac. The ordering
	 * is by SBB priority.
	 * 
	 * @return list of SbbEIDs
	 * 
	 */
	public Set<SbbEntityID> getSortedSbbAttachmentSet(Set<SbbEntityID> excludeSet);

	/**
	 * test if the activity context is ending.
	 * 
	 * @return true if ending.
	 */
	public boolean isEnding();

	/**
	 * Add the given name to the set of activity context names that we are bound
	 * to. The AC Naming facility implicitly ends the activity after all names
	 * are unbound.
	 * 
	 * @param aciName
	 *            -- name to which we are bound.
	 * @return true if name bind was removed; false otherwise
	 * 
	 */
	public boolean removeNameBinding(String aciName);

	/**
	 * Requests that on the end of the transaction the activity should be
	 * checked regarding its references.
	 */
	public void scheduleCheckForUnreferencedActivity();

	/**
	 * Set a shared data item for the ACI
	 * 
	 * @param key
	 *            -- name of the shared data item.
	 * @param newValue
	 *            -- value of the shared data item.
	 */
	public void setDataAttribute(String key, Object newValue);

}
