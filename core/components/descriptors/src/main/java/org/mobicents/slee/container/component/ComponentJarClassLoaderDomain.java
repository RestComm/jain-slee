package org.mobicents.slee.container.component;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.slee.SLEEException;

import org.jboss.classloader.spi.ClassLoaderDomain;
import org.jboss.classloader.spi.ClassLoaderPolicy;
import org.jboss.classloader.spi.ClassLoaderSystem;
import org.jboss.classloader.spi.Loader;
import org.jboss.classloader.spi.ParentPolicy;
import org.jboss.classloading.spi.metadata.ExportAll;
import org.jboss.classloading.spi.vfs.policy.VFSClassLoaderPolicy;
import org.jboss.virtual.VFS;
import org.jboss.virtual.VirtualFile;

/**
 * The class loader domain for a component jar. Extends
 * {@link ClassLoaderDomain} to provide multiple parents (the real parent + each
 * dependency domain for each component in the jar).
 * 
 * @author Eduardo Martins
 * 
 */
public class ComponentJarClassLoaderDomain extends ClassLoaderDomain {

	/**
	 * the set of domains from referenced components
	 */
	private final Set<ComponentJarClassLoaderDomain> refs = new HashSet<ComponentJarClassLoaderDomain>();

	/**
	 * cache of packages coming from the parent or a reference domain
	 */
	private final ConcurrentHashMap<String, Package> packageCache = new ConcurrentHashMap<String, Package>();

	/**
	 * cache of loaders coming from the parent or a reference domain
	 */
	private final ConcurrentHashMap<String, Loader> loaderCache = new ConcurrentHashMap<String, Loader>();

	/**
	 * cache of classes coming from the parent or a reference domain
	 */
	private final ConcurrentHashMap<String, Class<?>> classCache = new ConcurrentHashMap<String, Class<?>>();

	/**
	 * the class loader bound to this domain
	 */
	private ClassLoader classLoader;

	/**
	 * Creates a new instance with the specified name.
	 * 
	 * @param name
	 */
	public ComponentJarClassLoaderDomain(String name) {
		super(name);
	}

	/**
	 * Adds the specified domain this domain depends
	 * 
	 * @param domain
	 */
	public void addDependencyDomain(ComponentJarClassLoaderDomain domain) {
		refs.add(domain);
	}

	/**
	 * Retrieves the class loader bound to this domain
	 * 
	 * @return
	 */
	public ClassLoader getClassLoader() {
		return classLoader;
	}

	/**
	 * Registers the domain in the system
	 */
	public void register() {
		ClassLoaderSystem classLoaderSystem = ClassLoaderSystem.getInstance();
		super.setParent(classLoaderSystem.getDefaultDomain());
		super.setParentPolicy(ParentPolicy.AFTER);
		setUseLoadClassForParent(true);
		classLoaderSystem.registerDomain(this);
		classLoader = classLoaderSystem.registerClassLoaderPolicy(this,
				createClassLoaderPolicy());
	}

	/**
	 * Unregisters the domain from the system
	 */
	public void unregister() {
		ClassLoaderSystem classLoaderSystem = ClassLoaderSystem.getInstance();
		classLoaderSystem.unregisterClassLoader(classLoader);
		classLoaderSystem.unregisterDomain(this);
		packageCache.clear();
		loaderCache.clear();
		classCache.clear();
	}

	/**
	 * creates a {@link ClassLoaderPolicy} pointing to the directory of this
	 * domain name
	 * 
	 * @return
	 */
	private ClassLoaderPolicy createClassLoaderPolicy() {
		// create class loading policy pointing to the dir
		VirtualFile tempClassDeploymentDirVF = null;
		try {
			tempClassDeploymentDirVF = VFS.getRoot(new File(getName()).toURL());
		} catch (Exception e) {
			throw new SLEEException(e.getMessage(), e);
		}
		VFSClassLoaderPolicy classLoaderPolicy = VFSClassLoaderPolicy
				.createVFSClassLoaderPolicy(tempClassDeploymentDirVF);
		classLoaderPolicy.setImportAll(true); // see other classes in the
		// domain
		classLoaderPolicy.setBlackListable(false);
		classLoaderPolicy.setExportAll(ExportAll.NON_EMPTY); // others will
		// see this
		// classes
		classLoaderPolicy.setCacheable(true);
		return classLoaderPolicy;
	}

	// ---- hacking the underlying ClassLoaderDomain

	@Override
	protected Package afterGetPackage(String name) {
		// look in cache
		Package result = packageCache.get(name);
		if (result != null) {
			if (isUseLoadClassForParent()) {
				// look in parent
				result = super.afterGetPackage(name);
				if (result == null) {
					// try to find it in a ref domain
					for (ComponentJarClassLoaderDomain ref : refs) {
						// the process to lookup in a ref domain needs to sync
						// the object and disable the lookup in parent, to avoid
						// loops
						synchronized (ref) {
							ref.setUseLoadClassForParent(false);
							result = ref.getPackage(name);
							ref.setUseLoadClassForParent(true);
						}
						if (result != null) {
							break;
						}
					}
				}
				// store in cache
				packageCache.put(name, result);
			}
		}
		return result;
	}

	@Override
	protected void afterGetPackages(Set<Package> packages) {
		if (isUseLoadClassForParent()) {
			super.afterGetPackages(packages);
			for (ComponentJarClassLoaderDomain ref : refs) {
				synchronized (ref) {
					ref.setUseLoadClassForParent(false);
					ref.getPackages(packages);
					ref.setUseLoadClassForParent(true);
				}
			}
		}
	}

	@Override
	protected Loader findAfterLoader(String name) {
		// look in cache
		Loader result = loaderCache.get(name);
		if (isUseLoadClassForParent()) {
			// look in parent
			result = super.findAfterLoader(name);
			if (result == null) {
				// try to find it in a ref domain
				for (ComponentJarClassLoaderDomain ref : refs) {
					// the process to lookup in a ref domain needs to sync the
					// object and disable the lookup in parent, to avoid loops
					synchronized (ref) {
						ref.setUseLoadClassForParent(false);
						result = ref.findLoader(name);
						ref.setUseLoadClassForParent(true);
					}
					if (result != null) {
						break;
					}
				}
			}
			// put in cache
			loaderCache.put(name, result);
		}
		return result;
	}

	@Override
	protected Class<?> loadClassAfter(String name) {
		// look in cache
		Class<?> result = classCache.get(name);
		if (isUseLoadClassForParent()) {
			// look in parent
			result = super.loadClassAfter(name);
			if (result == null) {
				// try to find it in a ref domain
				for (ComponentJarClassLoaderDomain ref : refs) {
					synchronized (ref) {
						// the process to lookup in a ref domain needs to sync
						// the object and disable the lookup in parent, to avoid
						// loops
						ref.setUseLoadClassForParent(false);
						result = ref.loadClass(name);
						ref.setUseLoadClassForParent(true);
					}
					if (result != null) {
						break;
					}
				}
			}
		}
		return result;
	}

	// BEFORE methods all empty, to optimize performance

	@Override
	protected Package beforeGetPackage(String name) {
		return null;
	}

	@Override
	protected void beforeGetPackages(Set<Package> packages) {
	}

	@Override
	protected URL beforeGetResource(String name) {
		return null;
	}

	@Override
	protected void beforeGetResources(String name, Set<URL> urls)
			throws IOException {
	}

	@Override
	protected Loader findBeforeLoader(String name) {
		return null;
	}

	@Override
	protected Class<?> loadClassBefore(String name) {
		return null;
	}

	@Override
	public void setParentPolicy(ParentPolicy parentPolicy) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setParent(Loader parent) {
		throw new UnsupportedOperationException();
	}

}
