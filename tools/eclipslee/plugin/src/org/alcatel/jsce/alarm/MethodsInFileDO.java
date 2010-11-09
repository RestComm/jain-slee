
/**
 *   Copyright 2006 Alcatel, OSP.
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
package org.alcatel.jsce.alarm;

import java.util.ArrayList;
import java.util.List;

/**
 *  Description:
 * <p>
 * A data object representing a a set of method contained in the source file.
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public class MethodsInFileDO {
	/** The file name in which the method are contained*/
	private String fileName = null;
	/** The list of methods conatined in the source file*/
	private List methods = null;

	/**
	 * 
	 */
	public MethodsInFileDO() {
		this.methods = new ArrayList();
	}

	/**
	 * @return Returns the methods.
	 */
	public List getMethods() {
		return methods;
	}

	/**
	 * @param methods The methods to set.
	 */
	public void setMethods(List methods) {
		this.methods = methods;
	}

	/**
	 * @return Returns the fileName.
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName The fileName to set.
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
