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

import java.util.HashMap;
import java.util.Vector;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.mobicents.eclipslee.util.Utils;


/**
 * @author cath
 */
public class UsageUtil {
	
	public static HashMap[] getUsageParameters(IFile usageFile) {
		
		Vector usageData = new Vector();
		
		try {
			
			ICompilationUnit unit = (ICompilationUnit) JavaCore.create(usageFile);						
			IType clazz = unit.getTypes()[0];
			IMethod methods[] = clazz.getMethods();
			
			for (int i = 0; i < methods.length; i++) {
				if (methods[i].getElementName().startsWith("sample")) {
					IMethod method = methods[i];
					
					String name = Utils.uncapitalize(method.getElementName().substring(6));
					
					HashMap data = new HashMap();
					data.put("Name", name);
					data.put("Type", "sample");
					usageData.add(data);
					continue;
				}
				
				if (methods[i].getElementName().startsWith("increment")) {
					IMethod method = methods[i];
					
					String name = Utils.uncapitalize(method.getElementName().substring(9));
					
					HashMap data = new HashMap();
					data.put("Name", name);
					data.put("Type", "increment");
					usageData.add(data);
					continue;
				}
				
				// Ignore this non-compliant method.
				
			}
		} catch (JavaModelException e) {
			// Ignore to return what has been successfully entered.
		}
		return (HashMap[]) usageData.toArray(new HashMap[usageData.size()]);
		
	}
}
