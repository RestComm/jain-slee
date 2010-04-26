/**
 * 
 */
package org.mobicents.slee.container.component.profile.query;

/**
 * @author martins
 * 
 */
public enum QueryExpressionType {

	Compare(1), LongestPrefixMatch(2), HasPrefix(3), RangeMatch(4), And(5), Or(
			6), Not(7);

	private int code = -1;

	QueryExpressionType(int val) {
		this.code = val;
	}

	public int getCode() {
		return code;
	}

}
