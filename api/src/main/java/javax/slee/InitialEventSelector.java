package javax.slee;

/**
 * This interface defines selection criteria for initial events for Services.  The SLEE
 * provides an implementation of this interface to an initial event selector
 * method defined in an SBB abstract class.
 *
 * @see Sbb
 */
public interface InitialEventSelector {
    /**
     * Determine if the Activity Context has been selected for use in the convergence name.
     * @return <code>true</code> if the Activity Context has been selected for use
     *        in the convergence name, <code>false</code> otherwise.
     */
    public boolean isActivityContextSelected();

    /**
     * Set the selection criteria for the Activity Context convergence name variable.
     * @param select flag indicating whether or not the Activity Context should be
     *        used in the convergence name.
     */
    public void setActivityContextSelected(boolean select);

    /**
     * Determine if the Address Profile has been selected for use in the convergence name.
     * <p>
     * <i>Note:</i> an Address Profile will only be used in the construction of a convergence
     * name if this method returns <code>true</code> and {@link #getAddress getAddress} returns
     * a non-null value.  If both of these conditions are true but no Address Profile with the
     * specified Address is found in the Service's Address Profile Table, no convergence name
     * is generated and the event is not an initial event for the Service.
     * 
     * @return <code>true</code> if the Address Profile has been selected for use
     *        in the convergence name, <code>false</code> otherwise.
     */
    public boolean isAddressProfileSelected();

    /**
     * Set the selection criteria for the Address Profile convergence name variable.
     * @param select flag indicating whether or not the Address Profile should be
     *        used in the convergence name.
     */
    public void setAddressProfileSelected(boolean select);

    /**
     * Determine if the Address has been selected for use in the convergence name.
     * <p>
     * <i>Note:</i> Addresses will only be used during the construction of a convergence name
     * if this method returns <code>true</code> and {@link #getAddress getAddress} returns a
     * non-null value.
     * @return <code>true</code> if the Address has been selected for use
     *        in the convergence name, <code>false</code> otherwise.
     */
    public boolean isAddressSelected();

    /**
     * Set the selection criteria for the Address convergence name variable.
     * @param select flag indicating whether or not the Address should be used in the
     *        convergence name.
     */
    public void setAddressSelected(boolean select);

    /**
     * Determine if the event type has been selected for use in the convergence name.
     * @return <code>true</code> if the event type has been selected for use
     *        in the convergence name, <code>false</code> otherwise.
     */
    public boolean isEventTypeSelected();

    /**
     * Set the selection criteria for the event type convergence name variable.
     * @param select flag indicating whether or not the event type should be
     *        used in the convergence name.
     */
    public void setEventTypeSelected(boolean select);

    /**
     * Determine if the event has been selected for use in the convergence name.
     * @return <code>true</code> if the event has been selected for use
     *        in the convergence name, <code>false</code> otherwise.
     */
    public boolean isEventSelected();

    /**
     * Set the selection criteria for the event convergence name variable.
     * @param select flag indicating whether or not the event should be
     *        used in the convergence name.
     */
    public void setEventSelected(boolean select);

    /**
     * Get the name of the event under consideration by the event router.  Event names are
     * defined by the SBB component in its deployment descriptor.
     * @return the name of the event.
     */
    public String getEventName();

    /**
     * Get the event object for the event under consideration by the event router.
     * @return the event object.
     */
    public Object getEvent();

    /**
     * Get the activity for which the event was fired.
     * @return the activity for which the event was fired.
     */
    public Object getActivity();

    /**
     * Get the Address for the event.  The initial value of this attribute is the
     * default Address the event was fired on.
     * @return the Address for the event.
     */
    public Address getAddress();

    /**
     * Set the Address for the event.  If the Address convergence name variable has
     * been selected for use in the convergence name, this method can be used to
     * change the event Address if the default event Address is unsuitable.
     * @param address the Address for the event.
     */
    public void setAddress(Address address);

    /**
     * Get the custom name set for the event.  The default value of the custom name is
     * <code>null</code>.  A custom name is only used in the construction of a convergence
     * name if it is non-null.
     * @return the custom name for the event.
     */
    public String getCustomName();

    /**
     * Set the custom name for the event.
     * @param name the custom name for the event,
     */
    public void setCustomName(String name);

    /**
     * Determine if the event under consideration by the event router is an initial
     * event for the Service.  The default value for this attribute is <code>true</code>.
     * @return <code>true</code> if the event under consideration is an initial event
     *        <code>false</code> otherwise.
     */
    public boolean isInitialEvent();

    /**
     * Set the initial event flag for the event under consideration by the event router.
     * If set to <code>false</code> the event is not an initial event and the event
     * router performs no further initial event processing for the Service.
     * @param initial whether or not further initial event processing should take place
     *        for this event.
     */
    public void setInitialEvent(boolean initial);

}

