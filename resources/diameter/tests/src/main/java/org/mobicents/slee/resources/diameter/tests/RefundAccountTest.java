package org.mobicents.slee.resources.diameter.tests;

import java.io.FileNotFoundException;

import org.junit.Test;
import org.mobicents.slee.resources.diameter.tests.framework.TestingFramework;

public class RefundAccountTest
{

  @Test
  public void runTest() throws FileNotFoundException
  {
    TestingFramework tF = new TestingFramework();
    
    tF.runTest( this.getClass().getResourceAsStream( "xml/refund-account-test.xml" ) );
  }
  
}
