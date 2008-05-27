package net.java.slee.resource.diameter;

import java.util.Set;

import javax.slee.ActivityContextInterface;

import dk.i1.diameter.Message;
import dk.i1.diameter.node.ConnectionKey;
import dk.i1.diameter.node.NodeManager;
import dk.i1.diameter.node.NotARequestException;
import dk.i1.diameter.node.NotAnAnswerException;
import dk.i1.diameter.node.NotProxiableException;
import dk.i1.diameter.node.NotRoutableException;
import dk.i1.diameter.node.Peer;
import dk.i1.diameter.node.StaleConnectionException;

//public interface DiameterResourceAdaptorSbbInterface extends ActivitiesFactory{
public interface DiameterResourceAdaptorSbbInterface extends ActivitiesFactory{
    
    /**
     * Add origin-host and origin-realm to a message. The configured host and realm is added to the message as origin-host and origin-realm AVPs
     * @param msg
     */
    public void addOurHostAndRealm(Message msg);
    /**
     * Returns the IP-address of the remote end of a connection.
     * @param connkey
     * @return
     */
    public java.net.InetAddress connectionKey2InetAddress(ConnectionKey connkey);
    /**
     * Returns the Peer on a connection.
     * @param connkey
     * @return
     */
    public Peer connectionKey2Peer(ConnectionKey connkey);
    /**
     * 
     * @param peer
     * @return Connection key for the peer.
     */
    public ConnectionKey findConnection(Peer peer);
    /**
     * Determine if a message is supported by a peer. The auth-application-id, acct-application-id or vendor-specific-application AVP is extracted and tested against the peer's capabilities.
     * @param msg
     * @param peer
     * @return <b>true</b> if peer announced that it supports application.
     */
    public boolean isAllowedApplication(Message msg, Peer peer);
    /**
     * Checks if connection for this key is still valid.
     * @param connkey
     * @return Returns <b>true</b> if the connection is still valid. This method is usually only of interest to programs that do lengthy processing of requests nad are located in a poor network. It is usually much easier to just call sendMessage() and catch the exception if the connection has gone stale.
     */
    public boolean isConnectionKeyValid(ConnectionKey connkey);
    /**
     * Generates unique session id.
     * @return
     */
    public String makeNewSessionId();
    /**
     * Generates unique session id.
     * @param optional_part - part which will be added to session id.
     * @return
     */
    public String makeNewSessionId(java.lang.String optional_part);

    
    /**
     * Creates new e2d-id
     * @return
     */
    public int nextEndToEndIdentifier();
    /**
     * Generates new hbh-id for conection. 
     * @param connkey - identifies connections
     * @return new hbh-id
     * @throws StaleConnectionException
     */
    public int nextHopByHopIdentifier(ConnectionKey connkey)throws StaleConnectionException;

    public int stateId();

    /**
     * Sends answer through connection identified by this key.
     * @param answer - answer to be sent
     * @param key - key identifing connection
     * @throws NotAnAnswerException
     */
    public  void     answer(Message answer,ConnectionKey key) throws NotAnAnswerException;
    /**
     * Forwards answer thorugh connection identified by this key.
     * @param answer - answer to be forwarded.
     * @param key - key identifing connection.
     * @throws StaleConnectionException
     * @throws NotAnAnswerException
     * @throws NotProxiableException
     */
    public  void     forwardAnswer(Message answer,ConnectionKey key)throws StaleConnectionException,NotAnAnswerException,NotProxiableException;
    /**
     * Forwards request through connection identified by this key.
     * @param request - request to be forwarded
     * @param connkey - key identifing connection.
     * @param state - Some object to which reference will be stored and passed into RA when answer arrives. It <b>should</b> be "DiameterRAActivityHandle" reference. If it is answer will be
     * passed to activity identified by this DAH, otherwise it will be ignored and RA will perform its own matching algorithm.
     * @throws StaleConnectionException
     * @throws NotARequestException
     * @throws NotProxiableException
     */
    public  void     forwardRequest(Message request, ConnectionKey connkey, java.lang.Object state)throws StaleConnectionException,NotARequestException,    NotProxiableException;
    /**
     * Sends request through connection identified by this key.
     * @param request - request to be forwarded
     * @param connkey - key identifing connection.
     * @param state - Some object to which reference will be stored and passed into RA when answer arrives. It <b>should</b> be "DiameterRAActivityHandle" reference. If it is answer will be
     * passed to activity identified by this DAH, otherwise it will be ignored and RA will perform its own matching algorithm.
     * @throws StaleConnectionException
     * @throws NotARequestException
     */
    public  void     sendRequest(Message request, ConnectionKey connkey, java.lang.Object state)  throws StaleConnectionException,NotARequestException;
    /**
     * Sends request. Method tries to send request to one of the peers from array.
     * @param request - request to be forwarded
     * @param peers - array of possible destinations
     * @param state - Some object to which reference will be stored and passed into RA when answer arrives. It <b>should</b> be "DiameterRAActivityHandle" reference. If it is answer will be
     * passed to activity identified by this DAH, otherwise it will be ignored and RA will perform its own matching algorithm.
     * @throws NotRoutableException
     * @throws NotARequestException
     */
    public  void     sendRequest(Message request, Peer[] peers, java.lang.Object state)throws NotRoutableException,NotARequestException;
 
    /**
     * 
     * @return Set<Peer> - set of peers from which we have received ( ever) request.
     */
    public Set<Peer> getConnectedPeers();
    /**
     * 
     * @return Set<Peer>  - set of peers to which we have tried to connect, according to configuraton file.
     */
    public Set<Peer> getPeers();
    
}
