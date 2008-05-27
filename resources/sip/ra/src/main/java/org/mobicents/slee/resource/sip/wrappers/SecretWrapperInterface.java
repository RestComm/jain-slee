package org.mobicents.slee.resource.sip.wrappers;

import javax.sip.Transaction;

import org.mobicents.slee.resource.sip.SipActivityHandle;
/**
 * 
 * This is interface which is implemented by TXs wrapper classes. This way we does not ahve
 * to check which Tx has been passes into method when argument is of type javax.sip.Transaction.
 * We can cast it to this interface and have quicker access to those two methods.
 * 
 * @author M. Ranganathan
 * @author B. Baranowski
 */
public interface SecretWrapperInterface {
  public Transaction getRealTransaction();
  public void setDialogWrapper(DialogWrapper wrapperDialog);
  public SipActivityHandle getActivityHandle();
}
