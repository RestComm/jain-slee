
/**
 *   Copyright 2005 Alcatel, OSP.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package org.alcatel.jsce.interfaces.com;

/**
 *  Description:
 * <p>
 * This interface allows back-end object to send message to the Eclipse plat-form or any 
 * other plat-form, without needing to know the receiver.
 * <br> The abstract method pattern</br>
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public interface ICommunication {
	
	
	/**
	 * Ask the receiver to show the message.
	 * @param msg
	 */
	public void sendMessageInfo(String msg);
	
	/**
	 * Ask the receiver to show the error message in an error dialog window.
	 * @param msg
	 */
	public void sendMessageError(String msg);

}
