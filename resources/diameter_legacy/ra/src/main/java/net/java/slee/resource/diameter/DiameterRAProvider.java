package net.java.slee.resource.diameter;

import java.net.InetAddress;
import java.util.Set;

import javax.slee.ActivityContextInterface;
import javax.slee.FactoryException;
import javax.slee.UnrecognizedActivityException;




import net.java.slee.resource.diameter.activities.ShInterfaceActivity;
import net.java.slee.resource.diameter.activities.ShInterfaceActivityImpl;
import static net.java.slee.resource.diameter.ShProtocolConstants.*;

import org.apache.log4j.Logger;

import dk.i1.diameter.Message;
import dk.i1.diameter.node.ConnectionKey;
import dk.i1.diameter.node.Node;
import dk.i1.diameter.node.NodeManager;
import dk.i1.diameter.node.NotARequestException;
import dk.i1.diameter.node.NotAnAnswerException;
import dk.i1.diameter.node.NotProxiableException;
import dk.i1.diameter.node.NotRoutableException;
import dk.i1.diameter.node.Peer;
import dk.i1.diameter.node.StaleConnectionException;
import static dk.i1.diameter.ProtocolConstants.*;


public class DiameterRAProvider implements DiameterResourceAdaptorSbbInterface,ActivitiesFactory{

    private static Logger logger=Logger.getLogger(DiameterResourceAdaptor.class);
    
    
    private DiameterResourceAdaptor ra=null;
    private Diameter2SLEENodeManager nm=null;
    private Node node=null;
    private DiameterRAActivityContextInterfaceFactory acif=null;
    public DiameterRAProvider(DiameterResourceAdaptor ra,Diameter2SLEENodeManager nm, DiameterRAActivityContextInterfaceFactory acif)
    {
        this.ra=ra;
        this.nm=nm;
        node=nm.node();
        this.acif=acif;
    }
    
    public void addOurHostAndRealm(Message msg) {
        node.addOurHostAndRealm(msg);
        
    }
    public InetAddress connectionKey2InetAddress(ConnectionKey connkey) {
        
        return node.connectionKey2InetAddress(connkey);
    }
    public Peer connectionKey2Peer(ConnectionKey connkey) {
        
        return node.connectionKey2Peer(connkey);
    }
    public ConnectionKey findConnection(Peer peer) {

        return node.findConnection(peer);
    }
    
    public boolean isAllowedApplication(Message msg, Peer peer) {
        
        return node.isAllowedApplication(msg,peer);
    }
    public boolean isConnectionKeyValid(ConnectionKey connkey) {

        return node.isConnectionKeyValid(connkey);
    }
    public String makeNewSessionId() {

        return node.makeNewSessionId();
    }
    public String makeNewSessionId(String optional_part) {
        
        return node.makeNewSessionId(optional_part);
    }
    public int nextEndToEndIdentifier() {
        
        return node.nextEndToEndIdentifier();
    }
    public int nextHopByHopIdentifier(ConnectionKey connkey) throws StaleConnectionException {

        return node.nextHopByHopIdentifier(connkey);
    }
   
    public int stateId() {
        
        return node.stateId();
    }

    public void answer(Message answer, ConnectionKey key) throws NotAnAnswerException {
        
        nm.sendAnswer(answer,key);
    }

    public void forwardAnswer(Message answer, ConnectionKey key) throws StaleConnectionException, NotAnAnswerException, NotProxiableException {
        nm.frwdAnswer(answer,key);
        
    }

    public void forwardRequest(Message request, ConnectionKey connkey, Object state) throws StaleConnectionException, NotARequestException, NotProxiableException {
        nm.frwdRequest(request,connkey,state);
        
    }

    public void sendRequest(Message request, ConnectionKey connkey, Object state) throws StaleConnectionException, NotARequestException {
        nm.sendRequest(request,connkey,state);
        
    }

    public void sendRequest(Message request, Peer[] peers, Object state) throws NotRoutableException, NotARequestException {
        nm.sendRequest(request,peers,state);
        
    }

    public ActivityContextInterface makeShActivity(String destHost, String destRealm,
             String sessID,
            int authSessionState, ConnectionKey key) throws IllegalArgumentException {
        
        if(sessID==null)
            sessID=node.makeNewSessionId();
        else
            sessID=node.makeNewSessionId(sessID);
        
        if(destHost==null || destRealm == null)
            throw new IllegalArgumentException("<><><> EITHER destinationHost or destinationRealm is NULL, SPECIFY VALUE <><><>");
        if(authSessionState!=DI_AUTH_SESSION_STATE_NO_STALE_MAINTAINED && authSessionState!=DI_AUTH_SESSION_STATE_STALE_MAINTAINED)
            throw new IllegalArgumentException("<><><> authSessionState MUST BE EQUAL TO \"dk.i1.diameter.ProtocolConstants.DI_AUTH_SESSION_STATE_NO_STALE_MAINTAINED\" OR \"dk.i1.diameter.ProtocolConstants.DI_AUTH_SESSION_STATE_STALE_MAINTAINED\" <><><>");
        if(key==null)
            throw new IllegalArgumentException("<><><> CONNECTION KEY IS NULL <><><>");
        
        DiameterRAActivityHandle DAH=new DiameterRAActivityHandle(sessID);
        ShInterfaceActivityImpl ac=new ShInterfaceActivityImpl(destHost,destRealm,sessID,authSessionState,key,DAH,(DiameterResourceAdaptorSbbInterface) ra.getSBBResourceAdaptorInterface("RAPROVIDER CALL"));
        ActivityContextInterface aci=null;
        try {
            logger.info("====== ACIF IS: "+acif+ " ==========");
          
            aci = acif.getActivityContextInterface(ac);
            logger.info("====== REGISTERING ACTIVITY =======");
            ra.registerActivity(DAH,ac);
            logger.info("====== ACTIVITY REGISTERED =======");
        } catch (FactoryException e) {
            logger.error("<><><> SLEE FATAL ERROR, COULDNT CREATE ACI FOR CHOOSEN ACTIVITY: SH <><><>");
            e.printStackTrace();
        } catch (NullPointerException e) {
            logger.error("<><><> PASSED ACTIVITY WAS NULLL, COULDNT CREATE ACI FOR CHOOSEN ACTIVITY: SH <><><>");
            e.printStackTrace();
        } catch (UnrecognizedActivityException e) {
            logger.error("<><><> NOT SUPPORTED ACTIVITY??, COULDNT CREATE ACI FOR CHOOSEN ACTIVITY: SH <><><>");
            e.printStackTrace();
        }
        logger.info("========== "+aci+" ========");
        return aci;
    }

    public Set<Peer> getConnectedPeers() {
        
        return ra.getConnectedPeers();
    }

    public Set<Peer> getPeers() {

        return ra.getPeers();
    }

    
    
    
 

    

}
