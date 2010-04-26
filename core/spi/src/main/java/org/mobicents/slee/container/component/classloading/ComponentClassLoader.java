package org.mobicents.slee.container.component.classloading;

/**
 * The SLEE component class loader implementation.
 * 
 * A component needs to have it's own class loader due to unique JNDI context
 * but in reality it just delegates to the related component jar class loader.
 * 
 * @author martins
 * 
 */
public abstract class ComponentClassLoader extends ClassLoader {

	/**
	 * 
	 * @param parent
	 */
	protected ComponentClassLoader(ClassLoader parent) {
		super(parent);
	}
	
	/**
	 * Loads a class locally, i.e., from managed URLs or URLs managed by dependencies.
	 * @param name
	 * @return
	 * @throws ClassNotFoundException
	 */
	public abstract Class<?> loadClassLocally(String name) throws ClassNotFoundException;
		
}
