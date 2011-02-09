/**
 * 
 */
package org.mobicents.slee.container.event;

/**
 * Abstract event context class, that only puts itself on the datasource if the handle is requested, i.e., is stored in a cmp 
 * @author martins
 *
 */
public abstract class LazyStoredEventContext implements EventContext {

	protected final EventContextFactoryImpl factory;
	
	private EventContextHandle handle;
	
	/**
	 * 
	 */
	public LazyStoredEventContext(EventContextFactoryImpl factory) {
		this.factory = factory;
	}
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.container.event.EventContext#getEventContextHandle()
	 */
	public EventContextHandle getEventContextHandle() {
		if (handle == null) {
			handle = new EventContextHandleImpl(factory.getSleeContainer().getUuidGenerator().createUUID());
			factory.getDataSource().addEventContext(handle, this);
		}
		return handle;
	}
	
	/**
	 * 
	 */
	public void remove() {
		if (handle != null) {
			factory.getDataSource().removeEventContext(handle);		
			handle = null;
		}
	}
}
