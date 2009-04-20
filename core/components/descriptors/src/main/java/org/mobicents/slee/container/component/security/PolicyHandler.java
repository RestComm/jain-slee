/**
 * Start time:12:56:13 2009-04-08<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.security;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.Policy;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import javax.slee.management.DeploymentException;

import sun.security.provider.PolicyParser;

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

	private static final String _FILE_NAME = "local.policy";
	private static final String _POLICY_PROP_URL = "policy.url.y";
	
	private List<URI> presentPolicyFiles = new ArrayList<URI>();

	public synchronized void addPolicy(File deployURL, String policyFileContent) throws DeploymentException {

		File policyFile = new File(deployURL, _FILE_NAME);
		if (presentPolicyFiles.contains(policyFile.toURI().normalize()) && policyFile.exists()) {
			// We already have this, why?
			System.err.println("Double policy entry: " + policyFile);
			return;
		}

		try {
			if (!policyFile.createNewFile()) {
				throw new DeploymentException("Failed to create file: " + policyFile);
			}
		} catch (IOException e1) {
			throw new DeploymentException("Failed to create file: " + policyFile,e1);
		}

		PolicyParser pp = new PolicyParser(true);
		try {
			pp.read(new StringReader(policyFileContent));
		} catch (Exception e) {
			throw new DeploymentException("Failed to parse passed security permissions", e);
		}

		instrumentCodeBase(deployURL, pp);
		instrumenPolicyObjectWithSLEE(pp, policyFile);
	}

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

	private static int counter =1;
	
	private void instrumenPolicyObjectWithSLEE(PolicyParser pp, File policyFile) throws DeploymentException {
		// Well here we have PolicyParser with some data, we need to write it to
		// a file and

		FileWriter fw;
		try {
			fw = new FileWriter(policyFile);
			
		} catch (IOException e) {
			throw new DeploymentException("Failed to write file.", e);
		}
		System.err.println("------------- Writing policy file. ------------------");
		pp.write(fw);
		pp.write(new OutputStreamWriter(System.err));
		
		System.err.println("------------- Refresh ------------------");
		Properties p = System.getProperties();
		p.setProperty(_POLICY_PROP_URL+(counter++), policyFile.toURI().normalize().toString());
		try{
			Policy.getPolicy().refresh();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		System.err.println("------------- Refreshed ------------------");

	}

	public static void main(String[] args) {

		try {
			URI u = new URI("../asd/asd/zxcv/artyw");
			System.err.println(u.getPath());
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
