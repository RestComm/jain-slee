package net.java.slee.resource.diameter.activities;

import dk.i1.diameter.Message;
import dk.i1.diameter.node.ConnectionKey;
import dk.i1.diameter.node.NotARequestException;
import dk.i1.diameter.node.NotAnAnswerException;
import dk.i1.diameter.node.NotProxiableException;
import dk.i1.diameter.node.NotRoutableException;
import dk.i1.diameter.node.Peer;
import dk.i1.diameter.node.StaleConnectionException;

public interface ActivityBaseInterface {

    
    

    /**
     * 
     * @return
     */
    public String getDestinationRealm();

    /**
     * 
     * @return
     */
    public String getDestinationHost();

    /**
     * 
     * @return Session-Id for this transaction. To which this transaction is
     *         bound
     */
    public String getSessionID();

    
    public int getAuthSessionState();
    public void setAuthSessionState(int sessionMaintanenceState);
    public ConnectionKey getConnectionKey();
    public int getApplication();
    
    
    public  void     answer(Message answer) throws NotAnAnswerException;
    public  void     forwardAnswer(Message answer)throws StaleConnectionException,NotAnAnswerException,NotProxiableException;
    public  void     forwardRequest(Message request, ConnectionKey connkey)throws StaleConnectionException,NotARequestException,    NotProxiableException;
    public  void     sendRequest(Message request)  throws StaleConnectionException,NotARequestException;
    public  void     sendRequest(Message request, Peer[] peers)throws NotRoutableException,NotARequestException;
    
}
