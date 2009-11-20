package net.java.slee.resource.diameter.sh.server;

import javax.slee.ActivityContextInterface;
import javax.slee.UnrecognizedActivityException;

/**
 * Obtains an ActivityContextInterface for a given Sh activity
 */
public interface ShServerActivityContextInterfaceFactory {

  /**
   * 
   * @param activity
   * @return
   * @throws UnrecognizedActivityException
   */
  ActivityContextInterface getActivityContextInterface(ShServerActivity activity) throws UnrecognizedActivityException;

  /**
   * 
   * @param activity
   * @return
   * @throws UnrecognizedActivityException
   */
  ActivityContextInterface getActivityContextInterface(ShServerSubscriptionActivity activity) throws UnrecognizedActivityException;
}
