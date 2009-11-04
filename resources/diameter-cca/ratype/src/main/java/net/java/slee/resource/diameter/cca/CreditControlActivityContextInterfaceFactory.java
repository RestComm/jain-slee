package net.java.slee.resource.diameter.cca;

import javax.slee.ActivityContextInterface;

/**
 * Interface for factory implemented by the SLEE to create ACIs.
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a> 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a> 
 */
public interface CreditControlActivityContextInterfaceFactory {

  /**
   * Method for obtaining a ACI wrapping the given Credit-Control Client Session.
   * @param cccs the Credit-Control Client Session
   * @return an ActivityContextInterface
   */
  public ActivityContextInterface getActivityContextInterface(CreditControlClientSession cccs);

  /**
   * Method for obtaining a ACI wrapping the given Credit-Control Server Session.
   * @param ccss the Credit-Control Client Session
   * @return an ActivityContextInterface
   */
  public ActivityContextInterface getActivityContextInterface(CreditControlServerSession ccss);

}
