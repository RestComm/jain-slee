package org.mobicents.slee.runtime.sbbentity.cache;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.slee.EventTypeID;

import org.mobicents.slee.container.activity.ActivityContextHandle;
import org.mobicents.slee.container.sbbentity.SbbEntityID;
import org.restcomm.cluster.MobicentsCluster;

public class SbbEntityCacheDataWrapper 
{
	private static final String PRIORITY_NODE_NAME = "priority";
	private static final String EXISTS = "exists";
	
	private AttachedActivityContextsCacheData attachedActivityContextsCacheData;
	private ChildRelationsCacheData childRelationsCacheData;
	private CmpFieldsCacheData cmpFieldsCacheData;
	private EventMasksCacheData eventMasksCacheData;
	private MetadataCacheData metadataCacheData;
	private ParentEntityCacheData parentEntityCacheData;
	
	private ConcurrentHashMap<String, Object> _cmpFieldsLocal = new ConcurrentHashMap<String, Object>();

	public SbbEntityCacheDataWrapper(SbbEntityID sbbEntityID,
			MobicentsCluster cluster) {
		
		this.attachedActivityContextsCacheData=new AttachedActivityContextsCacheData(sbbEntityID, cluster.getMobicentsCache());
		this.childRelationsCacheData=new ChildRelationsCacheData(sbbEntityID, cluster.getMobicentsCache());
		this.cmpFieldsCacheData=new CmpFieldsCacheData(sbbEntityID, cluster.getMobicentsCache());
		this.eventMasksCacheData=new EventMasksCacheData(sbbEntityID, cluster.getMobicentsCache());
		this.metadataCacheData=new MetadataCacheData(sbbEntityID, cluster.getMobicentsCache());
		this.parentEntityCacheData=new ParentEntityCacheData(sbbEntityID, cluster.getMobicentsCache());
	}
	
	public Boolean create() {
		Boolean created=metadataCacheData.setObject(true,EXISTS,true);
		if(!created)
			return false;
		
		parentEntityCacheData.create(true);
		return true; 
	}
	
	public Boolean exists() {
		return metadataCacheData.exists();
	}
	
	public Byte getPriority() {	
		return (Byte)metadataCacheData.getObject(false, PRIORITY_NODE_NAME);		
	}
	
	public void setPriority(Byte priority) {
		metadataCacheData.setObject(true, PRIORITY_NODE_NAME, priority);
	}
	
	public void attachActivityContext(ActivityContextHandle ac) {
		attachedActivityContextsCacheData.attachHandle(true, ac);		
	}

	public void detachActivityContext(ActivityContextHandle ac) {
		attachedActivityContextsCacheData.detachHandle(false, ac);			
	}

	public boolean isAttached(ActivityContextHandle ach) {
		return attachedActivityContextsCacheData.hasHandle(false,ach);
	}
	
	public Set<ActivityContextHandle> getActivityContexts() {
		return attachedActivityContextsCacheData.getHandles(false);
	}
	
	public Set<EventTypeID> getMaskedEventTypes(ActivityContextHandle ac) {
		return eventMasksCacheData.getMask(false, ac);
	}
	
	public void setEventMask(ActivityContextHandle ac, Set<EventTypeID> eventMask) {
		eventMasksCacheData.setEventMask(true, ac, eventMask);
	}
	
	public void updateEventMask(ActivityContextHandle ac, Set<EventTypeID> maskedEvents) {
		eventMasksCacheData.updateEventMask(true, ac, maskedEvents);
	}
	
	public void setCmpField(String cmpField, Object cmpValue) {
		cmpFieldsCacheData.setField(true, cmpField, cmpValue);
		
		if (cmpField != null && cmpValue != null) {
			_cmpFieldsLocal.put(cmpField, cmpValue);
		}
	}
	
	public Object getCmpField(String cmpField) {
		try {
			return cmpFieldsCacheData.getField(false, cmpField);
		}
		catch (IllegalStateException stateException) {
			// this happen when transaction is rollbackOnly and Infinispan throws exception:
			// IllegalStateException (ActionStatus.ABORT_ONLY >
			// is not in a valid state to be invoking cache operations on)
			return _cmpFieldsLocal.get(cmpField);
		}
	}
	
	public Set<SbbEntityID> getChildRelationSbbEntities(String getChildRelationMethod) {
		return childRelationsCacheData.getChildRelationSbbEntities(false, getChildRelationMethod);
	}
	
	public Set<SbbEntityID> getAllChildSbbEntities() {
		return childRelationsCacheData.getAllChildSbbEntities(false);
	}
	
	public void remove() {
		System.out.println("REMOVING NODE");
		attachedActivityContextsCacheData.removeNode();
		cmpFieldsCacheData.removeNode();
		eventMasksCacheData.removeNode();
		metadataCacheData.removeNode();
		parentEntityCacheData.removeNode();
		_cmpFieldsLocal.clear();		
	}
}