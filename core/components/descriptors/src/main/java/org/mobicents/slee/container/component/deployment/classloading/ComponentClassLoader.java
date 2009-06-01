package org.mobicents.slee.container.component.deployment.classloading;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;

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
	private final URLClassLoaderDomain parent;
	
	public ComponentClassLoader(ComponentID componentID, URLClassLoaderDomain parent) {
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
	public Class<?> loadClass(String name) throws ClassNotFoundException {		
		return parent.loadClass(name);
	}
	
	@Override
	public URL getResource(String name) {
		return parent.getResource(name);
	}
	
	@Override
	public InputStream getResourceAsStream(String name) {
		return parent.getResourceAsStream(name);
	}
	
	@Override
	public Enumeration<URL> getResources(String name) throws IOException {
		return parent.getResources(name);
	}
	
	@Override
	public String toString() {
		return "ComponentClassLoader[ componentID = " + componentID	+ " ]";
	}

}
