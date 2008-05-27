/*
 * SampleStatisticsImpl.java
 * 
 * Created on Jan 12, 2005
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

package org.mobicents.slee.container.management.jmx;

import java.io.Serializable;

import javax.slee.usage.SampleStatistics;

import org.jboss.logging.Logger;

/**
 * The implementation of the sample statistics class.
 * 
 * @author M. Ranganathan
 */
public class SampleStatisticsImpl implements SampleStatistics, Serializable {
    private static Logger logger = Logger.getLogger(SampleStatisticsImpl.class);

    private long sampleCount;

    private long minimum;

    private long maximum;

    private double mean;

    public SampleStatisticsImpl(double mean, long minimum, long maximum,
            long sampleCount) {
        //new Exception().printStackTrace();
        this.sampleCount = sampleCount;
        this.minimum = minimum;
        this.maximum = maximum;
        /*if (sampleCount != 0) {
            this.mean = (double) ((double) sum / (double) sampleCount);
        } else
            this.mean = 0;*/
        this.mean = mean;
            
        if (logger.isDebugEnabled()) {
            logger.debug("Generating SampleSatisticsImpl ... ");
            logger.debug("sampleCount = " + sampleCount);
            logger.debug("minimum = " + minimum);
            logger.debug("maximum = " + maximum);
            logger.debug("mean = " + mean);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.slee.usage.SampleStatistics#getSampleCount()
     */
    public long getSampleCount() {

        return sampleCount;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.slee.usage.SampleStatistics#getMinimum()
     */
    public long getMinimum() {

        return this.minimum;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.slee.usage.SampleStatistics#getMaximum()
     */
    public long getMaximum() {

        return this.maximum;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.slee.usage.SampleStatistics#getMean()
     */
    public double getMean() {

        return this.mean;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
      return "Sample Count: " + sampleCount + "\r\n" +
      "Minimum: " + minimum + "\r\n" +
      "Maximum: " + maximum + "\r\n" +
      "Mean: " + mean + "\r\n";
    }
}

