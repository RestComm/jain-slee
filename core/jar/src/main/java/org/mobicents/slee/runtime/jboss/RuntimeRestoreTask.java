/*
 * RuntimeRestoreTask.java
 * 
 * Created on Sep 11, 2005
 * 
 * Created by: M. Ranganathan
 *
 * The Mobicents Open SLEE project
 * 
 * A SLEE for the people!
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

package org.mobicents.slee.runtime.jboss;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.runtime.facilities.TimerFacilityImpl;
import org.mobicents.slee.runtime.facilities.nullactivity.NullActivityFactoryImpl;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;

/**
 * 
 * A task that restores the runtime state of the slee after failover.
 *  
 */
public class RuntimeRestoreTask implements Runnable {
    Logger logger = Logger.getLogger(RuntimeRestoreTask.class);

    public RuntimeRestoreTask() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Runnable#run()
     */
    public void run() {

        SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
        SleeTransactionManager txmgr = sleeContainer.getTransactionManager();
        boolean rb = true;
        try {
           
            
            // Get the timer facility and restart timers.
            logger.info("Restore state after restart!");

            txmgr.begin();
            // Restore the runtime state of the delivered sets.
            // The slee might have failed mid event delivery.

            NullActivityFactoryImpl naf = sleeContainer
                    .getNullActivityFactory();
            naf.restart();            
            rb = false;
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("An exception occurred while restoring facility", ex);
            try {
                if (txmgr.getTransaction() != null && rb) {
                    txmgr.setRollbackOnly();
                }
            } catch (Exception e) {
                throw new RuntimeException("Error restoring cache!", e);
            }

        } finally {
            try {
                if (txmgr.getTransaction() != null)
                    txmgr.commit();
            } catch (Exception ex) {
                throw new RuntimeException("Error restoring cache!!", ex);
            }

        }

    }

}

