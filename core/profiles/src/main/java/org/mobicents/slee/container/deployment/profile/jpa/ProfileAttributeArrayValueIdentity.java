package org.mobicents.slee.container.deployment.profile.jpa;

public class ProfileAttributeArrayValueIdentity {

	private final String string;
	private final Object serializable;
	
	public ProfileAttributeArrayValueIdentity(String string, Object serializable) {
		this.string = string;
		this.serializable = serializable;
	}

	public ProfileAttributeArrayValueIdentity(ProfileEntityArrayAttributeValue value) {
		this.string = value.string;
		this.serializable = value.serializable;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((serializable == null) ? 0 : serializable.hashCode());
		result = prime * result
				+ ((string == null) ? 0 : string.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj.getClass() == this.getClass()) {
			ProfileAttributeArrayValueIdentity other = (ProfileAttributeArrayValueIdentity) obj;
			if (serializable == null) {
				if (other.serializable != null)
					return false;
			} else if (!serializable.equals(other.serializable))
				return false;
			if (string == null) {
				if (other.string != null)
					return false;
			} else if (!string.equals(other.string))
				return false;
			return true;
		}
		else {
			return false;
		}		
	}

	
	
}
