package org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.query;

/**
 * Start time:11:42:20 2009-01-29<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MLongestPrefixMatch {

	private org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.LongestPrefixMatch longestPrefixMatch11;

	private String attributeName;
	private String value;
	private String collatorRef;
	private String parameter;

  public MLongestPrefixMatch(org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.LongestPrefixMatch longestPrefixMatch11)
  {
    this.longestPrefixMatch11 = longestPrefixMatch11;
    
    this.attributeName = longestPrefixMatch11.getAttributeName();
    this.value = longestPrefixMatch11.getValue();
    this.parameter = longestPrefixMatch11.getParameter();
    this.collatorRef = longestPrefixMatch11.getCollatorRef();
  }

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

}
