/**
 *   Copyright 2005 Open Cloud Ltd.
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

package org.mobicents.eclipslee.servicecreation.util;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

/**
 * @author cath
 */
public class ClassUtil {

	/**
	 * Creates a method in the specified 'file' with the given 'contents'.  'contents'
	 * must include the method signature, e.g. public void setFoo(String foo) { this.foo = foo; }
	 * @param file
	 * @param contents
	 * @throws JavaModelException
	 */
	
	public static void addMethodToClass(IFile file, String contents) throws JavaModelException {
		ICompilationUnit unit = (ICompilationUnit) JavaCore.create(file);
		IType clazz = unit.getTypes()[0];
		
		clazz.createMethod(contents, null, true, null);		
	}
	
	public static void removeMethodFromClass(IFile file, String methodName) throws JavaModelException {
		ICompilationUnit unit = (ICompilationUnit) JavaCore.create(file);
		IType clazz = unit.getTypes()[0];
		IMethod methods[] = clazz.getMethods();
		for (int i = 0; i < methods.length; i++) {
			IMethod method = methods[i];
			
			if (method.getElementName().equals(methodName)) {
				method.delete(true, null);
				return;
			}			
		}
	}

	public static void renameMethodInClass(IFile file, String oldMethodName, String newMethodName) throws JavaModelException {
		ICompilationUnit unit = (ICompilationUnit) JavaCore.create(file);
		IType clazz = unit.getTypes()[0];
		IMethod methods[] = clazz.getMethods();
		for (int i = 0; i < methods.length; i++) {
			IMethod method = methods[i];
		
			if (method.getElementName().equals(oldMethodName)) {
				String source = method.getSource();
				source = "\t" + source.replaceFirst(oldMethodName, newMethodName) + "\n\n";				
				removeMethodFromClass(file, oldMethodName);
				addMethodToClass(file, source);
				return;
			}
			
		}
	}

}
