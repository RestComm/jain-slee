/*
 * DialogHandle.java
 *
 * Created on 6 Декабрь 2006 г., 13:37
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.mobicents.slee.resource.smpp.ra;

import javax.slee.resource.ActivityHandle;
import net.java.slee.resource.smpp.Dialog;

/**
 *
 * @author Oleg Kulikov
 */
public class DialogHandle  implements ActivityHandle {
    
    private Dialog dialog;
    
    public DialogHandle(Dialog dialog) {
        this.dialog = dialog;
    }

    
    public boolean equals(Object o) {
    	if (o != null && o.getClass() == this.getClass()) {
			return ((DialogHandle)o).dialog.getId().equals(this.dialog.getId());
		}
		else {
			return false;
		}
    }
       
    public int hashCode() {
        return dialog.getId().hashCode();
    }       
    
}
