package org.mobicents.slee.runtime.eventrouter;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.slee.EventTypeID;
import javax.slee.SLEEException;
import javax.slee.resource.Marshaler;

import org.apache.log4j.Logger;
import org.jboss.serial.io.JBossObjectInputStream;
import org.jboss.serial.io.JBossObjectOutputStream;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.resource.ResourceAdaptorEntity;
import org.mobicents.slee.runtime.activity.ActivityContextHandle;
import org.mobicents.slee.runtime.activity.ActivityType;

/**
 * 
 * @author martins
 *
 */
public class EventContextID implements Serializable {

	private static final transient Logger logger = Logger.getLogger(EventContextID.class);
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * the handle of the activity context related with the event context
	 */
	private final ActivityContextHandle ach;
	
	/**
	 * 
	 */
	private final EventTypeID eventTypeID;
	
	/**
	 * the event object related with the event context
	 */
	private transient Object eventObject;
	
	public EventContextID(ActivityContextHandle ach, Object eventObject, EventTypeID eventTypeID) {
		this.ach = ach;
		this.eventObject = eventObject;
		this.eventTypeID = eventTypeID;
	}
	
	public ActivityContextHandle getActivityContextHandle() {
		return ach;
	}
	
	public Object getEventObject() {
		return eventObject;
	}
	
	@Override
	public int hashCode() {
		return ach.hashCode()*31+ eventObject.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj.getClass() == this.getClass()) {
			EventContextID other = (EventContextID) obj;
			return ((this.eventObject.equals(other.eventObject)) && (this.ach.equals(other.ach)));
		}
		else {
			return false;
		}
	}
	
	private void writeObject(ObjectOutputStream stream) throws IOException {
		
		// write ac handle and event type id
		stream.defaultWriteObject();
        
		// now write the event object
		if(ach.getActivityType() == ActivityType.RA) {
            final ResourceAdaptorEntity entity = SleeContainer.lookupFromJndi().getResourceManagement().getResourceAdaptorEntity(ach.getActivitySource());
            final Marshaler marshaler = entity.getMarshaler();
            if(marshaler != null) {
            	//FIXME: Here we possibly should use some optimized buffers, but for now its enough.
            	marshaler.marshalEvent(entity.getFireableEventType(eventTypeID),eventObject,stream);
            }
            else {
            	// serialize it using jboss serialization
            	writeObjectUsingJBossSerialization(stream);
            }
        } 
        else {
        	// not an external activity, serialize it using jboss serialization
        	writeObjectUsingJBossSerialization(stream);
        }
	}
	
	private void writeObjectUsingJBossSerialization(ObjectOutputStream stream) {
		JBossObjectOutputStream out = null;
    	try {
    		out = new JBossObjectOutputStream(stream);
    		out.writeObject(eventObject);
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
    		throw new SLEEException("Failed to marshall event object using jboss serialization.", e);
    	}
	}
 	
	private void readObject(ObjectInputStream stream)  throws IOException, ClassNotFoundException {
		
		// read ac handle and event type id
		stream.defaultReadObject();

		// now read the event object
		if(ach.getActivityType() == ActivityType.RA) {
            final ResourceAdaptorEntity entity = SleeContainer.lookupFromJndi().getResourceManagement().getResourceAdaptorEntity(ach.getActivitySource());
			Marshaler marshaler = entity.getMarshaler();
			if (marshaler != null) {
				// use ra entity marshaller to read the event object
            	eventObject = marshaler.unmarshalEvent(entity.getFireableEventType(eventTypeID),stream);
			}
			else {
				// no marshaller use jboss serialization to read the event object
				readObjectUsingJBossSerialization(stream);
			}
		}
		else {
			// not an external activity, read object using jboss serialization
			readObjectUsingJBossSerialization(stream);
		}
	}
	
	private void readObjectUsingJBossSerialization(ObjectInputStream stream) {
		JBossObjectInputStream in = null;
		try {
			in = new JBossObjectInputStream(stream);
			eventObject = in.readObject();
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
			throw new SLEEException("Failed to unmarshall event object using jboss serialization.", e);		       	  
		}
	}

}
