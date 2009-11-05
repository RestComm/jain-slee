/*
 * The SMPP resource adaptor.
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

package org.mobicents.slee.resource.smpp.ra;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import net.java.slee.resource.smpp.ClientTransaction;
import net.java.slee.resource.smpp.Dialog;
import net.java.slee.resource.smpp.SmppProvider;

//import org.apache.log4j.Logger;
import net.java.slee.resource.smpp.Transaction;

/**
 *
 * @author Oleg Kulikov
 */
public class SmppProviderImpl implements SmppProvider {
    
    protected ConcurrentHashMap dialogs = new ConcurrentHashMap();        
    private SmppResourceAdaptor resourceAdaptor;
    
//    private Logger logger = Logger.getLogger(SmppProviderImpl.class);
    
    /** Creates a new instance of SmppProviderImpl */
    public SmppProviderImpl(SmppResourceAdaptor resourceAdaptor) {
        this.resourceAdaptor = resourceAdaptor;
    }

    /**
     * Non Java-doc.
     *
     * @see net.java.slee.resource.sms.ratype.Smsprovider#getDialog(String String).
     */
    public Dialog getDialog(String origination, String destination) {
        String key = origination + "#" + destination;
        if (dialogs.containsKey(key)) {
            return (Dialog) dialogs.get(key);
        }
        
        SmppDialogImpl dialog = new SmppDialogImpl(resourceAdaptor, origination, destination);
        dialogs.put(key, dialog);
        
        resourceAdaptor.createDialogHandle(dialog);
        return dialog;
    }

    
    protected ClientTransaction getClientTransaction(int id) {
        System.out.println("Search tx: " + id);
        Iterator list = dialogs.values().iterator();
        while (list.hasNext()) {
            SmppDialogImpl dialog = (SmppDialogImpl) list.next();
            System.out.println("Checking " + dialog);
            Transaction tx = dialog.getTransaction(id);
            if (tx != null) {
                return (ClientTransaction)tx;
            }
            System.out.println("Not found");
        }
        return null;
    }
}
