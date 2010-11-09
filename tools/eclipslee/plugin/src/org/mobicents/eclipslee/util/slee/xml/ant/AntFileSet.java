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

package org.mobicents.eclipslee.util.slee.xml.ant;

import java.util.StringTokenizer;

import org.mobicents.eclipslee.util.slee.xml.XML;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * @author cath
 */
public class AntFileSet extends XML {

	protected AntFileSet(Document document, Element root) {		
		super(document, root);
	}
	
	public void setDir(String dir) {
		root.setAttribute("dir", dir);
	}
	
	public String getDir() {
		return root.getAttribute("dir");
	}
	
	public void setIncludes(String [] includes) {
		String tmp = "";
		
		for (int i = 0; i < includes.length; i++) {
			if (i > 0)
				tmp += ",";
			tmp += includes[i];
		}
		
		root.setAttribute("includes", tmp);
	}

	public String[] getIncludes() {
		String includes = root.getAttribute("includes");
		if (includes == null)
			return new String[0];
		
		StringTokenizer tok = new StringTokenizer(includes, ",");
		String output[] = new String[tok.countTokens()];
		for (int i = 0; i < output.length; i++)
			output[i] = tok.nextToken();
		
		return output;
	}
	
	public void addInclude(String include) {
		String oldIncs[] = getIncludes();
		String newIncs[] = new String[oldIncs.length + 1];
		
		System.arraycopy(oldIncs, 0, newIncs, 0, oldIncs.length);
		newIncs[oldIncs.length] = include;
		setIncludes(newIncs);		
	}
}
