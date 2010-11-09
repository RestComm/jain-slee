
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
package org.alcatel.jsce.util.jad;

import java.util.List;



/**
 *  Description:
 * <p>
 * Adapter between the decompiler and the application.
 * The bridge point to a decompilator implementing @link jadclipse.IDecompiler
 * If we want to change the decompiltor implementation it's here.
 * <br>
 * Singleton pattern.
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public class DecompilatorBridge implements IDecompiler {
	/** The singlton instance*/
	private static  DecompilatorBridge instance = null;
	/** The link to the decompiler*/
	private IDecompiler decompiler = null;

	/**
	 * By default the decompiler is the jad bridge.
	 * Do not forget that we need the modified JAD plug-in and the 
	 * JAD application exe installled.
	 */
	private DecompilatorBridge() {
		decompiler = new JadBrigde();
		
	}
	
	/**
	 * @return the singleton instance
	 */
	public static DecompilatorBridge getInstance(){
		if(instance == null){
			instance = new DecompilatorBridge();
		}
		return instance;
	}

	/**
	 * @return Returns the decompiler.
	 */
	public IDecompiler getDecompiler() {
		return decompiler;
	}

	/**
	 * @param decompiler The decompiler to set.
	 */
	public void setDecompiler(IDecompiler decompiler) {
		this.decompiler = decompiler;
	}

	/**
	 * @see org.alcatel.jsce.util.jad.IDecompiler#getSourceCode(java.lang.String, java.lang.String, java.lang.String)
	 */
	public String getSourceCode(String root, String packageName, String className) {
		return this.decompiler.getSourceCode(root, packageName, className);
	}

	/**
	 * @see org.alcatel.jsce.util.jad.IDecompiler#getLogError()
	 */
	public List getLogError() {
		return this.decompiler.getLogError();
	}

	/**
	 * @see org.alcatel.jsce.util.jad.IDecompiler#getLog()
	 */
	public String getLog() {
		return this.decompiler.getLog();
	}

}
