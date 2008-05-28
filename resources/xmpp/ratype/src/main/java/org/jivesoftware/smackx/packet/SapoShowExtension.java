package org.jivesoftware.smackx.packet;

import org.jivesoftware.smack.packet.PacketExtension;

/**
 * sapo:show packet extension. Ex: <x xmlns="sapo:show"><show>sleeping</show><mood>smile</mood></x>
 * @author Eduardo Martins
 *
 */
public class SapoShowExtension implements PacketExtension {

	public static final String ELEMENT_NAME = "x";
	public static final String ELEMENT_NAMESPACE = "sapo:show";
	
    private String show = null;
    private String mood = null;

    /**
    * Returns the XML element name of the extension sub-packet root element.
    * Always returns "x"
    *
    * @return the XML element name of the packet extension.
    */
    public String getElementName() {
        return ELEMENT_NAME;
    }

    /** 
     * Returns the XML namespace of the extension sub-packet root element.
     * According the specification the namespace is always "sapo:show"
     *
     * @return the XML namespace of the packet extension.
     */
    public String getNamespace() {
        return ELEMENT_NAMESPACE;
    }

    /**
     * Returns the XML representation of a sapo:show extension according the specification.
     * 
     */
    public String toXML() {
        StringBuffer buf = new StringBuffer();
        buf.append("<").append(getElementName()).append(" xmlns=\"").append(getNamespace()).append(
            "\">");
        if (show == null || show.equals(""))
        	buf.append("<show/>");
        else
        	buf.append("<show>"+show+"</show>");
        if (mood == null || mood.equals(""))
        	buf.append("<mood/>");
        else
        	buf.append("<mood>"+mood+"</mood>");
        buf.append("</").append(getElementName()).append(">");
        return buf.toString();
    }

    public void setMood(String mood) {
		this.mood = mood;
	}
    
    public void setShow(String show) {
		this.show = show;
	}
    
    public String getMood() {
		return mood;
	}
    
    public String getShow() {
		return show;
	}
    
    
	
}
