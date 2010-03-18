 /*
  * Mobicents: The Open Source SLEE Platform      
  *
  * Copyright 2003-2005, CocoonHive, LLC., 
  * and individual contributors as indicated
  * by the @authors tag. See the copyright.txt 
  * in the distribution for a full listing of   
  * individual contributors.
  *
  * This is free software; you can redistribute it
  * and/or modify it under the terms of the 
  * GNU Lesser General Public License as
  * published by the Free Software Foundation; 
  * either version 2.1 of
  * the License, or (at your option) any later version.
  *
  * This software is distributed in the hope that 
  * it will be useful, but WITHOUT ANY WARRANTY; 
  * without even the implied warranty of
  * MERCHANTABILITY or FITNESS FOR A PARTICULAR 
  * PURPOSE. See the GNU Lesser General Public License
  * for more details.
  *
  * You should have received a copy of the 
  * GNU Lesser General Public
  * License along with this software; 
  * if not, write to the Free
  * Software Foundation, Inc., 51 Franklin St, 
  * Fifth Floor, Boston, MA
  * 02110-1301 USA, or see the FSF site:
  * http://www.fsf.org.
  */

package org.mobicents.slee.service.common;


import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
/**
 * State interface for the different state classes that implement 
 * the simple (no 1) call flow as recommended in RFC 3725. <br><br>
 * 
 * 1. Initial -- Callee has been invited. Change state depending on response: <br> 
 * --> CalleeTrying <br>
 * --> CalleeRinging <br>
 * --> CalleeInvited <br>
 * --> Terminated <br>
 * <br>
 *  
 * 2. CalleeTrying -- Callee UA has responded 100 Trying <br>
 *  --> CalleeRinging <br>
 *  --> CalleeInvited <br>
 *  --> Terminated <br>
 *  <br>
 * 
 * 3. CalleeRinging -- Callee UA has responded 180 Ringing <br>
 *  --> CalleeInvited <br>
 *  --> Terminated <br>
 *  <br>
 * 
 * 4. CalleeInvited -- Callee has responded 200 OK and caller has been invited <br>
 * --> CallerTrying <br>
 * --> CallerRinging <br>
 * --> CallerInvited <br>
 * --> ?? On cancellation <br>
 * <br>
 * 	 
 * 5. CallerTrying -- Caller has responded 100 Trying <br>
 * --> CallerRinging <br>
 * --> CallerAnswered <br>
 * --> ?? On cancellation <br>
 * <br>
 * 6. CallerRinging -- Caller has responded 180 Ringing <br>
 * --> CallerAnswered <br>
 * --> ?? On cancellation <br>
 * <br>
 * 7. SessionEstablished -- Acks sent to both parties <br>
 * --> UATermination <br>
 *  <br>
 * 8. UATermination -- Bye received and OK sent and Bye sent <br>
 * --> Terminated <br>
 * <br>
 * 9. Terminated <br>
 * 
 * @author niklas
 *
 */
public interface SimpleCallFlowState {
	public void execute(ResponseEvent event);
	public void execute(RequestEvent event);

}
