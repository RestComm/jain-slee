package net.java.slee.resource.diameter.base;

import java.io.IOException;

import net.java.slee.resource.diameter.base.events.AccountingAnswer;
import net.java.slee.resource.diameter.base.events.AccountingRequest;

/**
 * 
 * An AccountingServerSessionActivity represents an accounting session for Diameter accounting clients.
 * 
 * A single AccountingServerSessionActivity will be created for the Diameter session. All requests received 
 * for the session will be fired as events on the same AccountingServerSessionActivity. 
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public interface AccountingServerSessionActivity extends AccountingSessionActivity {

  /**
   * Create an Accounting-Answer with the Acct-Application-Id set to 3.
   * 
   * @return an Accounting-Answer
   */
  AccountingAnswer createAccountingAnswer();

  /**
   * Create an Accounting-Answer with some AVPs populated from the provided Accounting-Request.
   * 
   * The ACA will contain the AVPs specified in createAccountingAnswer() and the following AVPs from the Accounting-Request:
   * 
   * * Accounting-Record-Type
   * * Accounting-Record-Number
   * 
   * @param acr Accounting-Request to copy AVPs from
   * @return an Accounting-Answer
   */
  AccountingAnswer createAccountingAnswer(AccountingRequest acr);

  /**
   * Send an Accounting Answer. 
   * 
   * @param aca answer message to send
   * @throws IOException if the message could not be sent
   * @throws IllegalArgumentException if accountingAnswer is missing any required AVPs
   */
  void sendAccountingAnswer(AccountingAnswer aca) throws IOException, IllegalArgumentException;

}
