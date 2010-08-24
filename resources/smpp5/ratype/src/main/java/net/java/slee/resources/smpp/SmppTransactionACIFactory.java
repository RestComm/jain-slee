package net.java.slee.resources.smpp;

/**
 * 
 * @author amit bhayani
 *
 */
public interface SmppTransactionACIFactory {
	javax.slee.ActivityContextInterface getActivityContextInterface(SmppTransaction txn);

}
