
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
package org.alcatel.jsce.util.log;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.mobicents.eclipslee.servicecreation.ServiceCreationPlugin;

/**
 *  Description:
 * <p>
 *   The logger of convenience of the SCE plug-in.
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public class SCELogger { 
	/**
	    * Log the specified information.
	    * 
	    * @param message, a human-readable message, 
	    * 		localized to the current locale.
	    */
	   public static void logInfo(String message) {
	      log(IStatus.INFO, IStatus.OK, message, null);
	   }

	   /**
	    * Log the specified error.
	    * 
	    * @param exception, a low-level exception.
	    */
	   public static void logError(Throwable exception) {
	      logError("Unexpected Exception", exception);
	   }

	   /**
	    * Log the specified error.
	    * 
	    * @param message, a human-readable message, 
	    * 		localized to the current locale.
	    * @param exception, a low-level exception, 
	    * 		or <code>null</code> if not applicable.
	    */
	   public static void logError(
	      String message, Throwable exception) {
	      log(IStatus.ERROR, IStatus.OK, message, exception);
	   }

	   /**
	    * Log the specified information.
	    * 
	    * @param severity, the severity; one of the following:
	    * 		<code>IStatus.OK</code>,
	    *		<code>IStatus.ERROR</code>, 
	    *		<code>IStatus.INFO</code>, 
	    *		or <code>IStatus.WARNING</code>.
	    * @param pluginId. the unique identifier of the relevant 
	    * plug-in.
	    * @param code, the plug-in-specific status code, or 
	    * <code>OK</code>.
	    * @param message, a human-readable message, 
	    * 		localized to the current locale.
	    * @param exception, a low-level exception, 
	    * 		or <code>null</code> if not applicable.
	    */
	   public static void log(
	      int severity,
	      int code,
	      String message,
	      Throwable exception) {

	      log(createStatus(severity, code, message, exception));
	   }

	   /**
	    * Create a status object representing the 
	    * specified information.
	    * 
	    * @param severity, the severity; one of the following:
	    * 		<code>IStatus.OK</code>,
	    *		<code>IStatus.ERROR</code>, 
	    *		<code>IStatus.INFO</code>, 
	    *		or <code>IStatus.WARNING</code>.
	    * @param pluginId, the unique identifier of the 
	                * relevant plug-in.
	    * @param code, the plug-in-specific status code, 
	    * or <code>OK</code>.
	    * @param message, a human-readable message, 
	    * 		localized to the current locale.
	    * @param exception, a low-level exception, 
	    * 		or <code>null</code> if not applicable.
	    * @return, the status object (not <code>null</code>).
	    */
	   public static IStatus createStatus(
	      int severity,
	      int code,
	      String message,
	      Throwable exception) {

	      return new Status(
	         severity,  ServiceCreationPlugin.getDefault().getBundle().getSymbolicName(),  code,
	         message,
	         exception);
	   }

	   /**
	    * Log the given status.
	    *
	    * @param status, the status to log.
	    */
	   public static void log(IStatus status) {
		   ServiceCreationPlugin.getDefault().getLog().log(status);
	   }}
