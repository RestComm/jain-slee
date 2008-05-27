package net.java.slee.resource.diameter.activities;

import net.java.slee.resource.diameter.DiameterRAActivityHandle;
import net.java.slee.resource.diameter.DiameterResourceAdaptorSbbInterface;
import dk.i1.diameter.Message;
import dk.i1.diameter.node.ConnectionKey;
import dk.i1.diameter.node.NotARequestException;
import dk.i1.diameter.node.NotAnAnswerException;
import dk.i1.diameter.node.NotProxiableException;
import dk.i1.diameter.node.NotRoutableException;
import dk.i1.diameter.node.Peer;
import dk.i1.diameter.node.StaleConnectionException;

 class DiameterActivityCommonPart implements ActivityBaseInterface {
    
    
    protected static DiameterResourceAdaptorSbbInterface d2sbb=null;
    protected String destinationHost = null;
    protected DiameterRAActivityHandle DAH=null;
    protected String destinationRealm = null;
    protected String sessionID = null;
    protected int authSessionState = -1;

    protected ConnectionKey key = null;
    protected int applicationID = -1;

    public DiameterActivityCommonPart(String destHost, String destRealm,
            int applciationID, String sessID,
            int authSessionState, ConnectionKey key, DiameterRAActivityHandle DAH,DiameterResourceAdaptorSbbInterface d2sbb) {
        destinationHost = destHost;
        destinationRealm = destRealm;
        this.sessionID=sessID;
        this.authSessionState = authSessionState;
        this.key = key;
        this.applicationID = applciationID;
        this.DAH=DAH;
        this.d2sbb=d2sbb;
    }

    public String getDestinationHost() {
        return destinationHost;
    }

    public String getDestinationRealm() {
        return destinationRealm;
    }

    public String getSessionID() {
        return sessionID;
    }

   

    public void send(Message message) {
        // TODO Auto-generated method stub

    }

    public void sendMessage(Message message, ConnectionKey key) {
        // TODO Auto-generated method stub

    }

    public int getAuthSessionState() {
        // TODO Auto-generated method stub
        return authSessionState;
    }

    public void setAuthSessionState(int authSessionState) {
        this.authSessionState = authSessionState;

    }



    public ConnectionKey getConnectionKey() {
        // TODO Auto-generated method stub
        return key;
    }

    public int getApplication() {
        // TODO Auto-generated method stub
        return applicationID;
    }
    /*
     static void setSbbInterface(DiameterResourceAdaptorSbbInterface d2sbb)
    {
        d2sbb=d2sbb;
    }
*/
    public void answer(Message answer) throws NotAnAnswerException {
        d2sbb.answer(answer,key);
        
    }

    public void forwardAnswer(Message answer) throws StaleConnectionException, NotAnAnswerException, NotProxiableException {
        d2sbb.forwardAnswer(answer,key);
        
    }

    public void forwardRequest(Message request, ConnectionKey connkey) throws StaleConnectionException, NotARequestException, NotProxiableException {
       d2sbb.forwardRequest(request,connkey,DAH);
        
    }

    public void sendRequest(Message request) throws StaleConnectionException, NotARequestException {
        d2sbb.sendRequest(request,key,DAH);
        
    }

    public void sendRequest(Message request, Peer[] peers) throws NotRoutableException, NotARequestException {
        d2sbb.sendRequest(request,peers,DAH);
        
    }
}
