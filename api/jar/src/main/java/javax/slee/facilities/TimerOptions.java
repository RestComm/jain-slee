package javax.slee.facilities;

import java.io.Serializable;

/**
 * The <code>TimerOptions</code> class specifies the behavior of a timer when it is set.
 * <p>
 * <b>Timeout</b> -
 * Under normal conditions and expected workload, a SLEE and its Timer Facility should be
 * able to fire timer events on time. However, under overload conditions, or when the SLEE
 * is not running, the Timer Facility may not be able to do so.  The timeout value allows
 * some flexibility in deciding when a scheduled timer becomes late.  A timer is considered
 * late if the Timer Facility cannot fire a scheduled timer event by its scheduled expiry
 * time + timer timeout.  For a periodic timer, the timeout must be less than the timer's
 * period.
 * <p>
 * <b>Preserving Missed Timer Events</b> -
 * The default timer options cause the Timer Facility to only generate the last event
 * in a sequence of late timer events for a timer that fires late.  This guarantees at
 * least one timer event (the last, for a periodic timer) will be generated for a timer.
 * However an SBB can still choose to receive all or none of the late timer events if desired.
 * <p>
 * <b>Persistent Timers</b> -
 * <i>Deprecated in SLEE 1.1</i> - This option has been deprecated.  The semantics of
 * persistent timers were unclear as the definition of persistent implied by this option
 * was inconsistent with the general use of the term "persistent" in the rest of the
 * specification.
 */
public class TimerOptions implements Serializable {
    /**
     * Create a <code>TimerOptions</code> object that describes the default timer behavior.
     * The default timer behavior is:
     * <ul>
     *   <li>the timeout is the SLEE's default timeout period.
     *   <li>only the last in a sequence of late timer events is preserved.
     * </ul>
     * The deprecated persistent option is set to <code>false</code> by this constructor.
     */
    public TimerOptions() {
        this(0, TimerPreserveMissed.LAST);
    }
 
    /**
     * Create a <code>TimerOptions</code> object that has the same behavioral properties as
     * another <code>TimerOptions</code>.
     * @param options the <code>TimerOptions</code> object to copy.
     */
    public TimerOptions(TimerOptions options) {
        this(options.persistent, options.timeout, options.preserveMissed);
    }

    /**
     * Create a <code>TimerOptions</code> object for the specified timer behavior.
     * @param persistent boolean value indicating whether the timer should be persistent or not.
     * @param timeout the timeout period for the timer (specified in milliseconds).
     * @param preserveMissed the behavior of late timers.
     * @throws NullPointerException if <code>preserveMissed</code> is <code>null</code>.
     * @throws IllegalArgumentException if <code>timeout</code> is less than zero.
     * @deprecated This constructor specifies the value of the persistent option, which
     *        has been deprecated.  It has been replaced with {@link #TimerOptions(long, TimerPreserveMissed)}.
     */
    public TimerOptions(boolean persistent, long timeout, TimerPreserveMissed preserveMissed) throws NullPointerException, IllegalArgumentException {
        setPersistent(persistent);
        setTimeout(timeout);
        setPreserveMissed(preserveMissed);
    }

    /**
     * Create a <code>TimerOptions</code> object for the specified timer behavior.
     * The deprecated persistent option is set to <code>false</code> by this constructor.
     * @param timeout the timeout period for the timer (specified in milliseconds).
     * @param preserveMissed the behavior of late timers.
     * @throws NullPointerException if <code>preserveMissed</code> is <code>null</code>.
     * @throws IllegalArgumentException if <code>timeout</code> is less than zero.
     * @since SLEE 1.1
     */
    public TimerOptions(long timeout, TimerPreserveMissed preserveMissed) throws NullPointerException, IllegalArgumentException {
        setPersistent(false);
        setTimeout(timeout);
        setPreserveMissed(preserveMissed);
    }

    /**
     * Test if the persistent timer flag is set.
     * @return <code>true</code> if the timer should be persistent, <code>false</code> otherwise.
     * @deprecated the persistent option has been deprecated.
     */
    public final boolean isPersistent() {
        return persistent;
    }

    /**
     * Set the persistent timer flag.
     * @param persistent boolean value indicating whether the timer should be persistent or not.
     * @deprecated the persistent option has been deprecated.
     */
    public final void setPersistent(boolean persistent) {
        this.persistent = persistent;
    }

    /**
     * Get the timeout period.
     * @return the timeout period.
     */
    public final long getTimeout() {
        return timeout;
    }

    /**
     * Set the timeout period.
     * @param timeout the timeout period (specified in milliseconds).
     * @throws IllegalArgumentException if <code>timeout</code> less than zero.
     */
    public final void setTimeout(long timeout) throws IllegalArgumentException {
        if (timeout < 0) throw new IllegalArgumentException("timeout cannot be less than zero");

        this.timeout = timeout;
    }

    /**
     * Get the value of the preserve-missed option.
     * @return the value of the preserve-missed option.
     */
    public final TimerPreserveMissed getPreserveMissed() {
        return preserveMissed;
    }

    /**
     * Set the value of the preserve missed option.
     * @param preserveMissed the behavior of late timers.
     * @throws NullPointerException if <code>preserveMissed</code> is <code>null</code>.
     */
    public final void setPreserveMissed(TimerPreserveMissed preserveMissed) throws NullPointerException {
        if (preserveMissed == null) throw new NullPointerException("preserveMissed is null");

        this.preserveMissed = preserveMissed;
    }

    /**
     * Compare this timer options object for equality with another.
     * @param obj the object to compare this with.
     * @return <code>true</code> if <code>obj</code> is an instance of this class
     *        representing the same timer options as this, <code>false</code> otherwise.
     */
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof TimerOptions)) return false;

        TimerOptions that = (TimerOptions)obj;
        return (this.persistent == that.persistent)
            && (this.timeout == that.timeout)
            && (this.preserveMissed.equals(that.preserveMissed));
    }

    /**
     * Get a hash code value for this timer options object.
     * @return a hash code value.
     */
    public int hashCode() {
        return (int)timeout | (persistent ? 0x1000 : 0) | (preserveMissed.hashCode() << 20);
    }

    /**
     * Get the textual representation of the timer options object.
     * @return the textual representation of the timer options object.
     */
    public String toString() {
        return "TimerOptions[persistent=" + persistent + ",timeout=" + timeout + ",preserve missed=" + preserveMissed + "]";
    }


    private boolean persistent;
    private long timeout;
    private TimerPreserveMissed preserveMissed;
}

