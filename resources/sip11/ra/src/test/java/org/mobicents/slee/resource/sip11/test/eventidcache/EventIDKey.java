package org.mobicents.slee.resource.sip11.test.eventidcache;

public class EventIDKey {

	final private String eventName;
	final private String eventVendor;
	final private String eventVersion;
	
	public EventIDKey(String eventName, String eventVendor,
			String eventVersion) {
		super();
		this.eventName = eventName;
		this.eventVendor = eventVendor;
		this.eventVersion = eventVersion;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((eventName == null) ? 0 : eventName.hashCode());
		result = prime * result
				+ ((eventVendor == null) ? 0 : eventVendor.hashCode());
		result = prime * result
				+ ((eventVersion == null) ? 0 : eventVersion.hashCode());
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
		final EventIDKey other = (EventIDKey) obj;
		if (eventName == null) {
			if (other.eventName != null)
				return false;
		} else if (!eventName.equals(other.eventName))
			return false;
		if (eventVendor == null) {
			if (other.eventVendor != null)
				return false;
		} else if (!eventVendor.equals(other.eventVendor))
			return false;
		if (eventVersion == null) {
			if (other.eventVersion != null)
				return false;
		} else if (!eventVersion.equals(other.eventVersion))
			return false;
		return true;
	}
	
}
