package org.mobicents.sleetests.usage.sample.resetandupdate;

import javax.slee.ActivityContextInterface;

import com.opencloud.sleetck.lib.resource.events.TCKResourceEventX;
import com.opencloud.sleetck.lib.sbbutils.BaseTCKSbb;
import com.opencloud.sleetck.lib.sbbutils.TCKSbbUtils;

/**
 * Tests if the reset method is actually correctly clearing values in the sample-type.
 * Related to Issue 25 @ http://code.google.com/p/mobicents/issues/detail?id=25
 *  
 * @author Alexandre Mendonça
 */
public abstract class SampleUsageResetAndUpdateSbb extends BaseTCKSbb
{
  // SBB Usage Method
  public abstract SampleUsageResetAndUpdateSbbUsage getDefaultSbbUsageParameterSet();  

  public void onTCKResourceEventX1(TCKResourceEventX event, ActivityContextInterface aci)
  {
    try
    {
      // Get the sample values...
      Integer[] samples = (Integer[]) event.getMessage();
      
      // Make them count
      for( Integer sample : samples )
      {
        this.getDefaultSbbUsageParameterSet().sampleVariables( sample );
      }
      
      // Send message saying we're done here
      TCKSbbUtils.getResourceInterface().sendSbbMessage(null);
    }
    catch (Exception e)
    {
      TCKSbbUtils.handleException(e);
    }   
  }
}