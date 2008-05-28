package net.java.slee.resource.diameter.activities;

import java.util.Iterator;

import org.apache.log4j.Logger;

import net.java.slee.resource.diameter.DiameterRAActivityHandle;
import net.java.slee.resource.diameter.DiameterResourceAdaptor;
import net.java.slee.resource.diameter.DiameterResourceAdaptorSbbInterface;
import net.java.slee.resource.diameter.ShProtocolConstants;
import net.java.slee.resource.diameter.utils.DRAUtils;
import static dk.i1.diameter.ProtocolConstants.*;
import static net.java.slee.resource.diameter.ShProtocolConstants.*;
import dk.i1.diameter.AVP;
import dk.i1.diameter.AVP_Grouped;
import dk.i1.diameter.AVP_Integer32;
import dk.i1.diameter.AVP_OctetString;
import dk.i1.diameter.AVP_UTF8String;
import dk.i1.diameter.AVP_Unsigned32;
import dk.i1.diameter.Message;
import dk.i1.diameter.Utils;

import dk.i1.diameter.node.ConnectionKey;

// OR SHOULD IT EXTENDS BasePrtocolActivityImpl??
public class ShInterfaceActivityImpl extends DiameterActivityCommonPart
        implements ShInterfaceActivity {

    private static transient Logger logger = Logger
    .getLogger(DiameterResourceAdaptor.class);
    public ShInterfaceActivityImpl(String destHost, String destRealm,
            String sessID, int authSessionState, ConnectionKey key,
            DiameterRAActivityHandle DAH,DiameterResourceAdaptorSbbInterface d2sbb) {
        super(destHost, destRealm, ShProtocolConstants.DIAMETER_APPLICATION_SH,
                sessID, authSessionState, key, DAH,d2sbb);
    }

    /**
     * 
     * Creates Sh-Pull request (UDR). Sets applcation to
     * {@link ShProtocolConstants.DIAMETER_APPLICATION_SH} and Vendor-Id to
     * {@link ShProtocolConstants.DIAMETER_3GPP_VENDOR_ID}<br>
     * Method fills destiantion host and realm etc ( all passed values are used
     * to create AVPs) and adds this node as origin node. To other AVPs refer to
     * page <b>7</b> of TS 29.329 and
     * http://wiki.java.net/bin/view/Communications/DiameterShInterface#Conditions_for_Information_Eleme
     * <br>
     * This method set all "M" bits and so on in AVPs with use of
     * dk.i1.diameter.Utils.setMandatorX and
     * net.java.slee.resource.diameter.utils. DRAUtils.setMandatory_TS...
     * methods.
     * 
     * @param destinationHost -
     *            simply FQDN of destination host:<b>"diamgate.nist.gov" - It
     *            should be set to null generaly as ActivityObject has those
     *            informations, and will use them to populate those AVPs. If its
     *            not set to null, passed value will be used</b>
     * @param destinationRealm -
     *            simply domain name of destination host:<b>"nist.gov"- It
     *            should be set to null generaly as ActivityObject has those
     *            informations, and will use them to populate those AVPs. If its
     *            not set to null, passed value will be used</b>
     * @param userIdentity -
     *            user identifier for which we make this reqest:<b>"sip:pinky@hells.kitchen.org"</b> -
     *            it becomes part of User-Identity AVP.
     * @param vendorSpecifiAppID -
     *            Vendor specific application ID for this app.
     * @param dataReference -
     *            references data this request has been created for. Possible
     *            values are:
     *            <ul>
     *            <li>{@link ShProtocolConstants.DI_DATA_REFERENCE_REPOSITORY_DATA}
     *            <li>{@link ShProtocolConstants.DI_DATA_REFERENCE_IMS_PUBLIC_IDENTITY}
     *            <li>{@link ShProtocolConstants.DI_DATA_REFERENCE_IMS_USER_STATE}
     *            <li>{@link ShProtocolConstants.DI_DATA_REFERENCE_S_CSCF_NAME}
     *            <li>{@link ShProtocolConstants.DI_DATA_REFERENCE_INITIAL_FILTER_CRITERIA}
     *            <li>{@link ShProtocolConstants.DI_DATA_REFERENCE_LOCATION_INFORMATION}
     *            <li>{@link ShProtocolConstants.DI_DATA_REFERENCE_USER_STATE}
     *            <li>{@link ShProtocolConstants.DI_DATA_REFERENCE_CHARGING_INFORMATION}
     *            <li>{@link ShProtocolConstants.DI_DATA_REFERENCE_MSISDN}
     *            </ul>
     * @param avps -
     *            Iterator containing AVPs that will be added as additional
     *            payload.
     * @return User data request message.
     */
    public Message makeShUserDataRequest(String destinationHost,
            String destiantionRealm, AVP_Grouped userIdentity,
            AVP_Grouped vendorSpecifiAppID, int dataReference,
            Iterator<AVP> avps) {

        Message msg = new Message();

        // < User-Data -Request> ::= < Diameter Header: 306, REQ, PXY, 16777217
        // >
        msg.hdr.setProxiable(true);
        msg.hdr.setRequest(true);
        msg.hdr.application_id = applicationID;
        msg.hdr.command_code = DIAMETER_SH_COMMAND_USER_DATA;
        // < Session-Id >
        msg.add(new AVP_UTF8String(DI_SESSION_ID, sessionID));
        // { Vendor-Specific-Application-Id }
        msg.add(vendorSpecifiAppID);
        // { Auth-Session-State }
        msg.add(new AVP_Unsigned32(DI_AUTH_SESSION_STATE, authSessionState));
        // { Origin-Host }
        // { Origin-Realm }
        d2sbb.addOurHostAndRealm(msg);
        // [ Destination-Host ]
        if (destinationHost != null) {
            msg.add(new AVP_UTF8String(DI_DESTINATION_HOST, destinationHost));
        } else if (this.destinationHost != null) {
            msg.add(new AVP_UTF8String(DI_DESTINATION_HOST,
                    this.destinationHost));
        }
        // { Destination-Realm }
        if (destinationRealm != null)
            msg.add(new AVP_UTF8String(DI_DESTINATION_REALM, destinationRealm));
        else
            msg.add(new AVP_UTF8String(DI_DESTINATION_REALM,
                    this.destinationRealm));
        // { User-Identity }
        msg.add(userIdentity);
        // [ Server-Name ]
        // [ Service-Indication ]

        // { Data-Reference }
        msg.add(new AVP_Integer32(DI_DATA_REFERENCE, dataReference));
        // *[ Requested-Domain ]
        // [ Current-Location ]
        // *[ AVP ]
        AVP avp = null;
        while (avps.hasNext()) {
            avp = avps.next();
            msg.add(avp);

        }

        // *[ Proxy-Info ]
        // *[ Route-Record ]
        Utils.setMandatory_RFC3588(msg);
        Utils.setMandatory_RFC4006(msg);
        DRAUtils.setMandatory_TS_29_329(msg);
        return msg;
    }

    /**
     * Creates user profile update request message.Sets applcation to
     * {@link ShProtocolConstants.DIAMETER_APPLICATION_SH} and Vendor-Id to
     * {@link ShProtocolConstants.DIAMETER_3GPP_VENDOR_ID}<br>
     * Method fills destiantion host and realm etc ( all passed values are used
     * to create AVPs) and adds this node as origin node. To other AVPs refer to
     * page <b>7</b> of TS 29.329 and
     * http://wiki.java.net/bin/view/Communications/DiameterShInterface#Conditions_for_Information_Eleme
     * <br>
     * This method set all "M" bits and so on in AVPs with use of
     * dk.i1.diameter.Utils.setMandatorX and
     * net.java.slee.resource.diameter.utils. DRAUtils.setMandatory_TS...
     * methods.
     * 
     * @param destinationHost -
     *            simply FQDN of destination host:<b>"diamgate.nist.gov" - It
     *            should be set to null generaly as ActivityObject has those
     *            informations, and will use them to populate those AVPs. If its
     *            not set to null, passed value will be used</b>
     * @param destinationRealm -
     *            simply domain name of destination host:<b>"nist.gov"- It
     *            should be set to null generaly as ActivityObject has those
     *            informations, and will use them to populate those AVPs. If its
     *            not set to null, passed value will be used</b>
     * @param userIdentity -
     *            user identifier for which we make this reqest:<b>"sip:pinky@hells.kitchen.org"</b> -
     *            it becomes part of User-Identity AVP.
     * @param vendorSpecifiAppID -
     *            Vendor specific application ID for this app.
     * @param dataValue -
     *            user data that is going to be updated. See TS 29.328 for
     *            format of data. ( User-Data AVP ).
     * @param avps -
     *            Iterator containing AVPs that will be added as additional
     *            payload.
     * @return Update user data request message.
     */
    public Message makeShProfileUpdateRequest(String destinationHost,
            String destiantionRealm, AVP_Grouped userIdentity,
            AVP_Grouped vendorSpecifiAppID, String dataValue, Iterator<AVP> avps) {

        Message msg = new Message();
        /*
        AVP_Unsigned32[] avps2=(AVP_Unsigned32[]) vendorSpecifiAppID.queryAVPs();
        for(AVP_Unsigned32 av : avps2)
        {
            logger.info("====== AVP CODE: "+av.code+". AVP VALUE: "+av.queryValue()+" ===========");
        }*/
        // < Profile-Update-Request > ::= < Diameter Header: 307, REQ, PXY,
        // 16777217 >
        msg.hdr.setProxiable(true);
        msg.hdr.setRequest(true);
        msg.hdr.application_id = applicationID;
        msg.hdr.command_code = DIAMETER_SH_COMMAND_PROFILE_UPDATE;
        // < Session-Id >
        msg.add(new AVP_UTF8String(DI_SESSION_ID, sessionID));
        // { Vendor-Specific-Application-Id }
        msg.add(vendorSpecifiAppID);
        /*AVP avp2 = msg.find(DI_VENDOR_SPECIFIC_APPLICATION_ID,DIAMETER_3GPP_VENDOR_ID);
        try {

        if(avp2!=null) {
            AVP g[] = new AVP_Grouped(avp2).queryAVPs();
            if(g.length==2 &&
               g[0].code==DI_VENDOR_ID) {
                int vendor_id = new AVP_Unsigned32(g[0]).queryValue();
                int app = new AVP_Unsigned32(g[1]).queryValue();
                
                    logger.info("========= vendor-id="+vendor_id+", app="+app);
                
            }
        }else
            logger.info("========= NO VEDNROS SPED APP ID!!! ==========");
        }catch(Exception e)
        {
            logger.info("ARGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGH");
            e.printStackTrace();
        }*/
        // { Auth-Session-State }
        msg.add(new AVP_Unsigned32(DI_AUTH_SESSION_STATE, authSessionState));
        // { Origin-Host }
        // { Origin-Realm }
        d2sbb.addOurHostAndRealm(msg);
        // { Destination-Realm }
        if (destinationRealm != null)
            msg.add(new AVP_UTF8String(DI_DESTINATION_REALM, destinationRealm));
        else
            msg.add(new AVP_UTF8String(DI_DESTINATION_REALM,
                    this.destinationRealm));
        // { Destination-Host }
        if (destinationHost != null)
            msg.add(new AVP_UTF8String(DI_DESTINATION_HOST, destinationHost));
        else
            msg.add(new AVP_UTF8String(DI_DESTINATION_HOST,
                    this.destinationHost));
        // { User-Identity }
        msg.add(userIdentity);
        // { User-Data }
        msg.add(new AVP_OctetString(DI_USER_DATA, dataValue.getBytes()));
        // *[ AVP ]
        AVP avp = null;
        while (avps.hasNext()) {
            avp = avps.next();
            msg.add(avp);

        }

        // *[ Proxy-Info ]
        // *[ Route-Record ]
        Utils.setMandatory_RFC3588(msg);
        Utils.setMandatory_RFC4006(msg);
        DRAUtils.setMandatory_TS_29_329(msg);
        return msg;
    }

    /**
     * Creates subscription request which will either subscribe or unsubscribe (
     * depends on subsriptionType). Sets applcation to
     * {@link ShProtocolConstants.DIAMETER_APPLICATION_SH} and Vendor-Id to
     * {@link ShProtocolConstants.DIAMETER_3GPP_VENDOR_ID}<br>Method fills destiantion host and realm etc (
     * all passed values are used to create AVPs) and adds this node as origin
     * node. To other AVPs refer to page <b>8</b> of TS 29.329 and
     * http://wiki.java.net/bin/view/Communications/DiameterShInterface#Conditions_for_Information_Eleme
     * <br>
     * This method set all "M" bits and so on in AVPs with use of
     * dk.i1.diameter.Utils.setMandatorX and
     * net.java.slee.resource.diameter.utils. DRAUtils.setMandatory_TS...
     * methods.
     * 
     * @param destinationHost -
     *            simply FQDN of destination host:<b>"diamgate.nist.gov" - use
     *            only when destinationHost is not directly connected to this
     *            node. If destinationHost is connected fill with null - it will
     *            use value specified in creation of transactions object.</b>
     * @param destinationRealm -
     *            simply domain name of destination host:<b>"nist.gov"- use
     *            only when destinationRealmt is not directly connected to this
     *            node. If destinationRealm is connected fill with null - it
     *            will use value specified in creation of transactions object.</b>
     * @param userIdentity -
     *            user identifier for which we make this reqest:<b>"sip:pinky@hells.kitchen.org"</b> -
     *            it becomes part of User-Identity AVP.
     * @param vendorSpecifiAppID -
     *            Vendor specific application ID for this app.
     * @param subscriptionType -
     *            telss HSS wheather we are subscri
     * @param dataRef -
     *            references data this request has been created for. Possible
     *            values are:
     *            <ul>
     *            <li>{@link ShProtocolConstants.DI_DATA_REFERENCE_REPOSITORY_DATA}
     *            <li>{@link ShProtocolConstants.DI_DATA_REFERENCE_IMS_PUBLIC_IDENTITY}
     *            <li>{@link ShProtocolConstants.DI_DATA_REFERENCE_IMS_USER_STATE}
     *            <li>{@link ShProtocolConstants.DI_DATA_REFERENCE_S_CSCF_NAME}
     *            <li>{@link ShProtocolConstants.DI_DATA_REFERENCE_INITIAL_FILTER_CRITERIA}
     *            <li>{@link ShProtocolConstants.DI_DATA_REFERENCE_LOCATION_INFORMATION}
     *            <li>{@link ShProtocolConstants.DI_DATA_REFERENCE_USER_STATE}
     *            <li>{@link ShProtocolConstants.DI_DATA_REFERENCE_CHARGING_INFORMATION}
     *            <li>{@link ShProtocolConstants.DI_DATA_REFERENCE_MSISDN}
     *            </ul>
     * @param avps -
     *            Iterator containing AVPs that will be added as additional
     *            payload.
     * @return Created notification subsription
     */
    public Message makeShSubscribeNotificationRequest(String destinationHost,
            String destiantionRealm, AVP_Grouped userIdentity,
            AVP_Grouped vendorSpecificAppID, int subscriptionType, int dataRef,
            Iterator<AVP> avps) {

        Message msg = new Message();

        // < Subscribe-Notifications-Request > ::= < Diameter Header: 308, REQ,
        // PXY, 16777217 >
        msg.hdr.setProxiable(true);
        msg.hdr.setRequest(true);
        msg.hdr.application_id = applicationID;
        msg.hdr.command_code = DIAMETER_SH_COMMAND_SUBSCRIBE_NOTIFICATIONS;
        // < Session-Id >
        msg.add(new AVP_UTF8String(DI_SESSION_ID, sessionID));
        // { Vendor-Specific-Application-Id }
        msg.add(vendorSpecificAppID);
        // { Auth-Session-State }
        msg.add(new AVP_Unsigned32(DI_AUTH_SESSION_STATE, authSessionState));
        // { Origin-Host }
        // { Origin-Realm }
        d2sbb.addOurHostAndRealm(msg);
        // [ Destination-Host ]
        if (destinationHost != null) {
            msg.add(new AVP_UTF8String(DI_DESTINATION_HOST, destinationHost));
        } else if (this.destinationHost != null) {
            msg.add(new AVP_UTF8String(DI_DESTINATION_HOST,
                    this.destinationHost));
        }
        // { Destination-Realm }
        if (destinationRealm != null)
            msg.add(new AVP_UTF8String(DI_DESTINATION_REALM, destinationRealm));
        else
            msg.add(new AVP_UTF8String(DI_DESTINATION_REALM,
                    this.destinationRealm));
        // { User-Identity }
        msg.add(userIdentity);
        // [ Service-Indication]
        // [ Server-Name ]
        // { Subs-Req-Type }
        msg.add(new AVP_Unsigned32(DI_SUBSCRIBE_TYPE, subscriptionType));
        // { Data-Reference }
        msg.add(new AVP_Integer32(DI_DATA_REFERENCE, dataRef));
        // *[ AVP ]
        AVP avp = null;
        while (avps.hasNext()) {
            avp = avps.next();
            msg.add(avp);

        }
        // *[ Proxy-Info ]
        // *[ Route-Record ]

        Utils.setMandatory_RFC3588(msg);
        Utils.setMandatory_RFC4006(msg);
        DRAUtils.setMandatory_TS_29_329(msg);
        return msg;
    }

    /**
     * Creates PNR message. Sets applcation to
     * {@link ShProtocolConstants.DIAMETER_APPLICATION_SH} and Vendor-Id to
     * {@link ShProtocolConstants.DIAMETER_3GPP_VENDOR_ID}<br>Method fills destiantion host and realm etc ( all
     * passed values are used to create AVPs) and adds this node as origin node.
     * To other AVPs refer to page <b>9</b> of TS 29.329 and
     * http://wiki.java.net/bin/view/Communications/DiameterShInterface#Conditions_for_Information_Eleme
     * <br>
     * This method set all "M" bits and so on in AVPs with use of
     * dk.i1.diameter.Utils.setMandatorX and
     * net.java.slee.resource.diameter.utils. DRAUtils.setMandatory_TS...
     * methods.
     * 
     * @param destinationHost -
     *            simply FQDN of destination host:<b>"diamgate.nist.gov" - use
     *            only when destinationHost is not directly connected to this
     *            node. If destinationHost is connected fill with null - it will
     *            use value specified in creation of transactions object.</b>
     * @param destinationRealm -
     *            simply domain name of destination host:<b>"nist.gov"- use
     *            only when destinationRealmt is not directly connected to this
     *            node. If destinationRealm is connected fill with null - it
     *            will use value specified in creation of transactions object.</b>
     * @param userIdentity -
     *            user identifier for which we make this reqest:<b>"sip:pinky@hells.kitchen.org"</b> -
     *            it becomes part of User-Identity AVP.
     * @param vendorSpecifiAppID -
     *            Vendor specific application ID for this app.
     * @param userData -
     *            user data which has changed.
     * @param avps -
     *            Iterator containing AVPs that will be added as additional
     *            payload.
     */
    public Message makeShPushNotificationRequest(String destinationHost,
            String destiantionRealm, AVP_Grouped userIdentity,
            AVP_Grouped vendorSpecifiAppID, String userData, Iterator<AVP> avps) {

        Message msg = new Message();

        // < Push-Notification-Request > ::= < Diameter Header: 309, REQ, PXY,
        // 16777217 >
        msg.hdr.setProxiable(true);
        msg.hdr.setRequest(true);
        msg.hdr.application_id = applicationID;
        msg.hdr.command_code = DIAMETER_SH_COMMAND_SUBSCRIBE_NOTIFICATIONS;
        // < Session-Id >
        msg.add(new AVP_UTF8String(DI_SESSION_ID, sessionID));
        // { Vendor-Specific-Application-Id }
        msg.add(vendorSpecifiAppID);
        // { Auth-Session-State }
        msg.add(new AVP_Unsigned32(DI_AUTH_SESSION_STATE, authSessionState));
        // { Origin-Host }
        // { Origin-Realm }
        d2sbb.addOurHostAndRealm(msg);
        // { Destination-Realm }
        if (destinationRealm != null)
            msg.add(new AVP_UTF8String(DI_DESTINATION_REALM, destinationRealm));
        else
            msg.add(new AVP_UTF8String(DI_DESTINATION_REALM,
                    this.destinationRealm));
        // { Destination-Host }
        if (destinationHost != null)
            msg.add(new AVP_UTF8String(DI_DESTINATION_HOST, destinationHost));
        else
            msg.add(new AVP_UTF8String(DI_DESTINATION_HOST,
                    this.destinationHost));
        // { User-Identity }
        msg.add(userIdentity);
        // { User-Data }
        msg.add(new AVP_OctetString(DI_USER_DATA, userData.getBytes()));
        // *[ AVP ]
        AVP avp = null;
        while (avps.hasNext()) {
            avp = avps.next();
            msg.add(avp);

        }
        // *[ Proxy-Info ]
        // *[ Route-Record ]

        Utils.setMandatory_RFC3588(msg);
        Utils.setMandatory_RFC4006(msg);
        DRAUtils.setMandatory_TS_29_329(msg);
        return msg;

    }

    /**
     * Creates UDA message. Uses prepareAnswer method of dk.i1.Message to create answer.<br> Method fills destiantion host and realm etc ( all
     * passed values are used to create AVPs) and adds this node as origin node.
     * To other AVPs refer to page <b>7</b> of TS 29.329 and
     * http://wiki.java.net/bin/view/Communications/DiameterShInterface#Conditions_for_Information_Eleme
     * <br>
     * This method set all "M" bits and so on in AVPs with use of
     * dk.i1.diameter.Utils.setMandatorX and
     * net.java.slee.resource.diameter.utils. DRAUtils.setMandatory_TS...
     * methods.
     * 
     * @param request -
     *            UDR to which answer will be created.
     * @param resultCode -
     *            result code which should be turned int AVP, if null its
     *            ommited
     * @param experimentaResultCode -
     *            experimantal result code which should be turned into AVP, if
     *            null it is ommited. Vendor id is set to ShProtocolConstantsDIAMETER_3GPP_VENDOR_ID.
     * @param userData -
     *            user data which will be sent back, has to be always present.
     * @param avps -
     *            list of additional AVPs which are going to be added to answer.
     */
    public Message makeShUserDataAnswer(Message request, Integer resultCode,
            Integer experimentaResultCode, String userData, Iterator<AVP> avps) throws IllegalArgumentException{

        if (request.hdr.command_code != DIAMETER_SH_COMMAND_USER_DATA) {
            throw new IllegalArgumentException("^^^ REQUEST COMMAND CODE DOESNT NOT MATCH COMMAND CODE OF ANSWER TO BE CREATED ^^^");
        }
        Message ans = new Message();
        
        // < User-Data-Answer > ::= < Diameter Header: 306, PXY, 16777217 >
        ans.prepareAnswer(request);
        // < Session-Id >
        // TODO: OR SHOULD WE PUT LOCAL VARIABLE "sessionId" VALUE HERE, INSTEAD
        // OF LOOKING IT UP?
        ans.add(request.find(DI_SESSION_ID));
        // { Vendor-Specific-Application-Id }
        AVP avp = request.find(DI_VENDOR_SPECIFIC_APPLICATION_ID);
        ans.add(avp);
        // [ Result-Code ]
        if (resultCode != null)
            ans.add(new AVP_Unsigned32(DI_RESULT_CODE, resultCode));
        // [ Experimental-Result ]
        if (experimentaResultCode != null) {
            AVP_Grouped res=new AVP_Grouped(DI_EXPERIMENTAL_RESULT,DIAMETER_3GPP_VENDOR_ID,new AVP_Unsigned32(DI_EXPERIMENTAL_RESULT_CODE, experimentaResultCode));
            ans.add(res);
        }
        // { Auth-Session-State }
        ans.add(new AVP_Unsigned32(DI_AUTH_SESSION_STATE, authSessionState));
        // { Origin-Host }
        // { Origin-Realm }
        d2sbb.addOurHostAndRealm(ans);
        // [ User-Data ]
        ans.add(new AVP_OctetString(DI_USER_DATA, userData.getBytes()));
        // *[ AVP ]
        avp = null;
        while (avps.hasNext()) {
            avp = avps.next();
            ans.add(avp);

        }
        // *[ Proxy-Info ]
        // *[ Route-Record ]

        Utils.setMandatory_RFC3588(ans);
        Utils.setMandatory_RFC4006(ans);
        DRAUtils.setMandatory_TS_29_329(ans);

        return ans;
    }

    /**
     * Creates PUA message.  Uses prepareAnswer method of dk.i1.Message to create answer.<br>Method fills destiantion host and realm etc ( all
     * passed values are used to create AVPs) and adds this node as origin node.
     * To other AVPs refer to page <b>8</b> of TS 29.329 and
     * http://wiki.java.net/bin/view/Communications/DiameterShInterface#Conditions_for_Information_Eleme
     * <br>
     * This method set all "M" bits and so on in AVPs with use of
     * dk.i1.diameter.Utils.setMandatorX and
     * net.java.slee.resource.diameter.utils. DRAUtils.setMandatory_TS...
     * methods.
     * 
     * @param req -
     *            request to which answer will be created.
     * @param resultCode -
     *            result code which should be turned int AVP, if null its
     *            ommited
     * @param experimentaResultCode -
     *            experimantal result code which should be turned into AVP, if
     *            null it is ommited. Vendor id is set to ShProtocolConstantsDIAMETER_3GPP_VENDOR_ID. 
     * @param avps -
     *            list of additional AVPs which are going to be added to answer.
     */
    public Message makeShProfileUpdateAnswer(Message req, Integer resultCode,
            Integer experimentalResultCode, Iterator<AVP> avps) throws IllegalArgumentException {

        if (req.hdr.command_code != DIAMETER_SH_COMMAND_PROFILE_UPDATE) {
            throw new IllegalArgumentException("^^^ REQUEST COMMAND CODE DOESNT NOT MATCH COMMAND CODE OF ANSWER TO BE CREATED ^^^");
        }
        Message ans = new Message();
        
        // < Profile-Update-Answer > ::=< Diameter Header: 307, PXY, 16777217 >
        ans.prepareAnswer(req);
        // < Session-Id > 
        ans.add(req.find(DI_SESSION_ID));
        // { Vendor-Specific-Application-Id }
        AVP avp = req.find(DI_VENDOR_SPECIFIC_APPLICATION_ID);
        ans.add(avp);
        // [ Result-Code ]
        if (resultCode != null)
            ans.add(new AVP_Unsigned32(DI_RESULT_CODE, resultCode));
        // [ Experimental-Result ]
        if (experimentalResultCode != null) {
            AVP_Grouped res=new AVP_Grouped(DI_EXPERIMENTAL_RESULT,DIAMETER_3GPP_VENDOR_ID,new AVP_Unsigned32(DI_EXPERIMENTAL_RESULT_CODE, experimentalResultCode));
            ans.add(res);
        }
        // { Auth-Session-State }
        ans.add(new AVP_Unsigned32(DI_AUTH_SESSION_STATE, authSessionState));
        // { Origin-Host }
        // { Origin-Realm }
        d2sbb.addOurHostAndRealm(ans);
        // *[AVP]
        avp = null;
        while (avps.hasNext()) {
            avp = avps.next();
            ans.add(avp);

        }
        // *[ Proxy-Info ]
        // *[ Route-Record ]

        Utils.setMandatory_RFC3588(ans);
        Utils.setMandatory_RFC4006(ans);
        DRAUtils.setMandatory_TS_29_329(ans);

        return ans;
    }
    /**
     * Creates SNA message.  Uses prepareAnswer method of dk.i1.Message to create answer.<br>Method fills destiantion host and realm etc ( all
     * passed values are used to create AVPs) and adds this node as origin node.
     * To other AVPs refer to page <b>8</b> of TS 29.329 and
     * http://wiki.java.net/bin/view/Communications/DiameterShInterface#Conditions_for_Information_Eleme
     * <br>
     * This method set all "M" bits and so on in AVPs with use of
     * dk.i1.diameter.Utils.setMandatorX and
     * net.java.slee.resource.diameter.utils. DRAUtils.setMandatory_TS...
     * methods.
     * 
     * @param req -
     *            request to which answer will be created.
     * @param resultCode -
     *            result code which should be turned int AVP, if null its
     *            ommited
     * @param experimentaResultCode -
     *            experimantal result code which should be turned into AVP, if
     *            null it is ommited. Vendor id is set to ShProtocolConstantsDIAMETER_3GPP_VENDOR_ID.
     * @param avps -
     *            list of additional AVPs which are going to be added to answer.
     */
    public Message makeShSubscribeNotificationAnswer(Message req,
            Integer resultCode, Integer experimentalResultCode,
            Iterator<AVP> avps) throws IllegalArgumentException{

        if (req.hdr.command_code != DIAMETER_SH_COMMAND_SUBSCRIBE_NOTIFICATIONS) {
            throw new IllegalArgumentException("^^^ REQUEST COMMAND CODE DOESNT NOT MATCH COMMAND CODE OF ANSWER TO BE CREATED ^^^");
        }
        Message ans = new Message();
        
        // < Subscribe-Notifications-Answer > ::= < Diameter Header: 308, PXY,
        // 16777217 >
        ans.prepareAnswer(req);
        // < Session-Id >
        ans.add(req.find(DI_SESSION_ID));
        // { Vendor-Specific-Application-Id }
        AVP avp = req.find(DI_VENDOR_SPECIFIC_APPLICATION_ID);
        ans.add(avp);
        // [ Result-Code ]
        if (resultCode != null)
            ans.add(new AVP_Unsigned32(DI_RESULT_CODE, resultCode));
        // [ Experimental-Result ]
        if (experimentalResultCode != null) {
            AVP_Grouped res=new AVP_Grouped(DI_EXPERIMENTAL_RESULT,DIAMETER_3GPP_VENDOR_ID,new AVP_Unsigned32(DI_EXPERIMENTAL_RESULT_CODE,                    experimentalResultCode));
            ans.add(res);
        }
        // { Auth-Session-State }
        ans.add(new AVP_Unsigned32(DI_AUTH_SESSION_STATE, authSessionState));
        // { Origin-Host }
        // { Origin-Realm }
        d2sbb.addOurHostAndRealm(ans);
        // *[ AVP ]
        avp = null;
        while (avps.hasNext()) {
            avp = avps.next();
            ans.add(avp);

        }
        // *[ Proxy-Info ]
        // *[ Route-Record ]

        Utils.setMandatory_RFC3588(ans);
        Utils.setMandatory_RFC4006(ans);
        DRAUtils.setMandatory_TS_29_329(ans);

        return ans;
    }

    /**
     * Creates PNA message.  Uses prepareAnswer method of dk.i1.Message to create answer.<br>Method fills destiantion host and realm etc ( all
     * passed values are used to create AVPs) and adds this node as origin node.
     * To other AVPs refer to page <b>9</b> of TS 29.329 and
     * http://wiki.java.net/bin/view/Communications/DiameterShInterface#Conditions_for_Information_Eleme
     * <br>
     * This method set all "M" bits and so on in AVPs with use of
     * dk.i1.diameter.Utils.setMandatorX and
     * net.java.slee.resource.diameter.utils. DRAUtils.setMandatory_TS...
     * methods.
     * 
     * @param req -
     *            request to which answer will be created.
     * @param resultCode -
     *            result code which should be turned int AVP, if null its
     *            ommited
     * @param experimentaResultCode -
     *            experimantal result code which should be turned into AVP, if
     *            null it is ommited. Vendor id is set to ShProtocolConstantsDIAMETER_3GPP_VENDOR_ID.
     * @param avps -
     *            list of additional AVPs which are going to be added to answer.
     */
    public Message makeShPushNotificationAnswer(Message req,
            Integer resultCode, Integer experimentalResultCode,
            Iterator<AVP> avps) throws IllegalArgumentException{

        if (req.hdr.command_code != DIAMETER_SH_COMMAND_PUSH_NOTIFICATION) {
            throw new IllegalArgumentException("^^^ REQUEST COMMAND CODE DOESNT NOT MATCH COMMAND CODE OF ANSWER TO BE CREATED ^^^");
        }
        Message ans = new Message();

        // < Subscribe-Notifications-Answer > ::= < Diameter Header: 309, PXY,
        // 16777217 >
        ans.prepareAnswer(req);
        // < Session-Id >
        ans.add(req.find(DI_SESSION_ID));
        // { Vendor-Specific-Application-Id }
        AVP avp = req.find(DI_VENDOR_SPECIFIC_APPLICATION_ID);
        ans.add(avp);
        // [ Result-Code ]
        if (resultCode != null)
            ans.add(new AVP_Unsigned32(DI_RESULT_CODE, resultCode));
        // [ Experimental-Result ]
        if (experimentalResultCode != null) {
            AVP_Grouped res=new AVP_Grouped(DI_EXPERIMENTAL_RESULT,DIAMETER_3GPP_VENDOR_ID,new AVP_Unsigned32(DI_EXPERIMENTAL_RESULT_CODE, experimentalResultCode));
            ans.add(res);
        }
        // { Auth-Session-State }
        ans.add(new AVP_Unsigned32(DI_AUTH_SESSION_STATE, authSessionState));
        // { Origin-Host }
        // { Origin-Realm }
        d2sbb.addOurHostAndRealm(ans);
        // *[ AVP ]
        avp = null;
        while (avps.hasNext()) {
            avp = avps.next();
            ans.add(avp);

        }
        // *[ Proxy-Info ]
        // *[ Route-Record ]

        Utils.setMandatory_RFC3588(ans);
        Utils.setMandatory_RFC4006(ans);
        DRAUtils.setMandatory_TS_29_329(ans);

        return ans;
    }

}
