

package org.jivesoftware.smackx.provider;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smack.util.PacketParserUtils;
import org.jivesoftware.smackx.packet.IQBasedAvatar;
import org.xmlpull.v1.XmlPullParser;

/**
* The DiscoverInfoProvider parses Service Discovery information packets.
*
* @author Gaston Dombiak
*/

public class IQBasedAvatarProvider implements IQProvider {

    public IQ parseIQ(XmlPullParser parser) throws Exception {
        IQBasedAvatar iQBasedAvatar = new IQBasedAvatar();
        boolean done = false;
        IQBasedAvatar.Data data = null;
        String dataText = "";
        String dataMimeType = "";       
        while (!done) {
            int eventType = parser.next();
            if (eventType == XmlPullParser.START_TAG) {
                if (parser.getName().equals("data")) {                   
                    dataMimeType = parser.getAttributeValue("", "mimetype");
                    dataText = parser.nextText();                    
                }
                // Otherwise, it must be a packet extension.
                else {
                    iQBasedAvatar.addExtension(PacketParserUtils.parsePacketExtension(parser
                            .getName(), parser.getNamespace(), parser));
                }
            } else if (eventType == XmlPullParser.END_TAG) {
                if (parser.getName().equals("data")) {
                   
                    data = new IQBasedAvatar.Data(dataText, dataMimeType);
                    iQBasedAvatar.setData(data);
                }               
                if (parser.getName().equals("query")) {
                    done = true;
                }
            }
        }

        return iQBasedAvatar;
    }
}