package org.mobicents.slee.runtime.activity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.slee.SLEEException;
import javax.slee.ServiceID;
import javax.slee.resource.ActivityHandle;
import javax.slee.resource.Marshaler;

import org.jboss.serial.io.JBossObjectInputStream;
import org.jboss.serial.io.JBossObjectOutputStream;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.service.ServiceActivityHandle;
import org.mobicents.slee.resource.ResourceAdaptorEntity;
import org.mobicents.slee.runtime.eventrouter.EventRouterActivity;
import org.mobicents.slee.runtime.facilities.nullactivity.NullActivityHandle;
import org.mobicents.slee.runtime.facilities.nullactivity.NullActivityImpl;
import org.mobicents.slee.runtime.facilities.profile.ProfileTableActivityHandle;
import org.mobicents.slee.runtime.facilities.profile.ProfileTableActivityImpl;

/**
 * The handle for an {@link ActivityContext}. Useful to understand what is the
 * source or the type of the related activity, or even get that activity object.
 * 
 * @author martins
 * 
 */
public class ActivityContextHandle implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
			
	private transient ActivityHandle activityHandle;
	private final String activitySource;
	private final ActivityType activityType;
	
	private transient int hashcode = 0;
	private transient String toString = null;
		                       	
	public ActivityContextHandle(ActivityType activityType,
			String activitySource, ActivityHandle activityHandle) {
		this.activityHandle = activityHandle;
		this.activitySource = activitySource;
		this.activityType = activityType;
	}

	public ActivityHandle getActivityHandle() {
		return activityHandle;
	}

	public String getActivitySource() {
		return activitySource;
	}

	public ActivityType getActivityType() {
		return activityType;
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
			final ActivityContextHandle other = (ActivityContextHandle) obj;
			if (other.activityHandle.equals(this.activityHandle) && other.activityType == this.activityType) {
				// only compare the source if the activity type is external
				if (this.activityType == ActivityType.RA) {
					return other.activitySource.equals(this.activitySource);
				} else {
					return true;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		if (hashcode == 0) {
			final int prime = 31;
			int result = activityHandle.hashCode();
			result = prime * result + activitySource.hashCode();
			result = prime * result + activityType.hashCode();
			hashcode = result;
		}
		return hashcode;
	}

	public Object getActivity() {

		Object activity = null;

		switch (activityType) {
		case RA:
			try {
				activity = SleeContainer.lookupFromJndi().getResourceManagement().getResourceAdaptorEntity(
						activitySource).getResourceAdaptorObject().getActivity(
						activityHandle);
			} catch (Exception e) {
				throw new SLEEException(e.getMessage(), e);
			}
			break;
		case NULL:
			activity = new NullActivityImpl((NullActivityHandle) activityHandle);
			break;
		case PTABLE:
			activity = new ProfileTableActivityImpl(
					(ProfileTableActivityHandle) activityHandle);
			break;
		case SERVICE:			
			final ServiceID serviceID  = ((ServiceActivityHandle)activityHandle).getServiceID();
			activity = SleeContainer.lookupFromJndi().getServiceActivityFactory().getActivity(serviceID);					
			break;
		default:
			throw new SLEEException("Unknown activity type " + activityType);
		}

		return activity;
	}
	
	@Override
	public String toString() {
		if (toString == null) {
			toString = new StringBuilder ("ACH=").append(activityType).append('>').append(activitySource).append('>').append(activityHandle).toString(); 
		}
		return toString;
	}
	
	// serialization
	
	private void writeObject(ObjectOutputStream stream) throws IOException {

	  stream.defaultWriteObject();

	  if (activityType == ActivityType.RA) {
	    byte[] bytes = null;
	    final SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
	    final EventRouterActivity era = sleeContainer != null ? sleeContainer.getEventRouter().getEventRouterActivity(this,false) : null;
	    if (era != null) {
	      bytes = era.getActivityHandleBytes();
	    }
	    if (bytes == null) {
	      // need to marshall the handle, but this happens at most once per AS instance
	      final ResourceAdaptorEntity raEntity = sleeContainer != null ? sleeContainer.getResourceManagement().getResourceAdaptorEntity(activitySource) : null;
	      final Marshaler marshaler = raEntity != null ? raEntity.getMarshaler() : null;
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
	  else {
	    stream.writeObject(activityHandle);
	  }
	} 

	private void readObject(ObjectInputStream stream)  throws IOException, ClassNotFoundException {

	  stream.defaultReadObject();

	  if (activityType == ActivityType.RA) {
	    final SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
	    final ResourceAdaptorEntity raEntity = sleeContainer != null ? sleeContainer.getResourceManagement().getResourceAdaptorEntity(activitySource) : null;
	    final Marshaler marshaler = raEntity != null ? raEntity.getMarshaler() : null;
	    if (marshaler != null) {
	      activityHandle = marshaler.unmarshalHandle(stream);
	    }
	    else {
	      final ObjectInputStream jbois = new JBossObjectInputStream(stream);
	      activityHandle = (ActivityHandle) jbois.readObject();
	    }
	  }
	  else {
	    activityHandle = (ActivityHandle) stream.readObject();
	  }
	} 

}