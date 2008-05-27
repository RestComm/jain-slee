package org.mobicents.csapi.jr.slee.am;

/**
 * The account manager interface provides methods for monitoring accounts. Applications can use this interface to enable or disable charging-related event notifications and to query account balances. 													This interface shall be implemented by an Account Management SCF.  The queryBalanceReq() method, or the retrieveTransactionHistoryReq() method, or  both the createNotification() and destroyNotification methods, or both the enableNotifications and disableNotifications methods shall be implemented as a minimum requirement.
 *
 * 
 * 
 */
public interface IpAccountManagerConnection extends org.mobicents.csapi.jr.slee.IpServiceConnection {


    /**
     *     This method is used by the application to enable charging event notifications to be sent to the application.
If the same application requests two notifications with exactly the same criteria but different callback references, the second callback will be treated as an additional callback. Both notifications will share the same assignmentID. The gateway will always use the most recent callback. In case this most recent callback fails the second most recent is used. In case the enableCallNotification contains no callback, at the moment the application needs to be informed the gateway will use as callback the callback that has been registered by setCallback().
@return assignmentId : Specifies the ID assigned by the account management object for this newly enabled event notification.
     *     @param chargingEventCriteria Specifies the event specific criteria used by the application to define the charging event required. Individual addresses or address ranges may be specified for subscriber accounts. Example of events are "charging" and "recharging".

     */
    int createNotification(org.csapi.am.TpChargingEventCriteria chargingEventCriteria) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_ADDRESS,org.csapi.P_INVALID_CRITERIA,org.csapi.P_INVALID_EVENT_TYPE,org.csapi.P_UNKNOWN_SUBSCRIBER,javax.slee.resource.ResourceException;


    /**
     *     This method is used by the application to disable charging notifications.This method only applies to notifications created with createNotification().
     *     @param assignmentId Specifies the assignment ID that was given by the account management object when the application enabled the charging notification.

     */
    void destroyNotification(int assignmentId) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_ASSIGNMENT_ID,javax.slee.resource.ResourceException;


    /**
     *     This method is used by the application to query the balance of an account for one or several users.
@return queryId : Specifies the ID of the balance query request.
     *     @param users Specifies the user(s) for which the balance is queried.

     */
    int queryBalanceReq(org.csapi.TpAddress[] users) throws org.csapi.TpCommonExceptions,org.csapi.P_UNKNOWN_SUBSCRIBER,org.csapi.am.P_UNAUTHORIZED_APPLICATION,javax.slee.resource.ResourceException;


    /**
     *     This method is used by the application to change the event criteria introduced with createNotification. Any stored criteria associated with the specified assignmentID will be replaced with the specified criteria.
     *     @param assignmentID Specifies the ID assigned by the manager interface for the event notification.
    @param eventCriteria Specifies the new set of event criteria used by the application to define the event required. Only events that meet these criteria are reported

     */
    void changeNotification(int assignmentID,org.csapi.am.TpChargingEventCriteria eventCriteria) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_ASSIGNMENT_ID,org.csapi.P_INVALID_CRITERIA,org.csapi.P_INVALID_EVENT_TYPE,org.csapi.P_UNKNOWN_SUBSCRIBER,org.csapi.P_INVALID_ADDRESS,javax.slee.resource.ResourceException;


    /**
     *     This method is used by the application to query the event criteria set with createNotification or changeNotification.
@return eventCriteria : Specifies the event criteria used by the application to define the event required. Only events that meet these criteria are reported.
     * 
     */
    org.csapi.am.TpChargingEventCriteriaResult[] getNotification() throws org.csapi.TpCommonExceptions,javax.slee.resource.ResourceException;


    /**
     *     This asynchronous method is used by the application to retrieve a transaction history of a subscriber's account. The history is a set of Detailed Records.
@return retrievalID : Specifies the retrieval ID of the transaction history retrieval request.
     *     @param user Specifies the subscriber for whose account the transaction history is to be retrieved.
    @param transactionInterval Specifies the time interval for which the application history is to be retrieved.

     */
    int retrieveTransactionHistoryReq(org.csapi.TpAddress user,org.csapi.TpTimeInterval transactionInterval) throws org.csapi.TpCommonExceptions,org.csapi.P_UNKNOWN_SUBSCRIBER,org.csapi.am.P_UNAUTHORIZED_APPLICATION,org.csapi.P_INVALID_TIME_AND_DATE_FORMAT,javax.slee.resource.ResourceException;


    /**
     *     This method is used to indicate that the application is able to receive which are provisioned from within the network (i.e. these notifications are NOT set using createNotification() but via, for instance, a network management system). If notifications provisioned for this application are created or changed, the application is unaware of this until the notification is reported.
If the same application requests to enable notifications for a second time with a different IpAppAccountManager reference (i.e. without first disabling them), the second callback will be treated as an additional callback. The gateway will always use the most recent callback. In case this most recent callback fails the second most recent is used. 
When this method is used, it is still possible to use createNotification() for service provider provisioned notifications on the same interface as long as the criteria in the network and provided by createNotification() do not overlap. However, it is NOT recommended to use both mechanisms on the same service manager.
The methods changeNotification(), getNotification(), and destroyNotification() do not apply to notifications provisioned in the network and enabled using enableNotifications(). These only apply to notifications created using createNotification().
@return assignmentID: Specifies the ID assigned by the manager interface for this operation. This ID is contained in any reportNotification() that relates to notifications provisioned from within the network Repeated calls to enableNotifications() return the same assignment ID.
     * 
     */
    int enableNotifications() throws org.csapi.TpCommonExceptions,javax.slee.resource.ResourceException;


    /**
     *     This method is used to indicate that the application is not able to receive notifications for which the provisioning has been done from within the network. (i.e. these notifications that are NOT set using createNotification() but via, for instance, a network management system). After this method is called, no such notifications are reported anymore.
     * 
     */
    void disableNotifications() throws org.csapi.TpCommonExceptions,javax.slee.resource.ResourceException;


} // IpAccountManagerConnection

