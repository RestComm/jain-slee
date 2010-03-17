package javax.slee.resource;

/**
 * This class defines the standard reason error codes for event processing failures reported to
 * resource adaptor objects by the {@link ResourceAdaptor#eventProcessingFailed eventProcessingFailed}
 * callback method.
 * <p>
 * A SLEE vendor may define any implementation-specific reason codes that may be useful
 * or appropriate by extending this class.  The integer representation of
 * implementation-specific event codes must be positive numbers greater than zero.  The
 * OTHER REASON reason code has the reserved value <tt>0</tt>, while negative numbers are
 * reserved for the remainder of the standard reason codes defined in this class.
 * <p>
 * A singleton instance of each enumerated value defined in this class is guaranteed (via
 * an implementation of <code>readResolve()</code> - refer
 * {@link java.io.Serializable java.io.Serializable}), so that equality tests using
 * <code>==</code> are always evaluated correctly.  (This equality test is only guaranteed
 * if this class is loaded in the application's boot class path, rather than dynamically
 * loaded at runtime.)  However, it must also be noted that for any vendor-defined reason
 * code, the {@link #isOtherReason} will return <code>true</code>, while the
 * <code>FailureReason</code> (or subclass of <code>FailureReason</code>) object representing
 * the vendor-defined reason code will <i>not</i> be equal, using the <code>==</code>
 * operator or the <code>equals</code> method, to {@link FailureReason#OTHER_REASON}.
 * @since SLEE 1.1
 */
public class FailureReason {
    /**
     * An integer representation of {@link #OTHER_REASON}.
     */
    public static final int REASON_OTHER_REASON = 0;

    /**
     * An integer representation of {@link #EVENT_QUEUE_FULL}.
     */
    public static final int REASON_EVENT_QUEUE_FULL = -1;

    /**
     * An integer representation of {@link #EVENT_QUEUE_TIMEOUT}.
     */
    public static final int REASON_EVENT_QUEUE_TIMEOUT = -2;

    /**
     * An integer representation of {@link #SYSTEM_OVERLOAD}.
     */
    public static final int REASON_SYSTEM_OVERLOAD = -3;

    /**
     * An integer representation of {@link #EVENT_MARSHALING_ERROR}.
     */
    public static final int REASON_EVENT_MARSHALING_ERROR = -4;

    /**
     * An integer representation of {@link #FIRING_TRANSACTION_ROLLED_BACK}.
     */
    public static final int REASON_FIRING_TRANSACTION_ROLLED_BACK = -5;


    /**
     * Event failure reason indicating that the event could not be processed for an other reason.
     */
    public static final FailureReason OTHER_REASON = new FailureReason(REASON_OTHER_REASON);

    /**
     * Event failure reason indicating the event could not be enqueued for processing because the
     * event queue is full.
     */
    public static final FailureReason EVENT_QUEUE_FULL = new FailureReason(REASON_EVENT_QUEUE_FULL);

    /**
     * Event failure reason indicating the event timeout period for processing of the event (if
     * supported by the SLEE) expired before the event could be processed.  Typically this means
     * the SLEE is overloaded.
     */
    public static final FailureReason EVENT_QUEUE_TIMEOUT = new FailureReason(REASON_EVENT_QUEUE_TIMEOUT);

    /**
     * Event failure reason indicating the event could not processed due to generic system
     * overload conditions.
     */
    public static final FailureReason SYSTEM_OVERLOAD = new FailureReason(REASON_SYSTEM_OVERLOAD);

    /**
     * Event failure reason indicating the event could not be marshaled by the resource adaptor
     * entity's {@link javax.slee.resource.Marshaler}.
     */
    public static final FailureReason EVENT_MARSHALING_ERROR = new FailureReason(REASON_EVENT_MARSHALING_ERROR);

    /**
     * Event failure reason indicating the transaction in which the event was fired
     * rolled back.
     */
    public static final FailureReason FIRING_TRANSACTION_ROLLED_BACK = new FailureReason(REASON_FIRING_TRANSACTION_ROLLED_BACK);
    
    /**
     * Get a <code>FailureReason</code> object from an integer value.
     * <p>
     * This method returns the singleton objects for the failure reasons defined in
     * this class.  For all vendor-defined reason codes (those greater than 0 in value),
     * a non-unique <code>FailureReason</code> object is return encapsulating that value.
     * @param reason the reason as an integer.
     * @return a <code>FailureReason</code> object corresponding to <code>reason</code>.
     * @throws IllegalArgumentException if <code>reason</code> is less that 0 and does not
     *         correspond to a SLEE-defined reason code.
     */
    public static FailureReason fromInt(int reason) throws IllegalArgumentException {
        switch (reason) {
            case REASON_OTHER_REASON: return OTHER_REASON;
            case REASON_EVENT_QUEUE_FULL: return EVENT_QUEUE_FULL;
            case REASON_EVENT_QUEUE_TIMEOUT: return EVENT_QUEUE_TIMEOUT;
            case REASON_SYSTEM_OVERLOAD: return SYSTEM_OVERLOAD;
            case REASON_EVENT_MARSHALING_ERROR: return EVENT_MARSHALING_ERROR;
            case REASON_FIRING_TRANSACTION_ROLLED_BACK: return FIRING_TRANSACTION_ROLLED_BACK;
            default:
                if (reason > 0)
                    return new FailureReason(reason);
                else
                    throw new IllegalArgumentException("Invalid failure reason: " + reason);
        }
    }

    /**
     * Get an integer value representation for this <code>FailureReason</code> object.
     * @return an integer value representation for this <code>FailureReason</code> object.
     */
    public final int toInt() {
        return reason;
    }

    /**
     * Determine if this FailureReason object represents the OTHER REASON reason,
     * or represents a vendor-defined reason.
     * <p>
     * This method returns <code>true</code> if the <code>FailureReason</code> object
     * is equal to {@link #OTHER_REASON}, or if the reason code is a vendor-defined
     * reason code, ie. is greater that 0.  This allows fallback to the generic reason
     * code if the consumer of the <code>FailureReason</code> object is not familiar with
     * vendor-defined codes.
     * @return <code>true</code> if this object represents the OTHER REASON reason,
     *       or represents a vendor-defined reason code, <code>false</code> otherwise.
     */
    public final boolean isOtherReason() {
        // REASON_OTHER_REASON == 0
        // vendor-defined codes > 0
        return reason >= 0;
    }

    /**
     * Determine if this FailureReason object represents the EVENT QUEUE FULL reason.
     * <p>
     * This method is effectively equivalent to the conditional test:
     * <code>(this == EVENT_QUEUE_FULL)</code>, ie. the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;&nbsp;if (reason.isEventQueueFull()) ...</code>
     * <p>
     * is interchangeable with the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;if (reason == FailureReason.EVENT_QUEUE_FULL) ...</code>
     * <p>
     * @return <code>true</code> if this object represents the EVENT QUEUE FULL reason,
     *       <code>false</code> otherwise.
     */
    public final boolean isEventQueueFull() {
        return reason == REASON_EVENT_QUEUE_FULL;
    }

    /**
     * Determine if this FailureReason object represents the EVENT QUEUE TIMEOUT reason.
     * <p>
     * This method is effectively equivalent to the conditional test:
     * <code>(this == EVENT_QUEUE_TIMEOUT)</code>, ie. the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;&nbsp;if (reason.isEventQueueTimeout()) ...</code>
     * <p>
     * is interchangeable with the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;if (reason == FailureReason.EVENT_QUEUE_TIMEOUT) ...</code>
     * <p>
     * @return <code>true</code> if this object represents the EVENT QUEUE TIMEOUT reason,
     *       <code>false</code> otherwise.
     */
    public final boolean isEventQueueTimeout() {
        return reason == REASON_EVENT_QUEUE_TIMEOUT;
    }

    /**
     * Determine if this FailureReason object represents the SYSTEM OVERLOAD reason.
     * <p>
     * This method is effectively equivalent to the conditional test:
     * <code>(this == SYSTEM_OVERLOAD)</code>, ie. the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;&nbsp;if (reason.isSystemOverload()) ...</code>
     * <p>
     * is interchangeable with the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;if (reason == FailureReason.SYSTEM_OVERLOAD) ...</code>
     * <p>
     * @return <code>true</code> if this object represents the SYSTEM OVERLOAD reason,
     *       <code>false</code> otherwise.
     */
    public final boolean isSystemOverLoad() {
        return reason == REASON_SYSTEM_OVERLOAD;
    }

    /**
     * Determine if this FailureReason object represents the EVENT MARSHALING ERROR reason.
     * <p>
     * This method is effectively equivalent to the conditional test:
     * <code>(this == EVENT_MARSHALING_ERROR)</code>, ie. the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;&nbsp;if (reason.isEventMarshalingError()) ...</code>
     * <p>
     * is interchangeable with the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;if (reason == FailureReason.EVENT_MARSHALING_ERROR) ...</code>
     * <p>
     * @return <code>true</code> if this object represents the EVENT MARSHALING ERROR reason,
     *       <code>false</code> otherwise.
     */
    public final boolean isEventMarshalingError() {
        return reason == REASON_EVENT_MARSHALING_ERROR;
    }

    /**
     * Determine if this FailureReason object represents the FIRING TRANSACTION ROLLED BACK reason.
     * <p>
     * This method is effectively equivalent to the conditional test:
     * <code>(this == FIRING_TRANSACTION_ROLLED_BACK)</code>, ie. the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;&nbsp;if (reason.isFiringTransactionRolledBack()) ...</code>
     * <p>
     * is interchangeable with the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;if (reason == FailureReason.FIRING_TRANSACTION_ROLLED_BACK) ...</code>
     * <p>
     * @return <code>true</code> if this object represents the FIRING TRANSACTION ROLLED BACK reason,
     *       <code>false</code> otherwise.
     */
    public final boolean isFiringTransactionRolledBack() {
        return reason == REASON_FIRING_TRANSACTION_ROLLED_BACK;
    }
    
    /**
     * Compare this failure reason for equality with another.
     * @param obj the object to compare this with.
     * @return <code>true</code> if <code>obj</code> is an instance of this class
     *        representing the same failure reason as this, <code>false</code> otherwise.
     */
    public boolean equals(Object obj) {
        if (obj == this) return true;

        return (obj instanceof FailureReason) && ((FailureReason)obj).reason == reason;
    }

    /**
     * Get a hash code value for this failure reason error code.
     * @return a hash code value.
     */
    public final int hashCode() {
        return reason;
    }

    /**
     * Get the textual representation of the FailureReason object.
     * @return the textual representation of the FailureReason object.
     */
    public String toString() {
        switch (reason) {
            case REASON_OTHER_REASON: return "Other reason";
            case REASON_EVENT_QUEUE_FULL: return "Event queue full";
            case REASON_EVENT_QUEUE_TIMEOUT: return "Event timed out while queued";
            case REASON_SYSTEM_OVERLOAD: return "System overload";
            case REASON_EVENT_MARSHALING_ERROR: return "Event marshaling error";
            case REASON_FIRING_TRANSACTION_ROLLED_BACK: return "Firing transaction rolled back";
            default: return "Vendor-defined reason: " + reason;
        }
    }


    /**
     * Protected constructor that allows subclasses to extend this class with additional
     * vendor-defined reason codes, while preventing unauthorized object creation.
     * @param reason the reason code.
     * @throws IllegalArgumentException if a vendor-defined reason code is less than or
     *        equal to 0.
     */
    protected FailureReason(int reason) {
        if (reason <= 0 && !getClass().equals(FailureReason.class))
            throw new IllegalArgumentException("Invalid vendor-defined reason code: " + reason);

        this.reason = reason;
    }

    /**
     * Get the reason code provided in the constructor.  This method allows subclasses
     * to implement a {@link #fromInt} method and override the {@link #toString} method
     * if desired.
     * @return the reason code.
     */
    protected final int getReason() {
        return reason;
    }

    /**
     * Resolve deserialisation references so that the singleton property of each
     * defined enumerated object is preserved.
     */
    private Object readResolve() {
        return fromInt(reason);
    }


    /**
     * The internal state representation of the enumerated type.
     */
    private final int reason;
}
