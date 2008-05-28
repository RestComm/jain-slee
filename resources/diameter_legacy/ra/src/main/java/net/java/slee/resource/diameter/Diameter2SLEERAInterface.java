package net.java.slee.resource.diameter;

import dk.i1.diameter.Message;
import dk.i1.diameter.node.ConnectionKey;
import dk.i1.diameter.node.Peer;

public interface Diameter2SLEERAInterface {
    public void onRequest(Message msg, ConnectionKey key,Peer peer);
    public void onAnswer(Message msg, ConnectionKey key,Object state);
}
