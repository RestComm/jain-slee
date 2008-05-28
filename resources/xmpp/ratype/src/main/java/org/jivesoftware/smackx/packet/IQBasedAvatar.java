package org.jivesoftware.smackx.packet;

import org.jivesoftware.smack.packet.IQ;

public class IQBasedAvatar extends IQ {

    private Data data;
  
    public Data getData() {
        return data;
    }
   
    public void setData(Data data) {
        this.data = data;
    }

    public String getChildElementXML() {
        StringBuffer buf = new StringBuffer();
        buf.append("<query xmlns=\"jabber:iq:avatar\">");
        if (getData() != null) {
            buf.append(getData().toXML());            
        }
        // Add packet extensions, if any are defined.
        buf.append(getExtensionsXML());
        buf.append("</query>");
        return buf.toString();
    }

    public static class Data {
    	
    	private String data;
    	private String mimetype;
    	
    	
    	public Data(String data, String mimetype) {
    		this.data = data;
    		this.mimetype = mimetype;
    	}
    	
    	public String getData() {
    		return data;
    	}
    	
    	public String getMimetype() {
    		return mimetype;
    	}
    	
    	public String toXML() {
    		StringBuffer buf = new StringBuffer();
    		buf.append("<data mimetype=\"").append(mimetype).append("\">").append(data).append("</data>");
    		return buf.toString();
    	}
    }
}