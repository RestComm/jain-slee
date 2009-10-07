package net.java.slee.resource.diameter.base;

import java.io.IOException;

import net.java.slee.resource.diameter.base.events.AbortSessionAnswer;
import net.java.slee.resource.diameter.base.events.AbortSessionRequest;
import net.java.slee.resource.diameter.base.events.ReAuthAnswer;
import net.java.slee.resource.diameter.base.events.ReAuthRequest;
import net.java.slee.resource.diameter.base.events.SessionTerminationRequest;
import net.java.slee.resource.diameter.base.events.avp.TerminationCauseType;

public interface AuthClientSessionActivity extends AuthSessionActivity{

  /**
   * Create an Abort-Session-Answer with the Auth-Application-Id set to 0.
   * 
   * @param abortSessionRequest
   * @return
   */
  AbortSessionAnswer createAbortSessionAnswer();

  /**
   * Create an Abort-Session-Answer with some AVPs populated from the provided Abort-Session-Request.
   * 
   * @param abortSessionRequest
   * @return
   */
  AbortSessionAnswer createAbortSessionAnswer(AbortSessionRequest abortSessionRequest);

  /**
   * Send an Abort-Session-Answer.
   * 
   * @param abortSessionAnswer
   * @throws IOException if the message could not be sent
   * @throws IllegalArgumentException if abortSessionAnswer is missing any required AVPs
   */
  void sendAbortSessionAnswer(AbortSessionAnswer abortSessionAnswer) throws IOException, IllegalArgumentException;

  /**
   * Create an Re-Auth-Answer with the Auth-Application-Id set to 0.
   * 
   * @return
   */
  ReAuthAnswer createReAuthAnswer();

  /**
   * Create an Re-Auth-Answer with some AVPs populated from the provided Re-Auth-Request.
   * 
   * @param reAuthRequest
   * @return
   */
  ReAuthAnswer createReAuthAnswer(ReAuthRequest reAuthRequest);

  /**
   * Send an Re-Auth-Answer.
   * 
   * @param reAuthAnswer answer message to send
   * @throws IOException if the message could not be sent
   * @throws IllegalArgumentException if abortSessionAnswer is missing any required AVPs
   */
  void sendReAuthAnswer(ReAuthAnswer reAuthAnswer) throws IOException, IllegalArgumentException;

  /**
   * Create an Session-Termination-Request message populated with the following AVPs:
   * 
   *  * Termination-Cause: as per terminationCause parameter
   *  * Auth-Application-Id: the value 0 as specified by RFC3588 
   * 
   * @param terminationCause
   * @return a new SessionTerminationRequest
   */
  SessionTerminationRequest createSessionTerminationRequest(TerminationCauseType terminationCause);

  /**
   * Send an Session Termination Request. An event containing the answer will be fired on this activity.
   * 
   * @param sessionTerminationRequest the Session-Termination-Request message to send
   * @throws IOException if the message could not be sent
   * @throws IllegalArgumentException if sessionTerminationRequest is missing any required AVPs
   */
  void sendSessionTerminationRequest(SessionTerminationRequest sessionTerminationRequest) throws IOException, IllegalArgumentException;

}
