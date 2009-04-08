package org.mobicents.slee.container.component;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;

import javax.slee.ComponentID;

/**
 * The SLEE component classloader implementation.
 * 
 * A component needs to have it's own class loader due to unique JNDI context
 * but in reality it just delegates to the related component jar class loader.
 * 
 * @author martins
 * 
 */
public class ComponentClassLoader extends ClassLoader {

	/**
	 * the real component class loader, the one from its component jar
	 */
	private final ClassLoader componentJarClassLoader;

	/**
	 * the component id, used to make this class loader unique
	 */
	private final ComponentID componentID;

	/**
	 * 
	 * @param componentJarClassLoader
	 * @param componentID
	 */
	public ComponentClassLoader(ClassLoader componentJarClassLoader,
			ComponentID componentID) {
		this.componentJarClassLoader = componentJarClassLoader;
		this.componentID = componentID;
	}

	@Override
	public void clearAssertionStatus() {
		componentJarClassLoader.clearAssertionStatus();
	}

	@Override
	public URL getResource(String name) {
		return componentJarClassLoader.getResource(name);
	}

	@Override
	public InputStream getResourceAsStream(String name) {
		return componentJarClassLoader.getResourceAsStream(name);
	}

	@Override
	public Enumeration<URL> getResources(String name) throws IOException {
		return componentJarClassLoader.getResources(name);
	}

	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		return componentJarClassLoader.loadClass(name);
	}

	@Override
	public void setClassAssertionStatus(String className, boolean enabled) {
		componentJarClassLoader.setClassAssertionStatus(className, enabled);
	}

	@Override
	public void setDefaultAssertionStatus(boolean enabled) {
		componentJarClassLoader.setDefaultAssertionStatus(enabled);
	}

	@Override
	public void setPackageAssertionStatus(String packageName, boolean enabled) {
		componentJarClassLoader.setPackageAssertionStatus(packageName, enabled);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() == this.getClass()) {
			return componentID.equals(((ComponentClassLoader) obj).componentID);
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return componentID.hashCode();
	}

	@Override
	public String toString() {
		return "ComponentClassLoader[ componentID = " + componentID
				+ " , componentJarClassLoader = " + componentJarClassLoader
				+ " ]";
	}

}
