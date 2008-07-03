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
public abstract class Operation implements Serializable  {
    
    private Object[] args;
    
    /** Creates a new instance of Operation */
    public Operation() {
    }
    
    public Operation(Object[] args) {
        this.args = args;
    }
    
    public static Operation getInstance(String name, Object[] args) {
        if (name.equals("rem")) {
            return new RemoveOperation(args);
        } else if (name.equals("ins")) {
            return new InsertOperation(args);
        }
        return null;
    }
    
    public abstract String doExecute(String[] args);
    
    public String execute() {
        String argv[] = new String[args.length];
        int i = 0;
        if (args[0] instanceof Operation) {
           argv[0] = ((Operation)args[0]).execute();
           i++;
        }
        
        while (i < args.length) {
            argv[i] = (String)args[i];
            i++;
        }
        
        return doExecute(argv);
    }
    
}
