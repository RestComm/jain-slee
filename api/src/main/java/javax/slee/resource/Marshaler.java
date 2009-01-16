package javax.slee.resource;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * The <code>Marshaler</code> interface defines methods that allow a resource adaptor
 * to control the serialization of event objects and activity handles.  Serialization
 * of these objects may be required, for example, in a clustered environment where events
 * and/or activities are replicated.
 * <p>
 * A resource adaptor is not required to implement the Marshaler interface.  A resource
 * adaptor that does not provide such an implementation is not permitted to use the
 * {@link ActivityFlags#SLEE_MAY_MARSHAL} flag when starting activities, or the
 * {@link EventFlags#SLEE_MAY_MARSHAL} flag when firing events.
 * @since SLEE 1.1
 */
public interface Marshaler {
    /**
     * Get an estimate of the size (in bytes) of the marshaled form of the specified event
     * or event type.  This method is used by the SLEE to help size internal bufferes used
     * for marshaling events.  The resource adaptor should provide a best-guess estimate.
     * <p>
     * This method can be invoked in two ways:
     * <ul>
     *   <li>If the <code>event</code> argument is not <code>null</code>, the SLEE is
     *       requesting an estimate of the marshaled size of the specified event object.
     *       The <code>eventType</code> argument specifies the event type of the event
     *       object.
     *   <li>If the <code>event</code> argument is <code>null</code>, the SLEE is requesting
     *       a general estimate of the marshaled size of all event objects of the event
     *       types specified by the <code>eventType</code> argument.
     * </ul>
     * <p>
     * This method is a non-transactional method.
     * @param eventType the event type the SLEE is requesting a marshaled size estimate for.
     * @param event the event object that the SLEE is requesting a marshaled size estimate
     *        for.  This may be <code>null</code> if the SLEE is requesting a general
     *        estimate for all event objects of the indicated event type.
     * @return an estimate of the size, in bytes, of the marshaled form of the specified
     *        event or event type.
     */
    public int getEstimatedEventSize(FireableEventType eventType, Object event);

    /**
     * Get a buffer containing an already marshaled for of the specified event, if such
     * a buffer exists.
     * <p>
     * A resource adaptor may choose to cache buffers containing the network messages it
     * has received which the resource adaptor subsequently generates event objects from.
     * This method provides a resource adaptor with the opportunity to give the SLEE
     * access to these buffers during event marshalling, allowing the SLEE to use the
     * already marshaled form, rather than the SLEE and resource adaptor unnecessarily
     * marshaling the event object again. The buffer returned by this should have a
     * position and limit that encapsulates only the marshaled event data, ie. if a
     * network buffer contains multiple messages then the resource adaptor should generate
     * a new <code>ByteBuffer</code> object that provides a view of the requested event
     * only.
     * <p>
     * A resource adaptor is not expected to marshal the event object during this method.
     * If an existing marshaled form of the event object exists, the resource adaptor
     * should return a reference to the buffer.  If a marshaled form does not exist, the
     * resource adaptor should return <code>null</code> from this method and the SLEE will
     * default to using the {@link #marshalEvent} method to perform the actual event
     * marshaling.
     * <p>
     * If a resource adaptor returns a reference to a buffer from this method, the resource
     * adaptor must ensure that the contents of the buffer are not modified and remain
     * valid until the SLEE notifies the resource adaptor that it no longer needs the buffer
     * via the {@link #releaseEventBuffer} method.
     * <p>
     * This method is a non-transactional method.
     * @param eventType the event type of the event.
     * @param event the event object the SLEE is requesting a buffer for.
     * @return a buffer containing the marshaled form of the event, or <code>null</code>
     *        if no such buffer exists.
     */
    public ByteBuffer getEventBuffer(FireableEventType eventType, Object event);

    /**
     * The SLEE invokes this method to notify the resource adaptor that a buffer it
     * previously obtained from the resource adaptor via the {@link #getEventBuffer} method
     * is no longer required.  The resource adaptor is therefore free to release any
     * resources associated with the buffer as required.
     * <p>
     * This method is a non-transactional method.
     * @param eventType the event type of the event.
     * @param event the event object that the buffer was obtained for.
     * @param buffer the buffer the resource adaptor provided to the SLEE for the event
     *        object.
     */
    public void releaseEventBuffer(FireableEventType eventType, Object event, ByteBuffer buffer);

    /**
     * Marshal an event.
     * <p>
     * This method is a non-transactional method.
     * @param eventType the event type of the event.
     * @param event the event object to marshal.
     * @param out a <code>DataOutput</code> object that the resource adaptor must use
     *        to write the marshaled event data to.
     * @throws IOException if an I/O error occurs while marshaling the event.
     */
    public void marshalEvent(FireableEventType eventType, Object event, DataOutput out)
        throws IOException;

    /**
     * Unmarshal a previously marshaled event.
     * <p>
     * This method is a non-transactional method.
     * @param eventType the event type of the marshaled event.
     * @param in a <code>DataInput</code> object that the resource adaptor uses to
     *         read the marshaled event data from.
     * @return the unmarshaled event object.
     * @throws IOException if an I/O error occurs while unmarshaling the event.
     */
    public Object unmarshalEvent(FireableEventType eventType, DataInput in)
        throws IOException;

    /**
     * Get an estimate of the size (in bytes) of the marshaled form of an activity handle.
     * This method is used by the SLEE to help size internal bufferes used for marshaling
     * activity handles.  The resource adaptor should provide a best-guess estimate.
     * <p>
     * This method can be invoked in two ways:
     * <ul>
     *   <li>If the <code>handle</code> argument is not <code>null</code>, the SLEE is
     *       requesting an estimate of the marshaled size of the specified activity handle.
     *   <li>If the <code>handle</code> argument is <code>null</code>, the SLEE is requesting
     *       a general estimate of the marshaled size of all activity handles generated by
     *       the resource adaptor.
     * </ul>
     * <p>
     * This method is a non-transactional method.
     * @param handle the activity handle that the SLEE is requesting a marshaled size
     *        estimate for.  This may be <code>null</code> if the SLEE is requesting a
     *        general estimate for all activity handles.
     * @return an estimate of the size, in bytes, of the marshaled form of the activity
     *        handle, or all activity handles.
     */
    public int getEstimatedHandleSize(ActivityHandle handle);

    /**
     * Marshal an activity handle.
     * <p>
     * This method is a non-transactional method.
     * @param handle the activity handle to marshal.
     * @param out a <code>DataOutput</code> object that the resource adaptor must use
     *        to write the marshaled activity handle to.
     * @throws IOException if an I/O error occurs while marshaling the event.
     */
    public void marshalHandle(ActivityHandle handle, DataOutput out)
        throws IOException;

    /**
     * Unmarshal a previously marshaled activity handle.
     * <p>
     * This method is a non-transactional method.
     * @param in a <code>DataInput</code> object that the resource adaptor uses to
     *         read the marshaled activity handle data from.
     * @return the unmarshaled activity handle object.
     * @throws IOException if an I/O error occurs while unmarshaling the activity handle.
     */
    public ActivityHandle unmarshalHandle(DataInput in)
        throws IOException;
}

