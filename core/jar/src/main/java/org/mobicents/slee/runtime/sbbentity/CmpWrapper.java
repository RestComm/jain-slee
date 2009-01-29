package org.mobicents.slee.runtime.sbbentity;

import java.io.Serializable;

/**
 * Storage wrapper for a cmp field.
 * @author martins
 *
 */
public class CmpWrapper implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final String name;
	private final CmpType type;
	private Object value;
	
	public CmpWrapper(String name, CmpType type,Object value) {		
		this.name = name;
		this.type = type;
		this.value = value;
	}
	
	public String getName() {
		return name;
	}
	
	public CmpType getType() {
		return type;
	}
	
	public Object getValue() {
		return value;
	}
	
	public void setValue(Object value) {
		this.value = value;
	}
	
	@Override
	public int hashCode() {		
		return name.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj.getClass() == this.getClass()) {
			return ((CmpWrapper)obj).name.equals(this.name);
		}
		else {
			return false;
		}
	}
}
