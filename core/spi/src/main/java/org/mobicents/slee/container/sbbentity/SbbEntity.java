/**
 * 
 */
package org.mobicents.slee.container.sbbentity;

import java.util.Set;

import javax.slee.ActivityContextInterface;
import javax.slee.EventTypeID;
import javax.slee.SbbID;
import javax.slee.SbbLocalObject;
import javax.slee.ServiceID;
import javax.slee.UnrecognizedEventException;
import javax.transaction.SystemException;
import javax.transaction.TransactionRequiredException;

import org.mobicents.slee.container.activity.ActivityContext;
import org.mobicents.slee.container.activity.ActivityContextHandle;
import org.mobicents.slee.container.component.sbb.SbbComponent;
import org.mobicents.slee.container.event.EventContext;
import org.mobicents.slee.container.sbb.SbbObject;
import org.mobicents.slee.container.sbb.SbbObjectPool;

/**
 * @author martins
 *
 */
public interface SbbEntity {

	/**
	 * @return
	 */
	public String getSbbEntityId();

	/**
	 * @return
	 */
	public SbbComponent getSbbComponent();

	/**
	 * @return
	 */
	public Set<ActivityContextHandle> getActivityContexts();

	/**
	 * @param ach
	 * @return
	 */
	public String[] getEventMask(ActivityContextHandle ach);

	/**
	 * @param ach
	 * @return
	 */
	public boolean isAttached(ActivityContextHandle ach);

	/**
	 * @return
	 */
	public SbbLocalObject getSbbLocalObject();

	/**
	 * @param ach
	 * @param eventNames
	 * @throws UnrecognizedEventException 
	 */
	public void setEventMask(ActivityContextHandle ach, String[] eventNames) throws UnrecognizedEventException;

	/**
	 * @param ach
	 */
	public void afterACDetach(ActivityContextHandle ach);

	/**
	 * 
	 * @param ach
	 */
	public void afterACAttach(ActivityContextHandle ach);

	/**
	 * @return
	 */
	public boolean isRootSbbEntity();

	/**
	 * @return
	 */
	public String getRootSbbId();

	/**
	 * @return
	 */
	public int getAttachmentCount();

	/**
	 * Remove the SbbEntity (Spec. 5.5.4) It detaches the SBB entity from all
	 * Activity Contexts. It invokes the appropriate life cycle methods (see
	 * Section 6.3) of an SBB object that caches the SBB entity’s state. It
	 * removes the SBB entity from the ChildRelation object that the SBB entity
	 * belongs to. It removes the persistent representation of the SBB entity.
	 * 
	 * @param removeFromParent
	 *            indicates if the entity should be removed from it's parent or
	 *            not
	 * @throws TransactionRequiredException
	 * @throws SystemException
	 */
	public void remove(boolean removeFromParent)
			throws TransactionRequiredException, SystemException;

	/**
	 * @return
	 */
	public String getServiceConvergenceName();

	/**
	 * @return
	 */
	public ServiceID getServiceId();

	/**
	 * Retreives the name of the child relation of the parent this sbb entity
	 * belongs.
	 * 
	 * @return
	 */
	public String getParentChildRelation();
	
	/**
	 * Retreives the id of the parent sbb entity.
	 * 
	 * @return
	 */
	public String getParentSbbEntityId();
	
	/**
	 * 
	 * @return
	 */
	public SbbID getSbbId();
	
	/**
	 * 
	 * @return
	 */
	public SbbObjectPool getObjectPool();
	
	/**
	 * 
	 * @return
	 */
	public SbbObject getSbbObject();
	
	/**
	 * Assigns an sbb object to this sbb entity.
	 * 
	 * @throws Exception
	 */
	public void assignSbbObject() throws Exception;
	
	/**
	 * 
	 * @return
	 */
	public byte getPriority();
	
	/**
	 * 
	 * @param priority
	 */
	public void setPriority(byte priority);
	
	/**
	 * 
	 * @return
	 */
	public boolean isCreated();
	
	/**
	 * 
	 * @param ach
	 * @return
	 */
	public Set<EventTypeID> getMaskedEventTypes(ActivityContextHandle ach);
	
	/**
	 * Invokes the event handler for the specified event, ac and event context.
	 * 
	 */
	public void invokeEventHandler(EventContext sleeEvent, ActivityContext ac,
			EventContext eventContextImpl) throws Exception;
	
	/**
	 * 
	 * Returns true if the SbbEntity is in the process of being removed
	 * 
	 * @return Returns the isRemoved.
	 */
	public boolean isRemoved();
	
	/**
	 * Invoke sbbPassivate() and then release the sbb object from the entity
	 * 
	 * @throws Exception
	 */
	public void passivateAndReleaseSbbObject() throws Exception;
	
	/**
	 * Retrieves the child relation impl with the specified name, if it's a
	 * valid name for this sbb component.
	 * 
	 * @param accessorName
	 * @return null if it's an invalid name for this sbb component.
	 */
	public ChildRelation getChildRelation(String accessorName);
	
	/**
	 * 
	 */
	public void trashObject();
	
	/**
	 * 
	 * @param event
	 * @param activityContextInterface
	 * @param removeRollback
	 */
	public void sbbRolledBack(Object event,
            ActivityContextInterface activityContextInterface,
            boolean removeRollback);
	
	/**
	 * Invoke sbbRemove() and then release the sbb object from the entity
	 * 
	 * @throws Exception
	 */
	public void removeAndReleaseSbbObject() throws Exception;

	/**
	 * @param cmpFieldName
	 * @param cmpFieldValue
	 */
	public void setCMPField(String cmpFieldName, Object cmpFieldValue);

	/**
	 * @param cmpFieldName
	 * @return
	 */
	public Object getCMPField(String cmpFieldName);
	
	/**
	 * @see SbbComponent#isReentrant()
	 * @return
	 */
	public boolean isReentrant();
}
