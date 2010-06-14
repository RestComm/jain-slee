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

package org.mobicents.jcc.inap;

import EDU.oswego.cs.dl.util.concurrent.ConcurrentReaderHashMap;
import EDU.oswego.cs.dl.util.concurrent.PooledExecutor;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import javax.csapi.cc.jcc.CallLoadControlListener;
import javax.csapi.cc.jcc.EventFilter;
import javax.csapi.cc.jcc.InvalidArgumentException;
import javax.csapi.cc.jcc.InvalidPartyException;
import javax.csapi.cc.jcc.InvalidStateException;
import javax.csapi.cc.jcc.JccAddress;
import javax.csapi.cc.jcc.JccCall;
import javax.csapi.cc.jcc.JccCallListener;
import javax.csapi.cc.jcc.JccConnectionListener;
import javax.csapi.cc.jcc.JccProvider;
import javax.csapi.cc.jcc.JccProviderListener;
import javax.csapi.cc.jcc.MethodNotSupportedException;
import javax.csapi.cc.jcc.PrivilegeViolationException;
import javax.csapi.cc.jcc.ProviderUnavailableException;
import javax.csapi.cc.jcc.ResourceUnavailableException;
import org.apache.log4j.Logger;
import org.mobicents.jcc.inap.address.JccCalledPartyBCDNumber;
import org.mobicents.jcc.inap.address.JccCalledPartyNumber;
import org.mobicents.jcc.inap.address.JccCallingPartyNumber;
import org.mobicents.jcc.inap.protocol.parms.CalledPartyBcdNumber;
import org.mobicents.jcc.inap.protocol.parms.CalledPartyNumber;
import org.mobicents.jcc.inap.protocol.parms.CallingPartyNumber;
import org.mobicents.protocols.ss7.sccp.SccpListener;
import org.mobicents.protocols.ss7.sccp.SccpProvider;
import org.mobicents.protocols.ss7.sccp.impl.SccpPeer;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.TCAPStackImpl;
import org.mobicents.protocols.ss7.tcap.api.TCAPProvider;
import org.mobicents.protocols.ss7.tcap.api.TCAPStack;
import org.mobicents.protocols.ss7.tcap.api.TCListener;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCBeginIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCContinueIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCEndIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCPAbortIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCUniIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCUserAbortIndication;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;


/**
 *
 * @author Oleg Kulikov
 */
public class JccInapProviderImpl implements JccProvider, TCListener {
    public final static int DEFAULT_POOL_SIZE = 10;
    protected final static String name = "Java call control provider for INAP 1.1";
    
    protected ArrayList callListeners = new ArrayList();
    protected ArrayList connectionListeners = new ArrayList();
    
    private int state = JccProvider.OUT_OF_SERVICE;
    
    protected SccpProvider sccpProvider = null;
    protected TCAPStack tcapStack = null;
    protected TCAPProvider tcapProvider = null;
    protected ConcurrentReaderHashMap calls = new ConcurrentReaderHashMap();
    protected ConcurrentReaderHashMap connections = new ConcurrentReaderHashMap();
    
    private Properties properties;
    
    private PooledExecutor threadPool;
    private Logger logger = Logger.getLogger(JccInapProviderImpl.class);
    
    private Thread monitor;
    private boolean stopped = false;
    
    /** Creates a new instance of JccInapProviderImpl */
    public JccInapProviderImpl(Properties properties) {
        this.properties = properties;
        try {
            String sccpProviderName = properties.getProperty("sccp.provider");
            String sccpProps = properties.getProperty("sccp.config");
            
            SccpPeer sccpPeer = new SccpPeer(sccpProviderName);
            this.sccpProvider = sccpPeer.getProvider(sccpProps);
            this.tcapStack = new TCAPStackImpl(this.sccpProvider);
            this.tcapProvider = this.tcapStack.getProvider();
            this.tcapProvider.addTCListener(this);
            //sccpProvider.setSccpListener(this);
            logger.info("Initialized SCCP provider");
                        
            int poolSize = DEFAULT_POOL_SIZE;
            if (properties.getProperty("thread.pool.size") != null) {
                try {
                    poolSize = Integer.parseInt(properties.getProperty("thread.pool.size")); 
                } catch (NumberFormatException e) {
                    logger.warn("Use default pool size", e);
                }
            }
            
            threadPool = new PooledExecutor(poolSize);
            logger.info("Initialized thread pool");
            
            state = JccProvider.IN_SERVICE;
            monitor = new Thread(new Monitor());
            monitor.start();
            logger.info("Started monitor");
        } catch (Exception e) {
            logger.error("Could not load JccProvider", e);
            throw new ProviderUnavailableException(e.getMessage());
        }
    }
    
    /**
     * (Non java-doc)
     *
     * @see javax.csapi.cc.jcc.JccProvier#addCallListener.
     */
    public void addCallListener(JccCallListener listener)
    throws MethodNotSupportedException, ResourceUnavailableException {
        callListeners.add(listener);
    }
    
    /**
     * (Non java-doc)
     *
     * @see javax.csapi.cc.jcc.JccProvier#addCallLoadControlListener.
     */
    public void addCallLoadControlListener(CallLoadControlListener listener)
    throws MethodNotSupportedException, ResourceUnavailableException {
        throw new MethodNotSupportedException("Call load controll not supported");
    }
    
    /**
     * (Non java-doc)
     *
     * @see javax.csapi.cc.jcc.JccProvier#addConnectionListener.
     */
    public void addConnectionListener(JccConnectionListener listener, EventFilter filter)
    throws ResourceUnavailableException, MethodNotSupportedException {
        Object[] ls = new Object[2];
        ls[0] = listener;
        ls[1] = new DefaultFilter();
        connectionListeners.add(ls);
    }
    
    public void addProviderListener(JccProviderListener providerlistener)
    throws ResourceUnavailableException, MethodNotSupportedException {
        throw new MethodNotSupportedException();
    }
    
    /**
     * (Non Java-doc).
     * @see javax.csapi.cc.jcc.JccProvider#createCall().
     */
    public JccCall createCall()
    throws InvalidStateException, ResourceUnavailableException, PrivilegeViolationException, MethodNotSupportedException {
        return null;
    }
    
    public EventFilter createEventFilterAddressRange(String lowAddress, String highAddress, int matchDisposition, int nomatchDisposition)
    throws ResourceUnavailableException, InvalidArgumentException {
        return null;
    }
    
    public EventFilter createEventFilterAddressRegEx(String addressRegex, int matchDisposition, int nomatchDisposition)
    throws ResourceUnavailableException, InvalidArgumentException {
        return null;
    }
    
    public EventFilter createEventFilterAnd(EventFilter[] filters, int nomatchDisposition)
    throws ResourceUnavailableException, InvalidArgumentException {
        return null;
    }
    
    public EventFilter createEventFilterCauseCode(int causeCode, int matchDisposition, int nomatchDisposition)
    throws ResourceUnavailableException, InvalidArgumentException {
        return null;
    }
    
    public EventFilter createEventFilterDestAddressRange(
            String lowDestAddress, String highDestAddress, int matchDisposition, int nomatchDisposition) throws ResourceUnavailableException, InvalidArgumentException {
        return null;
    }
    
    public EventFilter createEventFilterDestAddressRegEx(
            String destAddressRegex, int matchDisposition, int nomatchDisposition) throws ResourceUnavailableException, InvalidArgumentException {
        return null;
    }
    
    public EventFilter createEventFilterEventSet(
            int[] blockEvents, int[] notifyEvents) throws ResourceUnavailableException, InvalidArgumentException {
        return null;
    }
    
    public EventFilter createEventFilterMidCallEvent(
            int midCallType, String midCallValue, int matchDisposition, int nomatchDisposition) throws ResourceUnavailableException, InvalidArgumentException {
        return null;
    }
    
    public EventFilter createEventFilterMinimunCollectedAddressLength(
            int minimumAddressLength, int matchDisposition, int nomatchDisposition) throws ResourceUnavailableException, InvalidArgumentException {
        return null;
    }
    
    public EventFilter createEventFilterOr(
            EventFilter[] filters, int nomatchDisposition) throws ResourceUnavailableException, InvalidArgumentException {
        return null;
    }
    
    public EventFilter createEventFilterOrigAddressRange(
            String lowOrigAddress, String highOrigAddress, int matchDisposition, int nomatchDisposition) throws ResourceUnavailableException, InvalidArgumentException {
        return null;
    }
    
    public EventFilter createEventFilterOrigAddressRegEx(
            String origAddressRegex, int matchDisposition, int nomatchDisposition) throws ResourceUnavailableException, InvalidArgumentException {
        return null;
    }
    
    public JccAddress getAddress(String address) throws InvalidPartyException {
        return null;
    }
    
    public String getName() {
        return name;
    }
    
    public int getState() {
        return state;
    }
    
    public void removeCallListener(JccCallListener calllistener) {
    }
    
    public void removeCallLoadControlListener(CallLoadControlListener loadcontrollistener) {
    }
    
    public void removeConnectionListener(JccConnectionListener connectionlistener) {
    }
    
    public void removeProviderListener(JccProviderListener providerlistener) {
    }
    
    public void setCallLoadControl(JccAddress[] address, double duration, double[] mechanism, int[] treatment) throws MethodNotSupportedException {
    }
    
    public void shutdown() {
    	stopped = true;
        callListeners.clear();
        connectionListeners.clear();
        calls.clear();
        connections.clear();
        threadPool.shutdownNow();
        sccpProvider.shutdown();
        state = JccProvider.SHUTDOWN;
        stopped = true;
    }
    
//    public void onMessage(SccpAddress calledPartyAddress, SccpAddress callingPartyAddress, byte[] data) {
//    	logger.info(" Received MSG: "+data.length+" --> "+calledPartyAddress+" --> "+callingPartyAddress);
//        ByteArrayInputStream in = new ByteArrayInputStream(data);
//        
//        int mt = in.read() & 0xff;
//        int len = 0;
//        
//        try {
//            len = Util.readLen(in);
//        } catch (IOException e) {
//            logger.error("Could not decode length of the TC_MESSAGE:", e);
//            return;
//        }
//        
//        byte[] buffer = new byte[len];
//        try {
//            in.read(buffer);
//        } catch (IOException e) {
//        }
//        
//        TCMessage tcMessage = null;
//        switch (mt) {
//            case TCMessage.BEGIN :
//                try {
//                    tcMessage = new TCBegin(buffer);
//                } catch (IOException e) {
//                    logger.error("Could not decode TC_BEGIN", e);
//                    return;
//                }
//                break;
//            case TCMessage.CONTINUE :
//                try {
//                    tcMessage = new TCContinue(buffer);
//                } catch (IOException e) {
//                    logger.error("Could not decode TC_CONTINUE", e);
//                    return;
//                }
//                break;
//            case TCMessage.ABORT :
//                try {
//                    tcMessage = new TCAbort(buffer);
//                } catch (IOException e) {
//                    logger.error("Could not decode TC_ABORT", e);
//                    return;
//                }
//                break;
//            case TCMessage.END :
//                try {
//                    tcMessage = new TCAbort(buffer);
//                } catch (IOException e) {
//                    logger.error("Could not decode TC_END", e);
//                    return;
//                }
//                break;
//            default :
//                logger.warn("Unexpected TCMessage: " + Integer.toHexString(mt) + "h");
//                return;
//        }
//        
//        if (logger.isDebugEnabled()) {
//            logger.debug("--> " + tcMessage);
//        }
//
//        TCHandler handler = new TCHandler(this, calledPartyAddress, callingPartyAddress, tcMessage);
//        try {
//            threadPool.execute(handler);
//        } catch (InterruptedException e) {
//        }
//    }
    
//    protected synchronized void send(SccpAddress calledParty, SccpAddress callingParty, TCMessage msg) throws IOException {
//        sccpProvider.send(calledParty, callingParty,  msg.toByteArray());
//        if (logger.isDebugEnabled()) {
//            logger.debug("<-- " + msg);
//        }
//    }
        
    protected JccCallImpl getCall(JccAddress callingNumber) {
        JccCallImpl call = (JccCallImpl) calls.get(callingNumber.getName());
        return call;
    }
      
    protected AbstractConnection getConnection(long connectionID) {
        return (AbstractConnection) connections.get(new Long(connectionID));
    }
    
    protected synchronized JccAddress createAddress(CalledPartyNumber cpn) {
        return new JccCalledPartyNumber(this, cpn);
    }
    
    protected synchronized JccAddress createAddress(CalledPartyBcdNumber cpn) {
        return new JccCalledPartyBCDNumber(this, cpn);
    }
    
    protected synchronized JccAddress createAddress(CallingPartyNumber cpn) {
        return new JccCallingPartyNumber(this, cpn);
    }
    
    protected JccCallImpl createCall(JccAddress callingNumber) {
        return new JccCallImpl(this, callingNumber);
    }
        
    //NOTE: for test, we are server, we dont care
    
    
    /* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.tcap.api.TCListener#dialogReleased(org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog)
	 */
	public void dialogReleased(Dialog d) {
		logger.info("Dialog has been released. Id: "+d.getDialogId());
		
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.tcap.api.TCListener#onInvokeTimeout(org.mobicents.protocols.ss7.tcap.asn.comp.Invoke)
	 */
	public void onInvokeTimeout(Invoke tcInvokeRequest) {
		logger.info("Invocation has timed out! ID: "+tcInvokeRequest.getInvokeId());
		//this is called for local tc invoke
		
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.tcap.api.TCListener#onTCBegin(org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCBeginIndication)
	 */
	public void onTCBegin(TCBeginIndication ind) {
		logger.info("Received TC Begin! Dialog ID: "+ind.getDialog().getDialogId());
		TCHandler handler = new TCHandler(this,this.tcapProvider, ind);
		try {
			threadPool.execute(handler);
		} catch (InterruptedException e) {
		}
		
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.tcap.api.TCListener#onTCContinue(org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCContinueIndication)
	 */
	public void onTCContinue(TCContinueIndication ind) {
		logger.info("Received TC Continue! Dialog ID: "+ind.getDialog().getDialogId());
		TCHandler handler = new TCHandler(this,this.tcapProvider, ind);
		try {
			threadPool.execute(handler);
		} catch (InterruptedException e) {
		}
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.tcap.api.TCListener#onTCEnd(org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCEndIndication)
	 */
	public void onTCEnd(TCEndIndication ind) {
		logger.info("Received TC End! Dialog ID: "+ind.getDialog().getDialogId());
		TCHandler handler = new TCHandler(this, this.tcapProvider,ind);
		try {
			threadPool.execute(handler);
		} catch (InterruptedException e) {
		}
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.tcap.api.TCListener#onTCUni(org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCUniIndication)
	 */
	public void onTCUni(TCUniIndication ind) {
		logger.info("Received TC Uni! Dialog ID: "+ind.getDialog().getDialogId());
		
	}
	

	public void onTCPAbort(TCPAbortIndication ind) {
		logger.info("Received TC PAbort! Dialog ID: "+ind.getDialog().getDialogId());
		TCHandler handler = new TCHandler(this, this.tcapProvider,ind);
		try {
			threadPool.execute(handler);
		} catch (InterruptedException e) {
		}
		
	}

	public void onTCUserAbort(TCUserAbortIndication ind) {
		logger.info("Received TC UAbort! Dialog ID: "+ind.getDialog().getDialogId());
		TCHandler handler = new TCHandler(this, this.tcapProvider,ind);
		try {
			threadPool.execute(handler);
		} catch (InterruptedException e) {
		}
		
	}


	private class Monitor implements Runnable {
        @SuppressWarnings("static-access")
        public void run() {
            while (!stopped) {
                try {
                    Thread.currentThread().sleep(60000);
                    logger.info("active calls:" + calls.size() + 
                            ", active connections: " + connections.size());
                } catch (InterruptedException e) {
                    stopped = true;
                }
            }
        }
    }




}
