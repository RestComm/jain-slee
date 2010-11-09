
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

/**
 *  Description:
 * <p>
 * *  <p>
 * This class is used to store a specific error.
 * </p>
 */
public class ErrorStatus {
	public static final String INFO = "Info";
	public static String WARNING="Warning";
	public static String ERROR="Error";
	public static String FATAL_ERROR="Fatal_Error";
	
	private String type="";
	private int line = 0;
	private Exception exception;
	private String message = "";

	/**
	 * 
	 */
	public ErrorStatus(String type, Exception e, int line) {
		this.type=type;
		this.exception=e;
		this.line= line;
	}

	public ErrorStatus(String sbbErrorMessage, int i) {
		this.type=INFO;
		this.line= i;
		this.message = sbbErrorMessage;
	}

	/**
	 * @return
	 */
	public Exception getException() {
		return exception;
	}

	/**
	 * @return
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param exception
	 */
	public void setException(Exception exception) {
		this.exception = exception;
		if(exception !=null)
			this.message= exception.getMessage();
	}

	/**
	 * @param string
	 */
	public void setType(String string) {
		type = string;
	}

	/**
	 * @return Returns the line.
	 */
	public int getLine() {
		return line;
	}
	/**
	 * @param line The line to set.
	 */
	public void setLine(int line) {
		this.line = line;
	}

	public String getMessage() {
		if(message.length()>0)
			return message;
		else
			if(exception!=null)
				return exception.getMessage();
			else
				return "";
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
