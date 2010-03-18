package org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.query;

/**
 * Start time:11:59:13 2009-01-29<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MRangeMatch {

	private org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.RangeMatch rangeMatch11;

	private String attributeName;

	private String fromValue;
	private String fromParameter;
	
	private String toValue;
	private String toParameter;
	
	private String collatorRef;

	public MRangeMatch(org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.RangeMatch rangeMatch11)
	{
		this.rangeMatch11 = rangeMatch11;
		
		this.attributeName = rangeMatch11.getAttributeName();
		
		this.fromValue = rangeMatch11.getFromValue();
		this.fromParameter = rangeMatch11.getFromParameter();
		
		this.toValue = rangeMatch11.getToValue();
		this.toParameter = rangeMatch11.getToParameter();
		
		this.collatorRef = rangeMatch11.getCollatorRef();
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
