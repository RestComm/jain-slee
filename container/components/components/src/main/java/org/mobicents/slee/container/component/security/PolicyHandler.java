/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

/**
 * Start time:12:56:13 2009-04-08<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.security;


/**
 * Start time:12:56:13 2009-04-08<br>
 * Project: mobicents-jainslee-server-core<br>
 * test impl of policy handling code
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 */
public class PolicyHandler {

	private static final PolicyHandler policyHandler = new PolicyHandler();

	private PolicyHandler() {
	}

	public static PolicyHandler getInstance() {
		return policyHandler;
	}

	/*
	private static final String _FILE_NAME = "local.policy";
	private static final String _POLICY_PROP_URL = "policy.url.y";
	
	private void instrumentCodeBase(File deployURL, PolicyParser pp) throws DeploymentException {

		// Here we must instrument code base, so it either points to whole dir,
		// or is
		URI uri = deployURL.toURI().normalize();
		Enumeration<PolicyParser.GrantEntry> grantEntries = pp.grantElements();
		while (grantEntries.hasMoreElements()) {
			PolicyParser.GrantEntry ge = grantEntries.nextElement();
			if (ge.codeBase == null) {
				ge.codeBase = uri.toString();
			} else {
				// We have URI here , it must not be absolute
				try {
					URI presentCodeBase = new URI(ge.codeBase);
					if (presentCodeBase.isAbsolute()) {
						throw new DeploymentException("Code base is absolute, it must be relative: " + ge.codeBase);
					}

					if (ge.codeBase.contains("..")) {
						throw new DeploymentException("Code contains \"..\", it must not: " + ge.codeBase);
					}
					ge.codeBase = new File(deployURL, presentCodeBase.getPath()).toURI().normalize().toString();
				} catch (URISyntaxException e) {
					throw new DeploymentException("Failed to parse code base: " + ge.codeBase, e);
				}
			}
		}
	}	
	
	public static void main(String[] args) {

		try {
			URI u = new URI("../asd/asd/zxcv/artyw");
			System.err.println(u.getPath());
			u.toURL();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	*/

}