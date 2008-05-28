package org.mobicents.csapi.jr.slee.cs;

/**
 * The Charging Session interface provides operations  to facilitate transactions between a merchant and a user. The application programmer can use this interface to debit or credit amounts and/or units towards a user, to create and extend the lifetime of a reservation and to get information about what is left of the reservation.							This interface shall be implemented by a Charging SCF.  As a minimum requirement, the release() method shall be implemented. If the reserveAmountReq() method is implemented, at least one of the debitAmountReq() or the creditAmountReq() methods shall also be implemented. If the reserveUnitReq() method is implemented, at least one of the debitUnitReq() or the creditUnitReq() methods shall also be implemented. If neither the reserveAmountReq() nor the reserveUnitReq() method is implemented, then at least one of the directDebitAmountReq() or the directDebitUnitReq(), or the directCreditAmountReq(), or the directCreditUnitReq() methods shall be implemented.
 *
 * 
 * 
 */
public interface IpChargingSessionConnection extends org.mobicents.csapi.jr.slee.IpServiceConnection {


    /**
     *     This method credits an amount towards the reservation associated with the session.
The amount left in the reservation will be increased by this amount.
Each request to debit / credit an amount towards a reservation is handled separately. For example, two requests for a payment of EUR 1,- will give a total payment of EUR 2,-.
A credit of EUR 1,- and a debit of EUR 1 will give a total payment of EUR 0,-.
     * 
    @param amount The amount of specified currency to be credited towards the user.
    @param closeReservation If set to true, this parameter indicates that the remaining part of the reservation can be freed. This may also mean addition of currency to the subscriber's account if more credits than debits have been made. The session is not released, this has to be done explicitly by calling the release() method.
    @param requestNumber Specifies the number given in the result of the previous operation on this session, or when creating the session. When no answer is received the same operation with the same parameters must be retried with the same requestNumber.

     */
    void creditAmountReq(org.csapi.cs.TpApplicationDescription applicationDescription,org.csapi.cs.TpChargingPrice amount,boolean closeReservation,int requestNumber) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_AMOUNT,org.csapi.P_INVALID_CURRENCY,org.csapi.cs.P_INVALID_REQUEST_NUMBER,javax.slee.resource.ResourceException;


    /**
     *     This method credits a volume of application usage towards the reservation.
The volumes left in the reservation of this  will be increased by this amount.
Each request to debit / credit a volume towards a reservation is handled separately.  For example, two requests for a payment for 10 kilobytes will give a total payment for 20 kilobytes
     * 
    @param volumes Specifies the credited volumes in different units, more specifically a sequence of data elements each containing the amount and applied unit. 
    @param closeReservation If set to true, this parameter indicates that the reservation can be freed. The session is not released, this has to be done explicitly by calling the release() method.
    @param requestNumber Specifies the number given in the result of the previous operation on this session, or when creating the session. When no answer is received the same operation with the same parameters must be retried with the same requestNumber.

     */
    void creditUnitReq(org.csapi.cs.TpApplicationDescription applicationDescription,org.csapi.cs.TpVolume[] volumes,boolean closeReservation,int requestNumber) throws org.csapi.TpCommonExceptions,org.csapi.cs.P_INVALID_VOLUME,org.csapi.cs.P_INVALID_REQUEST_NUMBER,javax.slee.resource.ResourceException;


    /**
     *     This method debits an amount from the reservation.
The amount left in the reservation will be decreased by this amount.
Each request to debit / credit an amount towards a reservation is handled separately.  For example, two requests for a payment of EUR 1,- will give a total payment of EUR 2,-.
A credit of EUR 1,- and a debit of EUR 1 will give a total payment of EUR 0,-.
When a debit operation would exceed the limit of the reservation, the debit operation fails.
     * 
    @param amount The amount of specified currency to be debited from the user.
    @param closeReservation If set to true, this parameter indicates that the reservation can be freed. The session is not released, this has to be done explicitly by calling the release() method.
    @param requestNumber Specifies the number given in the result of the previous operation on this session, or when creating the session. When no answer is received the same operation with the same parameters must be retried with the same requestNumber.

     */
    void debitAmountReq(org.csapi.cs.TpApplicationDescription applicationDescription,org.csapi.cs.TpChargingPrice amount,boolean closeReservation,int requestNumber) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_AMOUNT,org.csapi.P_INVALID_CURRENCY,org.csapi.cs.P_INVALID_REQUEST_NUMBER,javax.slee.resource.ResourceException;


    /**
     *     This method debits a volume of application usage from the reservation.
The volumes left in the reservation will be decreased by this amount.
Each request to debit / credit a volume towards a reservation is handled separately.  For example, two requests for a payment for 10 kilobytes will give a total payment for 20 kilobytes.
When a debit operation would exceed the limit of the reservation, the debit operation succeeds, and the debited volumes will be the rest of the volumes in the reservation.
     * 
    @param volumes Specifies the charged volumes in different units, more specifically a sequence of data elements each containing the amount and applied unit. 
    @param closeReservation If set to true, this parameter indicates that the reservation can be freed. The session is not released, this has to be done explicitly by calling the release() method.
    @param requestNumber Specifies the number given in the result of the previous operation on this session, or when creating the session. When no answer is received the same operation with the same parameters must be retried with the same requestNumber.

     */
    void debitUnitReq(org.csapi.cs.TpApplicationDescription applicationDescription,org.csapi.cs.TpVolume[] volumes,boolean closeReservation,int requestNumber) throws org.csapi.TpCommonExceptions,org.csapi.cs.P_INVALID_VOLUME,org.csapi.cs.P_INVALID_REQUEST_NUMBER,javax.slee.resource.ResourceException;


    /**
     *     This method directly credits an amount towards the user. 
A possible reservation associated with this session is not influenced.
     * 
    @param chargingParameters These parameters and their values specify to the charging service what was provided to the end user so that the charging service can determine the applicable tariff..
    @param amount The amount of specified currency to be credited towards the user.
    @param requestNumber Specifies the number given in the result of the previous operation on this session, or when creating the session. When no answer is received the same operation with the same parameters must be retried with the same requestNumber.

     */
    void directCreditAmountReq(org.csapi.cs.TpApplicationDescription applicationDescription,org.csapi.cs.TpChargingParameter[] chargingParameters,org.csapi.cs.TpChargingPrice amount,int requestNumber) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_AMOUNT,org.csapi.P_INVALID_CURRENCY,org.csapi.cs.P_INVALID_REQUEST_NUMBER,javax.slee.resource.ResourceException;


    /**
     *     This method directly credits a volume of application usage towards the user.
The volumes in a possible reservation associated with this session are not influenced.
     *     @param sessionID The ID of the reservation.
    @param chargingParameters These parameters and their values specify to the charging service what was provided to the end user so that the charging service can determine the applicable tariff..
    @param volumes Specifies the credited volumes in different units, more specifically a sequence of data elements each containing the amount and applied unit. 
    @param requestNumber Specifies the number given in the result of the previous operation on this session, or when creating the session. When no answer is received the same operation with the same parameters must be retried with the same requestNumber.

     */
    void directCreditUnitReq(org.csapi.cs.TpApplicationDescription applicationDescription,org.csapi.cs.TpChargingParameter[] chargingParameters,org.csapi.cs.TpVolume[] volumes,int requestNumber) throws org.csapi.TpCommonExceptions,org.csapi.cs.P_INVALID_VOLUME,org.csapi.cs.P_INVALID_REQUEST_NUMBER,javax.slee.resource.ResourceException;


    /**
     *     This method directly debits an amount towards the user. 
A possible reservation associated with this session is not influenced.
     * 
    @param chargingParameters These parameters and their values specify to the charging service what was provided to the end user so that the charging service can determine the applicable tariff..
    @param amount The amount of specified currency to be debited from the user.
    @param requestNumber Specifies the number given in the result of the previous operation on this session, or when creating the session. When no answer is received the same operation with the same parameters must be retried with the same requestNumber.

     */
    void directDebitAmountReq(org.csapi.cs.TpApplicationDescription applicationDescription,org.csapi.cs.TpChargingParameter[] chargingParameters,org.csapi.cs.TpChargingPrice amount,int requestNumber) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_AMOUNT,org.csapi.P_INVALID_CURRENCY,org.csapi.cs.P_INVALID_REQUEST_NUMBER,javax.slee.resource.ResourceException;


    /**
     *     This method directly credits a volume of application usage towards the user.
The volumes in a possible reservation associated with this session are not influence.
     *     @param sessionID The ID of the reservation.
    @param chargingParameters These parameters and their values specify to the charging service what was provided to the end user so that the charging service can determine the applicable tariff..
    @param volumes Specifies the charged volumes in different units, more specifically a sequence of data elements each containing the amount and applied unit. 
    @param requestNumber Specifies the number given in the result of the previous operation on this session, or when creating the session. When no answer is received the same operation with the same parameters must be retried with the same requestNumber.

     */
    void directDebitUnitReq(org.csapi.cs.TpApplicationDescription applicationDescription,org.csapi.cs.TpChargingParameter[] chargingParameters,org.csapi.cs.TpVolume[] volumes,int requestNumber) throws org.csapi.TpCommonExceptions,org.csapi.cs.P_INVALID_VOLUME,org.csapi.cs.P_INVALID_REQUEST_NUMBER,javax.slee.resource.ResourceException;


    /**
     *     With this method an application can request the lifetime of the reservation to be extended. If no reservation has been made on the charging session, this method raises an exception (P_TASK_REFUSED).
     * 

     */
    void extendLifeTimeReq() throws org.csapi.TpCommonExceptions,javax.slee.resource.ResourceException;


    /**
     *     With this method an application can request the remaining amount of the reservation.
@return amountLeft: Gives the amount left in the reservation.
     * 

     */
    org.csapi.cs.TpChargingPrice getAmountLeft() throws org.csapi.TpCommonExceptions,javax.slee.resource.ResourceException;


    /**
     *     With this method an application can request the remaining lifetime of the reservation. If no reservation has been made on the charging session, this method raises an exception (P_TASK_REFUSED).
@return reservationTimeLeft: Indicates the number of seconds that the session remains valid.
     * 

     */
    int getLifeTimeLeft() throws org.csapi.TpCommonExceptions,javax.slee.resource.ResourceException;


    /**
     *     With this method an application can request the remaining amount of the reservation.
@return volumesLeft: Specifies the remaining volumes in different units, more specifically a sequence of data elements each containing the amount and applied unit.
     * 

     */
    org.csapi.cs.TpVolume[] getUnitLeft() throws org.csapi.TpCommonExceptions,javax.slee.resource.ResourceException;


    /**
     *     This method is used when the application wants to have an item rated by the charging service. The result can be used to present pricing information to the end-user before the end-user actually wants to start using the service.
     * 
    @param chargingParameters These parameters and their values specify to the charging service what was provided to the end user so that the charging service can determine the applicable tariff..

     */
    void rateReq(org.csapi.cs.TpChargingParameter[] chargingParameters) throws org.csapi.TpCommonExceptions,javax.slee.resource.ResourceException;


    /**
     *     This method releases the session, no operations can be done towards this session anymore (not even retries). Unused parts of a reservation are freed.
     * 
    @param requestNumber Specifies the number given in the result of the previous operation on this session, or when creating the session.

     */
    void release(int requestNumber) throws org.csapi.TpCommonExceptions,org.csapi.cs.P_INVALID_REQUEST_NUMBER,javax.slee.resource.ResourceException;


    /**
     *     This method is used when an application wants to reserve an amount of money for services to be delivered to a user. It is also possible to enlarge the existing amount reservation by invoking this method. If a reservation is extended, the lifetime of the reservation is re-initialized.
     * 
    @param chargingParameters These parameters and their values specify to the charging service what was provided to the end user so that the charging service can determine the applicable tariff.
    @param preferredAmount The amount of specified currency that the application wants to be reserved.
    @param minimumAmount The minimum amount that can be used by the application if the preferred amount cannot be granted.
    @param requestNumber Specifies the number given in the result of the previous operation on this session, or when creating the session. When no answer is received the same operation with the same parameters must be retried with the same requestNumber.

     */
    void reserveAmountReq(org.csapi.cs.TpApplicationDescription applicationDescription,org.csapi.cs.TpChargingParameter[] chargingParameters,org.csapi.cs.TpChargingPrice preferredAmount,org.csapi.cs.TpChargingPrice minimumAmount,int requestNumber) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_AMOUNT,org.csapi.P_INVALID_CURRENCY,org.csapi.cs.P_INVALID_REQUEST_NUMBER,javax.slee.resource.ResourceException;


    /**
     *     This method is used when an application wants to reserve volumes of application usage to be delivered to a user in the session. When using units it is assumed that the price setting for the units is handled by the network side services. It is also possible to enlarge the existing unit reservation by invoking this method.
     * 
    @param chargingParameters These parameters and their values specify to the charging service what was provided to the end user so that the charging service can determine the applicable tariff..
    @param volumes Specifies the reserved volumes in different units, more specifically a sequence of data elements each containing the amount and applied unit. It is e.g. possible to make a reservation for 10 000 octets and 5 charging units. 
    @param requestNumber Specifies the number given in the result of the previous operation on this session, or when creating the session. When no answer is received the same operation with the same parameters must be retried with the same requestNumber.

     */
    void reserveUnitReq(org.csapi.cs.TpApplicationDescription applicationDescription,org.csapi.cs.TpChargingParameter[] chargingParameters,org.csapi.cs.TpVolume[] volumes,int requestNumber) throws org.csapi.TpCommonExceptions,org.csapi.cs.P_INVALID_VOLUME,org.csapi.cs.P_INVALID_REQUEST_NUMBER,javax.slee.resource.ResourceException;


} // IpChargingSessionConnection

