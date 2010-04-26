/**
 * 
 */
package org.mobicents.slee.resource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.slee.resource.ActivityHandle;
import javax.slee.resource.Marshaler;

import org.jboss.serial.io.JBossObjectInputStream;
import org.jboss.serial.io.JBossObjectOutputStream;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.activity.ActivityType;
import org.mobicents.slee.container.activity.LocalActivityContext;
import org.mobicents.slee.container.management.ResourceManagementImpl;
import org.mobicents.slee.container.resource.ResourceAdaptorActivityContextHandle;
import org.mobicents.slee.container.resource.ResourceAdaptorEntity;

/**
 * @author martins
 *
 */
public class ResourceAdaptorActivityContextHandleImpl implements ResourceAdaptorActivityContextHandle {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private transient ActivityHandle activityHandle;
	private transient ResourceAdaptorEntity raEntity;
		
	/**
	 * 
	 */
	public ResourceAdaptorActivityContextHandleImpl(ResourceAdaptorEntity raEntity, ActivityHandle activityHandle) {
		this.raEntity = raEntity;
		this.activityHandle = activityHandle;
	}
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.runtime.activity.ActivityContextHandle#getActivityObject()
	 */
	public Object getActivityObject() {
		return raEntity.getResourceAdaptorObject().getActivity(activityHandle);
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.runtime.activity.ActivityContextHandle#getActivityHandle()
	 */
	public ActivityHandle getActivityHandle() {
		return activityHandle;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.resource.ResourceAdaptorActivityContextHandle#getResourceAdaptorEntity()
	 */
	public ResourceAdaptorEntity getResourceAdaptorEntity() {
		return raEntity;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.runtime.activity.ActivityContextHandle#getActivityType()
	 */
	public ActivityType getActivityType() {
		return ActivityType.RA;
	}

	@Override
	public boolean equals(Object obj) {

		if (this == obj) {
			return true;
		}
		
		if (obj == null) {
			return false;
		}
		
		if (obj.getClass() == this.getClass()) {
			final ResourceAdaptorActivityContextHandleImpl other = (ResourceAdaptorActivityContextHandleImpl) obj;
			return other.activityHandle.equals(this.activityHandle) && other.raEntity.equals(this.raEntity);
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return activityHandle.hashCode() * 31 + raEntity.hashCode();
	}
	
	@Override
	public String toString() {
		return new StringBuilder ("ACH=").append(getActivityType()).append('>').append(getResourceAdaptorEntity().getName()).append('>').append(activityHandle).toString(); 		
	}
	
	// serialization
	
	private void writeObject(ObjectOutputStream stream) throws IOException {

	  stream.defaultWriteObject();
	  
	  // write ra entity name
	  stream.writeUTF(raEntity.getName());
	  
	  // write activity handle
	  byte[] bytes = null;
	  final SleeContainer sleeContainer = ResourceManagementImpl.getInstance().getSleeContainer();
	  final LocalActivityContext era = sleeContainer.getActivityContextFactory().getLocalActivityContext(this, false);
	  if (era != null) {
		  bytes = era.getActivityHandleBytes();
	  }
	  if (bytes == null) {
		  // need to marshall the handle, but this happens at most once per AS instance
		  final Marshaler marshaler = raEntity.getMarshaler();
		  int bufferSize = marshaler != null ? marshaler.getEstimatedHandleSize(activityHandle) : 1024;
		  final ByteArrayOutputStream baos =  new ByteArrayOutputStream(bufferSize);
		  final JBossObjectOutputStream jboos = new JBossObjectOutputStream(baos);
		  if (marshaler != null) {
			  marshaler.marshalHandle(activityHandle,jboos);
		  }
		  else {
			  jboos.writeObject(activityHandle);
		  }
		  bytes = baos.toByteArray();
		  jboos.close(); 
		  if (era != null) {
			  // cache bytes in local activity resources
			  era.setActivityHandleBytes(bytes);
		  }
	  }
	  stream.write(bytes);
	  	 
	} 

	private void readObject(ObjectInputStream stream)  throws IOException, ClassNotFoundException {

	  stream.defaultReadObject();

	  // read ra entity name
	  final String raEntityName = stream.readUTF(); 

	  // read activity handle
	  this.raEntity = ResourceManagementImpl.getInstance().getResourceAdaptorEntity(raEntityName);
	  if (raEntity == null) {
		  throw new IOException("RA Entity with name "+raEntityName+" not found.");
	  }
	  final Marshaler marshaler = raEntity.getMarshaler();
	  if (marshaler != null) {
		  activityHandle = marshaler.unmarshalHandle(stream);
	  }
	  else {
		  final ObjectInputStream jbois = new JBossObjectInputStream(stream);
		  activityHandle = (ActivityHandle) jbois.readObject();
	  }	
	} 
	
}
