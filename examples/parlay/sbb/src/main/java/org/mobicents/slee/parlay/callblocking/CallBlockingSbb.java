package org.mobicents.slee.parlay.callblocking;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.resource.ResourceException;
import javax.slee.ActivityContextInterface;
import javax.slee.Address;
import javax.slee.AddressPlan;
import javax.slee.CreateException;
import javax.slee.RolledBackContext;
import javax.slee.Sbb;
import javax.slee.SbbContext;
import javax.slee.UnrecognizedComponentException;
import javax.slee.facilities.Level;
import javax.slee.facilities.TraceFacility;
import javax.slee.profile.AttributeNotIndexedException;
import javax.slee.profile.AttributeTypeMismatchException;
import javax.slee.profile.ProfileFacility;
import javax.slee.profile.ProfileID;
import javax.slee.profile.UnrecognizedAttributeException;
import javax.slee.profile.UnrecognizedProfileNameException;
import javax.slee.profile.UnrecognizedProfileTableNameException;
import javax.slee.serviceactivity.ServiceStartedEvent;

import org.csapi.TpAddress;
import org.csapi.TpAddressPlan;
import org.csapi.TpAddressRange;
import org.csapi.cc.TpAdditionalCallEventCriteria;
import org.csapi.cc.TpCallEventRequest;
import org.csapi.cc.TpCallEventType;
import org.csapi.cc.TpCallMonitorMode;
import org.csapi.cc.TpCallNotificationRequest;
import org.csapi.cc.TpCallNotificationScope;
import org.csapi.cc.TpReleaseCause;
import org.mobicents.csapi.jr.slee.ParlayConnection;
import org.mobicents.csapi.jr.slee.TpServiceIdentifier;
import org.mobicents.csapi.jr.slee.cc.mpccs.IpMultiPartyCallConnection;
import org.mobicents.csapi.jr.slee.cc.mpccs.IpMultiPartyCallControlManagerConnection;
import org.mobicents.csapi.jr.slee.cc.mpccs.ReportNotificationEvent;
import org.mobicents.csapi.jr.slee.cc.mpccs.TpMultiPartyCallIdentifier;
import org.mobicents.slee.resource.parlay.ParlayResourceAdapterSbbInterface;

/**
 * Implementation of a simple call blocking sbb. The address profile table for
 * the sbb maintains a list of blocked addresses. Each profile in the address
 * profile table has a list of blocked address associated with it. Incoming
 * calls from a blocked address are blocked by simply releasing the connection.
 */
public abstract class CallBlockingSbb implements Sbb {

    private static final String PROFILE_JNDI_NAME = "profile";

    private static final String RA_JNDI_NAME = "slee/resources/parlay/4.2/resourceAdapterSbbInterface";

    private static final String TRACE_JNDI_NAME = "trace";

    private static final String FACILITIES_JNDI_LOCATION = "java:comp/env/slee/facilities";

    private SbbContext context;

    private TraceFacility traceFacility;

    private ProfileFacility profileFacility;

    private ParlayResourceAdapterSbbInterface resourceAdapterSbbInterface;

    private static final TpAddressRange ALL_E164_RANGE = new TpAddressRange(
            TpAddressPlan.P_ADDRESS_PLAN_E164, "*", "", "");

    public void setSbbContext(SbbContext context) {
        this.context = context;
    }

    public void unsetSbbContext() {
        context = null;
    }

    public void sbbCreate() throws CreateException {
        try {
            Context facilities = (Context) new InitialContext()
                    .lookup(FACILITIES_JNDI_LOCATION);

            traceFacility = (TraceFacility) facilities.lookup(TRACE_JNDI_NAME);
            profileFacility = (ProfileFacility) facilities
                    .lookup(PROFILE_JNDI_NAME);
        }
        catch (NamingException ne) {
            throw new CreateException("no trace facility", ne);
        }
    }

    public void sbbPostCreate() {
    }

    public void sbbActivate() {
    }

    public void sbbPassivate() {
    }

    public void sbbLoad() {
    }

    public void sbbStore() {
    }

    public void sbbRemove() {
    }

    public void sbbExceptionThrown(Exception exception, Object event,
            ActivityContextInterface aci) {
    }

    public void sbbRolledBack(RolledBackContext context) {
    }

    /**
     * Event handler for ReportNotification. Handles call interrupts on
     * originating call attempts.
     * 
     * @param event
     * @param aci
     */
    public void onReportNotificationEvent(ReportNotificationEvent event,
            ActivityContextInterface aci) {

        // Call connection handle
        IpMultiPartyCallConnection callConnection = null;
        try {

            // Get a call connection
            callConnection = getIpMultiPartyCallConnection(event.getService(),
                    event.getCallReference());

            // Get call originator and destination from the event
            TpAddress originatingAddress = event.getNotificationInfo().CallNotificationReportScope.OriginatingAddress;
            TpAddress destinationAddress = event.getNotificationInfo().CallNotificationReportScope.DestinationAddress;

            // check that we have an originating call attempt
            if (event.getNotificationInfo().CallEventInfo.CallEventType.value() != TpCallEventType._P_CALL_EVENT_ADDRESS_COLLECTED) {
                trace(Level.FINE, "not an incoming call on address: "
                        + destinationAddress.AddrString + " - ignoring event");
                return;
            }
            else {
                trace(Level.FINE, "incoming call on address: " + destinationAddress.AddrString);
            }

            // determine if the call should be blocked
            boolean isBlocked = isAddressBlocked(originatingAddress,
             destinationAddress);
            //boolean isBlocked = true;

            if (isBlocked) {
                // block the call
                trace(Level.INFO, "Blocking incoming call from address "
                        + originatingAddress.AddrString + " to address "
                        + destinationAddress.AddrString);

                // Release ends the call on the network and in the Parlay
                // Gateway.
                callConnection.release(TpReleaseCause.P_USER_NOT_AVAILABLE);
            }
            else {
                trace(Level.FINE, "address " + originatingAddress.AddrString
                        + " not selected for blocking");
                // deassign implicitly continues the call and ends it on the
                // Parlay gateway
                callConnection.deassignCall();
                return;
            }
        }
        catch (Exception e) {
            trace(Level.WARNING, "ERROR: Exception caught processing event", e);
        }
        finally {
            try {
                // close connection handles
                if (callConnection != null) {
                    callConnection.closeConnection();
                }
            }
            catch (Exception e) {
                trace(Level.WARNING,
                        "ERROR: Exception caught in closeConnection()", e);
            }

            // there are no more events on this activity we care about, so
            // detach
            aci.detach(context.getSbbLocalObject());
        }
    }

    /**
     * Sets up a call interrupt for all e164 numbers on receipt of the service
     * started event.
     * 
     * @param event
     * @param aci
     */
    public void onServiceStartedEvent(ServiceStartedEvent event,
            ActivityContextInterface aci) {

        try {
            Context ctx = (Context) new InitialContext()
                    .lookup("java:comp/env");
            // get the reference to the ParlayResourceAdapterSbb class which
            // implements ParlayResourceAdapterSbbInterface
            resourceAdapterSbbInterface = (ParlayResourceAdapterSbbInterface) ctx
                    .lookup(RA_JNDI_NAME);

            setupCallInterrupt();
        }
        catch (NamingException ne) {
            trace(Level.WARNING,
                    "ERROR: Exception caught processing setSbbContext", ne);
        }

    }


    /**
     * Invokes createNotification on the RA to create a call interrupt. Uses *,*
     * address ranges on E164 numbers. Ideally this would be service
     * configurable.
     */
    private void setupCallInterrupt() {

        ParlayConnection connection = null;
        try {
            // TODO properties util
            connection = resourceAdapterSbbInterface.getParlayConnection();
            TpServiceIdentifier serviceIdentifier = connection.getService(
                    "P_MULTI_PARTY_CALL_CONTROL", new Properties());

            IpMultiPartyCallControlManagerConnection callControlManagerConnection = (IpMultiPartyCallControlManagerConnection) connection
                    .getIpServiceConnection(serviceIdentifier);

            int assignmentID = callControlManagerConnection
                    .createNotification(createControlCallsToNotificationRequest(
                            ALL_E164_RANGE, ALL_E164_RANGE));

            // could persist assignmentID here

            callControlManagerConnection.closeConnection();

        }
        catch (Exception e) {
            trace(Level.WARNING, "ERROR: Exception creating interrupt", e);
        }
        finally {
            if (connection != null) {
                try {
                    connection.close();
                }
                catch (ResourceException e) {
                    trace(Level.WARNING, "ERROR: Exception closing connection",
                            e);
                }
            }
        }
        trace(Level.INFO, "Interrupt successfully created.");
    }
    
    /**
     * Creates an interrupt on calls from the specified range to the specified range.
     * Interrupts are on the destination leg.
     * 
     * @param destinationAddressRange
     * @param originatingAddressRange
     * @return
     */
    public static TpCallNotificationRequest createControlCallsToNotificationRequest(
            TpAddressRange destinationAddressRange,
            TpAddressRange originatingAddressRange) {

        TpCallNotificationScope callNotificationScope = new TpCallNotificationScope(
                destinationAddressRange, originatingAddressRange);

        TpCallNotificationRequest callNotificationRequest = new TpCallNotificationRequest(
                callNotificationScope,
                createCallEventRequestArrayToNumber(TpCallMonitorMode.P_CALL_MONITOR_MODE_INTERRUPT));

        return callNotificationRequest;
    }
    
    /**
     * Creates the event request array for a terminating leg interrupt.
     * 
     * @param callMonitorMode
     * @return
     */
    public static TpCallEventRequest[] createCallEventRequestArrayToNumber(
            TpCallMonitorMode callMonitorMode) {

        // ANALYSED AND COLLECTED are the same event
        TpCallEventRequest[] callEventRequests = new TpCallEventRequest[] {
                //createCallEventRequest_CALL_EVENT_ADDRESS_ANALYSED(callMonitorMode),
                createCallEventRequest_CALL_EVENT_ADDRESS_COLLECTED(callMonitorMode) };

        return callEventRequests;
    }
    
    /**
     * Creates an event type for P_CALL_EVENT_ADDRESS_COLLECTED.
     * 
     * @param callMonitorMode
     * @return
     */
    private static TpCallEventRequest createCallEventRequest_CALL_EVENT_ADDRESS_COLLECTED(
            TpCallMonitorMode callMonitorMode) {

        TpAdditionalCallEventCriteria pAdditionalCallEventCriteria = new TpAdditionalCallEventCriteria();
        pAdditionalCallEventCriteria
                .Dummy(Short.MIN_VALUE);

        TpCallEventRequest request = new TpCallEventRequest(
                TpCallEventType.P_CALL_EVENT_ADDRESS_COLLECTED,
                pAdditionalCallEventCriteria, callMonitorMode);

        return request;
    }

    /**
     * Uses profile table to check whether specified call has been blocked.
     * 
     * @param originatingAddress
     * @param destinationAddress
     * @return @throws
     *         UnrecognizedProfileTableNameException
     * @throws UnrecognizedAttributeException
     * @throws AttributeNotIndexedException
     * @throws AttributeTypeMismatchException
     * @throws UnrecognizedProfileNameException
     */
    private boolean isAddressBlocked(TpAddress originatingAddress,
            TpAddress destinationAddress)
            throws UnrecognizedProfileTableNameException,
            UnrecognizedAttributeException, AttributeNotIndexedException,
            AttributeTypeMismatchException, UnrecognizedProfileNameException {
        boolean isBlocked = false;

        // find address profile for destination address
        ProfileID id = profileFacility.getProfileByIndexedAttribute(
                "CallBlockingProfiles", "addresses", new Address(
                        AddressPlan.E164, destinationAddress.AddrString));
        CallBlockingAddressProfileCMP profile = getProfile(id);

        // get blocked addresses from profile
        Address[] blocked = profile.getBlockedAddresses();
        if (blocked == null) {
            trace(Level.FINE, "no blocked addresses set in profile");
            isBlocked = false;
        }

        // Is originating address in blocked list
        for (int i = 0; i < blocked.length && !isBlocked; i++) {
            isBlocked = (blocked[i] != null && blocked[i].getAddressString()
                    .equals(originatingAddress.AddrString));
        }

        return isBlocked;
    }

    // implemented by the SLEE
    public abstract CallBlockingAddressProfileCMP getProfile(ProfileID id)
            throws UnrecognizedProfileTableNameException,
            UnrecognizedProfileNameException;

    private void trace(Level level, String message) {
        trace(level, message, null);
    }

    /**
     * Utility method to use trace facility.
     * 
     * @param level
     * @param message
     * @param cause
     */
    private void trace(Level level, String message, Throwable cause) {
        if (context != null) {
            try {
                traceFacility.createTrace(context.getSbb(), level,
                        "sbb.parlay.callblocking", message, cause, System
                                .currentTimeMillis());
            }
            catch (UnrecognizedComponentException uce) {
                uce.printStackTrace();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("***************** " + message);
        if (cause != null) {
            cause.printStackTrace();
        }
    }

    /**
     * Gets a callConnection for the call identified by the specified
     * identifiers.
     * 
     * @param serviceIdentifier
     *            service instance identifier
     * @param callIdentifier
     *            call identifier
     * @return the call connection handle
     * @throws javax.slee.resource.ResourceException
     */
    private IpMultiPartyCallConnection getIpMultiPartyCallConnection(
            TpServiceIdentifier serviceIdentifier,
            TpMultiPartyCallIdentifier callIdentifier)
            throws javax.slee.resource.ResourceException {

        // RA connection handles
        ParlayConnection parlayConnection = null;
        IpMultiPartyCallControlManagerConnection managerConnection = null;
        IpMultiPartyCallConnection callConnection = null;

        try {
            // Get a parlay connection from the pool
            parlayConnection = resourceAdapterSbbInterface
                    .getParlayConnection();

            // Get call manager connection from the parlay connection using the
            // service ID
            managerConnection = (IpMultiPartyCallControlManagerConnection) parlayConnection
                    .getIpServiceConnection(serviceIdentifier);

            // Get the call connection from the manager connection
            callConnection = managerConnection
                    .getIpMultiPartyCallConnection(callIdentifier);
        }
        catch (javax.slee.resource.ResourceException e) {
            try {
                if (callConnection != null) {
                    callConnection.closeConnection();
                }
            }
            catch (Exception e1) {
                trace(Level.WARNING,
                        "ERROR: Exception caught in callConnection.close()", e1);
            }
            throw e;
        }
        finally {
            // Close all handles except the callConnection

            try {
                if (parlayConnection != null) {
                    parlayConnection.close();
                }
            }
            catch (Exception e) {
                trace(Level.WARNING,
                        "ERROR: Exception caught in parlayConnection.close()",
                        e);
            }
            try {
                if (managerConnection != null) {
                    managerConnection.closeConnection();
                }
            }
            catch (Exception e) {
                trace(
                        Level.WARNING,
                        "ERROR: Exception caught in managerConnection.closeConnection()",
                        e);
            }
        }

        return callConnection;
    }
}