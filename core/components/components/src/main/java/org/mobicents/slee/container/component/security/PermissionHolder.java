/**
 * Start time:12:49:40 2009-04-13<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.security;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.slee.management.DeploymentException;

import org.apache.log4j.Logger;

import sun.security.provider.PolicyParser;
import sun.security.provider.PolicyParser.ParsingException;

/**
 * Start time:12:49:40 2009-04-13<br>
 * Project: mobicents-jainslee-server-core<br>
 * this class holds permission loaded from Slee components (only from slee
 * components) PermissionsLoaded from different location are not represented by
 * this class. Set of this elements is present for each slee component (library,
 * sbb (actually one :), etc)
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 */
public class PermissionHolder implements Comparable<PermissionHolder> {

	private static Logger logger = Logger.getLogger(PermissionHolder.class);
	private URI permissionCodeBaseURI = null;
	// Sun class to parse and augment permissions
	private PolicyParser policyParser = new PolicyParser(true);
	// This is plain policy string
	private String policy = null;

	private Set<PolicyHolderEntry> policyHolderEntry = new HashSet<PolicyHolderEntry>();

	public PermissionHolder(URI permissionCodeBaseURI, String policy) {
		super();
		setPermissionCodeBaseURI(permissionCodeBaseURI);
		setPolicy(policy);

	}

	public PermissionHolder() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;

		result = prime * result + ((policy == null) ? 0 : policy.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PermissionHolder other = (PermissionHolder) obj;
		if (permissionCodeBaseURI == null) {
			if (other.permissionCodeBaseURI != null)
				return false;
		} else if (!permissionCodeBaseURI.equals(other.permissionCodeBaseURI))
			return false;
		if (policy == null) {
			if (other.policy != null)
				return false;
		} else if (!policy.equals(other.policy))
			return false;
		return true;
	}

	public URI getPermissionCodeBaseURI() {
		return permissionCodeBaseURI;
	}

	public void setPermissionCodeBaseURI(URI permissionCodeBaseURI) {
		if (permissionCodeBaseURI == null)
			throw new IllegalArgumentException("URI must not be null");
		this.permissionCodeBaseURI = permissionCodeBaseURI;
	}

	public String getPolicy() {
		return policy;
	}

	public void setPolicy(String policy) {

		if (policy == null)
			throw new IllegalArgumentException("Policy must not be null");

		this.policy = policy;
		try {
			this.policyParser.read(new StringReader(this.policy));
			instrumentCodeBase();
		} catch (IllegalArgumentException e) {
			throw e;
		} catch (Exception e) {
			throw new IllegalArgumentException("Failed to parse.", e);
		}
	}

	public PolicyParser getPolicyParser() {
		return policyParser;
	}

	void addPolicyHolderEntry(PolicyHolderEntry phe) {
		this.policyHolderEntry.add(phe);
	}

	Set<PolicyHolderEntry> getPolicyHolderEntry() {
		return this.policyHolderEntry;

	}

	private void instrumentCodeBase() throws IllegalArgumentException {

		// Here we must instrument code base, so it either points to whole dir,
		// or is

		URI uri = this.permissionCodeBaseURI.normalize();
		Enumeration<PolicyParser.GrantEntry> grantEntries = this.policyParser.grantElements();
		while (grantEntries.hasMoreElements()) {
			PolicyParser.GrantEntry ge = grantEntries.nextElement();
			try {

				if (ge.codeBase == null) {
					ge.codeBase = uri.getPath();

					ge.codeBase = PolicyFile.fileToEncodedURL(new File(ge.codeBase)).toString();

				} else {
					// We have URI here , it must not be absolute

					URI presentCodeBase = new URI("file", "", new URI(ge.codeBase).getPath());
					if (presentCodeBase.isAbsolute()) {
						throw new IllegalArgumentException("Code base is absolute, it must be relative: " + ge.codeBase);
					}

					if (ge.codeBase.contains("..")) {
						throw new IllegalArgumentException("Code contains \"..\", it must not: " + ge.codeBase);
					}
					String p = this.permissionCodeBaseURI.getPath();

				}
			} catch (URISyntaxException e) {
				throw new IllegalArgumentException("Failed to parse code base: " + ge.codeBase, e);
			} catch (MalformedURLException e) {
				throw new IllegalArgumentException("Failed to parse code base: " + ge.codeBase, e);
			}
		}

	}

	public int compareTo(PermissionHolder o) {
		if (o == null)
			return 1;
		if (o == this || o.equals(this))
			return 0;
		// Might not be a best idea.
		return o.permissionCodeBaseURI.toString().compareTo(this.permissionCodeBaseURI.toString());

	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + " uri: " + permissionCodeBaseURI;
	}

	public void addPermissionHolder(PermissionHolder ph, PolicyFile policyFile, boolean b) {
		// TODO Auto-generated method stub

	}

}
