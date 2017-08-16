/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2017, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

/**
 * Start time:12:56:13 2009-04-08<br>
 * Project: restcomm-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.security;


/**
 * Start time:12:56:13 2009-04-08<br>
 * Project: restcomm-jainslee-server-core<br>
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