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
 * Start time:12:49:40 2009-04-13<br>
 * Project: restcomm-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.security;

import java.io.File;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import org.mobicents.slee.container.component.security.PermissionHolder;

import sun.security.provider.PolicyParser;

/**
 * Start time:12:49:40 2009-04-13<br>
 * Project: restcomm-jainslee-server-core<br>
 * this class holds permission loaded from Slee components (only from slee
 * components) PermissionsLoaded from different location are not represented by
 * this class. Set of this elements is present for each slee component (library,
 * sbb (actually one :), etc)
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 */
public class PermissionHolderImpl implements Comparable<PermissionHolderImpl>, PermissionHolder {

	private URI permissionCodeBaseURI = null;
	// Sun class to parse and augment permissions
	private PolicyParser policyParser = new PolicyParser(true);
	// This is plain policy string
	private String policy = null;

	private Set<PolicyHolderEntry> policyHolderEntry = new HashSet<PolicyHolderEntry>();

	public PermissionHolderImpl(URI permissionCodeBaseURI, String policy) {
		super();
		setPermissionCodeBaseURI(permissionCodeBaseURI);
		setPolicy(policy);

	}

	public PermissionHolderImpl() {
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
		PermissionHolderImpl other = (PermissionHolderImpl) obj;
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

					ge.codeBase = PolicyFileImpl.fileToEncodedURL(new File(ge.codeBase)).toString();

				} else {
					// We have URI here , it must not be absolute

					URI presentCodeBase = new URI("file", "", new URI(ge.codeBase).getPath());
					if (presentCodeBase.isAbsolute()) {
						throw new IllegalArgumentException("Code base is absolute, it must be relative: " + ge.codeBase);
					}

					if (ge.codeBase.contains("..")) {
						throw new IllegalArgumentException("Code contains \"..\", it must not: " + ge.codeBase);
					}

				}
			} catch (URISyntaxException e) {
				throw new IllegalArgumentException("Failed to parse code base: " + ge.codeBase, e);
			} catch (MalformedURLException e) {
				throw new IllegalArgumentException("Failed to parse code base: " + ge.codeBase, e);
			}
		}

	}

	public int compareTo(PermissionHolderImpl o) {
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

	public void addPermissionHolder(PermissionHolderImpl ph, PolicyFileImpl policyFile, boolean b) {
		// TODO Auto-generated method stub

	}

}