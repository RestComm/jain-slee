package net.java.slee.resource.diameter.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.java.slee.resource.diameter.ShProtocolConstants;

import dk.i1.diameter.AVP;
import dk.i1.diameter.Message;

import static net.java.slee.resource.diameter.ShProtocolConstants.*;
import static dk.i1.diameter.ProtocolConstants.*;
public final class DRAUtils {

    
    private DRAUtils(){};
    
    //CODES LIST OF AVPS THAT ARE MEANT TO BE MANDATORY WITH 3GPP VENDOR ID
    private static List<Integer> TS_29_329_Mandatory;
    
    
    //LETS INITIALIZE TS_29_329_Mandatory
    static
    {
        Integer[] tmp={DI_USER_IDENTITY,DI_MSISDN,DI_USER_DATA,DI_DATA_REFERENCE,DI_SERVICE_INDICATION,DI_SUBSCRIBE_TYPE,DI_REQUESTED_DOMAIN,DI_CURRENT_LOCATION,DI_SERVER_NAME,DI_PUBLIC_IDENTITY};
        TS_29_329_Mandatory=Arrays.asList(tmp);
    }
    /**
     * Sets "M" and vendor id in all AVPs that are eligible for that. See TS 29.329.
     * @param msg
     */
    public static void setMandatory_TS_29_329(Message msg)
    {
           Iterable<AVP> it=msg.avps();
           for(AVP avp : it)
           {
               
               if(TS_29_329_Mandatory.contains(avp.code))
               {
                   avp.setMandatory(true);
                   avp.vendor_id=DIAMETER_3GPP_VENDOR_ID;
               }
               
           }
    }
}
