package net.java.slee.resource.diameter;

import dk.i1.diameter.Message;
import dk.i1.diameter.node.ConnectionKey;
import dk.i1.diameter.node.NodeSettings;
import dk.i1.diameter.node.NotARequestException;
import dk.i1.diameter.node.NotAnAnswerException;
import dk.i1.diameter.node.NotProxiableException;
import dk.i1.diameter.node.Peer;
import dk.i1.diameter.node.StaleConnectionException;

public class Diameter2SLEENodeManager extends dk.i1.diameter.node.NodeManager {

    private Diameter2SLEERAInterface ra=null;
    
    public Diameter2SLEENodeManager(NodeSettings nodeSettings, Diameter2SLEERAInterface ra) {
        super(nodeSettings);
        this.ra=ra;
        // TODO Auto-generated constructor stub
    }
   
    @Override
    protected void handleRequest(Message msg, ConnectionKey key, Peer peer) {
        // TODO PUT SOME LOGIC HERE
        //super.handleRequest(msg, key, peer);
        ra.onRequest(msg,key,peer);
    }

    @Override
    protected void handleAnswer(Message msg, ConnectionKey key, Object state) {
        // TODO PUT SOME LOGIC HERE
        //super.handleAnswer(msg, key, peer);
        ra.onAnswer(msg,key,state);
    }
    public void sendAnswer(Message answer,ConnectionKey connkey) throws NotAnAnswerException
    {
     this.answer(answer,connkey);   
    }
    public void frwdRequest(Message request, ConnectionKey connkey, java.lang.Object state) throws StaleConnectionException,  NotARequestException, NotProxiableException
    {
        forwardRequest(request,connkey,state);
    }
    public void frwdAnswer(Message answer, ConnectionKey connkey)  throws StaleConnectionException, NotAnAnswerException,  NotProxiableException
    {
        forwardAnswer(answer,connkey);
    }
}
