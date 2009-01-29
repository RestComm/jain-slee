/**
 * Start time:11:42:20 2009-01-29<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.query;

import org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.LongestPrefixMatch;

/**
 * Start time:11:42:20 2009-01-29<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MLongestPrefixMatch {

	private LongestPrefixMatch longestPrefixMatch = null;

	protected  String attributeName = null;

	// optional
	protected  String value = null;
	protected String collatorRef = null;
	protected String parameter = null;

	public String getAttributeName() {
		return attributeName;
	}

	public String getParameter() {
		return parameter;
	}

	public String getValue() {
		return value;
	}

	public String getCollatorRef() {
		return collatorRef;
	}

	public MLongestPrefixMatch(LongestPrefixMatch longestPrefixMatch) {
		super();
		this.longestPrefixMatch = longestPrefixMatch;
		this.attributeName = this.longestPrefixMatch.getAttributeName();
		this.value = this.longestPrefixMatch.getValue();
		this.parameter = this.longestPrefixMatch.getParameter();
		this.collatorRef = this.longestPrefixMatch.getCollatorRef();
	}

	//This is only for extending - hasPrefix for instance
	protected MLongestPrefixMatch() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	
}
