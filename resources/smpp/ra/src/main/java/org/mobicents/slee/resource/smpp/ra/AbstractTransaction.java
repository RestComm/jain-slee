/*
 * AbstractTransaction.java
 *
 * Created on 30 ������� 2006 �., 19:28
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.mobicents.slee.resource.smpp.ra;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import net.java.slee.resource.smpp.Transaction;
//import org.apache.log4j.Logger;

/**
 *
 * @author Oleg Kulikov
 */
public class AbstractTransaction extends SmppActivityImpl implements Transaction {
    public final static int TTL = 60;
    
    protected SmppDialogImpl dialog;
    protected int id;
    
    protected Timer timer = new Timer();
    //private Logger logger = Logger.getLogger(AbstractTransaction.class);
    
    public AbstractTransaction(int id, SmppDialogImpl dialog) {
    	super();
    	this.id = id;
        this.dialog = dialog;
        timer.schedule(new Terminator(this), TTL * 1000, TTL * 1000);
        super.setActivityHandle(new TransactionHandle(id));
    }

    /**
     * (Non Java-doc).
     *
     * @see net.java.slee.resource.smpp.Transaction#getId().
     */
    public int getId() {
        return id;
    }

    public Date getLastActivity() {
        return  null;
    }
    
    private class Terminator extends TimerTask {
        private Transaction tx;
        
        public Terminator(Transaction tx) {
            this.tx = tx;
        }
        
        public void run() {
            timer.cancel();
            dialog.terminate(tx);
            //if (logger.isDebugEnabled()) {
            //    logger.debug("Transaction " + id + " is expired");
            //}
        }
    }
    
    public String toString() {
        return "tx =" + id; 
    }
    
}
