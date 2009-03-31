/*
* "Downloading, compilation and use of this TCK
* is governed by the terms of the JAIN SLEE (JSLEE) V1.1 TECHNOLOGY
* COMPATIBILITY KIT LICENCE AGREEMENT, the text of which is included in
* the download package as LICENSE and can be viewed at
* https://jsleetck11.dev.java.net/jslee-1.1-tck-license.txt"
*/

package com.opencloud.util;

import java.util.HashMap;

/**
 * Simple implementation of Future type, which holds
 * a nullable mutable value, and blocks querying
 * threads until the value is set.
 *
 * This implementation does not support interrupts on
 * querying threads, so as to avoid exception handling code
 * in the caller.
 */
public class Future {
    /**
     * Constructs a future with an unset value
     */
    public Future() {
        value=this;
    }

    /**
     * Constructs a future with the given set value
     */
    public Future(Object newValue) {
        value=newValue;
    }

    /**
     * Blocks the calling thread until the value is set,
     * then returns the value.
     */
    public synchronized Object getValue() {
        while (value == this) {
            try {
                wait();
            }catch(InterruptedException ie) {}
        }
        return value;
    }

    /**
     * Blocks the calling thread until the value is set or
     * the timeout is reached, then returns the value
     * or throws a TimeoutException.
     * @param timeout The amount of time to wait before timing out.
     *  If zero, real time is not taken into consideration and
     *  the method blocks until a value is set.
     */
    public synchronized Object getValue(long timeout) throws Future.TimeoutException {
        if (value != this)
            return value;

        ++threadCount;
        boolean inMap = false;
        long started = -1;
        while (value == this) {
            if (wantsDump > 0 && !inMap) {
                inMap = true;
                threadMap.put(Thread.currentThread(), new Exception().getStackTrace());
                notifyAll();
            }

            long delay = 0;
            if (timeout > 0) {
                if (started == -1) {
                    started = System.currentTimeMillis();
                    delay = timeout;
                } else {
                    delay = started + timeout - System.currentTimeMillis();
                    if (delay <= 0)
                        break;
                }
            }

            try { wait(delay); }
            catch(InterruptedException ie) {}
        }

        --threadCount;
        if (inMap)
            threadMap.remove(Thread.currentThread());
        if (value == this)
            throw new Future.TimeoutException(timeout);
        return value;
    }

    /**
     * Returns whether or not the value has been set.
     */
    public synchronized boolean isSet(){
        return (value!=this);
    }

    /**
     * Sets the given value.
     * @throws IllegalArgumentException
     *  if the argument is this Future object (which is a reserved value)
     */
    public synchronized void setValue(Object newValue){
        if(newValue == this) throw new IllegalArgumentException("Can't set the Future value to the Future instance");
        if(value == this) notifyAll(); // notify waiting threads when the value is set for the first time
        value = newValue;
    }

    public synchronized StackTraceElement[][] getWaitingThreads() {
        if (threadMap == null)
            threadMap = new HashMap();           

        ++wantsDump;

        while (threadMap.size() != threadCount) {
            notifyAll();

            try { wait(); }
            catch (InterruptedException e) {}
        }

        --wantsDump;
        return (StackTraceElement[][])threadMap.values().toArray(new StackTraceElement[0][]);
    }

    /**
     * Thrown from getValue(long timeout) upon a timeout.
     */
    public static class TimeoutException extends Exception {
        public TimeoutException(long timeout) {
            super("The Future value was not set within the specified timeout of "+timeout+" ms");
        }
    }

    private Object value;

    private int threadCount = 0;
    private HashMap threadMap = null;
    private int wantsDump = 0;
}
