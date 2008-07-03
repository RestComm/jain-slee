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

import java.io.Serializable;

/**
 *
 * @author Oleg Kulikov
 */
public class Pattern implements Serializable, Comparable {
    private String pattern;
    private String template;
    
    /** Creates a new instance of Pattern */
    public Pattern(String pattern) {
        this.pattern = pattern;
        this.template = toRegExpression(pattern.split("/")[0]);
    }
    
    private String toRegExpression(String pattern) {
        pattern = pattern.replaceAll("x", "\\\\d");
        pattern = pattern.replaceAll("\\*", "\\\\w*");
        return pattern;
    }
    
    public String getTemplate() {
        return template;
    }
    
    public String getResult(String number) {
        String[] parts = pattern.split("/");
        if (parts.length == 1) {
            return number;
        }
        
        int i = 1;
        Object expression = number;
        
        while ( i < parts.length) {
            String operationExpression = parts[i];
            
            String[] oparts = operationExpression.split(" ");
            
            String name = oparts[0];
            String[] args = oparts[1].split(",");
            
            Object[] argv = new Object[args.length + 1];
            argv[0] = expression;
            for (int j = 0; j < args.length; j++ ) {
                argv[j+1] = args[j];
            }
            expression = Operation.getInstance(name, argv);
            i++;
        }
        return ((Operation) expression).execute();
    }
    
    public boolean matches(String pattern) {
        return pattern.matches(template);
    }
    
    public int compareTo(Object other) {
        return template.compareTo(((Pattern)other).getTemplate());
    }
}
