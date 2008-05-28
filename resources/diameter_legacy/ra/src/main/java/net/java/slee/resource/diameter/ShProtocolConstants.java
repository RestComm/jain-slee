package net.java.slee.resource.diameter;



public class ShProtocolConstants {

	
/**
 * This AVP is of type "Grouped". It contains either Public-Identity ( which syntax corresponds to SIP URI or TEL URI) or MSISDN ( which is weird ) AVP.<br>
 * For instance SIP URI:<b>sip:bob@example.com</b>
 */	
public static final int DI_USER_IDENTITY=700;
/**
 * 
 */
public static final int DI_MSISDN=701;
/**
 * See TS 29 328 page 26.
 */
public static final int DI_USER_DATA=702;
/**
 * Again see TS 29 328. Defined types are: <b><li>DI_DATA_REFERENCE_REPOSITORY_DATA, <li>DI_DATA_REFERENCE_IMS_PUBLIC_IDENTITY,<li>DI_DATA_REFERENCE_IMS_USER_STATE
 *,<li>DI_DATA_REFERENCE_S_CSCF_NAME,<li>DI_DATA_REFERENCE_INITIAL_FILTER_CRITERIA,<li>DI_DATA_REFERENCE_LOCATION_INFORMATION,<li>DI_DATA_REFERENCE_USER_STATE,
 * <li>DI_DATA_REFERENCE_CHARGING_INFORMATION,<li>DI_DATA_REFERENCE_MSISDN;</b>
 */
public static final int DI_DATA_REFERENCE=703;
/**
 * This AVP is of Type OctetString. 
 */
public static final int DI_SERVICE_INDICATION=704;
/**
 * This AVP is of type Enumerated which is derived from AVP_Integer32. Its used within notification subscription request. Its value tells HSS wheather it should subscribe origin hos or unsubscribe.
 * Possible values are:<b><li>{@link #DI_SUBSCRIBE_REQUEST_SUBSCRIBE};<li>{@links #DI_SUBSCRIBE_REQUEST_UNSUBSCRIBE};</b>
 */
public static final int DI_SUBSCRIBE_TYPE=705;
/**
 * This AVP is of type Enumerated which is derived from AVP_Integer32. It tells HSS for which access domain data is requested. <br>Possible values:
 * <b><li>{@link #DI_REQUESTED_DOMAIN_CS_DOMAIN}=0;
 * <li>{@link #DI_REQUESTED_DOMAIN_PS_DOMAIN}=1
 * </b>
 */
public static final int DI_REQUESTED_DOMAIN=706;
/**
 * This AVP is of type Enumerated which is derived from AVP_Integer32. It tells HSS wheather it should retrieve refresh current location or not. <br>Possible values:
 * <b><li>{@link #DI_CURRENT_LOCATION_DO_NOT_INIT_ACTIVE_LOCATION_RETRIEVAL}=0;
 * <li>{@link #DI_CURRENT_LOCATION_INIT_ACTIVE_LOCATION_RETRIEVAL}=1
 * </b>
 */
public static final int DI_CURRENT_LOCATION=707;
/**
 * This AVP is of type UTF8String. It contains SIP URI compilant name of the server.
 */
public static final int DI_SERVER_NAME=602;
/**
 * The Public-Identity AVP is of type UTF8String. This AVP contains the public identity of a user in the IMS. The syntax
 *of this AVP corresponds either to a SIP URL (with the format defined in IETF RFC 3261 [3] and IETF RFC 2396 [4])
 *or a TEL URL (with the format defined in IETF RFC 2806 [8]).
 */
public static final int DI_PUBLIC_IDENTITY=601;



/**
 * 
 */
public static final int DI_DATA_REFERENCE_REPOSITORY_DATA=0;
/**
 * 
 */
public static final int DI_DATA_REFERENCE_IMS_PUBLIC_IDENTITY=10;
/**
 * 
 */
public static final int DI_DATA_REFERENCE_IMS_USER_STATE=11;
/**
 * 
 */
public static final int DI_DATA_REFERENCE_S_CSCF_NAME=12;
/**
 * 
 */
public static final int DI_DATA_REFERENCE_INITIAL_FILTER_CRITERIA=13;
/**
 * 
 */
public static final int DI_DATA_REFERENCE_LOCATION_INFORMATION=14;
/**
 * 
 */
public static final int DI_DATA_REFERENCE_USER_STATE=15;
/**
 * 
 */
public static final int DI_DATA_REFERENCE_CHARGING_INFORMATION=16;
/**
 * 
 */
public static final int DI_DATA_REFERENCE_MSISDN=17;
/**
 * 
 */


/**
 * 
 */
public static final int DI_SUBSCRIBE_REQUEST_SUBSCRIBE=0;
/**
 * 
 */
public static final int DI_SUBSCRIBE_REQUEST_UNSUBSCRIBE=1;

/**
 * 
 */
public static final int DI_REQUESTED_DOMAIN_CS_DOMAIN=0;
/**
 * 
 */
public static final int DI_REQUESTED_DOMAIN_PS_DOMAIN=1;

/**
 * 
 */
public static final int DI_CURRENT_LOCATION_DO_NOT_INIT_ACTIVE_LOCATION_RETRIEVAL=0;
/**
 * 
 */
public static final int DI_CURRENT_LOCATION_INIT_ACTIVE_LOCATION_RETRIEVAL=1;

/**
 * Identifies type of diameter Sh interface application. Set in message header.
 * 
 */
public static final int DIAMETER_APPLICATION_SH=16777217;
/**
 * Vendor Id set in AVPs in message body.
 */
public static final int DIAMETER_3GPP_VENDOR_ID=10415;

/**
 * Identifies type of diameter Cx/Dx application. Set in message header.
 * 
 */
public static final int DIAMETER_APPLICATION_CxDx=16777216;
/**
 * Request user data code
 */
public static final int DIAMETER_SH_COMMAND_USER_DATA=306;
/**
 * Request update user info code
 */
public static final int DIAMETER_SH_COMMAND_PROFILE_UPDATE=307;
/**
 * Request subscription code
 */
public static final int DIAMETER_SH_COMMAND_SUBSCRIBE_NOTIFICATIONS=308;
/**
 * Request push notification code
 */
public static final int DIAMETER_SH_COMMAND_PUSH_NOTIFICATION=309;





/**
 * Fatal error. This value is set in Experimental-Result-Code AVP when requested data doesnt exist?
 */
public static final int DIAMETER_ERROR_USER_DATA_NOT_RECOGNIZED=5100;
/**
 * Fatal error. This value is set in Experimental-Result-Code AVP when HSS cant fulfill request for requesting AS.
 */
public static final int DIAMETER_RESULT_OPERATION_NOT_ALLOWED=5101;
/**
 * Fatal error. This value is set in Experimental-Result-Code AVP when AS doesnt have privileges to read user data.
 */
public static final int DIAMETER_RESULT_DATA_CANNOT_BE_READ=5102;
/**
 * Fatal error. This value is set in Experimental-Result-Code AVP when SA doesnt have privileges to modify user data.
 */
public static final int DIAMETER_RESULT_USER_DATA_CANNOT_BE_MODIFIED=5103;
/**
 * Fatal error. This value is set in Experimental-Result-Code AVP when AS cant be notified ( or doesnt have rights ) of data changes.
 */
public static final int DIAMETER_RESULT_USER_DATA_CANNOT_BE_NOTIFIED=5104;
/**
 * Fatal error. This value is set in Experimental-Result-Code AVP when data that has been sent is too big to be stored in HSS.
 */
public static final int DIAMETER_RESULT_TOO_MUCH_DATA=5008;
/**
 * Fatal error. This value is set in Experimental-Result-Code AVP when AS is trying to update data stored with older values that currently stored.
 */
public static final int DIAMETER_RESULT_TRANSPARENT_DATA_OUT_OF_SYNC=5105;
/**
 * Not fatal error. This value is set in Experimental-Result-Code AVP when user data is not available at this time. 
 */
public static final int DIAMETER_RESULT_USER_DATA_NOT_AVAILABLE=4100;
/**
 * Not fatal error. This value is set in Experimental-Result-Code AVP when someone else is updating data.
 */
public static final int DIAMETER_RESULT_PRIOR_UPDATE_IN_PROGRESS=4101;




}
