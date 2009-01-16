package javax.slee.management;

import java.io.Serializable;
import java.io.StreamCorruptedException;

/**
 * This class defines an enumerated type that encapsulates the state of a Resource Adaptor Entity.
 * <p>
 * A singleton instance of each enumerated value is guaranteed (via an implementation
 * of <code>readResolve()</code> - refer {@link java.io.Serializable java.io.Serializable}),
 * so that equality tests using <code>==</code> are always evaluated correctly.  (This
 * equality test is only guaranteed if this class is loaded in the application's boot class
 * path, rather than dynamically loaded at runtime.)
 * @since SLEE 1.1
 */
public final class ResourceAdaptorEntityState implements Serializable {
    /**
     * An integer representation of the {@link #INACTIVE} state.
     */
    public static final int ENTITY_INACTIVE = 0;

    /**
     * An integer representation of the {@link #ACTIVE} state.
     */
    public static final int ENTITY_ACTIVE = 1;

    /**
     * An integer representation of the {@link #STOPPING} state.
     */
    public static final int ENTITY_STOPPING = 2;


    /**
     * A string representation of the {@link #INACTIVE} state.
     */
    public static final String INACTIVE_STRING = "Inactive";

    /**
     * A string representation of the {@link #ACTIVE} state.
     */
    public static final String ACTIVE_STRING = "Active";

    /**
     * A string representation of the {@link #STOPPING} state.
     */
    public static final String STOPPING_STRING = "Stopping";


    /**
     * The INACTIVE state indicates that the resource adaptor entity has been successfully
     * created and configured.  All required files and parameters have been set up, so that
     * the resource adaptor entity may be connected to the underlying resource, but it is
     * not currently producing events.
     */
    public static final ResourceAdaptorEntityState INACTIVE = new ResourceAdaptorEntityState(ENTITY_INACTIVE);

    /**
     * The ACTIVE state indicates that the resource adaptor entity has been activated and
     * it is able to deliver events from the underlying resource to the SLEE.
     */
    public static final ResourceAdaptorEntityState ACTIVE = new ResourceAdaptorEntityState(ENTITY_ACTIVE);

    /**
     * The STOPPING state indicates that an active resource adaptor entity has been
     * deactivated but some activities created by the resource adaptor entity still exist.
     * Once all activities have completed, the resource adaptor entity state spontaneously
     * returns to the INACTIVE state.
     */
    public static final ResourceAdaptorEntityState STOPPING = new ResourceAdaptorEntityState(ENTITY_STOPPING);


    /**
     * Get a <code>ResourceAdaptorEntityState</code> object from an integer value.
     * @param state the state as an integer.
     * @return a <code>ResourceAdaptorEntityState</code> object corresponding to <code>state</code>.
     * @throws IllegalArgumentException if <code>state</code> is not a valid resource
     *        adaptor entity state value.
     */
    public static ResourceAdaptorEntityState fromInt(int state) throws IllegalArgumentException {
        switch (state) {
            case ENTITY_INACTIVE: return INACTIVE;
            case ENTITY_ACTIVE: return ACTIVE;
            case ENTITY_STOPPING: return STOPPING;
            default: throw new IllegalArgumentException("Invalid state: " + state);
        }
    }

    /**
     * Get a <code>ResourceAdaptorEntityState</code> object from a string value.
     * @param state the state as a string, for example as returned by the {@link #toString()}
     *        method (case insensitive).
     * @return a <code>ResourceAdaptorEntityState</code> object corresponding to <code>state</code>.
     * @throws NullPointerException if <code>state</code> is <code>null</code>.
     * @throws IllegalArgumentException if <code>state</code> is not a valid resource
     *        adaptor entity state string.
     */
    public static ResourceAdaptorEntityState fromString(String state) throws NullPointerException, IllegalArgumentException {
        if (state == null) throw new NullPointerException("state is null");
        if (state.equalsIgnoreCase(INACTIVE_STRING)) return INACTIVE;
        if (state.equalsIgnoreCase(ACTIVE_STRING)) return ACTIVE;
        if (state.equalsIgnoreCase(STOPPING_STRING)) return STOPPING;
        throw new IllegalArgumentException("Invalid state: " + state);
    }

    /**
     * Get an integer value representation for this <code>ResourceAdaptorEntityState</code> object.
     * @return an integer value representation for this <code>ResourceAdaptorEntityState</code> object.
     */
    public int toInt() {
        return state;
    }

    /**
     * Determine if this ResourceAdaptorEntityState object represents the INACTIVE state of
     * a resource adaptor entity.
     * <p>
     * This method is effectively equivalent to the conditional test:
     * <code>(this == INACTIVE)</code>, ie. the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;&nbsp;if (state.isInactive()) ...</code>
     * <p>
     * is interchangeable with the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;if (state == ResourceAdaptorEntityState.INACTIVE) ...</code>
     * <p>
     * @return <code>true</code> if this object represents the INACTIVE state of a resource
     *       adaptor entity, <code>false</code> otherwise.
     */
    public boolean isInactive() {
        return state == ENTITY_INACTIVE;
    }

    /**
     * Determine if this ResourceAdaptorEntityState object represents the ACTIVE state of
     * a resource adaptor entity.
     * <p>
     * This method is effectively equivalent to the conditional test:
     * <code>(this == ACTIVE)</code>, ie. the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;&nbsp;if (state.isActive()) ...</code>
     * <p>
     * is interchangeable with the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;if (state == ResourceAdaptorEntityState.ACTIVE) ...</code>
     * <p>
     * @return <code>true</code> if this object represents the ACTIVE state of a resource
     *       adaptor entity, <code>false</code> otherwise.
     */
    public boolean isActive() {
        return state == ENTITY_ACTIVE;
    }

    /**
     * Determine if this ResourceAdaptorEntityState object represents the STOPPING state of
     * a resource adaptor entity.
     * <p>
     * This method is effectively equivalent to the conditional test:
     * <code>(this == STOPPING)</code>, ie. the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;&nbsp;if (state.isStopping()) ...</code>
     * <p>
     * is interchangeable with the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;if (state == ResourceAdaptorEntityState.STOPPING) ...</code>
     * <p>
     * @return <code>true</code> if this object represents the STOPPING state of a
     *       resource adaptor entity, <code>false</code> otherwise.
     */
    public boolean isStopping() {
        return state == ENTITY_STOPPING;
    }

    /**
     * Compare this resource adaptor state for equality with another.
     * @param obj the object to compare this with.
     * @return <code>true</code> if <code>obj</code> is an instance of this class
     *        representing the same resource adaptor state as this, <code>false</code>
     *        otherwise.
     */
    public boolean equals(Object obj) {
        if (obj == this) return true;

        return (obj instanceof ResourceAdaptorEntityState)
            && ((ResourceAdaptorEntityState)obj).state == state;
    }

    /**
     * Get a hash code value for this resource adaptor state.
     * @return a hash code value.
     */
    public int hashCode() {
        return state;
    }

    /**
     * Get the textual representation of the ResourceAdaptorEntityState object.
     * @return the textual representation of the ResourceAdaptorEntityState object.
     */
    public String toString() {
        switch (state) {
            case ENTITY_INACTIVE: return INACTIVE_STRING;
            case ENTITY_ACTIVE: return ACTIVE_STRING;
            case ENTITY_STOPPING: return STOPPING_STRING;
            default: return "ResourceAdaptorEntityState in Unknown and Invalid State";
        }
    }


    /**
     * Private constructor to prevent unauthorized object creation.
     */
    private ResourceAdaptorEntityState(int state) {
        this.state = state;
    }

    /**
     * Resolve deserialisation references so that the singleton property of each
     * enumerated object is preserved.
     */
    private Object readResolve() throws StreamCorruptedException {
        try {
            return fromInt(state);
        }
        catch (IllegalArgumentException iae) {
            throw new StreamCorruptedException("Invalid internal state found");
        }
    }


    /**
     * The internal state representation of the enumerated type.
     */
    private final int state;
}
