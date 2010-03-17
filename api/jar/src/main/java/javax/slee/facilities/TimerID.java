package javax.slee.facilities;

import java.io.Serializable;

/**
 * A <code>TimerID</code> is used to identify a timer started by an SBB entity.
 * An implementation of this class must be Java serializable to support persistence
 * (in a potentially arbitrary data structure) by SBBs.
 */
public interface TimerID extends Serializable {
    /**
     * Compare this timer ID for equality with another.
     * @param obj the object to compare this with.
     * @return <code>true</code> if <code>obj</code> is an instance of this class
     *        representing the same timer as this, <code>false</code> otherwise.
     */
    public boolean equals(Object obj);

    /**
     * Get a hash code value for this timer ID.
     * @return a hash code value.
     */
    public int hashCode();

    /**
     * Get the textual representation of the timer ID object.
     * @return the textual representation of the timer ID object.
     */
    public String toString();

}

