package javax.slee.connection;

/**
 * A marker interface implemented by external activity handle implementations.
 * Instances implementing this interface are returned by
 * {@link SleeConnection#createActivityHandle}.
 *
 * @see SleeConnection
 */
public interface ExternalActivityHandle extends java.io.Serializable {
    /**
     * Compares this handle to another for equality.
     *
     * @param other an object to compare to
     * @return true if <code>other</code> refers to an ExternalActivityHandle
     *   corresponding to the same underlying SLEE activity.
     * @see Object#equals(Object)
     */
    public boolean equals(Object other);

    /**
     * Returns a hashcode for this handle. Implementations of
     * ExternalActivityHandle should ensure their implementation of hashCode()
     * is consistent with equals().
     *
     * @return a hashcode value consistent with equals()
     * @see Object#hashCode
     */
    public int hashCode();
}
