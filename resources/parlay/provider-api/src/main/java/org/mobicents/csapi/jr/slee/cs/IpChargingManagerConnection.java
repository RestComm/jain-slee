package org.mobicents.csapi.jr.slee.cs;

/**
 * This interface is the 'service manager' interface for the Charging Service. The Charging manager interface provides  management functions to the charging service. The application programmer can use this interface to start charging sessions.																															This interface shall be implemented by a Charging SCF.  As a minimum requirement, at least one of createChargingSession() or createSplitChargingSession() shall be implemented.
 *
 * 
 * 
 */
public interface IpChargingManagerConnection extends org.mobicents.csapi.jr.slee.IpServiceConnection {

    /**
     * Obtains Access To a IpChargingSessionConnection interface
     * @exception ResourceException If it is not possible to create the connection
     * @return A IpChargingSessionConnection interface
     */
    org.mobicents.csapi.jr.slee.cs.IpChargingSessionConnection  getIpChargingSessionConnection(TpChargingSessionID tpChargingSessionID) throws javax.slee.resource.ResourceException;

    /**
     *     This method creates an instance of the IpChargingSession interface to handle the charging events related to the specified user and to the application invoking this method. An IpAppChargingManager should already have been passed to the IpChargingManager, otherwise the charging manager will not be able to report a sessionAborted() to the application (the application should invoke setCallback() if it wishes to ensure this).
@return chargingSession: Defines the session.
     *     @param sessionDescription Descriptive text for informational purposes.
    @param merchantAccount Identifies the account of the party providing the application to be used.
    @param user Specifies the user that is using the application. This may or may not be the user that will be charged. The Charging service will determine the charged user. When this method is invoked the Charging service shall determine if charging is allowed for this application for this subscriber. An exception shall be thrown if this type of charging is not allowed.
    @param correlationID This value can be used to correlate the charging to network activity.

     */
    org.mobicents.csapi.jr.slee.cs.TpChargingSessionID createChargingSession(String sessionDescription,org.csapi.cs.TpMerchantAccountID merchantAccount,org.csapi.TpAddress user,org.csapi.cs.TpCorrelationID correlationID) throws org.csapi.TpCommonExceptions,org.csapi.cs.P_INVALID_USER,org.csapi.cs.P_INVALID_ACCOUNT,javax.slee.resource.ResourceException;


    /**
     *     This method creates an instance of the IpChargingSession interface to handle the charging events related to the specified users and to the application invoking this method. This method differs from createChargingSession() in that it allows to specify multiple users to be charged. The SCS implementation is responsible to figure out how later reserve and charge operations are split among these subscribers. The algorithm may be selected and controlled e.g. through the chargingParameter argument in the respective methods. The algorithms provided and the details how they interpret any parameters are vendor specific.
@return chargingSession: Defines the session. 
     *     @param sessionDescription Descriptive text for informational purposes.
    @param merchantAccount Identifies the account of the party providing the application to be used.
    @param users Specifies the users that are involved in using the application. This could be all users in a multi-party application (conference call, multi-user-game).
    @param correlationID This value can be used to correlate the charging to network activity.

     */
    org.mobicents.csapi.jr.slee.cs.TpChargingSessionID createSplitChargingSession(String sessionDescription,org.csapi.cs.TpMerchantAccountID merchantAccount,org.csapi.TpAddress[] users,org.csapi.cs.TpCorrelationID correlationID) throws org.csapi.TpCommonExceptions,org.csapi.cs.P_INVALID_USER,org.csapi.cs.P_INVALID_ACCOUNT,javax.slee.resource.ResourceException;


} // IpChargingManagerConnection

