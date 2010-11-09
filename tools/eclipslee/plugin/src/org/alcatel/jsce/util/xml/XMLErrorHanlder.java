
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
package org.alcatel.jsce.util.xml;

import java.util.List;

import org.alcatel.jsce.util.log.SCELogger;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 *  Description:
 * <p>
 *  Handle the error coming from the XML validator and store them in a vector that can 
 *  be retrieve.
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public class XMLErrorHanlder  implements ErrorHandler {
	/** The list of erros handled*/
	private List error = null;

	/**
	 * Constructor. <br> the error vector have to be instancied before.
	 * This vector will collect alla the generated exceptions.
	 */
	public XMLErrorHanlder(List error) {
		super();
		this.error = error;
	}

		/**)
		 * @see org.xml.sax.ErrorHandler#warning(org.xml.sax.SAXParseException)
		 */
		public void warning(SAXParseException arg0) throws SAXException {
			//SCELogger.logInfo("WARNING: line:" + arg0.getLineNumber() +" : "+ arg0.getMessage()); 
			error.add(new ErrorStatus(ErrorStatus.WARNING, arg0, arg0.getLineNumber()));

		}

		/**
		 * @see org.xml.sax.ErrorHandler#error(org.xml.sax.SAXParseException)
		 */
		public void error(SAXParseException arg0) throws SAXException {
			SCELogger.logError("Simple ERROR: "+ arg0.getMessage() + ", System ID: "+ arg0.getSystemId(), arg0.getException());
			error.add(new ErrorStatus(ErrorStatus.ERROR, arg0,arg0.getLineNumber()));

		}

		/**
		 * @see org.xml.sax.ErrorHandler#fatalError(org.xml.sax.SAXParseException)
		 */
		public void fatalError(SAXParseException arg0) throws SAXException {
			SCELogger.logError("Fatal ERROR: "+ arg0.getMessage() + " \n" +
					arg0.toString(), arg0.getException());
			error.add(new ErrorStatus(ErrorStatus.FATAL_ERROR, arg0, arg0.getLineNumber()));

		}
		
		///////////////////////////////////////////
		//
		// Utility methods
		//
		//////////////////////////////////////////
		
		/**
		 * @param typeError error type
		 * @param errors list of @link ErrorStatus
		 * Find if a specific error exists in the list of error.
		 *
		  */
		public static boolean isPresentError(String typeError, List errors){
			for(int i=0;i<errors.size();i++){
				if( ((ErrorStatus) errors.get(i) ).getType().equals(typeError) ){
					return true;
				}
			}
			return false;
		}



}
