package javax.slee.resource;

/**
 * This class defines flags that a Resource Adaptor can use when firing an event
 * to the SLEE.  These flags enable additional contracts between the SLEE and the
 * Resource Adaptor relating to the event to be used.
 * <p>
 * A Resource Adaptor specifies event flags when submitting an event using the
 * {@link SleeEndpoint#fireEvent(ActivityHandle, FireableEventType, Object, javax.slee.Address, ReceivableService, int)},
 * {@link SleeEndpoint#fireEventTransacted(ActivityHandle, FireableEventType, Object, javax.slee.Address, ReceivableService, int)}
 * methods.
 * @since SLEE 1.1
 */
public final class EventFlags {
    /**
     * Requests no additional behavior from the SLEE.
     */
    public static final int NO_FLAGS = 0x00000000;
    /**
     * Indicate to the SLEE that the SLEE may marshal and unmarshal the event if required
     * to do so.  If this flag is specified the Resource Adaptor must provide an
     * implementation of the {@link Marshaler} interface that is capable of marshaling
     * events.
     * @see Marshaler#marshalEvent
     * @see Marshaler#unmarshalEvent
     */
    public static final int SLEE_MAY_MARSHAL = 0x00000010;

    /**
     * Request an {@link ResourceAdaptor#eventProcessingSuccessful} callback from the
     * SLEE if the event is successfully processed.
     */
    public static final int REQUEST_PROCESSING_SUCCESSFUL_CALLBACK = 0x00000020;

    /**
     * Request an {@link ResourceAdaptor#eventProcessingFailed} callback from the SLEE
     * if the SLEE is unable to successfully process the event.
     */
    public static final int REQUEST_PROCESSING_FAILED_CALLBACK = 0x00000040;

    /**
     * Request an {@link ResourceAdaptor#eventUnreferenced} callback from the SLEE when
     * the SLEE no longer holds a reference to the event.
     */
    public static final int REQUEST_EVENT_UNREFERENCED_CALLBACK = 0x00000080;

    /**
     * This flag is set by the SLEE if an SBB event handler method was invoked for the
     * event.  A Resource Adaptor should not set this flag but should instead test for
     * its presence in an event processing callback method.
     */
    public static final int SBB_PROCESSED_EVENT = 0x00000100;
    
    /**
     * This bit-mask specifies the event flag bits reserved for current and future use
     * by the JAIN SLEE specification.  Bits 0-23 are reserved for use by the SLEE
     * specification.
     */
    public static final int STANDARD_FLAGS_MASK = 0x00ffffff;

    /**
     * This mask specifies the event flag bits reserved for SLEE vendors to represent
     * optional proprietary behavior.  Bits 24-31 are reserved for use by SLEE vendors.
     */
    public static final int VENDOR_FLAGS_MASK = 0xff000000;

    /**
     * Determine whether the specified event flags request no special behavior.
     * @param flags integer representation of a set of event flags to test.
     * @return <code>true</code> if the flags define no special behavior,
     *        <code>false</code> otherwise.
     */
    public static boolean hasNoFlags(int flags) {
        return flags == NO_FLAGS;
    }

    /**
     * Determine whether the specified flags request any of the SLEE specification
     * defined special behavior.
     * @param flags integer representation of a set of event flags to test.
     * @return <code>true</code> if the flags define any SLEE defined special behavior,
     *        <code>false</code> otherwise.
     */
    public static boolean hasStandardFlags(int flags) {
        return (flags & STANDARD_FLAGS_MASK) != NO_FLAGS;
    }

    /**
     * Determine whether the specified flags request any vendor specific behavior.
     *
     * @param flags integer representation of a set of event flags to test.
     * @return <code>true</code> if the flags define any vendor defined special behavior,
     *        <code>false</code> otherwise.
     */
    public static boolean hasVendorFlags(int flags) {
        return (flags & VENDOR_FLAGS_MASK) != NO_FLAGS;
    }

    /**
     * Test an integer for the presence of a flag.
     * @param flags integer representation of a set of event flags to test.
     * @param flagsToTestFor the flags to test for.  This may include flags defined
     *        by this class and/or vendor specific flags.
     * @return <code>true</code> if the specified flags are set, <code>false</code>
     *        otherwise.
     */
    public static boolean hasFlags(int flags, int flagsToTestFor) {
        return (flags & flagsToTestFor) != NO_FLAGS;
    }

    /**
     * Test for the presence of the {@link #SLEE_MAY_MARSHAL} flag.
     * @param flags integer representation of a set of event flags to test.
     * @return <code>true</code> if the flags include the <code>SLEE_MAY_MARSHAL</code>
     *        flag, <code>false</code> otherwise.
     */
    public static boolean hasSleeMayMarshal(int flags) {
        return hasFlags(flags, SLEE_MAY_MARSHAL);
    }

    /**
     * Test for the presence of the {@link #REQUEST_PROCESSING_SUCCESSFUL_CALLBACK} flag.
     * @param flags integer representation of a set of event flags to test.
     * @return <code>true</code> if the flags include the
     *        <code>REQUEST_PROCESSING_SUCCESSFUL_CALLBACK</code> flag, <code>false</code>
     *        otherwise.
     */
    public static boolean hasRequestProcessingSuccessfulCallback(int flags) {
        return hasFlags(flags, REQUEST_PROCESSING_SUCCESSFUL_CALLBACK);
    }

    /**
     * Test for the presence of the {@link #REQUEST_PROCESSING_FAILED_CALLBACK} flag.
     * @param flags integer representation of a set of event flags to test.
     * @return <code>true</code> if the flags include the
     *        <code>REQUEST_PROCESSING_FAILED_CALLBACK</code> flag, <code>false</code>
     *        otherwise.
     */
    public static boolean hasRequestProcessingFailedCallback(int flags) {
        return hasFlags(flags, REQUEST_PROCESSING_FAILED_CALLBACK);
    }

    /**
     * Test for the presence of the {@link #REQUEST_EVENT_UNREFERENCED_CALLBACK} flag.
     * @param flags integer representation of a set of event flags to test.
     * @return <code>true</code> if the flags include the
     *        <code>REQUEST_EVENT_UNREFERENCED_CALLBACK</code> flag, <code>false</code>
     *        otherwise.
     */
    public static boolean hasRequestEventReferenceReleasedCallback(int flags) {
        return hasFlags(flags, REQUEST_EVENT_UNREFERENCED_CALLBACK);
    }

    /**
     * Test for the presence of the {@link #SBB_PROCESSED_EVENT} flag.
     * @param flags integer representation of a set of event flags to test.
     * @return <code>true</code> if the flags include the <code>SBB_PROCESSED_EVENT</code>
     *        flag, <code>false</code> otherwise.
     */
    
    public static boolean hasSbbProcessedEvent(int flags) {
        return hasFlags(flags, SBB_PROCESSED_EVENT);
    }
    
    /**
     * Add the {@link #SLEE_MAY_MARSHAL} flag to an existing integer representation of
     * event flags.  The new flag is bitwise OR'ed onto the existing flags.  This method
     * has no effect if the <code>SLEE_MAY_MARSHAL</code> flag is already present in the
     * existing flags.
     * @param currentFlags the existing integer representation of event flags.
     * @return an integer equivalent to the input event flags with the
     *        <code>SLEE_MAY_MARSHAL</code> flag set.
     */
    public static int setSleeMayMarshal(int currentFlags) {
        return currentFlags | SLEE_MAY_MARSHAL;
    }

    /**
     * Add the {@link #REQUEST_PROCESSING_SUCCESSFUL_CALLBACK} flag to an existing
     * integer representation of event flags.  The new flag is bitwise OR'ed onto the
     * existing flags.  This method has no effect if the <code>REQUEST_PROCESSING_SUCCESSFUL_CALLBACK</code>
     * flag is already present in the existing flags.
     * @param currentFlags the existing integer representation of event flags.
     * @return an integer equivalent to the input event flags with the
     *        <code>REQUEST_PROCESSING_SUCCESSFUL_CALLBACK</code> flag set.
     */
    public static int setRequestProcessingSuccessfulCallback(int currentFlags) {
        return currentFlags | REQUEST_PROCESSING_SUCCESSFUL_CALLBACK;
    }

    /**
     * Add the {@link #REQUEST_PROCESSING_FAILED_CALLBACK} flag to an existing integer
     * representation of event flags.  The new flag is bitwise OR'ed onto the existing flags.
     * This method has no effect if the <code>REQUEST_PROCESSING_FAILED_CALLBACK</code> flag
     * is already present in the existing flags.
     * @param currentFlags the existing integer representation of event flags.
     * @return an integer equivalent to the input event flags with the
     *        <code>REQUEST_PROCESSING_FAILED_CALLBACK</code> flag set.
     */
    public static int setRequestProcessingFailedCallback(int currentFlags) {
        return currentFlags | REQUEST_PROCESSING_FAILED_CALLBACK;
    }

    /**
     * Add the {@link #REQUEST_EVENT_UNREFERENCED_CALLBACK} flag to an existing integer
     * representation of event flags.  The new flag is bitwise OR'ed onto the existing
     * flags.  This method has no effect if the <code>REQUEST_EVENT_UNREFERENCED_CALLBACK</code>
     * flag is already present in the existing flags.
     * @param currentFlags the existing integer representation of event flags.
     * @return an integer equivalent to the input event flags with the
     *        <code>REQUEST_EVENT_UNREFERENCED_CALLBACK</code> flag set.
     */
    public static int setRequestEventReferenceReleasedCallback(int currentFlags) {
        return currentFlags | REQUEST_EVENT_UNREFERENCED_CALLBACK;
    }

    /**
     * Add the {@link #SBB_PROCESSED_EVENT} flag to an existing integer representation
     * of event flags.  The new flag is bitwise OR'ed onto the existing flags.
     * This method has no effect if the <code>SBB_PROCESSED_EVENT</code> flag is already
     * present in the existing flags.
     * <p>
     * Note that this method should not be invoked by Resource Adaptors.  This flag is
     * used by the SLEE to report SBB event processing activity in the SLEE.
     * @param currentFlags the existing integer representation of event flags.
     * @return an integer equivalent to the input event flags with the
     *        <code>SBB_PROCESSED_EVENT</code> flag set.
     */
    public static int setSbbProcessedEvent(int currentFlags) {
        return currentFlags | SBB_PROCESSED_EVENT;
    }
    
    /**
     * Get a string representation of an integer containing event flags.
     * @param flags integer representation of a set of event flags.
     * @return a string representation containing all the SLEE specification defined
     *        event flags and a hex representation of any vendor defined flags.
     */
    public static String toString(int flags) {
        if (hasNoFlags(flags)) return "NO_FLAGS";

        StringBuffer buf = new StringBuffer(64);

        if (hasFlags(flags, SLEE_MAY_MARSHAL))
            buf.append("SLEE_MAY_MARSHAL");

        if( hasFlags(flags, REQUEST_PROCESSING_SUCCESSFUL_CALLBACK)) {
            if (buf.length() > 0) buf.append('|');
            buf.append("REQUEST_PROCESSING_SUCCESSFUL_CALLBACK");
        }

        if( hasFlags(flags, REQUEST_PROCESSING_FAILED_CALLBACK)) {
            if (buf.length() > 0) buf.append('|');
            buf.append("REQUEST_PROCESSING_FAILED_CALLBACK");
        }

        if( hasFlags(flags, REQUEST_EVENT_UNREFERENCED_CALLBACK)) {
            if (buf.length() > 0) buf.append('|');
            buf.append("REQUEST_EVENT_UNREFERENCED_CALLBACK");
        }

        if( hasFlags(flags, SBB_PROCESSED_EVENT)) {
            if (buf.length() > 0) buf.append('|');
            buf.append("SBB_PROCESSED_EVENT");
        }
        
        if (hasFlags(flags, VENDOR_FLAGS_MASK)) {
            if (buf.length() > 0) buf.append('|');
            buf.append("VENDOR_FLAGS == 0x").append(Integer.toHexString(flags & VENDOR_FLAGS_MASK));
        }

        return buf.toString();
    }


    /**
     * Private constructor to prevent object creation.
     */
    private EventFlags() {}
}
