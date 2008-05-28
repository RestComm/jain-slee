package net.java.slee.resource.diameter.activities;

import java.util.Iterator;

import dk.i1.diameter.AVP;
import dk.i1.diameter.AVP_Grouped;
import dk.i1.diameter.Message;

public interface BaseProtocolActivity extends ActivityBaseInterface{

    
    
    
    
    
    
    /**
     * Creates abbort session request. Ussualy this request is sent from server to network access device.
     * 
     * @param destinationHost -
     *            simply FQDN of destination host:<b>"diamgate.nist.gov"</b> -
     *            use only when destinationHost is not directly connected to
     *            this node. If destinationHost is connected fill with null - it
     *            will use value specified in creation of transactions object.
     * @param destinationRealm -
     *            simply domain name of destination host:<b>"nist.gov"-</b>
     *            use only when destinationRealmt is not directly connected to
     *            this node. If destinationRealm is connected fill with null -
     *            it will use value specified in creation of transactions
     *            object.
     * @param userName -
     *            name of the user for whom the session has been estabilished.
     *            For instance: <b>"sip:kaczor@nist.gov" would give
     *            userName==kaczor</b>
     * @param AVPS
     * @return
     */
    public Message createBaseProtocolAbortSessionRequest(
            String destinationHost, String destinationRealm, String userName,Iterator<AVP> AVPS);

    /**
     * Creates ReAuth request. This kind of requests are usually sent by server to client nodes in order initiate autorization process again. 
     * @param destinationHost -
     *            simply FQDN of destination host:<b>"diamgate.nist.gov"</b> -
     *            use only when destinationHost is not directly connected to
     *            this node. If destinationHost is connected fill with null - it
     *            will use value specified in creation of transactions object.
     * @param destinationRealm -
     *            simply domain name of destination host:<b>"nist.gov"-</b>
     *            use only when destinationRealmt is not directly connected to
     *            this node. If destinationRealm is connected fill with null -
     *            it will use value specified in creation of transactions
     *            object.
     * @param authApplicationId -
     *            identifier of Authentication part of application.
     * @param reAuthType -
     *            type of ReAuth request:
     *            <ul>
     *            <li>AUTHORIZE_ONLY=0 -- An authorization only re-auth is
     *            expected upon expiration of the Authorization-Lifetime. This
     *            is the default value if the AVP is not present in answer
     *            messages that include the Authorization-Lifetime.
     *            <li>AUTHORIZE_AUTHENTICATE=1 -- An authentication and
     *            authorization re-auth is expected upon expiration of the
     *            Authorization-Lifetime.
     *            </ul>
     * @param userName -
     *            name of the user for whom the session has been estabilished.
     *            For instance: <b>"sip:kaczor@nist.gov" would give
     *            userName==kaczor</b> 
     * @param originState
     * @param AVPs - other AVP which are going to be appended after all required ones.
     * @return
     */
    public Message createBaseProtocolReAuthRequest(String destinationHost,
            String destinationRealm,  int reAuthType,
            String userName,Iterator<AVP> AVPS);

    
    
    
    
    
    
public Message createBaseProtocolAccountingRequest(String destRealm,int accRecordType, int accRecordNumber,Iterator<AVP> AVPS);
    
    /**
     * Creates request which should initiate session termination. 
     * After creation of message One shoudl call 
     * @param destinationHost -
     *            simply FQDN of destination host:<b>"diamgate.nist.gov"</b> -
     *            use only when destinationHost is not directly connected to
     *            this node. If destinationHost is connected fill with null - it
     *            will use value specified in creation of transactions object.
     * @param destinationRealm -
     *            simply domain name of destination host:<b>"nist.gov"-</b>
     *            use only when destinationRealmt is not directly connected to
     *            this node. If destinationRealm is connected fill with null -
     *            it will use value specified in creation of transactions
     *            object.
     * @param originState - ?
     * @param userName -
     *            name of the user for whom the session has been estabilished.
     *            For instance: <b>"sip:kaczor@nist.gov" would give
     *            userName==kaczor</b>
     * @param terminationCause - is used to indicate the reason why a session was terminated on the access device. Supported values are:
     * <ul>
     * <li>DIAMETER_LOGOUT=1 -- The user initiated a disconnect
     * <li>DIAMETER_SERVICE_NOT_PROVIDED=2 -- This value is used when the user disconnected prior to the receipt of the authorization answer message.
     * <li>DIAMETER_BAD_ANSWER=3 -- This value indicates that the authorization answer received by the access device was not processed successfully.
     * <li>DIAMETER_ADMINISTRATIVE=4 -- The user was not granted access, or was disconnected, due to administrative reasons, such as the receipt of a Abort-Session-Request message.
     * <li>DIAMETER_LINK_BROKEN=5 -- The communication to the user was abruptly disconnected.
     * <li>DIAMETER_AUTH_EXPIRED=6 -- The user's access was terminated since its authorized session time has expired.
     * <li>DIAMETER_USER_MOVED=7 -- The user is receiving services from another access device.
     * <li>DIAMETER_SESSION_TIMEOUT=8 The user's session has timed out, and service has been terminated.
     * </ul>
     * @return
     */
    public Message createBaseProtocolSessionTerminationRequest(String destinationHost,
            String destinationRealm,int originState,String userName, int terminationCause ,Iterator<AVP> AVPS);
    
    //ANSWERS
    public Message createBaseProtocolAbortSessionAnswer(Message msg, int resultCode,Iterator<AVP> AVPS);
    public Message createBaseProtocolReAuthAnswer(Message msg,int resultCode,Iterator<AVP> AVPS);
    public Message createBaseProtocolSessionTerminationAnswer(Message msg, int resultCode,Iterator<AVP> AVPS);
    public Message createBaseProtocolAccountingAnswer(Message msg, int resultCode,AVP_Grouped vendorSpecificAppId,Iterator<AVP> AVPS);
    
}
