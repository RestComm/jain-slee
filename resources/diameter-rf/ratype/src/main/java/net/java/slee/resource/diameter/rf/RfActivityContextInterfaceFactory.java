package net.java.slee.resource.diameter.rf;

import javax.slee.ActivityContextInterface;

/**
 * Declares the methods to obtain an ActivityContextInterface for Rf activities.
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public interface RfActivityContextInterfaceFactory {

  /**
   * 
   * @param cSession
   * @return
   */
  public ActivityContextInterface getActivityContextInterface(RfClientSession cSession);

  /**
   * 
   * @param sSession
   * @return
   */
  public ActivityContextInterface getActivityContextInterface(RfServerSession sSession);

}
