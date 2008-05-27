package org.mobicents.slee.resource.sip;

import java.util.Iterator;

import javax.sip.InvalidArgumentException;
import javax.sip.ListeningPoint;
import javax.sip.ObjectInUseException;
import javax.sip.ProviderDoesNotExistException;
import javax.sip.SipException;
import javax.sip.SipProvider;
import javax.sip.SipStack;
import javax.sip.TransportNotSupportedException;
import javax.sip.address.Router;

/**
 * <p>
 * Title: SIP_RA
 * </p>
 * 
 * <p>
 * Description: JAIN SIP Resource Adaptor
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * 
 * <p>
 * Company: Lucent Technologies
 * </p>
 * 
 * @author mvera@lucent.com
 * @version 1.0
 */
public class SipStackProxy implements SipStack {
    private SipStack stack = null;

    private SipProvider proxy = null;

    public void release() {
        proxy = null;
        stack = null;
    }

    public SipStackProxy(SipStack stack, SipProviderProxy proxy) {
        this.stack = stack;
        this.proxy = proxy;
    }

    /**
     * createListeningPoint
     * 
     * @param _int
     *            int
     * @param string
     *            String
     * @return ListeningPoint
     * @throws TransportNotSupportedException
     * @throws InvalidArgumentException
     */
    public ListeningPoint createListeningPoint(int _int, String string)
            throws TransportNotSupportedException, InvalidArgumentException {
        return stack.createListeningPoint(_int, string);
    }

    /**
     * createSipProvider
     * 
     * @param listeningPoint
     *            ListeningPoint
     * @return SipProvider
     * @throws ObjectInUseException
     */
    public SipProvider createSipProvider(ListeningPoint listeningPoint)
            throws ObjectInUseException {
        throw new java.lang.SecurityException();
    }

    /**
     * deleteListeningPoint
     * 
     * @param listeningPoint
     *            ListeningPoint
     * @throws ObjectInUseException
     */
    public void deleteListeningPoint(ListeningPoint listeningPoint)
            throws ObjectInUseException {
        stack.deleteListeningPoint(listeningPoint);
    }

    /**
     * deleteSipProvider
     * 
     * @param sipProvider
     *            SipProvider
     * @throws ObjectInUseException
     */
    public void deleteSipProvider(SipProvider sipProvider)
            throws ObjectInUseException {
        throw new java.lang.SecurityException();
    }

    /**
     * getIPAddress
     * 
     * @return String
     */
    public String getIPAddress() {
        return stack.getIPAddress();
    }

    /**
     * getListeningPoints
     * 
     * @return Iterator
     */
    public Iterator getListeningPoints() {
        return stack.getListeningPoints();
    }

    /**
     * getRouter
     * 
     * @return Router
     */
    public Router getRouter() {
        return stack.getRouter();
    }

    /**
     * getSipProviders
     * 
     * @return Iterator
     */
    public Iterator getSipProviders() {
        return new MyIterator();
    }

    /**
     * getStackName
     * 
     * @return String
     */
    public String getStackName() {
        return stack.getStackName();
    }

    /**
     * isRetransmissionFilterActive
     * 
     * @return boolean
     */
    public boolean isRetransmissionFilterActive() {
        return stack.isRetransmissionFilterActive();
    }

    /**
     * <p>
     * Title: SIP_RA
     * </p>
     * 
     * <p>
     * Description: JAIN SIP Resource Adaptor
     * </p>
     * 
     * <p>
     * Copyright: Copyright (c) 2005
     * </p>
     * 
     * <p>
     * Company: Lucent Technologies
     * </p>
     * 
     * @author mvera@lucent.com
     * @version 1.0
     */
    private class MyIterator implements Iterator {
        boolean read = false;

        /**
         * Returns <tt>true</tt> if the iteration has more elements.
         * 
         * @return <tt>true</tt> if the iterator has more elements.
         */
        public boolean hasNext() {
            return !read;
        }

        /**
         * Returns the next element in the iteration.
         * 
         * @return the next element in the iteration.
         */
        public Object next() {
            if (!read) {
                read = true;
                return proxy;
            }
            throw new java.util.NoSuchElementException();
        }

        /**
         * Removes from the underlying collection the last element returned by
         * the iterator (optional operation).
         */
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.sip.SipStack#createListeningPoint(java.lang.String, int,
     *      java.lang.String)
     */
    public ListeningPoint createListeningPoint(String ipAddress, int port,
            String transport) throws TransportNotSupportedException,
            InvalidArgumentException {

        return this.stack.createListeningPoint(ipAddress, port, transport);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.sip.SipStack#stop()
     */
    public void stop() {
        this.stack.stop();

    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.sip.SipStack#start()
     */
    public void start() throws ProviderDoesNotExistException, SipException {
        this.stack.start();

    }
}