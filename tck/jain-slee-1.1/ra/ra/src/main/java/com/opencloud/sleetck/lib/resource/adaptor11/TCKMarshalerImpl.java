/*
* "Downloading, compilation and use of this TCK
* is governed by the terms of the JAIN SLEE (JSLEE) V1.1 TECHNOLOGY
* COMPATIBILITY KIT LICENCE AGREEMENT, the text of which is included in
* the download package as LICENSE and can be viewed at
* https://jsleetck11.dev.java.net/jslee-1.1-tck-license.txt"
*/

package com.opencloud.sleetck.lib.resource.adaptor11;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.slee.resource.ActivityHandle;
import javax.slee.resource.FireableEventType;
import javax.slee.resource.Marshaler;

/**
 * Marshaler for TCK activities and events.
 */
public class TCKMarshalerImpl implements Marshaler {

    public int getEstimatedEventSize(FireableEventType eventType, Object event) {
        // TODO Auto-generated method stub
        return 0;
    }

    public int getEstimatedHandleSize(ActivityHandle handle) {
        // TODO Auto-generated method stub
        return 0;
    }

    public ByteBuffer getEventBuffer(FireableEventType eventType, Object event) {
        // TODO Auto-generated method stub
        return null;
    }

    public void marshalEvent(FireableEventType eventType, Object event, DataOutput out) throws IOException {
        // TODO Auto-generated method stub

    }

    public void marshalHandle(ActivityHandle handle, DataOutput out) throws IOException {
        // TODO Auto-generated method stub

    }

    public void releaseEventBuffer(FireableEventType eventType, Object event, ByteBuffer buffer) {
        // TODO Auto-generated method stub

    }

    public Object unmarshalEvent(FireableEventType eventType, DataInput in) throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    public ActivityHandle unmarshalHandle(DataInput in) throws IOException {
        // TODO Auto-generated method stub
        return null;
    }
/*
    // empty constructor
    public TCKMarshalerImpl() {}

    public ActivityHandle unmarshalHandle(byte[] marshalledHandle) throws IOException {
        // create input streams
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(marshalledHandle));

        // deserialise activity handle
        return new TCKActivityHandleImpl(dis);
    }

    public byte[] marshalHandle(ActivityHandle handle) throws IOException {
        if(!(handle instanceof TCKActivityHandleImpl)) throw new IOException("Unknown handle type");

        // create output streams
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        // serialise the activity handle
        ((TCKActivityHandleImpl)handle).toStream(dos);
        dos.flush();

        return baos.toByteArray();
    }

    public byte[] marshalEvent(Object event, int eventID) throws IOException{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream( baos );
        // wrap the event in a MarshalledObject so that exported remote objects
        // (e.g. the TCK resource) will be replaced with their stubs before serialization
        oos.writeObject(new MarshalledObject(event));
        oos.flush();
        return baos.toByteArray();
    }

    public Object unmarshalEvent(byte[] marshalledEvent, int eventID) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream( marshalledEvent );
        ObjectInputStream ois = new ObjectInputStream( bais );
        Object event;
        try {
            event = ((MarshalledObject)ois.readObject()).get();
        } catch (ClassNotFoundException e) {
            throw new IOException("Caught ClassNotFoundException while unmarshalling event: "+e.toString());
        }
        return event;
    }*/

}
