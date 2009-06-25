package org.mobicents.slee.container.component.deployment.classloading;

import javax.slee.ComponentID;

/**
 * The SLEE component class loader implementation.
 * 
 * A component needs to have it's own class loader due to unique JNDI context
 * but in reality it just delegates to the related component jar class loader.
 * 
 * @author martins
 * 
 */
public class ComponentClassLoader extends ClassLoader {

	/**
	 * the component id, used to make this class loader unique
	 */
	private final ComponentID componentID;
	
	/**
	 * the class loader pointing to component jar url related with the component
	 */
	private final URLClassLoaderDomain parent;
	
	/**
	 * 
	 * @param componentID
	 * @param parent
	 */
	public ComponentClassLoader(ComponentID componentID, URLClassLoaderDomain parent) {
		super(parent);
		this.parent = parent;		
		this.componentID = componentID;
	}

	/**
	 * Loads a class locally, i.e., from managed URLs or URLs managed by dependencies.
	 * @param name
	 * @return
	 * @throws ClassNotFoundException
	 */
	public Class<?> loadClassLocally(String name) throws ClassNotFoundException {
		return parent.loadClassLocally(name);
	}
		
	@Override
	public String toString() {
		return "ComponentClassLoader[ componentID = " + componentID	+ " ]";
	}

}
