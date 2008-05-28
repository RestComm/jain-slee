package net.java.slee.resource.diameter.activities;

import static dk.i1.diameter.ProtocolConstants.*;


import java.util.Iterator;

import net.java.slee.resource.diameter.DiameterRAActivityHandle;
import net.java.slee.resource.diameter.DiameterResourceAdaptorSbbInterface;
import dk.i1.diameter.AVP;
import dk.i1.diameter.AVP_Grouped;
import dk.i1.diameter.AVP_UTF8String;
import dk.i1.diameter.AVP_Unsigned32;
import dk.i1.diameter.Message;
import dk.i1.diameter.Utils;
import dk.i1.diameter.node.ConnectionKey;

public class BaseProtocolActivityImpl extends DiameterActivityCommonPart
        implements BaseProtocolActivity {

    public BaseProtocolActivityImpl(String destHost, String destRealm,
            int applciationID, String sessID, int authSessionState,
            ConnectionKey key, DiameterRAActivityHandle DAH,DiameterResourceAdaptorSbbInterface d2sbb) {
        super(destHost, destRealm, applciationID, sessID, authSessionState,
                key, DAH,d2sbb);
    }

    public Message createBaseProtocolAbortSessionRequest(
            String destinationHost, String destinationRealm, String userName,Iterator<AVP> AVPS) {


        Message msg = new Message();
        // <ASR> ::= < Diameter Header: 274, REQ, PXY >
        msg.hdr.command_code = DIAMETER_COMMAND_ABORT_SESSION;
        msg.hdr.application_id = applicationID;
        msg.hdr.setProxiable(true);
        msg.hdr.setRequest(true);
        // < Session-Id >
        msg.add(new AVP_UTF8String(DI_SESSION_ID, sessionID));
        // { Origin-Host }
        // { Origin-Realm }
        d2sbb.addOurHostAndRealm(msg);
        // { Destination-Realm }
        if (destinationRealm != null)
            msg.add(new AVP_UTF8String(DI_DESTINATION_REALM, destinationRealm));
        else
            msg.add(new AVP_UTF8String(DI_DESTINATION_REALM, this.destinationRealm));
        // { Destination-Host }
        if (destinationHost != null)
            msg.add(new AVP_UTF8String(DI_DESTINATION_HOST, destinationHost));
        else
            msg.add(new AVP_UTF8String(DI_DESTINATION_HOST, this.destinationHost));
        // { Auth-Application-Id }
        msg.add(new AVP_Unsigned32(DI_AUTH_APPLICATION_ID, applicationID));
        // [ User-Name ]
        if (userName != null)
            msg.add(new AVP_UTF8String(DI_USER_NAME, userName));
      
        // [ Origin-State-Id ]
        // * [ Proxy-Info ]
        // * [ Route-Record ]
        // * [ AVP ]
        AVP avp=null;
        while(AVPS.hasNext())
        {
            avp=AVPS.next();
            msg.add(avp);
            
        }
        
        
        Utils.setMandatory_RFC3588(msg);
        Utils.setMandatory_RFC4006(msg);
        return msg;
        
        
    }

    public Message createBaseProtocolReAuthRequest(String destinationHost,
            String destinationRealm, int reAuthType, String userName,Iterator<AVP> AVPS) {

        
        Message msg = new Message();
        // <RAR> ::= < Diameter Header: 258, REQ, PXY >
        msg.hdr.command_code = DIAMETER_COMMAND_REAUTH;
        msg.hdr.application_id = applicationID;
        msg.hdr.setProxiable(true);
        msg.hdr.setRequest(true);
        // < Session-Id >
        msg.add(new AVP_UTF8String(DI_SESSION_ID, sessionID));
        // { Origin-Host }
        // { Origin-Realm }
        d2sbb.addOurHostAndRealm(msg);
        // { Destination-Realm }
        if (destinationRealm != null)
            msg.add(new AVP_UTF8String(DI_DESTINATION_REALM, destinationRealm));
        else
            msg.add(new AVP_UTF8String(DI_DESTINATION_REALM, this.destinationRealm));
        // { Destination-Host }
        if (destinationHost != null)
            msg.add(new AVP_UTF8String(DI_DESTINATION_HOST, destinationHost));
        else
            msg.add(new AVP_UTF8String(DI_DESTINATION_HOST, this.destinationHost));

        // TODO : CHECK FOR CONDITIONS? >0 ?
        // { Auth-Application-Id }
        msg.add(new AVP_Unsigned32(DI_AUTH_APPLICATION_ID, applicationID));
        // { Re-Auth-Request-Type }
        msg.add(new AVP_Unsigned32(DI_RE_AUTH_REQUEST_TYPE, reAuthType));
        // [ User-Name ]
        if (userName != null)
            msg.add(new AVP_UTF8String(DI_USER_NAME, userName));
        // [ Origin-State-Id ]
        // * [ Proxy-Info ]
        // * [ Route-Record ]
        // * [ AVP ]
        AVP avp=null;
        while(AVPS.hasNext())
        {
            avp=AVPS.next();
            msg.add(avp);
            
        }
        Utils.setMandatory_RFC3588(msg);
        Utils.setMandatory_RFC4006(msg);
        return msg;
        
        
        
    }

    public Message createBaseProtocolAccountingRequest(String destRealm,
            int accRecordType, int accRecordNumber,Iterator<AVP> AVPS) {

        Message msg = new Message();

        // <ACR> ::= < Diameter Header: 271, REQ, PXY >
        msg.hdr.command_code = DIAMETER_COMMAND_ACCOUNTING;
        msg.hdr.application_id = applicationID;
        msg.hdr.setProxiable(true);
        msg.hdr.setRequest(true);
        // < Session-Id >
        msg.add(new AVP_UTF8String(DI_SESSION_ID, sessionID));
        // { Origin-Host }
        // { Origin-Realm }
        d2sbb.addOurHostAndRealm(msg);
        // { Destination-Realm }
        if (destRealm != null)
            msg.add(new AVP_UTF8String(DI_DESTINATION_REALM, destRealm));
        else
            msg.add(new AVP_UTF8String(DI_DESTINATION_REALM, destinationRealm));
        // { Accounting-Record-Type }
        msg.add(new AVP_Unsigned32(DI_ACCOUNTING_RECORD_TYPE, accRecordType));
        // { Accounting-Record-Number }
        msg.add(new AVP_Unsigned32(DI_ACCOUNTING_RECORD_NUMBER,
                        accRecordNumber));
        // [ Acct-Application-Id ]
        // [ Vendor-Specific-Application-Id ]
        // [ User-Name ]
        // [ Accounting-Sub-Session-Id ]
        // [ Acct-Session-Id ]
        // [ Acct-Multi-Session-Id ]
        // [ Acct-Interim-Interval ]
        // [ Accounting-Realtime-Required ]
        // [ Origin-State-Id ]
        // [ Event-Timestamp ]
        // * [ Proxy-Info ]
        // * [ Route-Record ]
        // * [ AVP ]
        AVP avp=null;
        while(AVPS.hasNext())
        {
            avp=AVPS.next();
            msg.add(avp);
            
        }
        
        Utils.setMandatory_RFC3588(msg);
        Utils.setMandatory_RFC4006(msg);
        return msg;
    }

    public Message createBaseProtocolSessionTerminationRequest(
            String destinationHost, String destinationRealm, int originState,
            String userName, int terminationCause,Iterator<AVP> AVPS) {


        
        
Message msg= new Message();
        
        
        //<STR> ::= < Diameter Header: 275, REQ, PXY >
        msg.hdr.command_code=DIAMETER_COMMAND_SESSION_TERMINATION;
        msg.hdr.application_id=this.applicationID;
        msg.hdr.setRequest(true);
        msg.hdr.setProxiable(true);
        
        //< Session-Id >
        msg.add(new AVP_UTF8String(DI_SESSION_ID,sessionID));
        
        //{ Origin-Host }
        //{ Origin-Realm }
        // DRA Provider method
        d2sbb.addOurHostAndRealm(msg);
        //{ Destination-Realm }
        if(destinationRealm!=null)
            msg.add(new AVP_UTF8String(DI_DESTINATION_REALM,destinationRealm));
        else
            msg.add(new AVP_UTF8String(DI_DESTINATION_REALM,this.destinationRealm));
        //{ Auth-Application-Id }
        msg.add(new AVP_Unsigned32(DI_AUTH_APPLICATION_ID,applicationID));
        //{ Termination-Cause }
        msg.add(new AVP_Unsigned32(DI_TERMINATION_CAUSE,terminationCause));
        
        //WE AHVE ALL REQUIRED AVPS, LETS TAKE CARE OF SOME THAT MAY BE PRESENT
        //[ User-Name ]
        if(userName!=null)
            msg.add(new AVP_UTF8String(DI_USER_NAME,userName));
        //[ Destination-Host ]
        if(destinationHost!=null)
            msg.add(new AVP_UTF8String(DI_DESTINATION_HOST,destinationHost));
        else
            msg.add(new AVP_UTF8String(DI_DESTINATION_HOST,this.destinationHost));
      //* [ Class ]
        
        //[ Origin-State-Id ]
      if(originState>=0)
          msg.add(new AVP_Unsigned32(DI_ORIGIN_STATE_ID,originState));
      //* [ Proxy-Info ]
      //* [ Route-Record ]
      //* [ AVP ]
      AVP avp=null;
      while(AVPS.hasNext())
      {
          avp=AVPS.next();
          msg.add(avp);
          
      }
      Utils.setMandatory_RFC3588(msg);
      Utils.setMandatory_RFC4006(msg);
        return msg;
    }

    public Message createBaseProtocolAbortSessionAnswer(Message msg,
            int resultCode,Iterator<AVP> AVPS) {


        Message ans=new Message(); 
//      <ASA>  ::= < Diameter Header: 274, PXY >
        ans.prepareAnswer(msg);
        //TODO: THROW EXCEPTION IF NOT PRESENT?
        //< Session-Id >
        AVP SSID=msg.find(DI_SESSION_ID);
        ans.add(SSID);
        //{ Result-Code }
        //TODO: CHECK Result-Code >0 ??
        ans.add(new AVP_Unsigned32(DI_RESULT_CODE,resultCode));    
        //{ Origin-Host }
        //{ Origin-Realm }
        d2sbb.addOurHostAndRealm(ans);
        //[ User-Name ]
        //[ Origin-State-Id ]
        //[ Error-Message ]
        //[ Error-Reporting-Host ]
        //* [ Failed-AVP ]
        //* [ Redirect-Host ]
        //[ Redirect-Host-Usage ]
        //[ Redirect-Max-Cache-Time ]
        //* [ Proxy-Info ]
        //* [ AVP ]
        AVP avp=null;
        while(AVPS.hasNext())
        {
            avp=AVPS.next();
            ans.add(avp);
            
        }
        Utils.setMandatory_RFC3588(ans);
        Utils.setMandatory_RFC4006(ans);
        return ans;
        
    }

    public Message createBaseProtocolReAuthAnswer(Message msg, int resultCode,Iterator<AVP> AVPS) {


Message ans=new Message();
        
        //<RAA>  ::= < Diameter Header: 258, PXY >
        ans.prepareAnswer(msg);
        //TODO: THROW EXPTEION IF NOT PRESENT?
        //< Session-Id >
        AVP SSID=msg.find(DI_SESSION_ID);
        ans.add(SSID);
        //{ Result-Code }
        //TODO: CHECK Result-Code >0 ??
        ans.add(new AVP_Unsigned32(DI_RESULT_CODE,resultCode));    
        //{ Origin-Host }
        //{ Origin-Realm }
        d2sbb.addOurHostAndRealm(ans);
        //[ User-Name ]
        //[ Origin-State-Id ]
        //[ Error-Message ]
        //[ Error-Reporting-Host ]
        //* [ Failed-AVP ]
        //* [ Redirect-Host ]
        //[ Redirect-Host-Usage ]
        //[ Redirect-Host-Cache-Time ]
        //* [ Proxy-Info ]
        //* [ AVP ]
        AVP avp=null;
        while(AVPS.hasNext())
        {
            avp=AVPS.next();
            ans.add(avp);
            
        }
        Utils.setMandatory_RFC3588(ans);
        Utils.setMandatory_RFC4006(ans);
        return ans;
        
    }

    public Message createBaseProtocolSessionTerminationAnswer(Message msg,
            int resultCode,Iterator<AVP> AVPS) {


        
        Message ans=new Message();
        // <STA> ::= < Diameter Header: 275, PXY >
        ans.prepareAnswer(msg);
        ans.hdr.setRequest(false);
        // < Session-Id >
        ans.add(new AVP_UTF8String(DI_SESSION_ID, sessionID));
        // { Result-Code }
//      { Origin-Host }
        // { Origin-Realm }
        d2sbb.addOurHostAndRealm(msg);
        // [ User-Name ]
        AVP avp=(AVP)msg.find(DI_USER_NAME);
        if(avp!=null)
            ans.add(avp);
        // * [ Class ]
        // [ Error-Message ]
        // [ Error-Reporting-Host ]
        // * [ Failed-AVP ]
        // [ Origin-State-Id ]
        // * [ Redirect-Host ]
        // [ Redirect-Host-Usage ]
        // [ Redirect-Max-Cache-Time ]
        // * [ Proxy-Info ]
        // * [ AVP ]
        avp=null;
        while(AVPS.hasNext())
        {
            avp=AVPS.next();
            ans.add(avp);
            
        }
        Utils.setMandatory_RFC3588(ans);
        Utils.setMandatory_RFC4006(ans);
        return ans;
        
        
    }

    public Message createBaseProtocolAccountingAnswer(Message msg,
            int resultCode, AVP_Grouped vendorSpecificAppId,Iterator<AVP> AVPS) {


//      <ACA> ::= < Diameter Header: 271, PXY >
        // < Session-Id >
        // { Result-Code }
        // { Origin-Host }
        // { Origin-Realm }
        // { Accounting-Record-Type }
        // { Accounting-Record-Number }
        // [ Acct-Application-Id ]
        // [ Vendor-Specific-Application-Id ]
        // [ User-Name ]
        // [ Accounting-Sub-Session-Id ]
        // [ Acct-Session-Id ]
        // [ Acct-Multi-Session-Id ]
        // [ Error-Reporting-Host ]
        // [ Acct-Interim-Interval ]
        // [ Accounting-Realtime-Required ]
        // [ Origin-State-Id ]
        // [ Event-Timestamp ]
        // * [ Proxy-Info ]
        // * [ AVP ]
        //Utils.setMandatory_RFC3588(ans);
        //Utils.setMandatory_RFC4006(ans);
        return null;
    }

}
