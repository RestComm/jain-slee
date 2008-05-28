package org.jivesoftware.smackx.provider;

import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.jivesoftware.smackx.packet.SapoShowExtension;
import org.xmlpull.v1.XmlPullParser;

/**
 * Provider for packet extension @{link SapoShowExtension}. 
 * @author Eduardo Martins
 *
 */
public class SapoShowExtensionProvider implements PacketExtensionProvider {



    /**
     * Creates a new SapoShowExtensionProvider.
     * ProviderManager requires that every PacketExtensionProvider has a public, no-argument constructor
     */
    public SapoShowExtensionProvider() {
    }

    /**
     * Parses a SapoShowExtension packet (extension sub-packet).
     *
     * @param parser the XML parser, positioned at the starting element of the extension.
     * @return a PacketExtension.
     * @throws Exception if a parsing error occurs.
     */
    public PacketExtension parseExtension(XmlPullParser parser) throws Exception {
        SapoShowExtension extension = new SapoShowExtension();
        
        boolean done = false;            
        while (!done) {
            int eventType = parser.next();
            if (eventType == XmlPullParser.START_TAG) {
                if (parser.getName().equals("show")) {                   
                    extension.setShow(parser.nextText());                    
                }
                else if (parser.getName().equals("mood")) {                   
                    extension.setMood(parser.nextText());                    
                }               
            } else if (eventType == XmlPullParser.END_TAG) {
               if (parser.getName().equals(extension.getElementName())) 
                    done = true;               
            }
        }
     return extension;
    }

}
