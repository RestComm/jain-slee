package org.mobicents.slee.runtime.sbbentity.cache;

import java.util.Set;

import javax.slee.EventTypeID;

import org.mobicents.slee.container.activity.ActivityContextHandle;
import org.mobicents.slee.container.sbbentity.SbbEntityID;
import org.mobicents.slee.container.transaction.SleeTransactionManager;
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
	private SleeTransactionManager transactionManager;
	public SbbEntityCacheDataWrapper(SbbEntityID sbbEntityID,
			MobicentsCluster cluster,SleeTransactionManager transactionManager) {
		
		this.transactionManager=transactionManager;
		this.attachedActivityContextsCacheData=new AttachedActivityContextsCacheData(sbbEntityID, cluster.getMobicentsCache());
		this.childRelationsCacheData=new ChildRelationsCacheData(sbbEntityID, cluster.getMobicentsCache());
		this.cmpFieldsCacheData=new CmpFieldsCacheData(sbbEntityID, cluster.getMobicentsCache());
		this.eventMasksCacheData=new EventMasksCacheData(sbbEntityID, cluster.getMobicentsCache());
		this.metadataCacheData=new MetadataCacheData(sbbEntityID, cluster.getMobicentsCache());
		this.parentEntityCacheData=new ParentEntityCacheData(sbbEntityID, cluster.getMobicentsCache());
	}
	
	public Boolean create() {
		Boolean result = (Boolean) metadataCacheData.getObject(false, EXISTS);
		if(result!=null && result)
			return false;
		
		metadataCacheData.setObject(true,EXISTS,true);		
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
		if(eventMask!=null && eventMask.size()>0)
			eventMasksCacheData.setEventMask(true, ac, eventMask);
		else
			eventMasksCacheData.removeMask(false, ac);
	}
	
	public void updateEventMask(ActivityContextHandle ac, Set<EventTypeID> maskedEvents) {
		eventMasksCacheData.updateEventMask(true, ac, maskedEvents);
	}
	
	@SuppressWarnings("unchecked")
	public void setCmpField(String cmpField, Object cmpValue) {
		if(cmpValue==null)
			cmpFieldsCacheData.removeField(true, cmpField);
		else
			cmpFieldsCacheData.setField(true, cmpField, cmpValue);
		
		if(cmpField!=null)
		{
			SbbEntityCacheKey rootKey=cmpFieldsCacheData.getKey();
			SbbEntityCmpFieldCacheKey subKey=new SbbEntityCmpFieldCacheKey(rootKey.getSbbEntityID(), rootKey.getType(), cmpField);
			transactionManager.getTransactionContext().getData().put(subKey, cmpValue);
		}
	}
	
	public Object getCmpField(String cmpField) {
		SbbEntityCacheKey rootKey=cmpFieldsCacheData.getKey();
		SbbEntityCmpFieldCacheKey subKey=new SbbEntityCmpFieldCacheKey(rootKey.getSbbEntityID(), rootKey.getType(), cmpField);
		if(transactionManager.getTransactionContext().getData().containsKey(subKey))
			return transactionManager.getTransactionContext().getData().get(subKey);
		
		return cmpFieldsCacheData.getField(false, cmpField);
	}
	
	public Set<SbbEntityID> getChildRelationSbbEntities(String getChildRelationMethod) {
		return childRelationsCacheData.getChildRelationSbbEntities(false, getChildRelationMethod);
	}
	
	public Set<SbbEntityID> getAllChildSbbEntities() {
		return childRelationsCacheData.getAllChildSbbEntities(false);
	}
	
	public void remove() {
		attachedActivityContextsCacheData.removeNode();
		cmpFieldsCacheData.removeNode();
		eventMasksCacheData.removeNode();
		metadataCacheData.removeNode();
		parentEntityCacheData.removeNode();
	}
}