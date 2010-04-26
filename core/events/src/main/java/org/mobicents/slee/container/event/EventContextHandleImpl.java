/**
 * 
 */
package org.mobicents.slee.container.event;

/**
 * @author martins
 *
 */
public class EventContextHandleImpl implements EventContextHandle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final String id;

	/**
	 * @param id
	 */
	public EventContextHandleImpl(String id) {
		this.id = id;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return id.hashCode();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EventContextHandleImpl other = (EventContextHandleImpl) obj;
		return this.id.equals(other.id);
	}
	
	
}
