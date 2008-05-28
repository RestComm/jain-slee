package net.java.slee.resource.diameter.message;

import java.util.EventObject;

import dk.i1.diameter.Message;
import dk.i1.diameter.node.ConnectionKey;
import dk.i1.diameter.node.Peer;
import net.java.slee.resource.diameter.message.MessageEvent;

public class MessageEventImpl extends EventObject implements MessageEvent {

       private Message msg=null;
       private Peer peer=null;
       private ConnectionKey key=null;
       
    public MessageEventImpl(Message msg,Object source,Peer peer,ConnectionKey key)
    {
        super(source);
        this.msg=msg;
        this.peer=peer;
        this.key=key;
    }
    
    public Message getMessage() {
        // TODO Auto-generated method stub
        return msg;
    }

    public Object getSource() {
        // TODO Auto-generated method stub
        return null;
    }

    
    /**
     * Returns peer that has sent this message.
     */
    public Peer getPeer() {
        // TODO Auto-generated method stub
        return peer;
    }
    /**
     * Returns ConnectionKey of connection through which this message has been received. It should never be null value;
     */
    public ConnectionKey getConnectionKey() {
        // TODO Auto-generated method stub
        return key;
    }

}
