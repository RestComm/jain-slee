
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
package org.alcatel.jsce.util.classpath;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaCore;

/**
 *  Description:
 * <p>
 * Represents a set of libraries needed to compile the code which will be generate by the SCE;
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public class ClasspathContainer implements IClasspathContainer {
	/** The project*/
	private IProject project = null;

	/**
	 * 
	 */
	public ClasspathContainer(IProject proj) {
		super();
		this.project = proj;
	}

	/**
	 * @see org.eclipse.jdt.core.IClasspathContainer#getClasspathEntries()
	 */
	public IClasspathEntry[] getClasspathEntries() {
//	    IClasspathEntry[]  entries = new IClasspathEntry[OSPProjectCreator.LIBS.length+1];
//	    IPath path = project.getFullPath().append(
//				"/" + OSPProjectCreator.SLEE_JAR.toOSString());
//	    IPath docPath = project.getFullPath().append("/" + OSPProjectCreator.SLEE_API_ZIP.toOSString());
//	    entries[0] = JavaCore.newLibraryEntry(path, docPath /* No available source */, null /* hell knows */);
//	    
//	    for (int i = 1; i < OSPProjectCreator.LIBS.length; i++) {
//			IPath path_i = OSPProjectCreator.LIBS[i];
//			IPath fullPath_i = project.getFullPath().append("/"+ path_i.toOSString());
//			entries[i] = JavaCore.newLibraryEntry(fullPath_i, null /* No available source */, null /* hell knows */);
//		}
//	    //Last library
//	   // SCELogger.logInfo("Defaut JVM installed : "+JavaRuntime.getDefaultVMInstall().getName() +" on "+JavaRuntime.getDefaultVMInstall().getInstallLocation().getPath());
//	    IPath toolsJarPath = new Path(JavaRuntime.getDefaultVMInstall().getInstallLocation().getPath()).append("lib").append("tools.jar"); 
//	    entries[OSPProjectCreator.LIBS.length]= JavaCore.newLibraryEntry(toolsJarPath, null /* No available source */, null /* hell knows */);
//		return entries;

			return null;
	}

	/**
	 * @see org.eclipse.jdt.core.IClasspathContainer#getDescription()
	 */
	public String getDescription() {
		return "Alcatel SCE-SE Libraries";
	}

	/**
	 * @see org.eclipse.jdt.core.IClasspathContainer#getKind()
	 */
	public int getKind() {
		return IClasspathContainer.K_APPLICATION;
	}

	/**
	 * @see org.eclipse.jdt.core.IClasspathContainer#getPath()
	 */
	public IPath getPath() {
		return new Path("org.alcatel.jsce.SCELIBS_CONTAINER");
	}

}
