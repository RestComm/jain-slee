package javax.slee.management;

import java.io.Serializable;
import java.io.StreamCorruptedException;

/**
 * This class defines an enumerated type that encapsulates the operational state of the SLEE.
 * <p>
 * A singleton instance of each enumerated value is guaranteed (via an implementation
 * of <code>readResolve()</code> - refer {@link java.io.Serializable java.io.Serializable}),
 * so that equality tests using <code>==</code> are always evaluated correctly.  (This
 * equality test is only guaranteed if this class is loaded in the application's boot class
 * path, rather than dynamically loaded at runtime.)
 */
public final class SleeState implements Serializable {
    /**
     * An integer representation of the {@link #STOPPED} state.
     */
    public static final int SLEE_STOPPED = 0;

    /**
     * An integer representation of the {@link #STARTING} state.
     */
    public static final int SLEE_STARTING = 1;

    /**
     * An integer representation of the {@link #RUNNING} state.
     */
    public static final int SLEE_RUNNING = 2;

    /**
     * An integer representation of the {@link #STOPPING} state.
     */
    public static final int SLEE_STOPPING = 3;


    /**
     * A string representation of the {@link #STOPPED} state.
     */
    public static final String STOPPED_STRING = "Stopped";

    /**
     * A string representation of the {@link #STARTING} state.
     */
    public static final String STARTING_STRING = "Starting";

    /**
     * A string representation of the {@link #RUNNING} state.
     */
    public static final String RUNNING_STRING = "Running";

    /**
     * A string representation of the {@link #STOPPING} state.
     */
    public static final String STOPPING_STRING = "Stopping";


    /**
     * The STOPPED state is the initial state for the SLEE on startup.
     * When in this state, the the SLEE and resource adaptors do not generate events
     * and SLEE's event routing subsystem is idle.
     */
    public static final SleeState STOPPED = new SleeState(SLEE_STOPPED);

    /**
     * The STARTING state is a transitional state between {@link #STOPPED}
     * and {@link #RUNNING}. When in this state, the SLEE is activating relevant
     * resource adaptors and performing any other tasks necessary to start event
     * processing.  The event router is not yet started in this state.
     */
    public static final SleeState STARTING = new SleeState(SLEE_STARTING);

    /**
     * In the RUNNING state the SLEE and resource adaptors are generating events
     * and the SLEE's event routing subsystem is actively creating SBBs and delivering
     * events to them.
     */
    public static final SleeState RUNNING = new SleeState(SLEE_RUNNING);

    /**
     * The STOPPING state is a transitional state between {@link #STARTING}
     * or {@link #RUNNING} and {@link #STOPPED}.  When in this state any remaining
     * activities are allowed to complete without new activities being created.
     */
    public static final SleeState STOPPING = new SleeState(SLEE_STOPPING);


    /**
     * Get a <code>SleeState</code> object from an integer value.
     * @param state the state as an integer.
     * @return a <code>SleeState</code> object corresponding to <code>state</code>.
     * @throws IllegalArgumentException if <code>state</code> is not a valid SLEE
     *        state value.
     */
    public static SleeState fromInt(int state) throws IllegalArgumentException {
        switch (state) {
            case SLEE_STOPPED: return STOPPED;
            case SLEE_STARTING: return STARTING;
            case SLEE_RUNNING: return RUNNING;
            case SLEE_STOPPING: return STOPPING;
            default: throw new IllegalArgumentException("Invalid state: " + state);
        }
    }

    /**
     * Get a <code>SleeState</code> object from a string value.
     * @param state the state as a string, for example as returned by the {@link #toString()}
     *        method (case insensitive).
     * @return a <code>SleeState</code> object corresponding to <code>state</code>.
     * @throws NullPointerException if <code>state</code> is <code>null</code>.
     * @throws IllegalArgumentException if <code>state</code> is not a valid SLEE
     *        state string.
     * @since SLEE 1.1
     */
    public static SleeState fromString(String state) throws NullPointerException, IllegalArgumentException {
        if (state == null) throw new NullPointerException("state is null");
        if (state.equalsIgnoreCase(STOPPED_STRING)) return STOPPED;
        if (state.equalsIgnoreCase(STARTING_STRING)) return STARTING;
        if (state.equalsIgnoreCase(RUNNING_STRING)) return RUNNING;
        if (state.equalsIgnoreCase(STOPPING_STRING)) return STOPPING;
        throw new IllegalArgumentException("Invalid state: " + state);
    }

    /**
     * Get an integer value representation for this <code>SleeState</code> object.
     * @return an integer value representation for this <code>SleeState</code> object.
     */
    public int toInt() {
        return state;
    }

    /**
     * Determine if this SleeState object represents the STOPPED state of the SLEE.
     * <p>
     * This method is effectively equivalent to the conditional test:
     * <code>(this == STOPPED)</code>, ie. the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;&nbsp;if (state.isStopped()) ...</code>
     * <p>
     * is interchangeable with the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;if (state == SleeState.STOPPED) ...</code>
     * <p>
     * @return <code>true</code> if this object represents the STOPPED state of the SLEE,
     *       <code>false</code> otherwise.
     */
    public boolean isStopped() {
        return state == SLEE_STOPPED;
    }

    /**
     * Determine if this SleeState object represents the STARTING state of the SLEE.
     * <p>
     * This method is effectively equivalent to the conditional test:
     * <code>(this == STARTING)</code>, ie. the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;&nbsp;if (state.isStarting()) ...</code>
     * <p>
     * is interchangeable with the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;if (state == SleeState.STARTING) ...</code>
     * <p>
     * @return <code>true</code> if this object represents the STARTING state of the SLEE,
     *       <code>false</code> otherwise.
     */
    public boolean isStarting() {
        return state == SLEE_STARTING;
    }

    /**
     * Determine if this SleeState object represents the RUNNING state of the SLEE.
     * <p>
     * This method is effectively equivalent to the conditional test:
     * <code>(this == RUNNING)</code>, ie. the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;&nbsp;if (state.isRunning()) ...</code>
     * <p>
     * is interchangeable with the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;if (state == SleeState.RUNNING) ...</code>
     * <p>
     * @return <code>true</code> if this object represents the RUNNING state of the SLEE,
     *       <code>false</code> otherwise.
     */
    public boolean isRunning() {
        return state == SLEE_RUNNING;
    }

    /**
     * Determine if this SleeState object represents the STOPPING state of the SLEE.
     * <p>
     * This method is effectively equivalent to the conditional test:
     * <code>(this == STOPPING)</code>, ie. the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;&nbsp;if (state.isStopping()) ...</code>
     * <p>
     * is interchangeable with the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;if (state == SleeState.STOPPING) ...</code>
     * <p>
     * @return <code>true</code> if this object represents the STOPPING state of the SLEE,
     *       <code>false</code> otherwise.
     */
    public boolean isStopping() {
        return state == SLEE_STOPPING;
    }

    /**
     * Compare this SLEE state for equality with another.
     * @param obj the object to compare this with.
     * @return <code>true</code> if <code>obj</code> is an instance of this class
     *        representing the same SLEE state as this, <code>false</code> otherwise.
     */
    public boolean equals(Object obj) {
        if (obj == this) return true;

        return (obj instanceof SleeState) && ((SleeState)obj).state == state;
    }

    /**
     * Get a hash code value for this SLEE state.
     * @return a hash code value.
     */
    public int hashCode() {
        return state;
    }

    /**
     * Get the textual representation of the SLEE state object.
     * @return the textual representation of the SLEE state object.
     */
    public String toString() {
        switch (state) {
            case SLEE_STOPPED: return STOPPED_STRING;
            case SLEE_STARTING: return STARTING_STRING;
            case SLEE_RUNNING: return RUNNING_STRING;
            case SLEE_STOPPING: return STOPPING_STRING;
            default: return "SleeState in Unknown and Invalid State";
        }
    }


    /**
     * Private constructor to prevent unauthorized object creation.
     */
    private SleeState(int state) {
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


    private final int state;
}
