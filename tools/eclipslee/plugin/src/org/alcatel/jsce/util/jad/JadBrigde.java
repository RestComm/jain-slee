
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

import java.util.ArrayList;
import java.util.List;

import org.alcatel.jsce.util.log.SCELogger;

/**
 *  Description:
 * <p>
 * Bridge between the jad de-compiler and the SCE-SE.
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public class JadBrigde implements IDecompiler {
	/** The jad decompiler*/
	//private JadDecompiler decompiler = null;

	/**
	 * COnstructor.
	 */
	public JadBrigde() {
		//decompiler = new JadDecompiler();
	}
	
	/**
	 * @see org.alcatel.jsce.util.jad.IDecompiler#getSourceCode(java.lang.String, java.lang.String, java.lang.String)
	 */
	public String getSourceCode(String root, String packageName, String className){
		//decompiler.decompile(root, packageName, className);
		SCELogger.logInfo("Decompiling source file in "+ packageName +"/"+ className);
		//String src = decompiler.getSource();
		String src = "JAD is not install on this version";
		return src;
	}
	
	/**
	 * @see org.alcatel.jsce.util.jad.IDecompiler#getLogError()
	 */
	public List getLogError(){
		//return decompiler.getExceptions();
		return new ArrayList();
	}
	
	/**
	 * @see org.alcatel.jsce.util.jad.IDecompiler#getLog()
	 */
	public String getLog(){
		//return decompiler.getLog();
		return  "JAD is not install on this version";
	}

}
