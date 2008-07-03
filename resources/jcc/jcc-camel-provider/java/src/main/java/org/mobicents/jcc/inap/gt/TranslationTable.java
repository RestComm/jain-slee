/*
 * The Java Call Control API for CAMEL 2
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

package org.mobicents.jcc.inap.gt;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.log4j.Logger;
import org.mobicents.jcc.inap.protocol.parms.CalledPartyBcdNumber;
import org.mobicents.jcc.inap.protocol.parms.CalledPartyNumber;

/**
 *
 * @author Oleg Kulikov
 */
public class TranslationTable {
    
    private String fileName;
    private ArrayList rules;
    
    private Logger logger = Logger.getLogger(TranslationTable.class);
    
    /** Creates a new instance of TranslationTable */
    public TranslationTable(String fileName) {
        this.fileName = fileName;
    }
    
    private void parse() throws FileNotFoundException, IOException {
        rules = new ArrayList();
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream(fileName)));
        String line = null;
        while ((line = reader.readLine()) != null) {
            line = line.toLowerCase();
            line = line.trim();
            
            //ignore empty lines and comments
            if (line.length() == 0 || line.startsWith("#")) {
                continue;
            }
            
            String[] tokens = line.split(":");
            if (tokens.length != 3) {
                logger.warn("Syntax error at line: " + line);
                continue;
            }
            
            int ind1 = Str2Indicator(tokens[0]);
            if (ind1 == -1) {
                logger.warn("Unknown prefix: " + tokens[0]);
                continue;
            }
            
            int ind2 = Str2Indicator(tokens[2]);
            if (ind2 == -1) {
                logger.warn("Unknown suffix: " + tokens[2]);
                continue;
            }
            
            LinePattern linePattern = new LinePattern(ind1, ind2, tokens[1]);
            
            if (linePattern != null) {
                rules.add(linePattern);
            }
        }
    }

    
    private int Str2Indicator(String str) {
        if (str.equals("spare")) {
            return CalledPartyNumber.SPARE;
        } else if (str.equals("unknown")) {
            return CalledPartyNumber.UNKNOWN;
        } else if (str.equals("subscriber")) {
            return CalledPartyNumber.SUBSCRIBER;
        } else if (str.equals("national")) {
            return CalledPartyNumber.NATIONAL;
        } else if (str.equals("international")) {
            return CalledPartyNumber.INTERNATIONAL;
        } else return -1; 
    }
    
    public CalledPartyNumber translate(CalledPartyNumber origin) throws FileNotFoundException, IOException {
        parse();
        Iterator list = rules.iterator();
        while (list.hasNext()) {
            LinePattern rule = (LinePattern) list.next();
            if (origin.getNai() == rule.getIncomingType() && 
                    rule.getPattern().matches(origin.getAddress())) {
                return new CalledPartyNumber(
                        rule.getOutgoingType(), 
                        origin.getInni(),
                        origin.getNpi(),
                        rule.getPattern().getResult(origin.getAddress()));
            }
        }
        return origin;
    }
    
    public CalledPartyNumber translate(CalledPartyBcdNumber origin) throws FileNotFoundException, IOException {
        parse();
        Iterator list = rules.iterator();
        while (list.hasNext()) {
            LinePattern rule = (LinePattern) list.next();
            if (origin.getNi() == rule.getIncomingType() && 
                    rule.getPattern().matches(origin.getAddress())) {
                return new CalledPartyNumber(
                        rule.getOutgoingType(), 
                        0,
                        origin.getNp(),
                        rule.getPattern().getResult(origin.getAddress()));
            }
        }
        return new CalledPartyNumber(
                        origin.getNi(), 
                        0,
                        origin.getNp(),
                        origin.getAddress());
    }
}
