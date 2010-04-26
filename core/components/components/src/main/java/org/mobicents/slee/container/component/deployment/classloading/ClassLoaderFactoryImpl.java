/**
 * 
 */
package org.mobicents.slee.container.component.deployment.classloading;

import java.net.URL;

import javax.slee.ComponentID;

import org.mobicents.slee.container.component.classloading.ClassLoaderFactory;
import org.mobicents.slee.container.component.classloading.ComponentClassLoader;
import org.mobicents.slee.container.component.classloading.ReplicationClassLoader;
import org.mobicents.slee.container.component.classloading.URLClassLoaderDomain;

/**
 * @author martins
 * 
 */
public class ClassLoaderFactoryImpl implements ClassLoaderFactory {

	private final ClassLoadingConfiguration configuration;
	
	/**
	 * 
	 */
	public ClassLoaderFactoryImpl(ClassLoadingConfiguration configuration) {
		this.configuration = configuration;
	}
	
	/**
	 * @return the configuration
	 */
	public ClassLoadingConfiguration getConfiguration() {
		return configuration;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.slee.core.component.classloading.ClassLoaderFactory#
	 * newClassLoaderDomain(java.net.URL[], java.lang.ClassLoader)
	 */
	public URLClassLoaderDomain newClassLoaderDomain(URL[] urls,
			ClassLoader sleeClassLoader) {
		return new URLClassLoaderDomainImpl(urls, sleeClassLoader,
				configuration.isLoadClassesFirstFromAS());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.slee.core.component.classloading.ClassLoaderFactory#
	 * newComponentClassLoader(javax.slee.ComponentID,
	 * org.mobicents.slee.core.component.classloading.URLClassLoaderDomain)
	 */
	public ComponentClassLoader newComponentClassLoader(
			ComponentID componentID, URLClassLoaderDomain parent) {
		return new ComponentClassLoaderImpl(componentID, parent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.slee.core.component.classloading.ClassLoaderFactory#
	 * newReplicationClassLoader(java.lang.ClassLoader)
	 */
	public ReplicationClassLoader newReplicationClassLoader(
			ClassLoader sleeClassLoader) {
		return new ReplicationClassLoaderImpl(sleeClassLoader,
				configuration.isLoadClassesFirstFromAS());
	}

}
