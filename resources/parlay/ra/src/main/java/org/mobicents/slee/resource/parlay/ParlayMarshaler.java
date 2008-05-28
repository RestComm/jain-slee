package org.mobicents.slee.resource.parlay;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;

import javax.slee.resource.ActivityHandle;
import javax.slee.resource.Marshaler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * Class Description for ParlayMarshaler
 */
public class ParlayMarshaler implements Marshaler {
    private static final String FAILED_TO_UNMARSHALL_EVENT = "Failed to unmarshallEvent";
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(ParlayMarshaler.class);

    public ParlayMarshaler() {
        super();
    }
    
    /* (non-Javadoc)
     * @see javax.slee.resource.Marshaler#marshalEvent(java.lang.Object, int)
     */
    public ByteBuffer marshalEvent(final Object event, final int eventID) throws IOException {
        
        // All our events implement serializable
        
        final ByteArrayOutputStream b = new ByteArrayOutputStream();
        
        final ObjectOutputStream out = new ObjectOutputStream(b) ;
        out.writeObject(event);
        out.flush();
        out.close();
        
        return ByteBuffer.wrap(b.toByteArray());
    }

    /* (non-Javadoc)
     * @see javax.slee.resource.Marshaler#unmarshalEvent(java.nio.ByteBuffer, int)
     */
    public Object unmarshalEvent(final ByteBuffer marshalledEvent, final int eventID) throws IOException {
        Object result = null;
        
        if(marshalledEvent.hasArray()) {
            final ByteArrayInputStream b = new ByteArrayInputStream(marshalledEvent.array());
	        
            final ObjectInputStream in = new ObjectInputStream(b);
	        try {
                result = in.readObject();
            }
            catch (ClassNotFoundException e) {
                logger.error(FAILED_TO_UNMARSHALL_EVENT);
                throw new IOException(FAILED_TO_UNMARSHALL_EVENT);
            }
        }
        
        return result;
    }

    /* (non-Javadoc)
     * @see javax.slee.resource.Marshaler#marshalHandle(javax.slee.resource.ActivityHandle)
     */
    public ByteBuffer marshalHandle(final ActivityHandle handle) throws IOException {

        // All our handles implement serializable
        
        final ByteArrayOutputStream b = new ByteArrayOutputStream();
        
        final ObjectOutputStream out = new ObjectOutputStream(b) ;
        out.writeObject(handle);
        out.flush();
        out.close();
        
        return ByteBuffer.wrap(b.toByteArray());
    }

    /* (non-Javadoc)
     * @see javax.slee.resource.Marshaler#unmarshalHandle(java.nio.ByteBuffer)
     */
    public ActivityHandle unmarshalHandle(final ByteBuffer marshalledHandle) throws IOException {
        Object result = null;
        
        if(marshalledHandle.hasArray()) {
            final ByteArrayInputStream b = new ByteArrayInputStream(marshalledHandle.array());
	        
            final ObjectInputStream in = new ObjectInputStream(b);
	        try {
                result = in.readObject();
            }
            catch (ClassNotFoundException e) {
                logger.error(FAILED_TO_UNMARSHALL_EVENT);
                throw new IOException(FAILED_TO_UNMARSHALL_EVENT);
            }
        }
        
        return (ActivityHandle)result;
    }

}
