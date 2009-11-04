/*
 * DialogHandle.java
 *
 * Created on 6 Декабрь 2006 г., 13:37
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.mobicents.slee.resource.smpp.ra;

/**
 *
 * @author Oleg Kulikov
 */
public class DialogHandle  implements SmppActivityHandle {
    
    private final String orig;
    private final String dest;
    
    public DialogHandle(String orig,String dest) {
        this.orig = orig;
        this.dest = dest;
    }

    
    @Override
    public boolean equals(Object o) {
    	if (o != null && o.getClass() == this.getClass()) {
			final DialogHandle other = (DialogHandle) o;
    		return this.dest.equals(other.dest) && this.orig.equals(other.orig);
		}
		else {
			return false;
		}
    }
       
    @Override
    public int hashCode() {
        return orig.hashCode()*31+dest.hashCode();
    }       
    
}
