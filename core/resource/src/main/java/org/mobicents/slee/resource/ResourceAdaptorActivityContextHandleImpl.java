/**
 * 
 */
package org.mobicents.slee.resource;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.slee.resource.ActivityHandle;
import javax.slee.resource.Marshaler;

import org.jgroups.Address;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.activity.ActivityType;
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
		return new StringBuilder ("RA:").append(getResourceAdaptorEntity().getName()).append(':').append(activityHandle).toString(); 		
	}
	
	// serialization
	
	private void writeObject(ObjectOutputStream stream) throws IOException {

	  stream.defaultWriteObject();
	  
	  // write ra entity name
	  stream.writeUTF(raEntity.getName());
	  
	  // write activity handle
	  if (activityHandle.getClass() == ActivityHandleReference.class) {
		  // a reference
		  stream.writeBoolean(true);
		  final ActivityHandleReference reference = (ActivityHandleReference) activityHandle;
		  stream.writeObject(reference.getAddress());
		  stream.writeUTF(reference.getId());
	  }
	  else {
		  stream.writeBoolean(false);
		  final Marshaler marshaler = raEntity.getMarshaler();
		  if (marshaler != null) {
			  marshaler.marshalHandle(activityHandle, stream);
		  }
		  else {
			  throw new IOException("marshaller from RA is null");
		  }
	  }
	   
	} 

	private void readObject(ObjectInputStream stream) throws IOException,
			ClassNotFoundException {

		stream.defaultReadObject();

		// read ra entity name
		final String raEntityName = stream.readUTF();

		// read activity handle
		this.raEntity = SleeContainer.lookupFromJndi().getResourceManagement()
				.getResourceAdaptorEntity(raEntityName);
		if (raEntity == null) {
			throw new IOException("RA Entity with name " + raEntityName
					+ " not found.");
		}

		// read activity handle
		boolean handleReference = stream.readBoolean();
		if (handleReference) {
			activityHandle = new ActivityHandleReference(null, (Address) stream.readObject(), stream.readUTF());
		} else {
			final Marshaler marshaler = raEntity.getMarshaler();
			if (marshaler != null) {
				activityHandle = marshaler.unmarshalHandle(stream);
			} else {
				throw new IOException("marshaller from RA is null");
			}
		}
	} 
	
}
