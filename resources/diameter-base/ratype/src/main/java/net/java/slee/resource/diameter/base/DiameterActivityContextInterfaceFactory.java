package net.java.slee.resource.diameter.base;

import javax.slee.ActivityContextInterface;
import javax.slee.FactoryException;
import javax.slee.UnrecognizedActivityException;

/**
 * 
 * Interface for factory implemented by the SLEE to create ACIs.
 * 
 * @author Open Cloud 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public interface DiameterActivityContextInterfaceFactory {

  /**
   * 
   * @param activity
   * @return
   * @throws NullPointerException
   * @throws UnrecognizedActivityException
   * @throws FactoryException
   */
  ActivityContextInterface getActivityContextInterface(DiameterActivity activity) throws UnrecognizedActivityException, FactoryException;

  /**
   * 
   * @param activity
   * @return
   * @throws UnrecognizedActivityException
   */
  ActivityContextInterface getActivityContextInterface(AccountingClientSessionActivity activity) throws UnrecognizedActivityException;

  /**
   * 
   * @param activity
   * @return
   * @throws UnrecognizedActivityException
   */
  ActivityContextInterface getActivityContextInterface(AccountingServerSessionActivity activity) throws UnrecognizedActivityException;
}
