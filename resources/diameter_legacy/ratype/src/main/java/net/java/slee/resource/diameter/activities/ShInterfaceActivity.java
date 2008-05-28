package net.java.slee.resource.diameter.activities;

import java.util.Iterator;

import dk.i1.diameter.AVP;
import dk.i1.diameter.AVP_Grouped;
import dk.i1.diameter.AVP_Integer32;
import dk.i1.diameter.Message;

public interface ShInterfaceActivity extends ActivityBaseInterface{

    
    
//CLIENT TYPE OF MESSAGES
    
    
    public Message makeShUserDataRequest(String destinationHost,String destiantionRealm,AVP_Grouped userIdentity,AVP_Grouped vendorSpecifiAppID,int dataReference,Iterator<AVP> avps);
    
    public Message makeShProfileUpdateRequest(String destinationHost,String destiantionRealm,AVP_Grouped userIdentity,AVP_Grouped vendorSpecifiAppID,String dataValue,Iterator<AVP> avps);
    
    public Message makeShSubscribeNotificationRequest(String destinationHost,String destiantionRealm,AVP_Grouped userIdentity,AVP_Grouped vendorSpecificAppID,int subscriptionType,int dataRef,Iterator<AVP> avps);
    public Message makeShPushNotificationAnswer(Message req, Integer resultCode, Integer experimentalResultCode, Iterator<AVP> avps )throws IllegalArgumentException;
    
    
    
    
    
    
    
    //SERVER TYPE OF EVENTS
    
    
    
    public Message makeShPushNotificationRequest(String destinationHost,String destiantionRealm,AVP_Grouped userIdentity,AVP_Grouped vendorSpecifiAppID,String userData,Iterator<AVP> avps);  
    public Message makeShUserDataAnswer(Message request,Integer resuleCode,Integer experimentaResultCode,String userData,Iterator<AVP> avps) throws IllegalArgumentException;  
    public Message makeShProfileUpdateAnswer(Message req, Integer resultCode, Integer experimentalResultCode, Iterator<AVP> avps )throws IllegalArgumentException;
    public Message makeShSubscribeNotificationAnswer(Message req, Integer resultCode, Integer experimentalResultCode, Iterator<AVP> avps )throws IllegalArgumentException;
    
    
}
