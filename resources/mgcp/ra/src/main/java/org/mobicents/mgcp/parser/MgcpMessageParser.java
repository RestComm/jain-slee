/*
 * File Name     : MgcpMessageParser.java
 *
 * The JAIN MGCP API implementaion.
 *
 * The source code contained in this file is in in the public domain.
 * It can be used in any project or product without prior permission,
 * license or royalty payments. There is  NO WARRANTY OF ANY KIND,
 * EXPRESS, IMPLIED OR STATUTORY, INCLUDING, WITHOUT LIMITATION,
 * THE IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE,
 * AND DATA ACCURACY.  We do not warrant or make any representations
 * regarding the use of the software or the  results thereof, including
 * but not limited to the correctness, accuracy, reliability or
 * usefulness of the software.
 */

package org.mobicents.mgcp.parser;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.StringReader;

import java.text.ParseException;
import org.apache.log4j.Logger;

/**
 * Provides processing of the MGCP message.
 *
 * @author Oleg Kulikov
 * @author Pavel Mitrenko
 */
public class MgcpMessageParser {
  
    private MgcpContentHandler contentHandler;
    private Logger logger = Logger.getLogger(MgcpMessageParser.class);
    
    /** Creates a new instance of MgcpMessageParser */
    public MgcpMessageParser(MgcpContentHandler contentHandler) {
        if (contentHandler == null) {
            throw new IllegalArgumentException("Content handler cannot be null");
        }
        this.contentHandler = contentHandler;
    }
    
    public void parse(String message) throws IOException, ParseException {
        StringReader stringReader = new StringReader(message);
        BufferedReader reader = new BufferedReader(stringReader);
        
        String header = reader.readLine();
        
        if (logger.isDebugEnabled()) {
            logger.debug("Read header: " + header);
        }
        contentHandler.header(header);

        boolean sdpPresent = false;
        String line = null;
        
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (logger.isDebugEnabled()) {
                logger.debug("Read line: " + line);
            }
            
            sdpPresent = line.length() == 0;
            if (sdpPresent) break;
            
            int pos = line.indexOf(':');
            
            if (pos < 0) {
                logger.warn("Unrecognized parameter: " + line);
                continue;
            }
            
            
            String parmName = line.substring(0, pos).trim();
            String parmValue = line.substring(pos + 1).trim();
            
            contentHandler.param(parmName, parmValue);
        }
        
        boolean hasMore = false;
        String ss = null;
        try {
            ss = reader.readLine();
        } catch (IOException e) {
            
        }
        hasMore = ss != null;
        
        if (sdpPresent || hasMore) {
            String sdp = ss + "\n";
            while ((line = reader.readLine()) != null) {
                sdp = sdp + line.trim() + "\r\n";
            }
            if (logger.isDebugEnabled()) {
                logger.debug("Read session description: " + sdp);
            }
            contentHandler.sessionDescription(sdp);
        }
    }
}
