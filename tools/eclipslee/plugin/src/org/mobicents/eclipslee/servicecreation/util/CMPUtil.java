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
import org.eclipse.jdt.core.Signature;
import org.mobicents.eclipslee.servicecreation.ServiceCreationPlugin;
import org.mobicents.eclipslee.util.Utils;
import org.mobicents.eclipslee.util.slee.xml.components.ProfileSpecXML;
import org.mobicents.eclipslee.util.slee.xml.components.SbbRefXML;
import org.mobicents.eclipslee.util.slee.xml.components.SbbXML;
import org.w3c.dom.Element;


/**
 * @author cath
 */
public class CMPUtil {
	
	public static HashMap[] getCMPFields(SbbXML sbb, IFile abstractFile, String project) {
		
		Vector cmpFields = new Vector();
		
		try {
			ICompilationUnit unit = (ICompilationUnit) JavaCore.create(abstractFile);		
			IType clazz = unit.getTypes()[0];
			IMethod methods[] = clazz.getMethods();
			
			for (int i = 0; i < methods.length; i++) {
				if (methods[i].getElementName().startsWith("get")) {
					IMethod method = methods[i];
					
					String name = Utils.uncapitalize(method.getElementName().substring(3));
					String type = method.getReturnType();
					
					if (sbb.getCMPField(name) != null) {		
						HashMap profileData = new HashMap();
						profileData.put("Name", name);
						profileData.put("Type", Signature.toString(type));		

						String alias = sbb.getCMPField(name).getSbbAliasRef();
						if (alias == null) {
							profileData.put("Stored SBB", "N/A");
						} else {	
							SbbRefXML refXML = sbb.getSbbRef(alias);
							SbbXML sbbXML = SbbFinder.getDefault().getSbbXML(refXML, project);
							
							profileData.put("Stored SBB", sbbXML.toString());
							profileData.put("SBB XML", sbbXML);
						}						
						
						cmpFields.add(profileData);
					}
				}
			}
		} catch (JavaModelException e) {
			ServiceCreationPlugin.log("Unable to retrieve CMP fields from " + sbb);			
		}
		
		return (HashMap []) cmpFields.toArray(new HashMap[cmpFields.size()]);
	}


	public static HashMap[] getCMPFields(ProfileSpecXML profile, IFile cmpClass, IFile mgmtIface) {
		
		Vector cmpFields = new Vector();
		
		try {
			ICompilationUnit unit = (ICompilationUnit) JavaCore.create(cmpClass);		
			IType clazz = unit.getTypes()[0];
			IMethod methods[] = clazz.getMethods();

			IType ifaceClazz = null;			
			if (mgmtIface != null) {		
				ICompilationUnit ifaceUnit = (ICompilationUnit) JavaCore.create(mgmtIface);
				if (ifaceUnit.exists())
					ifaceClazz = ifaceUnit.getTypes()[0];
			}
			
			for (int i = 0; i < methods.length; i++) {
				if (methods[i].getElementName().startsWith("get")) {
					IMethod method = methods[i];
					
					String name = Utils.uncapitalize(method.getElementName().substring(3));
					String type = method.getReturnType();
					
					HashMap profileData = new HashMap();
					profileData.put("Name", name);
					profileData.put("Type", Signature.toString(type));

					Element attr = profile.getIndexedAttribute(name);
					if (attr == null) {
						profileData.put("Indexed", Boolean.FALSE);
						profileData.put("Unique", Boolean.FALSE);
					} else {
						profileData.put("Indexed", Boolean.TRUE);
						if (attr.getAttribute("unique").equalsIgnoreCase("true"))
							profileData.put("Unique", Boolean.TRUE);					
						else
							profileData.put("Unique", Boolean.FALSE);					
					}
					
					if (ifaceClazz != null) {						
						IMethod meth = ifaceClazz.getMethod(method.getElementName(), new String[] {});
						if (meth.exists())
							profileData.put("Visible", Boolean.TRUE);
						else
							profileData.put("Visible", Boolean.FALSE);
					} else
						profileData.put("Visible", Boolean.TRUE);			
					
					cmpFields.add(profileData);
				}
			}

			
			
		} catch (JavaModelException e) {
			ServiceCreationPlugin.log("Unable to load profile CMP fields from: " + profile);
		}
		
		return (HashMap []) cmpFields.toArray(new HashMap[cmpFields.size()]);
	}

	
	public static String getAccessors(Vector cmpFields) {		
		String output = "";
		
		for (int i = 0; i < cmpFields.size(); i++) {
			HashMap cmpData = (HashMap) cmpFields.get(i);
			
			String name = (String) cmpData.get("Name");
			String type = (String) cmpData.get("Type");

			output += "\t//'" + Utils.uncapitalize(name) + "' CMP field\n";
			output += "\tpublic abstract void set" + Utils.capitalize(name) + "(" + type + " value);\n";
			output += "\tpublic abstract " + type + " get" + Utils.capitalize(name) + "();\n\n";			
		}

		return output;
	}

	public static String[] getAccessors(HashMap [] cmpFields) {
		
		if (cmpFields == null)
			return new String[0];
		
		String output[] = new String[cmpFields.length * 2];
		int index = 0;
		
		for (int i = 0; i < cmpFields.length; i++) {
			HashMap cmpData = cmpFields[i];
			
			String name = (String) cmpData.get("Name");
			String type = (String) cmpData.get("Type");
			
			output[index++] = "\t// '" + Utils.uncapitalize(name) + "' CMP field setter\n\tpublic abstract void set" + Utils.capitalize(name) + "(" + type + " value);\n";
			output[index++] = "\t// '" + Utils.uncapitalize(name) + "' CMP field getter\n\tpublic abstract " + type + " get" + Utils.capitalize(name) + "();\n\n";
		}

		return output;
	}
	
	public static boolean isValidName(String s) {
		
		if (s.length() == 0 || !Character.isJavaIdentifierStart(s.charAt(0))) {
			return false;
		}
		
		for (int i = 1; i < s.length(); i++) {
			if (!Character.isJavaIdentifierPart(s.charAt(i))) {
				return false;
			}
		}
		
		return true;
	}
	
	public static boolean isValidType(String s) {
		// TODO: Check the type out (Class.forName(s) => no exception = ok, except for primitives)
		return true;
	}
	
}
