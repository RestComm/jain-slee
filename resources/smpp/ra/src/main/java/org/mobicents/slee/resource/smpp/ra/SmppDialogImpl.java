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

import java.util.Date;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import net.java.slee.resource.smpp.ClientTransaction;
import net.java.slee.resource.smpp.Dialog;
import net.java.slee.resource.smpp.ServerTransaction;
import net.java.slee.resource.smpp.ShortMessage;
import net.java.slee.resource.smpp.Transaction;

/**
 * Implements Generic Dialog interface.
 *
 * @author Oleg Kulikov
 */
public class SmppDialogImpl extends SmppActivityImpl implements Dialog {
    private static int GENERATOR = 1;
    public final static int TTL = 300;
    
    private String localAddress;
    private String remoteAddress;
    private Date lastActivity;
    
    private ConcurrentHashMap<Integer, AbstractTransaction> transactions = new ConcurrentHashMap<Integer,AbstractTransaction>();
    protected SmppResourceAdaptor resourceAdaptor;
    
    private Timer timer;
    //private Logger logger = Logger.getLogger(SmppDialogImpl.class);
    
    /** 
     * Creates a new instance of SmppDialogImpl.
     *
     * @param provider represents provider.
     * @param localAddress the address of the origination party.
     * @param remoteAddress the address of the termination party. 
     */
    public SmppDialogImpl(SmppResourceAdaptor resourceAdaptor, 
            String localAddress, String remoteAddress) {
    	super();
    	this.resourceAdaptor = resourceAdaptor;
        this.localAddress = localAddress;
        this.remoteAddress = remoteAddress;
    	this.lastActivity = new Date();
        this.timer = new Timer();
        this.timer.schedule(new TimerTask() {
            public void run() {
                close();
            }
        }, TTL * 1000, TTL * 1000);
    }
        
    /**
     * (Non Java-doc)
     *
     * @see net.java.slee.resource.sms.ratype.Dialog#getId();
     */
    public String getId() {
        lastActivity = new Date();
        return localAddress + "$" + remoteAddress;
    }
    
    /**
     * (Non Java-doc)
     *
     * @see net.java.slee.resource.sms.ratype.Dialog#getLastActivity();
     */
    public Date getLastActivity() {
        return lastActivity;
    }
    
    /**
     * (Non Java-doc)
     *
     * @see net.java.slee.resource.sms.ratype.Dialog#getLocalAddress();
     */
    public String getLocalAddress() {
        lastActivity = new Date();
        return localAddress;
    }
    
    /**
     * (Non Java-doc)
     *
     * @see net.java.slee.resource.sms.ratype.Dialog#getRemoteAddress();
     */
    public String getRemoteAddress() {
        lastActivity = new Date();
        return remoteAddress;
    }
    
    /**
     * (Non Java-doc)
     *
     * @see net.java.slee.resource.sms.ratype.Dialog#createMessage();
     */
    public ShortMessage createMessage() {
        lastActivity = new Date();
        ShortMessage message = new SmppMessageImpl(localAddress, remoteAddress);
        return message;
    }
    
    protected ClientTransaction getTransaction(int id) {
        return (ClientTransaction) transactions.get(Integer.valueOf(id));
    }
    
    /**
     * (Non Java-doc)
     *
     * @see net.java.slee.resource.sms.ratype.Dialog#createDataSmTransaction();
     */
    public ClientTransaction createDataSmTransaction() {
        lastActivity = new Date();
        final ClientDataSmTransactionImpl tx = new ClientDataSmTransactionImpl(GENERATOR++, 
                this);
        final AbstractTransaction anotherTx = createTransaction(tx);
        return anotherTx == null ? tx : (ClientTransaction) anotherTx;
    }

    protected ServerTransaction createDataSmServerTransaction(int id, ShortMessage msg) {
    	final ServerDataSmTransactionImpl tx = new ServerDataSmTransactionImpl(id, this, msg);
        final AbstractTransaction anotherTx = createTransaction(tx);
        return anotherTx == null ? tx : (ServerTransaction) anotherTx;
        
    }
    
    private AbstractTransaction createTransaction(AbstractTransaction newTx) {
    	AbstractTransaction tx = transactions.putIfAbsent(Integer.valueOf(newTx.getId()), newTx);
    	if (tx == null) {
    		// was inserted
    		resourceAdaptor.createTransactionHandle(newTx);
    	}
        return tx;
    }
    
    /**
     * (Non Java-doc)
     *
     * @see net.java.slee.resource.sms.ratype.Dialog#createSubmitSmTransaction();
     */
    public ClientTransaction createSubmitSmTransaction() {
        lastActivity = new Date();
        final ClientSubmitSmTransactionImpl tx = new ClientSubmitSmTransactionImpl((int)System.currentTimeMillis(), 
                this);
        final AbstractTransaction anotherTx = createTransaction(tx);
        return anotherTx == null ? tx : (ClientTransaction) anotherTx;
    }

    protected ServerTransaction createSubmitSmServerTransaction(int id, ShortMessage msg) {
        final ServerSubmitSmTransactionImpl tx = new ServerSubmitSmTransactionImpl(id, this, msg);
        final AbstractTransaction anotherTx = createTransaction(tx);
        return anotherTx == null ? tx : (ServerTransaction) anotherTx;
    }
    
    /**
     * (Non Java-doc)
     *
     * @see net.java.slee.resource.sms.ratype.Dialog#createDeliverSmTransaction();
     */
    public ClientTransaction createDeliverSmTransaction() {
        lastActivity = new Date();
        final ClientDeliverSmTransactionImpl tx = new ClientDeliverSmTransactionImpl(GENERATOR++, 
                this);
        final AbstractTransaction anotherTx = createTransaction(tx);
        return anotherTx == null ? tx : (ClientTransaction) anotherTx;
    }
    
    protected ServerTransaction createDeliverSmServerTransaction(int id, ShortMessage msg) {
        final ServerDeliverSmTransactionImpl tx = new ServerDeliverSmTransactionImpl(id, this, msg);
        final AbstractTransaction anotherTx = createTransaction(tx);
        return anotherTx == null ? tx : (ServerTransaction) anotherTx;
    }
    
    /**
     * Termonates specified transactions.
     *
     * @param tx the transaction bean termonated.
     */
    protected void terminate(Transaction tx) {
        try {
            ((AbstractTransaction) tx).timer.cancel();
            //if (logger.isDebugEnabled()) {
            //    logger.debug("Disable timer for tx=" + tx);
            //}
        } catch (Exception e) {
            //ignore
        }
        transactions.remove(Integer.valueOf(tx.getId()));
        resourceAdaptor.terminate(tx);
        //if (logger.isDebugEnabled()) {
        //    logger.debug("Terminate transaction handle " + tx);
        //}
    }
    
    /**
     * (Non Java-doc)
     *
     * @see net.java.slee.resource.sms.ratype.Dialog#close();
     */
    public void close() {
        try {
            timer.cancel();
        } catch (Exception e) {
            //ignore
        }
        
        Iterator list = transactions.values().iterator();
        while (list.hasNext()) {
            Transaction tx = (Transaction) list.next();
            terminate(tx);
        }
        
        resourceAdaptor.terminate(this);
        //if (logger.isDebugEnabled()) {
        //    logger.debug("Terminate dialog handler " + this);
        //}
    }

    @Override
    public String toString() {
        return "SmppDialogImpl id = " + localAddress + "#" + remoteAddress; 
    }
}
