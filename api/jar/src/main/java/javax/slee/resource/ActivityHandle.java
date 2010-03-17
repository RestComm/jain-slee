package javax.slee.resource;

/**
 * The ActivityHandle interface is implemented by each Resource Adaptor. An activity handle uniquely identifies an activity.  There is a one-to-one relationship
 * between an activity handle and its associated activity. Within the SLEE events are always delivered on an ActivityContext. 
 * The ActivityHandle is used by the SLEE to identify which ActivityContext to use.
 * <p>
 * An implementation of this interface must implement the <code>equals</code> and
 * <code>hashCode</code> methods in this interface so that:
 * <ul>
 *   <li><code>handle1.equals(handle2)</code> is true if and only if the two handles
 *       reference the same underlying activity object
 *   <li><code>handle.hashCode()</code> always returns the same value for the same underlying
 *       activity object, regardless of the Java VM process in which the activity may reside.
 * </ul>
 * @since SLEE 1.1
 */
public interface ActivityHandle {
    /**
     * Compare this activity handle for equality with another.  Two activity handles are
     * considered equal if they both reference the same underlying activity object.
     * @param obj the object to compare this with.
     * @return <code>true</code> if <code>obj</code> is an instance of this class and
     *        references the same underlying activity as this, <code>false</code> otherwise.
     */
    public boolean equals(Object obj);

    /**
     * Get a hash code for this activity handle.  The hash code must be consistent
     * across multiple Java VM processes and marshal/unmarshal operations.
     * @return a hash code value.
     */
    public int hashCode();
}

