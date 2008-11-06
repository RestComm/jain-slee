package org.mobicents.slee.container.management;

import java.util.concurrent.ConcurrentHashMap;

import javax.slee.EventTypeID;
import javax.slee.management.AlreadyDeployedException;
import javax.slee.management.DeployableUnitID;

import org.jboss.logging.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.ComponentKey;
import org.mobicents.slee.container.component.EventTypeIDImpl;
import org.mobicents.slee.container.component.MobicentsEventTypeDescriptor;
import org.mobicents.slee.runtime.transaction.TransactionalAction;

public class EventManagement {

	private static final Logger logger = Logger
			.getLogger(EventManagement.class);

	private final SleeContainer sleeContainer;

	// all the event types that are known to the slee.
	private ConcurrentHashMap<Integer, EventTypeIDImpl> eventID2eventTypeIDs;
	// FIXME redundant map, descriptor and event type id objects should be
	// merged
	private ConcurrentHashMap<EventTypeIDImpl, MobicentsEventTypeDescriptor> eventTypeID2Descriptor;
	private ConcurrentHashMap<ComponentKey, EventTypeIDImpl> componentKey2EventTypeID;

	public EventManagement(SleeContainer sleeContainer) {
		this.sleeContainer = sleeContainer;
		this.eventID2eventTypeIDs = new ConcurrentHashMap<Integer, EventTypeIDImpl>();
		this.eventTypeID2Descriptor = new ConcurrentHashMap<EventTypeIDImpl, MobicentsEventTypeDescriptor>();
		this.componentKey2EventTypeID = new ConcurrentHashMap<ComponentKey, EventTypeIDImpl>();
	}

	/**
	 * Retrieves the {@link EventTypeIDImpl} for the specified component key.
	 * 
	 * @param componentKey
	 * @return
	 */
	public EventTypeIDImpl getEventType(ComponentKey componentKey) {
		return this.componentKey2EventTypeID.get(componentKey);
	}

	/**
	 * Retrieves the {@link EventTypeIDImpl} for the specified event id.
	 * 
	 * @param eventID
	 * @return
	 */
	public EventTypeIDImpl getEventTypeID(int eventID) {
		return eventID2eventTypeIDs.get(new Integer(eventID));
	}

	/**
	 * Retrieves the {@link MobicentsEventTypeDescriptor} for the specified
	 * event id.
	 * 
	 * @param id
	 * @return
	 */
	public MobicentsEventTypeDescriptor getEventDescriptor(EventTypeID id) {
		return this.eventTypeID2Descriptor.get(id);
	}

	/**
	 * @return the event types that are supported by the container.
	 * 
	 */
	public EventTypeID[] getEventTypes() {
		return this.eventID2eventTypeIDs.values().toArray(
				new EventTypeIDImpl[this.eventID2eventTypeIDs.size()]);
	}

	/**
	 * Installs a new event type in the SLEE container.
	 * 
	 * @param descriptor
	 * @throws AlreadyDeployedException
	 */
	public void installEventType(MobicentsEventTypeDescriptor descriptor)
			throws AlreadyDeployedException {

		final ComponentKey ckey = new ComponentKey(descriptor.getName(),
				descriptor.getVendor(), descriptor.getVersion());

		if (!this.componentKey2EventTypeID.containsKey(ckey)) {
			final EventTypeIDImpl eventTypeID = new EventTypeIDImpl(ckey);
			if (logger.isDebugEnabled())
				logger.debug("Installing event " + eventTypeID);
			this.eventTypeID2Descriptor.put(eventTypeID, descriptor);
			this.componentKey2EventTypeID.put(ckey, eventTypeID);
			this.eventID2eventTypeIDs.put(Integer.valueOf(eventTypeID
					.getEventID()), eventTypeID);
			descriptor.setID(eventTypeID);
			// add a action to remove the event if tx rollbacks
			TransactionalAction action = new TransactionalAction() {
				public void execute() {
					eventTypeID2Descriptor.remove(eventTypeID);
					componentKey2EventTypeID.remove(ckey);
					eventID2eventTypeIDs.remove(Integer.valueOf(eventTypeID
							.getEventID()));
					logger.info("Removed event " + ckey +" due to transaction rollback");
				}
			};
			sleeContainer.getTransactionManager()
					.addAfterRollbackAction(action);
			logger.info("Installed event " + ckey);
		} else {
			throw new AlreadyDeployedException("The event " + ckey
					+ " is already deployed");
		}
	}

	/**
	 * Remove all the deployed evnet type information related to the deployable
	 * unit id. Searches through the set of deployed event information and
	 * removes any event information associated with this deployableUnitID
	 * 
	 * @param deployableUnitID
	 */

	public void removeEventType(DeployableUnitID deployableUnitID) {

		if (logger.isDebugEnabled()) {
			logger.debug("Uninstalling events from DU " + deployableUnitID);
		}

		for (final EventTypeIDImpl eventTypeID : this.eventTypeID2Descriptor
				.keySet()) {
			final MobicentsEventTypeDescriptor desc = this.eventTypeID2Descriptor
					.get(eventTypeID);
			if (desc != null && desc.getDeployableUnit() != null
					&& desc.getDeployableUnit().equals(deployableUnitID)) {
				this.eventTypeID2Descriptor.remove(eventTypeID);
				this.componentKey2EventTypeID.remove(eventTypeID
						.getComponentKey());
				this.eventID2eventTypeIDs.remove(Integer.valueOf(eventTypeID
						.getEventID()));
				// add a action to re-install the event if tx rollbacks
				TransactionalAction action = new TransactionalAction() {
					public void execute() {
						eventTypeID2Descriptor.put(eventTypeID, desc);
						componentKey2EventTypeID.put(eventTypeID
								.getComponentKey(), eventTypeID);
						eventID2eventTypeIDs.put(Integer.valueOf(eventTypeID
								.getEventID()), eventTypeID);
						logger.info("Reinstalled event " + eventTypeID +" due to transaction rollback");
					}
				};
				sleeContainer.getTransactionManager().addAfterRollbackAction(
						action);
				logger.info("Uninstalled event " + eventTypeID);
			}
		}
	}

	/**
	 * Checks that the specified {@link EventTypeID} is installed.
	 * 
	 * @param eventTypeID
	 * @return
	 */
	public boolean isInstalled(EventTypeID eventTypeID) {
		return eventTypeID2Descriptor.containsKey(eventTypeID);
	}

	@Override
	public String toString() {
		return "Event Management: " + "\n+-- Event Type IDs: "
				+ eventTypeID2Descriptor.keySet() + "\n+-- Component Keys: "
				+ componentKey2EventTypeID.keySet() + "\n+-- Event IDs: "
				+ eventID2eventTypeIDs.keySet();
	}
}
