package org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.query;

public enum MQueryExpressionType {

	Compare(1), LongestPrefixMatch(2), HasPrefix(3), RangeMatch(4), And(5),Or(6),Not(7);
	
	private int code=-1;
	MQueryExpressionType(int val)
	{
		this.code=val;
		
		
	}
	public int getCode() {
		return code;
	}
	
	
}
