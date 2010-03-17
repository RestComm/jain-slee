package javax.slee.management;

import java.io.Serializable;
import java.io.StreamCorruptedException;

/**
 * This class defines an enumerated type that encapsulates the state of a Service.
 * <p>
 * A singleton instance of each enumerated value is guaranteed (via an implementation
 * of <code>readResolve()</code> - refer {@link java.io.Serializable java.io.Serializable}),
 * so that equality tests using <code>==</code> are always evaluated correctly.  (This
 * equality test is only guaranteed if this class is loaded in the application's boot class
 * path, rather than dynamically loaded at runtime.)
 */
public final class ServiceState implements Serializable {
    /**
     * An integer representation of the {@link #INACTIVE} state.
     */
    public static final int SERVICE_INACTIVE = 0;

    /**
     * An integer representation of the {@link #ACTIVE} state.
     */
    public static final int SERVICE_ACTIVE = 1;

    /**
     * An integer representation of the {@link #STOPPING} state.
     */
    public static final int SERVICE_STOPPING = 2;


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
     * The INACTIVE state indicates that the Service has been successfully
     * installed.  All required files and parameters have been set up, so that the
     * Service may be activated, but it is currently not running.  (This means that
     * instances of the Service are not consuming memory or CPU resources.)
     */
    public static final ServiceState INACTIVE = new ServiceState(SERVICE_INACTIVE);

    /**
     * The ACTIVE state indicates that the Service has been activated.  In
     * this state, instances of the Service are created by the SLEE's event routing
     * subsystem in response to initial events, and the Service instances are invoked
     * to respond to events.
     */
    public static final ServiceState ACTIVE = new ServiceState(SERVICE_ACTIVE);

    /**
     * The STOPPING state indicates that an active Service has been deactivated,
     * but some instances of the Service are still running.  Once all Service instances
     * have finished running, the Service state spontaneously returns to the
     * INACTIVE state.
     */
    public static final ServiceState STOPPING = new ServiceState(SERVICE_STOPPING);


    /**
     * Get a <code>ServiceState</code> object from an integer value.
     * @param state the state as an integer.
     * @return a <code>ServiceState</code> object corresponding to <code>state</code>.
     * @throws IllegalArgumentException if <code>state</code> is not a valid service
     *        state value.
     */
    public static ServiceState fromInt(int state) throws IllegalArgumentException {
        switch (state) {
            case SERVICE_INACTIVE: return INACTIVE;
            case SERVICE_ACTIVE: return ACTIVE;
            case SERVICE_STOPPING: return STOPPING;
            default: throw new IllegalArgumentException("Invalid state: " + state);
        }
    }

    /**
     * Get a <code>ServiceState</code> object from an integer value.
     * @param state the state as a string, for example as returned by the {@link #toString()}
     *        method (case insensitive).
     * @return a <code>ServiceState</code> object corresponding to <code>state</code>.
     * @throws NullPointerException if <code>state</code> is <code>null</code>.
     * @throws IllegalArgumentException if <code>state</code> is not a valid service
     *        state string.
     * @since SLEE 1.1
     */
    public static ServiceState fromString(String state) throws NullPointerException, IllegalArgumentException {
        if (state == null) throw new NullPointerException("state is null");
        if (state.equalsIgnoreCase(INACTIVE_STRING)) return INACTIVE;
        if (state.equalsIgnoreCase(ACTIVE_STRING)) return ACTIVE;
        if (state.equalsIgnoreCase(STOPPING_STRING)) return STOPPING;
        throw new IllegalArgumentException("Invalid state: " + state);
    }

    /**
     * Get an integer value representation for this <code>ServiceState</code> object.
     * @return an integer value representation for this <code>ServiceState</code> object.
     */
    public int toInt() {
        return state;
    }

    /**
     * Determine if this ServiceState object represents the INACTIVE state of a Service.
     * <p>
     * This method is effectively equivalent to the conditional test:
     * <code>(this == INACTIVE)</code>, ie. the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;&nbsp;if (state.isInactive()) ...</code>
     * <p>
     * is interchangeable with the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;if (state == ServiceState.INACTIVE) ...</code>
     * <p>
     * @return <code>true</code> if this object represents the INACTIVE state of a Service,
     *       <code>false</code> otherwise.
     */
    public boolean isInactive() {
        return state == SERVICE_INACTIVE;
    }

    /**
     * Determine if this ServiceState object represents the ACTIVE state of a Service.
     * <p>
     * This method is effectively equivalent to the conditional test:
     * <code>(this == ACTIVE)</code>, ie. the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;&nbsp;if (state.isActive()) ...</code>
     * <p>
     * is interchangeable with the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;if (state == ServiceState.ACTIVE) ...</code>
     * <p>
     * @return <code>true</code> if this object represents the ACTIVE state of a Service,
     *       <code>false</code> otherwise.
     */
    public boolean isActive() {
        return state == SERVICE_ACTIVE;
    }

    /**
     * Determine if this ServiceState object represents the STOPPING state of a Service.
     * <p>
     * This method is effectively equivalent to the conditional test:
     * <code>(this == STOPPING)</code>, ie. the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;&nbsp;if (state.isStopping()) ...</code>
     * <p>
     * is interchangeable with the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;if (state == ServiceState.STOPPING) ...</code>
     * <p>
     * @return <code>true</code> if this object represents the STOPPING state of a Service,
     *       <code>false</code> otherwise.
     */
    public boolean isStopping() {
        return state == SERVICE_STOPPING;
    }

    /**
     * Compare this Service state for equality with another.
     * @param obj the object to compare this with.
     * @return <code>true</code> if <code>obj</code> is an instance of this class
     *        representing the same Service state as this, <code>false</code> otherwise.
     */
    public boolean equals(Object obj) {
        if (obj == this) return true;

        return (obj instanceof ServiceState) && ((ServiceState)obj).state == state;
    }

    /**
     * Get a hash code value for this Service state.
     * @return a hash code value.
     */
    public int hashCode() {
        return state;
    }

    /**
     * Get the textual representation of this Service state object.
     * @return the textual representation of this Service state object.
     */
    public String toString() {
        switch (state) {
            case SERVICE_INACTIVE: return INACTIVE_STRING;
            case SERVICE_ACTIVE: return ACTIVE_STRING;
            case SERVICE_STOPPING: return STOPPING_STRING;
            default: return "ServiceState in Unknown and Invalid State";
        }
    }


    /**
     * Private constructor to prevent unauthorized object creation.
     */
    private ServiceState(int state) {
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
