/*
 * File Name     : Utils.java
 *
 * The JAIN MGCP API implementaion.
 *
 * The source code contained in this file is in in the public domain.
 * It can be used in any project or product without prior permission,
 * license or royalty payments. There is  NO WARRANTY OF ANY KIND,
 * EXPRESS, IMPLIED OR STATUTORY, INCLUDING, WITHOUT LIMITATION,
 * THE IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE,
 * AND DATA ACCURACY.  We do not warrant or make any representations
 * regarding the use of the software or the  results thereof, including
 * but not limited to the correctness, accuracy, reliability or
 * usefulness of the software.
 */
package org.mobicents.mgcp.parser;

import java.text.ParseException;
import jain.protocol.ip.mgcp.message.parms.*;
import jain.protocol.ip.mgcp.pkg.*;

/**
 *
 * @author Oleg Kulikov
 * @author Pavel Mitrenko
 */
public class Utils {

    /** Creates a new instance of Utils */
    public Utils() {
    }

    /**
     * Instantiates endpoint identifier as Jain MGCP EndpointIdentifier object.
     *
     * @param name the fully qualified name of the endpoint.
     * @return EndpointIdentifier object.
     */
    public static EndpointIdentifier createEndpointIdentifier(String name) {
        String tokens[] = name.split("@");
        return new EndpointIdentifier(tokens[0], tokens[1]);
    }

    /**
     * Create ConnectionMode object from given text value.
     *
     * @param mode the text value of the connection mode.
     * @return the ConnectionMode object.
     */
    public static ConnectionMode createConnectionMode(String mode) {
        if (mode.equals("sendrecv")) {
            return ConnectionMode.SendRecv;
        } else if (mode.equalsIgnoreCase("sendonly")) {
            return ConnectionMode.SendOnly;
        } else if (mode.equalsIgnoreCase("recvonly")) {
            return ConnectionMode.RecvOnly;
        } else if (mode.equalsIgnoreCase("confrnce")) {
            return ConnectionMode.Confrnce;
        } else if (mode.equalsIgnoreCase("conttest")) {
            return ConnectionMode.Conttest;
        } else if (mode.equalsIgnoreCase("data")) {
            return ConnectionMode.Data;
        } else if (mode.equalsIgnoreCase("loopback")) {
            return ConnectionMode.Loopback;
        } else if (mode.equalsIgnoreCase("netwloop")) {
            return ConnectionMode.Netwloop;
        } else if (mode.equalsIgnoreCase("netwtest")) {
            return ConnectionMode.Netwtest;
        }
        return ConnectionMode.Inactive;
    }

    /**
     * Create BearerInformation object from given text.
     *
     * @param text the text value of the object.
     * @return BearerInformation object.
     */
    public static BearerInformation createBearerInformation(String text) throws ParseException {
        // BearerInformation =BearerAttribute 0*(","0*(WSP)BearerAttribute)
        // BearerAttribute =("e"":"BearerEncoding)
        // (BearerExtensionName [":"BearerExtensionValue ])
        // BearerExtensionName =PackageLCOExtensionName
        // BearerExtensionValue =LocalOptionExtensionValue
        // BearerEncoding ="A"/"mu"

        text = text.toLowerCase();
        if (!text.startsWith("e:")) {
            throw new ParseException("Bearer extensions not supported", 0);
        }
        
        text = text.substring(text.indexOf(":") + 1).trim();
        
        if (text.equals("a")) {
            return BearerInformation.EncMethod_A_Law;
        } else if (text.equals("mu")) {
            return BearerInformation.EncMethod_mu_Law;
        } else {
            throw new ParseException("Unknown value for BearerInformation: " + text, 0);
        }
    }
    
    /**
     * Create local connection options from given text.
     *
     * @param text the text value.
     * @return array of LocalOptionValue objects.
     */
    public static LocalOptionValue[] createLocalOptions(String text) throws ParseException {
        // LocalConnectionOptions =LocalOptionValue 0*(WSP)
        // 0*(","0*(WSP)LocalOptionValue 0*(WSP))
        
        String[] tokens = text.split(",");
        LocalOptionValue[] options = new LocalOptionValue[tokens.length];
        
        for (int i = 0; i < tokens.length; i++) {
            options[i] = createLocalOption(tokens[i]);
        }
        
        return options;
    }
    
    /**
     * Create local connection object from given text.
     *
     * @param text the text value of the LocalOptionValue object.
     * @return LocalOption object.
     */
    public static LocalOptionValue createLocalOption(String text) throws ParseException {
        // LocalOptionValue =("p"":"packetizationPeriod)
        // /("a"":"compressionAlgorithm)
        // /("b"":"bandwidth)
        // /("e"":"echoCancellation)
        // /("gc"":"gainControl)
        // /("s"":"silenceSuppression)
        // /("t"":"typeOfService)
        // /("r"":"resourceReservation)
        // /("k"":"encryptiondata)
        // /("nt"":"(typeOfNetwork /
        // supportedTypeOfNetwork))
        // /(LocalOptionExtensionName
        // [":"LocalOptionExtensionValue ])
        
        int pos = text.indexOf(':');
        if (pos < 0) {
            throw new ParseException("Could not parse local connection option: " + text, 0);
        }
        
        String name = text.substring(0, pos).trim();
        String value = text.substring(pos +1).trim();
        
        if (name.equalsIgnoreCase("a")) {
            return createCompressionAlgorithm(value);
        } else if (name.equalsIgnoreCase("p")) {
            return createPacketizationPeriod(value);
        } else if (name.equalsIgnoreCase("b")) {
            return createBandwidth(value);
        } else if (name.equalsIgnoreCase("e")) {
            return decodeEchoCancellation(value);
        } else if (name.equalsIgnoreCase("gc")) {
            return decodeGainControl(value);
        } else if (name.equalsIgnoreCase("s")) {
            return decodeSilenceSuppression(value);
        } else if (name.equalsIgnoreCase("t")) {
            return decodeTypeOfService(value);
        } else if (name.equalsIgnoreCase("r")) {
            return decodeResourceReservation(value);
        } else if (name.equalsIgnoreCase("k")) {
            return decodeEncryptionMethod(value);
        } else if (name.equalsIgnoreCase("nt")) {
            return decodeTypeOfNetwork(value);
        } else {
            return new LocalOptionExtension(name, value);
        }
        
    }
    
    /**
     * Create CompressionAlgorithm object from given text.
     *
     * @param text the text value of the compression algoritm.
     * @return CompressionAlgorithm object.
     */
    public static CompressionAlgorithm createCompressionAlgorithm(String value)
    throws ParseException {
        // compressionAlgorithm =algorithmName 0*(";"algorithmName)
        return new CompressionAlgorithm(value.split(";"));
    }
    
    /**
     * Create PacketizationPeriod object from given text.
     *
     * @param text the text view of the PacketizationPeriod object.
     * @return PacketizationPeriod object.
     */
    public static PacketizationPeriod createPacketizationPeriod(String value)
    throws ParseException {
        // packetizationPeriod =1*4(DIGIT)["-"1*4(DIGIT)]
        int pos = value.indexOf('-');
        if (pos < 0) {
            try {
                return new PacketizationPeriod(Integer.parseInt(value));
            } catch (Exception e) {
                throw new ParseException("Invalid packetization period:" + value, 0);
            }
        } else {
            String low = value.substring(0, pos).trim();
            String hight = value.substring(pos + 1).trim();
            
            try {
                return new PacketizationPeriod(Integer.parseInt(low),
                        Integer.parseInt(hight));
            } catch (Exception e) {
                throw new ParseException("Invalid packetization period:" + value, 0);
            }
        }
    }
    
    /**
     * Create Bandwidth object from given text.
     *
     * @param text the text view of the Bandwidth object.
     * @return Bandwidth object.
     */
    public static Bandwidth createBandwidth(String value)
    throws ParseException {
        // bandwidth =1*4(DIGIT)["-"1*4(DIGIT)]
        int pos = value.indexOf('-');
        if (pos < 0) {
            try {
                return new Bandwidth(Integer.parseInt(value));
            } catch (Exception e) {
                throw new ParseException("Invalid packetization period:" + value, 0);
            }
        } else {
            String low = value.substring(0, pos).trim();
            String hight = value.substring(pos + 1).trim();
            
            try {
                return new Bandwidth(Integer.parseInt(low),
                        Integer.parseInt(hight));
            } catch (Exception e) {
                throw new ParseException("Invalid packetization period:" + value, 0);
            }
        }
    }
    
    /**
     * Decode EchoCancellation object from given text.
     *
     * @param text the text value of the EchoCancellation.
     * @return EchoCancellation object.
     */
    public static EchoCancellation decodeEchoCancellation(String value)
    throws ParseException {
        // echoCancellation ="on"/"off"
        if (value.equalsIgnoreCase("on")) {
            return EchoCancellation.EchoCancellationOn;
        } else if (value.equalsIgnoreCase("of")) {
            return EchoCancellation.EchoCancellationOff;
        } else {
            throw new ParseException("Invalid value for EchoCancellation :" +  value, 0);
        }
    }
    
    /**
     * Decode GainControl object from given text.
     *
     * @param text the text value of the GainControl.
     * @return GainControl object.
     */
    public static GainControl decodeGainControl(String value) throws ParseException {
        // gainControl ="auto"/["-"] 1**4(DIGIT)
        if (value.equalsIgnoreCase("auto")) {
            return new GainControl();
        } else {
            try {
                return new GainControl(Integer.parseInt(value));
            } catch (Exception e) {
                throw new ParseException("Invalid value for EchoCancellation :" +  value, 0);
            }
        }
    }
    
    /**
     * Decode SilenceSuppression object from given text.
     *
     * @param text the text value of the SilenceSuppression.
     * @return SilenceSuppression object.
     */
    public static SilenceSuppression decodeSilenceSuppression(String value)
    throws ParseException {
        // silenceSuppression ="on"/"off"
        if (value.equalsIgnoreCase("on")) {
            return SilenceSuppression.SilenceSuppressionOn;
        } else if (value.equalsIgnoreCase("of")) {
            return SilenceSuppression.SilenceSuppressionOff;
        } else {
            throw new ParseException("Invalid value for SilenceSuppression :" +  value, 0);
        }
    }
    
    /**
     * Decode TypeOfService object from given text.
     *
     * @param text the text value of the TypeOfService.
     * @return TypeOfService object.
     */
    public static TypeOfService decodeTypeOfService(String value)
    throws ParseException {
        // typeOfService =1*2(HEXDIG);1 hex only for capabilities
        try {
            return new TypeOfService((byte)Integer.parseInt(value));
        } catch (Exception e) {
            throw new ParseException("Invalid value for TypeOfService :" +  value, 0);
        }
    }
    
    /**
     * Decode ResourceReservation object from given text.
     *
     * @param text the text value of the ResourceReservation.
     * @return ResourceReservation object.
     */
    public static ResourceReservation decodeResourceReservation(String value)
    throws ParseException {
        // resourceReservation ="g"/"cl"/"be"
        if (value.equalsIgnoreCase("g")) {
            return ResourceReservation.Guaranteed;
        } else if (value.equalsIgnoreCase("cl")) {
            return ResourceReservation.ControlledLoad;
        } else if (value.equalsIgnoreCase("be")){
            return ResourceReservation.BestEffort;
        } else {
            throw new ParseException("Invalid value for EchoCancellation :" +  value, 0);
        }
    }
    
    /**
     * Decode EncryptionMethod object from given text.
     *
     * @param text the text value of the EncryptionMethod.
     * @return EncryptionMethod object.
     */
    public static EncryptionMethod decodeEncryptionMethod(String value)
    throws ParseException {
        // encryptiondata =("clear"":"encryptionKey )
        // /("base64"":"encodedEncryptionKey )
        // /("uri"":"URItoObtainKey )
        // /("prompt");defined in SDP,not usable in MGCP!
        int pos = value.indexOf(':');
        if (pos < 0) {
            throw new ParseException("Invalid value for EncryptionData: " + value, 0);
        }
        
        String method = value.substring(0,pos).trim();
        String key = value.substring(pos + 1).trim();
        
        if (method.equalsIgnoreCase("clear")) {
            return new EncryptionMethod(EncryptionMethod.CLEAR, key);
        } else if (method.equalsIgnoreCase("base64")) {
            return new EncryptionMethod(EncryptionMethod.BASE64, key);
        } else if (method.equalsIgnoreCase("uri")) {
            return new EncryptionMethod(EncryptionMethod.URI, key);
        } else {
            throw new ParseException("Invalid value for EncryptionData: " + method, 0);
        }
    }
    
    /**
     * Decode TypeOfNetwork object from given text.
     *
     * @param text the text value of the TypeOfNetwork.
     * @return TypeOfNetwork object.
     */
    public static TypeOfNetwork decodeTypeOfNetwork(String value)
    throws ParseException {
        // typeOfNetwork ="IN"/"ATM"/"LOCAL"/OtherTypeOfNetwork
        if (value.equalsIgnoreCase("in")) {
            return TypeOfNetwork.In;
        } else if (value.equalsIgnoreCase("atm")) {
            return TypeOfNetwork.Atm;
        } else if (value.equalsIgnoreCase("local")){
            return TypeOfNetwork.Local;
        } else {
            throw new ParseException("Invalid value for TypeOfNetwork :" +  value, 0);
        }
    }
    
    public static RestartMethod decodeRestartMethod(String value) throws ParseException {
        // RestartMethod = "graceful" / "forced" / "restart" / "disconnected"
		//        / "cancel-graceful" / extensionRestartMethod
		//        extensionRestartMethod = PackageExtensionRM
		//        PackageExtensionRM     = packageName "/" 1*32(ALPHA / DIGIT / HYPHEN)

        if (value.equalsIgnoreCase("graceful")) {
            return RestartMethod.Graceful;
        } else if (value.equalsIgnoreCase("forced")) {
            return RestartMethod.Forced;
        } else if (value.equalsIgnoreCase("restart")){
            return RestartMethod.Restart;
        } else if (value.equalsIgnoreCase("disconnected")){
            return RestartMethod.Disconnected;
        } else if (value.equalsIgnoreCase("cancel-grateful")){
            return RestartMethod.CancelGraceful;
        } else {
        	// TODO: Add support for extension restart method.
            throw new ParseException("Extension restarts not (yet) supported:" +  value, 0);
        }
    }
    
    public static RequestedEvent[] decodeRequestedEvents(String value) throws ParseException {
        // RequestedEvents =requestedEvent 0*(","0*(WSP)requestedEvent)
        String[] tokens = value.split(",");
        RequestedEvent[] events = new RequestedEvent[tokens.length];
        
        for (int i = 0; i < tokens.length; i++) {
            events[i] = decodeRequestedEvent(tokens[i]);
        }
        
        return events;
    }
    
    public static RequestedEvent decodeRequestedEvent(String value) throws ParseException {
        // requestedEvent =(eventName ["("requestedActions ")"])
        // /(eventName "("requestedActions ")" "("eventParameters ")")
        int pos1 = value.indexOf('(');
        int pos2 = value.indexOf(')', pos1);
        int pos3 =  value.indexOf('(', pos2);
        int pos4 =  value.indexOf(')', pos3);
        
        if (pos1 == -1) { // no actions, no parameters
            return new RequestedEvent(decodeEventName(value, null), null);
        }
        
        if (pos3 == -1) {
            String evtName = value.substring(0, pos1);
            String actions = value.substring(pos1 + 1, pos2 - 1);
            return new RequestedEvent(
                    decodeEventName(evtName, null),
                    decodeRequestedActions(actions));
        }
        
        String evtName = value.substring(0, pos1);
        String actions = value.substring(pos1 + 1, pos2);
        String parms = value.substring(pos3 + 1, pos4);
        return new RequestedEvent(
                decodeEventName(evtName, parms),
                decodeRequestedActions(actions));
        
    }
    
    public static EventName decodeEventName(String value, String param) throws ParseException {
        // eventName =[(packageName /"*")"/"]
        // (eventId /"all"/eventRange
        // /"*"/"#");for DTMF
        // ["@"(ConnectionId /"$"/"*")]
        String tokens[] = value.split("/");
        if (tokens.length == 1) {
            return new EventName(PackageName.AllPackages,
                    MgcpEvent.factory(tokens[0]).withParm(param));
        } else if (tokens.length == 2) {
            int pos = tokens[1].indexOf('@');
            if (pos > 0) {
                String cid = tokens[1].substring(pos + 1);
                return new EventName(PackageName.factory(tokens[0]),
                        MgcpEvent.factory(tokens[1].substring(0, pos)).withParm(param),
                        new ConnectionIdentifier(cid));
            } else {
                return new EventName(PackageName.factory(tokens[0]),
                        MgcpEvent.factory(tokens[1]).withParm(param));
            }
        } else if (tokens.length == 3) {
            int pos = tokens[2].indexOf('@');
            if (pos < 0) {
                throw new ParseException("Invalid token " + tokens[2], 0);
            }
            
            String cid = tokens[1].substring(pos + 1);
            return new EventName(PackageName.factory(tokens[0]),
                    MgcpEvent.factory(tokens[1].substring(0, pos)).withParm(param),
                    new ConnectionIdentifier(cid));
        } else {
            throw new ParseException("Unexpected event name " + value, 0);
        }
    }
    
    public static RequestedAction[] decodeRequestedActions(String value) throws ParseException {
        //requestedActions =requestedAction 0*(","0*(WSP)requestedAction)
        String tokens[] = value.split(",");
        RequestedAction[] actions = new RequestedAction[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            actions[i] = decodeRequestedAction(tokens[i]);
        }
        return actions;
    }
    
    public static RequestedAction decodeRequestedAction(String value) throws ParseException {
        // requestedAction ="N"/"A"/"D"/"S"/"I"/"K"
        // /"E""("EmbeddedRequest ")"
        // /ExtensionAction
        value = value.trim();
        if (value.equalsIgnoreCase("N")) {
            return RequestedAction.NotifyImmediately;
        } else if (value.equalsIgnoreCase("A")) {
            return RequestedAction.Accumulate;
        } else if (value.equalsIgnoreCase("S")) {
            return RequestedAction.Swap;
        } else if (value.equalsIgnoreCase("I")) {
            return RequestedAction.Ignore;
        } else if (value.equalsIgnoreCase("K")) {
            return RequestedAction.KeepSignalsActive;
        } else if (value.equalsIgnoreCase("D")) {
            return RequestedAction.TreatAccordingToDigitMap;
        } else if (value.toLowerCase().startsWith("e")) {
            return new RequestedAction(decodeEmbeddedRequest(value));
        } else {
            throw new ParseException("Extension action not suported", 0);
        }
    }
    
    public static EmbeddedRequest decodeEmbeddedRequest(String value) throws ParseException {
        // EmbeddedRequest =("R""("EmbeddedRequestList ")"
        // [","0*(WSP)"S""("EmbeddedSignalRequest ")"]
        // [","0*(WSP)"D""("EmbeddedDigitMap ")"])
        // /("S""("EmbeddedSignalRequest ")"
        // [","0*(WSP)"D""("EmbeddedDigitMap ")"])
        // /("D""("EmbeddedDigitMap ")")
        String[] tokens = value.split(",");
        
        RequestedEvent[] requestedEvents = null;
        EventName[] signalEvents = null;
        DigitMap digitMap = null;
        
        for (int i = 0; i < tokens.length; i++) {
            String s = tokens[i].toLowerCase();
            if (s.startsWith("r")) {
                int p1 = s.indexOf('(');
                int p2 = s.indexOf(')');
                
                s = s.substring(p1 + 1, p2);
                requestedEvents = decodeRequestedEvents(s);
            } else if (s.startsWith("s")) {
                int p1 = s.indexOf('(');
                int p2 = s.indexOf(')');
                
                s = s.substring(p1 + 1, p2);
                signalEvents = decodeEventNames(s);
            } else if (s.startsWith("d")) {
                digitMap = new DigitMap(s);
            } else {
                throw new ParseException("Unexpected embedded request string:" + value, 0);
        	}
        }
        
        return new EmbeddedRequest(requestedEvents, signalEvents, digitMap);
    }
    
    public static EventName[] decodeEventNames(String value) throws ParseException {
        String tokens[] = value.split(",");
        EventName[] events = new EventName[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            String name = null;
            String parm = null;
            
            int pos = tokens[i].indexOf('(');
            if (pos > 0) {
                name = tokens[i].substring(0, pos);
                parm = tokens[i].substring(pos + 1, tokens[i].length() - 1);
            } else {
                name = tokens[i];
            }
            events[i] = decodeEventName(name, parm);
        }
        return events;
    }
    
    public static String encodeEventNames(EventName[] events) {
        String s = "";
        boolean first = true;
        for (EventName e : events) {
            if (first) {
            	first = false;
            }
            else {
            	s += ',';
            }
            s += e.getPackageName().toString()+'/'+e.getEventIdentifier().getName();
            if (e.getConnectionIdentifier() != null) {
            	s += '@'+e.getConnectionIdentifier().toString();
            }
            if (e.getEventIdentifier().getParms() != null) {
            	s += '('+e.getEventIdentifier().getParms()+')';
            }
        }
        return s;
    }
    
    /**
     * Creates EndpointIdentifier object from givent endpont's name.
     *
     * @param name the name of the given endpoint.
     * @return EdnpointIdentifier object.
     */
    public static EndpointIdentifier decodeEndpointIdentifier(String name) {
        String[] s = name.split("@");
        return new EndpointIdentifier(s[0], s[1]);
    }
    
    public static ReturnCode decodeReturnCode(int code) throws ParseException {
        switch (code) {
            case ReturnCode.CAS_SIGNALING_PROTOCOL_ERROR :
                return ReturnCode.CAS_Signaling_Protocol_Error;
            case ReturnCode.CONNECTION_WAS_DELETED :
                return ReturnCode.Connection_Was_Deleted;
            case ReturnCode.ENDPOINT_HAS_NO_DIGIT_MAP :
                return ReturnCode.Endpoint_Has_No_Digit_Map;
            case ReturnCode.ENDPOINT_INSUFFICIENT_RESOURCES :
                return ReturnCode.Endpoint_Insufficient_Resources;
            case ReturnCode.ENDPOINT_IS_RESTARTING :
                return ReturnCode.Endpoint_Is_Restarting;
            case ReturnCode.ENDPOINT_NOT_READY :
                return ReturnCode.Endpoint_Not_Ready;
            case ReturnCode.ENDPOINT_REDIRECTED :
                return ReturnCode.Endpoint_Redirected;
            case ReturnCode.ENDPOINT_UNKNOWN :
                return ReturnCode.Endpoint_Unknown;
            case ReturnCode.GATEWAY_CANNOT_DETECT_REQUESTED_EVENT :
                return ReturnCode.Gateway_Cannot_Detect_Requested_Event;
            case ReturnCode.GATEWAY_CANNOT_GENERATE_REQUESTED_SIGNAL :
                return ReturnCode.Gateway_Cannot_Generate_Requested_Signal;
            case ReturnCode.GATEWAY_CANNOT_SEND_SPECIFIED_ANNOUNCEMENT :
                return ReturnCode.Gateway_Cannot_Send_Specified_Announcement;
            case ReturnCode.INCOMPATIBLE_PROTOCOL_VERSION :
                return ReturnCode.Incompatible_Protocol_Version;
            case ReturnCode.INCORRECT_CONNECTION_ID :
                return ReturnCode.Incorrect_Connection_ID;
            case ReturnCode.INSUFFICIENT_BANDWIDTH :
                return ReturnCode.Insufficient_Bandwidth;
            case ReturnCode.INSUFFICIENT_BANDWIDTH_NOW :
                return ReturnCode.Insufficient_Bandwidth_Now;
            case ReturnCode.INSUFFICIENT_RESOURCES_NOW :
                return ReturnCode.Insufficient_Resources_Now;
            case ReturnCode.INTERNAL_HARDWARE_FAILURE :
                return ReturnCode.Internal_Hardware_Failure;
            case ReturnCode.INTERNAL_INCONSISTENCY_IN_LOCALCONNECTIONOPTIONS :
                return ReturnCode.Internal_Inconsistency_In_LocalConnectionOptions;
            case ReturnCode.MISSING_REMOTECONNECTIONDESCRIPTOR :
                return ReturnCode.Missing_RemoteConnectionDescriptor;
            case ReturnCode.NO_SUCH_EVENT_OR_SIGNAL :
                return ReturnCode.No_Such_Event_Or_Signal;
            case ReturnCode.PHONE_OFF_HOOK :
                return ReturnCode.Phone_Off_Hook;
            case ReturnCode.PHONE_ON_HOOK :
                return ReturnCode.Phone_On_Hook;
            case ReturnCode.PROTOCOL_ERROR :
                return ReturnCode.Protocol_Error;
            case ReturnCode.TRANSACTION_BEING_EXECUTED :
                return ReturnCode.Transaction_Being_Executed;
            case ReturnCode.TRANSACTION_EXECUTED_NORMALLY :
                return ReturnCode.Transaction_Executed_Normally;
            case ReturnCode.TRANSIENT_ERROR :
                return ReturnCode.Transient_Error;
            case ReturnCode.TRUNK_GROUP_FAILURE :
                return ReturnCode.Trunk_Group_Failure;
            case ReturnCode.UNKNOWN_CALL_ID :
                return ReturnCode.Unknown_Call_ID;
            case ReturnCode.UNKNOWN_EXTENSION_IN_LOCALCONNECTIONOPTIONS :
                return ReturnCode.Unknown_Extension_In_LocalConnectionOptions;
            case ReturnCode.UNKNOWN_OR_ILLEGAL_COMBINATION_OF_ACTIONS :
                return ReturnCode.Unknown_Or_Illegal_Combination_Of_Actions;
            case ReturnCode.UNRECOGNIZED_EXTENSION :
                return ReturnCode.Unrecognized_Extension;
            case ReturnCode.UNSUPPORTED_OR_INVALID_MODE :
                return ReturnCode.Unsupported_Or_Invalid_Mode;
            case ReturnCode.UNSUPPORTED_OR_UNKNOWN_PACKAGE :
                return ReturnCode.Unsupported_Or_Unknown_Package;
            default:
            	// TODO: 0xx should be treated as response acknowledgement.
            	if ((code > 99)&& (code < 200))
            		return(ReturnCode.Transaction_Being_Executed);
            	else if ((code > 199) && code < 300)
            		return(ReturnCode.Transaction_Executed_Normally);
            	else if ((code > 299) && code < 400)
            		return(ReturnCode.Endpoint_Redirected);
            	else if ((code > 399) && (code > 500))
            		return(ReturnCode.Transient_Error);
            	else if ((code > 499) && (code < 1000))
            		return(ReturnCode.Protocol_Error);
            	else
            		throw new ParseException("unknown response code: " + code,0);
        }
    }
    
    public static String encodeLocalConnectionOptions(LocalOptionValue[] options) {
        String msg = "L:";
        for (int i = 0; i < options.length-1; i++) {
            String s = options[i].toString();
            s = s.substring(0, s.indexOf("\n"));
            msg += s + ",";
        }
        msg += options[options.length - 1];
        return msg;
    }
    
    public static String encodeNotificationRequestParms(NotificationRequestParms parms) {
    	String msg = "X:" + parms.getRequestIdentifier() + "\n";
        if (parms.getSignalRequests() != null) {
           msg += "S:" + encodeEventNames(parms.getSignalRequests()) + "\n";
    	}
        if (parms.getRequestedEvents() != null) {
            msg += "R:" + encodeRequestedEvents(parms.getRequestedEvents()) + "\n";
        }
        if (parms.getDetectEvents() != null) {
            msg += "T:" + encodeEventNames(parms.getDetectEvents()) + "\n";
        }
        if (parms.getDigitMap() != null) {
            msg += "D:" + parms.getDigitMap() + "\n";
        }
        return msg;
    }

    public static String encodeRequestedEvent(RequestedEvent evt) {
        String s = evt.getEventName().getPackageName() + "/" + evt.getEventName().getEventIdentifier().getName();

        if (evt.getEventName().getConnectionIdentifier() != null) {
        	s +=  '@' + evt.getEventName().getConnectionIdentifier().toString();
        }
        String parms = evt.getEventName().getEventIdentifier().getParms();
        RequestedAction[] actions = evt.getRequestedActions();
        
        if (actions != null) {
            String ac = encodeRequestedActions(actions);
            s += " (" + ac + ")";
        }
        
        if (parms != null) {
            s += " (" + parms + ")";
        }
        
        return s;
    }
    
    public static String encodeRequestedEvents(RequestedEvent[] evts) {
        String s = "";
        for (int i = 0; i < evts.length; i++) {
            s += encodeRequestedEvent(evts[i]);
            if (i != evts.length - 1) {
                s += ',';
            }
        }
        return s;
    }
    
    public static String encodeRequestedActions(RequestedAction[] actions) {
        String s = "";
        String d = "";
        for (int i = 0; i < actions.length; i++) {
            d = i == 0 ? "" : ",";
            s += d + encodeRequestedAction(actions[i]);
        }
        return s;
    }
    
    public static String encodeRequestedAction(RequestedAction action) {
        return action.toString();
    }
    
    public static String encodeConnectionParm(ConnectionParm parm) {
        int type = parm.getConnectionParmType();
        if (type == RegularConnectionParm.JITTER) {
            return "JI=" + parm.getConnectionParmValue();
        } else if (type == RegularConnectionParm.LATENCY) {
            return "LA=" + parm.getConnectionParmValue();
        } else if (type == RegularConnectionParm.OCTETS_RECEIVED) {
            return "OR=" + parm.getConnectionParmValue();
        } else if (type == RegularConnectionParm.OCTETS_SENT) {
            return "OS=" + parm.getConnectionParmValue();
        } else if (type == RegularConnectionParm.PACKETS_LOST) {
            return "PL=" + parm.getConnectionParmValue();
        } else if (type == RegularConnectionParm.PACKETS_RECEIVED) {
            return "PR=" + parm.getConnectionParmValue();
        } else if (type == RegularConnectionParm.PACKETS_SENT) {
            return "PS=" + parm.getConnectionParmValue();
        } else {
            return ((ExtendedConnectionParm)parm).getConnectionParmExtensionName() +
                    "=" + parm.getConnectionParmValue();
        }
        
    }
    
    public static ConnectionParm decodeConnectionParm(String parm) {
        String[] tokens = parm.split("=");
        
        String name = tokens[0].trim();
        String value = tokens[1].trim();
        
        if (name.equalsIgnoreCase("JI")) {
            return new RegularConnectionParm(
                    RegularConnectionParm.JITTER,
                    Integer.parseInt(value));
        } else if (name.equalsIgnoreCase("LA")) {
            return new RegularConnectionParm(
                    RegularConnectionParm.LATENCY,
                    Integer.parseInt(value));
        } else if (name.equalsIgnoreCase("OR")) {
            return new RegularConnectionParm(
                    RegularConnectionParm.OCTETS_RECEIVED,
                    Integer.parseInt(value));
        } else if (name.equalsIgnoreCase("OS")) {
            return new RegularConnectionParm(
                    RegularConnectionParm.OCTETS_SENT,
                    Integer.parseInt(value));
        } else if (name.equalsIgnoreCase("PL")) {
            return new RegularConnectionParm(
                    RegularConnectionParm.PACKETS_LOST,
                    Integer.parseInt(value));
        } else if (name.equalsIgnoreCase("PR")) {
            return new RegularConnectionParm(
                    RegularConnectionParm.PACKETS_RECEIVED,
                    Integer.parseInt(value));
        } else if (name.equalsIgnoreCase("PS")) {
            return new RegularConnectionParm(
                    RegularConnectionParm.PACKETS_SENT,
                    Integer.parseInt(value));
        } else {
            return new ExtendedConnectionParm(name, Integer.parseInt(value));
        }
    }
    
    public static String encodeConnectionParms(ConnectionParm[] parms) {
        String s = "";
        for (int i = 0; i < parms.length - 1; i++) {
            s += encodeConnectionParm(parms[i]) + ",";
        }
        
        s += encodeConnectionParm(parms[parms.length - 1]);
        return s;
    }
    
    public static ConnectionParm[] decodeConnectionParms(String value) {
        String tokens[] = value.split(",");
        ConnectionParm[] parms = new ConnectionParm[tokens.length];
        
        for (int i = 0; i < tokens.length; i++) {
            parms[i] = decodeConnectionParm(tokens[i].trim());
        }
        
        return parms;
    }
    
    public static ReasonCode decodeReasonCode(String value) {
        String[] tokens = value.split("\\s");
        
        int code = Integer.parseInt(tokens[0]);
        ReasonCode reasonCode = null;
        
        switch (code) {
            case ReasonCode.ENDPOINT_MALFUNCTIONING :
                reasonCode = ReasonCode.Endpoint_Malfunctioning;
                break;
            case ReasonCode.ENDPOINT_OUT_OF_SERVICE :
                reasonCode = ReasonCode.Endpoint_Out_Of_Service;
                break;
            case ReasonCode.ENDPOINT_STATE_IS_NOMINAL :
                reasonCode = ReasonCode.Endpoint_State_Is_Nominal;
                break;
            case ReasonCode.LOSS_OF_LOWER_LAYER_CONNECTIVITY :
                reasonCode = ReasonCode.Loss_Of_Lower_Layer_Connectivity;
                break;
        }
        
        return reasonCode;
    }
}
