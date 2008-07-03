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

/**
 *
 * @author Oleg Kulikov
 */
public class LinePattern implements Comparable{
    
    private int incomingType;
    private int outgoingType;
    private Pattern pattern;
    
    /** Creates a new instance of RoutePattern */
    public LinePattern(int incomingType, int outgoingType, String pattern)  {
        this.incomingType = incomingType;
        this.outgoingType = outgoingType;
        this.pattern = new Pattern(pattern);
    }
    
    public int getIncomingType() {
        return incomingType;
    }
    
    public int getOutgoingType() {
        return outgoingType;
    }
    
    public Pattern getPattern() {
        return pattern;
    }
    
    public int compareTo(Object other) {
        return pattern.compareTo(((LinePattern)other).getPattern());
    }
    
    
}
