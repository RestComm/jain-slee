/**
 * Start time:11:59:13 2009-01-29<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.query;

import org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.RangeMatch;

/**
 * Start time:11:59:13 2009-01-29<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MRangeMatch {

	private RangeMatch rangeMatch = null;

	protected String attributeName = null;

	// optional
	protected String fromValue = null;
	protected String fromParameter = null;
	protected String toValue = null;
	protected String toParameter = null;
	protected String collatorRef = null;

	public MRangeMatch(RangeMatch rangeMatch) {
		super();
		this.rangeMatch = rangeMatch;
		this.attributeName=this.rangeMatch.getAttributeName();
		this.fromValue=this.rangeMatch.getFromValue();
		this.fromParameter=this.rangeMatch.getFromParameter();
		this.toValue=this.rangeMatch.getToValue();
		this.toParameter=this.rangeMatch.getToParameter();
		this.collatorRef=this.rangeMatch.getCollatorRef();
	}

	public String getAttributeName() {
		return attributeName;
	}

	public String getFromValue() {
		return fromValue;
	}

	public String getFromParameter() {
		return fromParameter;
	}

	public String getToValue() {
		return toValue;
	}

	public String getToParameter() {
		return toParameter;
	}

	public String getCollatorRef() {
		return collatorRef;
	}

}
