package org.mobicents.slee.runtime.activity;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.slee.SLEEException;
import javax.slee.ServiceID;
import javax.slee.resource.ActivityHandle;
import javax.slee.resource.Marshaler;

import org.apache.log4j.Logger;
import org.jboss.serial.io.JBossObjectInputStream;
import org.jboss.serial.io.JBossObjectOutputStream;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.management.ResourceManagement;
import org.mobicents.slee.container.service.ServiceActivityHandle;
import org.mobicents.slee.resource.ResourceAdaptorEntity;
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

	private static final transient Logger logger = Logger.getLogger(ActivityContextHandle.class);
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static transient ResourceManagement _resourceManagement;
	private static ResourceManagement getResourceManagement() {
		if (_resourceManagement == null) {
			_resourceManagement = SleeContainer.lookupFromJndi().getResourceManagement();
		}
		return _resourceManagement;
	}
	
	//in case of RA, this wont be serializable, and even if it is, we must use RA.Marshaler ?
	private transient ActivityHandle activityHandle;
	private String activitySource;
	private ActivityType activityType;
	
	private transient int hashcode = 0;
	private transient String toString = null;
	
	protected ActivityContextHandle(ActivityType activityType,
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
				activity = getResourceManagement().getResourceAdaptorEntity(
						activitySource).getResourceAdaptorObject().getActivity(
						getActivityHandle());
			} catch (Exception e) {
				throw new SLEEException(e.getMessage(), e);
			}
			break;
		case NULL:
			activity = new NullActivityImpl((NullActivityHandle) activityHandle);
			break;
		case PTABLE:
			activity = new ProfileTableActivityImpl(
					(ProfileTableActivityHandle) getActivityHandle());
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
			toString = "ACH=" + activityType + ">"+ activitySource + ">" + activityHandle; 
		}
		return toString;
	}
	
	private void writeObject(ObjectOutputStream stream) throws IOException {
		
		// write activity type and source
		stream.defaultWriteObject();
        
		// now write the activity handle
		if(activityType == ActivityType.RA) {
            final ResourceAdaptorEntity entity = getResourceManagement().getResourceAdaptorEntity(activitySource);
            final Marshaler marshaler = entity.getMarshaler();
            if(marshaler != null) {
            	//FIXME: Here we possibly should use some optimized buffers, but for now its enough.
            	marshaler.marshalHandle(this.activityHandle, stream);
            }
            else {
            	// serialize it using jboss serialization
            	JBossObjectOutputStream out = null;
            	try {
            		out = new JBossObjectOutputStream(stream);
            		out.writeObject(this.activityHandle);
            		out.close();
            	}
            	catch(Throwable e) {
            		if (out != null)  {
            			try {
            				out.close();
            			} catch (IOException e1) {
            				logger.error(e.getMessage(),e);
            			}  
            		}
            		throw new SLEEException("Failed to marshall activity handle using jboss serialization.", e);
            	}
            }
        } 
        else {
        	// not an external activity, write object in stream
            stream.writeObject(this.activityHandle);
        }
	}
	
	private void readObject(ObjectInputStream stream)  throws IOException, ClassNotFoundException {
		
		
		stream.defaultReadObject();
		
		if(activityType == ActivityType.RA) {
			ResourceAdaptorEntity entity = getResourceManagement().getResourceAdaptorEntity(activitySource);
			Marshaler marshaler = entity.getMarshaler();
			if (marshaler != null) {
				// use ra entity marshaller to read the handle
				this.activityHandle=marshaler.unmarshalHandle(stream);
			}
			else {
				// use jboss serialization to read the handle
				JBossObjectInputStream in = null;
				try {
					in = new JBossObjectInputStream(stream);
					this.activityHandle = (ActivityHandle) in.readObject();
					in.close();
				}
				catch(Throwable e) {
					if (in != null)  {
						try {
							in.close();
						} catch (IOException e1) {
							logger.error(e.getMessage(),e);
						}    
					}
					throw new SLEEException("Failed to unmarshall activity handle using jboss serialization.", e);		       	  
				}
			}
		}
		else {
			// not an external activity, read object from stream
			this.activityHandle=(ActivityHandle) stream.readObject();
		}
	}

}