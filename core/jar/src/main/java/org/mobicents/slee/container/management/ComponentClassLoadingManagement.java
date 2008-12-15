package org.mobicents.slee.container.management;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

import javax.slee.ComponentID;
import javax.slee.management.DeploymentException;

import org.apache.log4j.Logger;
import org.jboss.classloader.spi.ClassLoaderPolicy;
import org.jboss.classloader.spi.ClassLoaderSystem;
import org.jboss.classloader.spi.ParentPolicy;
import org.jboss.classloading.spi.metadata.ExportAll;
import org.jboss.classloading.spi.vfs.policy.VFSClassLoaderPolicy;
import org.jboss.virtual.VFS;
import org.jboss.virtual.VirtualFile;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.runtime.transaction.TransactionalAction;

/**
 * Manages component class loading
 * 
 * @author martins
 * 
 */
public class ComponentClassLoadingManagement {

	private static final Logger logger = Logger
			.getLogger(ComponentClassLoadingManagement.class);

	/**
	 * info about a component class loading
	 * 
	 * @author martins
	 * 
	 */
	private class ClassLoadingInfo {

		private final ComponentID componentID;
		private ClassLoader classLoader;
		private final ClassLoaderPolicy classLoaderPolicy;

		public ClassLoadingInfo(ComponentID componentID,
				ClassLoaderPolicy classLoaderPolicy) {
			this.componentID = componentID;
			this.classLoaderPolicy = classLoaderPolicy;
		}

		@Override
		public int hashCode() {
			return componentID.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			if (obj != null && obj.getClass() == getClass()) {
				return ((ClassLoadingInfo) obj).componentID.equals(this.componentID);
			} else {
				return false;
			}
		}
	}

	/**
	 * stores class loading info for all components
	 */
	private final ConcurrentHashMap<ComponentID, ClassLoadingInfo> classLoadingInfoMap = new ConcurrentHashMap<ComponentID, ClassLoadingInfo>();

	public static final ComponentClassLoadingManagement INSTANCE = new ComponentClassLoadingManagement();

	private ComponentClassLoadingManagement() {
	}

	public void createClassLoader(final ComponentID componentID,
			URL url) throws MalformedURLException, IOException,
			DeploymentException {

		// create a class loader for this component ponting to the specified url
		VirtualFile virtualFile = VFS.getRoot(url);
		
		final VFSClassLoaderPolicy classLoaderPolicy = VFSClassLoaderPolicy
				.createVFSClassLoaderPolicy(virtualFile.toString() + ":"
						+ componentID.toString(), virtualFile);
		classLoaderPolicy.setImportAll(true); // if you want to see other
												// classes in the domain
		classLoaderPolicy.setExportAll(ExportAll.NON_EMPTY); // if you want
																// others to see
																// your classes
		classLoaderPolicy.setBlackListable(false);	
    	if (logger.isDebugEnabled()) {
    		logger.debug("Component "+componentID+" classLoader exported packages: "+Arrays.asList(classLoaderPolicy.getExportedPackages()));
    	}
		final ClassLoaderSystem classLoaderSystem = ClassLoaderSystem
				.getInstance();
		ClassLoadingInfo classLoadingInfo = new ClassLoadingInfo(componentID,
				classLoaderPolicy);
		if (classLoadingInfoMap.putIfAbsent(componentID, classLoadingInfo) == null) {
			classLoadingInfo.classLoader = classLoaderSystem
					.registerClassLoaderPolicy(classLoaderSystem.DEFAULT_DOMAIN_NAME,ParentPolicy.AFTER,classLoaderPolicy);
			if (logger.isDebugEnabled()) {
				logger.debug("Registred class loading policy for component "+componentID);
			}
			TransactionalAction action = new TransactionalAction() {
				public void execute() {
					classLoadingInfoMap.remove(componentID);
					classLoaderSystem
							.unregisterClassLoaderPolicy(classLoaderPolicy);
					if (logger.isDebugEnabled()) {
						logger.debug("Due to tx rollback, unregistred class loading policy for component "+componentID);
					}
				}
			};
			SleeContainer.getTransactionManager()
					.addAfterRollbackAction(action);
		} else {
			throw new DeploymentException(
					"Error registring class loader for component " + componentID
							+ "! Already registred");
		}

	}

	public ClassLoader getClassLoader(ComponentID componentID) {
		ClassLoadingInfo cli = classLoadingInfoMap.get(componentID);
		if (cli != null) {
			return cli.classLoader;
		} else {
			return null;
		}
	}

	public void removeClassLoader(final ComponentID componentID) {
		final ClassLoadingInfo classLoadingInfo = classLoadingInfoMap
				.get(componentID);
		final ClassLoaderSystem classLoaderSystem = ClassLoaderSystem
				.getInstance();
		classLoadingInfoMap.remove(componentID);
		classLoaderSystem
				.unregisterClassLoaderPolicy(classLoadingInfo.classLoaderPolicy);
		if (logger.isDebugEnabled()) {
			logger.debug("Unregistred class loading policy for component "+componentID);
		}
        
		TransactionalAction action = new TransactionalAction() {
			public void execute() {
				if (classLoadingInfoMap.putIfAbsent(componentID, classLoadingInfo) == null) {
					classLoadingInfo.classLoader = classLoaderSystem
							.registerClassLoaderPolicy(classLoaderSystem.DEFAULT_DOMAIN_NAME,ParentPolicy.AFTER,
									classLoadingInfo.classLoaderPolicy);
					if (logger.isDebugEnabled()) {
						logger.debug("Due to tx rollback, re-registred class loading policy for component "+componentID);
					};
				} else {
					logger
							.error("Failed to restore class loader for component "
									+ componentID
									+ " due to tx rollback. Already registred!");
				}
			}
		};
		SleeContainer.getTransactionManager().addAfterRollbackAction(action);
	}

	@Override
	public String toString() {
		return "Component Class Loading Management: " + "\n+-- Component Class Loaders: "
				+ classLoadingInfoMap.keySet();
	}
}
