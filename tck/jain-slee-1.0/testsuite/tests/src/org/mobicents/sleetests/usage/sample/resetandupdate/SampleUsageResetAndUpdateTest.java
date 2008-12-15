package org.mobicents.sleetests.usage.sample.resetandupdate;

import java.util.Random;

import javax.management.ObjectName;
import javax.slee.usage.SampleStatistics;

import com.opencloud.sleetck.lib.TCKTestResult;
import com.opencloud.sleetck.lib.resource.TCKActivityID;
import com.opencloud.sleetck.lib.resource.events.TCKResourceEventX;
import com.opencloud.sleetck.lib.testsuite.usage.common.GenericUsageTest;
import com.opencloud.sleetck.lib.testutils.QueuingNotificationListener;
import com.opencloud.sleetck.lib.testutils.QueuingResourceListener;
import com.opencloud.sleetck.lib.testutils.jmx.SbbUsageMBeanProxy;

/**
 * Makes updates to usage parameters, then accesses them with reset parameter se to true.
 * Makes more updates, then accesses the values to check they're still ok.
 * Related to Issue 25 @ http://code.google.com/p/mobicents/issues/detail?id=25
 *  
 * @author Alexandre Mendonça
 */
public class SampleUsageResetAndUpdateTest extends GenericUsageTest
{
  private static final String SERVICE_DU_PATH_PARAM = "serviceDUPath";
  private static final Random rand = new Random();
  
  public TCKTestResult run() throws Exception
  {
    TCKActivityID activityID = utils().getResourceInterface().createActivity( "SampleUsageResetAndUpdateTest-Activity" );

    utils().getLog().info( "Sending first update requests" );
    
    Integer[] samples = new Integer[] {rand.nextInt(), rand.nextInt(), rand.nextInt(), rand.nextInt()};
    
    sendInstructionsAndWait( activityID, samples );

    utils().getLog().debug( "Got back! Checking values." );

    // Get data and reset!
    SampleStatistics sbbSS = (SampleStatistics) utils().getMBeanFacade().invoke( suObjectName, "getVariables", new Object[] {new Boolean(true)}, new String[] {"boolean"} );

    if(!checkResult( samples, sbbSS ))
      return TCKTestResult.failed( 0, "Result returned by MBean differs from expected!" );
    
    // Add new data...
    samples = new Integer[] {rand.nextInt(), rand.nextInt(), rand.nextInt(), rand.nextInt()};
    
    sendInstructionsAndWait( activityID, samples );
    
    utils().getLog().debug( "Got back! Checking values (2nd time)." );

    sbbSS = (SampleStatistics) utils().getMBeanFacade().invoke( suObjectName, "getVariables", new Object[] {new Boolean(true)}, new String[] {"boolean"} );

    if( !checkResult( samples, sbbSS ) )
      return TCKTestResult.failed( 0, "Result returned by MBean differs from expected!" );

    return TCKTestResult.passed();
  }
  
  private boolean checkResult(Integer[] samples, SampleStatistics ss)
  {
    long calcMin = Long.MAX_VALUE;
    long calcMax = Long.MIN_VALUE;
    double calcMean = 0;
    long calcCount = samples.length;
    
    for(Integer i : samples)
    {
      if( i < calcMin )
        calcMin = i;
      
      if( i > calcMax )
        calcMax = i;
      
      calcMean += i;
    }
    
    calcMean /= samples.length;
    
    long resultMin = ss.getMinimum();
    long resultMax = ss.getMaximum();
    double resultMean = ss.getMean();
    long resultSampleCount = ss.getSampleCount();

    if( calcMin == resultMin && calcMax == resultMax && resultMean == calcMean && resultSampleCount == calcCount )
      return true;
    
    String output = "Minimum => Expected: " + calcMin + " -- Got: " + resultMin + "\r\n";
    output += "Maximum => Expected: " + calcMax + " -- Got: " + resultMax + "\r\n";
    output += "Mean => Expected: " + calcMean + " -- Got: " + resultMean + "\r\n";
    output += "Sample Count => Expected: " + calcCount + " -- Got: " + resultSampleCount;
    
    utils().getLog().error( output );
    
    return false;
  }

  private void sendInstructionsAndWait( TCKActivityID activityID, Integer[] samples ) throws Exception
  {
    utils().getLog().info( "Firing event to Sbb" );
    utils().getResourceInterface().fireEvent( TCKResourceEventX.X1, samples, activityID, null );
  
    utils().getLog().info( "Waiting for reply from Sbb" );
    resourceListener.nextMessage();
    utils().getLog().info( "Received reply" );
  
    utils().getLog().info( "Waiting for usage notifications" );
    for( int i = 0; i < samples.length; i++ )
    {
      notificationListener.nextNotification();
    }
    utils().getLog().info( "Received all " + samples.length + " usage notifications" );
  }

  public void setUp() throws Exception
  {
    setupService( SERVICE_DU_PATH_PARAM, true );

    setResourceListener( resourceListener = new QueuingResourceListener( utils() ) );

    utils().getResourceInterface().setResourceListener(resourceListener);

    notificationListener = new QueuingNotificationListener( utils() );

    suObjectName = new ObjectName( "slee:SbbUsageMBean=ServiceID[SampleUsageResetAndUpdateService#org.mobicents#1.0]/SbbID[SampleUsageResetAndUpdateSbb#org.mobicents#1.0]" );

    sbbUsageMBeanProxy = utils().getMBeanProxyFactory().createSbbUsageMBeanProxy( suObjectName );
    
    sbbUsageMBeanProxy.addNotificationListener( notificationListener, null, null );
  }

  public void tearDown() throws Exception
  {
    if(sbbUsageMBeanProxy != null && notificationListener != null)
      sbbUsageMBeanProxy.removeNotificationListener( notificationListener );
    
    super.tearDown();
  }

  private QueuingResourceListener resourceListener;
  private QueuingNotificationListener notificationListener;

  private ObjectName suObjectName;
  private SbbUsageMBeanProxy sbbUsageMBeanProxy;
  
}