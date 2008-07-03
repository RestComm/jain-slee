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

package org.mobicents.util;

import EDU.oswego.cs.dl.util.concurrent.Semaphore;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Oleg Kulikov
 */
public class LocalTimer {
    
    private Runnable task;
    private Semaphore semaphore = new Semaphore(0);
    private boolean canceled = false;
    private int delay;
    
    private Timer timer;
    private TimerTask timerTask;
    
    /** Creates a new instance of Timer */
    public LocalTimer() {
    }
    
    public synchronized void schedule(Runnable task, int delay) {
        this.task = task;
        this.delay = delay;
        timer = new Timer();
        timer.schedule(new LocalTimerTask(), delay*1000);
        canceled = false;
    }
    
    public synchronized void stop() {
        if (!canceled) {
            timer.cancel();
        }
        canceled = true;
    }
    
    public synchronized void reset(int delay) {
        stop();
        schedule(task, delay);
    }
    
    private class LocalTimerTask extends TimerTask {
        public void run() {
            task.run();
            canceled = true;
        }
    }
}
