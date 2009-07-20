package org.mobicents.slee.container.component;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import javassist.LoaderClassPath;

import javax.slee.ComponentID;
import javax.slee.management.AlreadyDeployedException;
import javax.slee.management.ComponentDescriptor;
import javax.slee.management.DependencyException;
import javax.slee.management.DeploymentException;

import org.mobicents.slee.container.component.deployment.ClassPool;
import org.mobicents.slee.container.component.deployment.DeployableUnit;
import org.mobicents.slee.container.component.deployment.classloading.ComponentClassLoader;
import org.mobicents.slee.container.component.deployment.classloading.URLClassLoaderDomain;
import org.mobicents.slee.container.component.security.PermissionHolder;

/**
 * Base class for a SLEE component, providing features related with class
 * loading, deployable unit and other component references
 * 
 * @author martins
 * 
 */
public abstract class SleeComponent {
	
	/**
	 * the component class loader
	 */
	private ComponentClassLoader classLoader;

	/**
	 * the class loader domain for the component jar this component belongs,
	 * components that depend on this component must add this in its domain
	 */
	private URLClassLoaderDomain classLoaderDomain;

	/**
	 * the javassist class pool
	 */
	private ClassPool classPool;

	/**
	 * the DU the component belongs
	 */
	private DeployableUnit deployableUnit;

	/**
	 * where this component is deployed
	 */
	private File deploymentDir;

	/**
	 * the source for this component (component jar/service descriptor) in the
	 * deployable unit
	 */
	private String deploymentUnitSource;

	
	protected Set<PermissionHolder> permissions = new TreeSet<PermissionHolder>();
	
	/**
	 * Retrieves the component class loader
	 * 
	 * @return
	 */
	public ComponentClassLoader getClassLoader() {
		return classLoader;
	}

	/**
	 * Sets the component class loader
	 * 
	 * @param classLoader
	 */
	public void setClassLoader(ComponentClassLoader classLoader) {
		this.classLoader = classLoader;
		if (classPool != null) {
			classPool.clean();
		}
		classPool = new ClassPool();
		// add class path for domain and dependencies
		addDomainLoadersToJavassistPool(new HashSet<URLClassLoaderDomain>(),classLoaderDomain);
		// add class path also for slee 
		classPool.appendClassPath(new LoaderClassPath(classLoaderDomain.getSleeClassLoader()));
	}

	private void addDomainLoadersToJavassistPool(Set<URLClassLoaderDomain> visitedDomains, URLClassLoaderDomain domain) {
		if (visitedDomains.add(domain)) {
			classPool.appendClassPath(new LoaderClassPath(domain));
			// add dependency loaders class paths to the component's javassist classpool
			for (URLClassLoaderDomain dependencyDomain : domain.getDependencies()) {
				addDomainLoadersToJavassistPool(visitedDomains, dependencyDomain);
			}
		}
	}
	
	/**
	 * Retrieves the class loader domain for the component jar this component
	 * belongs, components that depend on this component must add this in its
	 * domain.
	 * 
	 * @return
	 */
	public URLClassLoaderDomain getClassLoaderDomain() {
		return classLoaderDomain;
	}

	/**
	 * Sets the class loader domain for the component jar this component belongs
	 * 
	 * @param classLoaderDomain
	 */
	public void setClassLoaderDomain(URLClassLoaderDomain classLoaderDomain) {
		this.classLoaderDomain = classLoaderDomain;
	}

	/**
	 * Sets the component javassist class pool
	 * 
	 * @param classPool
	 */
	public void setClassPool(ClassPool classPool) {
		this.classPool = classPool;
	}

	/**
	 * Retrieves the component javassist class pool
	 * 
	 * @return
	 */
	public ClassPool getClassPool() {
		return classPool;
	}

	/**
	 * Retrieves the file pointing to where this component is deployed
	 * 
	 * @return
	 */
	public File getDeploymentDir() {
		return deploymentDir;
	}

	/**
	 * Sets the the file pointing where this component is deployed
	 * 
	 * @param deploymentDir
	 */
	public void setDeploymentDir(File deploymentDir) {
		this.deploymentDir = deploymentDir;
	}

	/**
	 * Retrieves the source for this component (component jar/service
	 * descriptor) in the deployable unit
	 * 
	 * @return
	 */
	public String getDeploymentUnitSource() {
		return deploymentUnitSource;
	}

	/**
	 * Sets the source for this component (component jar/service descriptor) in
	 * the deployable unit
	 * 
	 * @param deploymentUnitSource
	 */
	public void setDeploymentUnitSource(String deploymentUnitSource) {
		this.deploymentUnitSource = deploymentUnitSource;
	}

	/**
	 * Retrieves the Deployable Unit this component belongs
	 * 
	 * @return
	 */
	public DeployableUnit getDeployableUnit() {
		return deployableUnit;
	}

	/**
	 * Specifies the the Deployable Unit this component belongs. This method
	 * also sets the reverse relation, adding the component to the deployable
	 * unit
	 * 
	 * @param deployableUnit
	 * @throws AlreadyDeployedException
	 *             if a component with same id already exists in the du
	 * @throws IllegalStateException
	 *             if this method is invoked and the deployable unit was already
	 *             set before
	 */
	public void setDeployableUnit(DeployableUnit deployableUnit)
			throws AlreadyDeployedException {
		if (this.deployableUnit != null) {
			throw new IllegalStateException(
					"deployable unit already set. du = " + this.deployableUnit);
		}
		this.deployableUnit = deployableUnit;
		if (!addToDeployableUnit()) {
			throw new AlreadyDeployedException(
					"unable to install du having multiple components with id "
							+ getComponentID());
		}
	}

	
	/**
	 * Gets set with permissions. This may be empty list in case of components that dont have them. 
	 * In case of compoennts that can have xml-desc wide permissions - this set contains this permissions,
	 *  as does any other components set defined in that xml-descr. however impl of Policy MUST 
	 *  handle double defined polic
	 * @return
	 */
	public Set<PermissionHolder> getPermissions() {
		return Collections.unmodifiableSet(this.permissions);
	}

	/**
	 * adds the component to the deployable unit
	 * 
	 * @return true if the component was added
	 */
	abstract boolean addToDeployableUnit();

	/**
	 * Retrieves the set of components IDs this component depends
	 * 
	 * @return
	 */
	public abstract Set<ComponentID> getDependenciesSet();

	/**
	 * Retrieves the ID of this component
	 * 
	 * @return
	 */
	public abstract ComponentID getComponentID();

	/**
	 * Indicates if the component is new to SLEE 1.1 specs
	 * 
	 * @return
	 */
	public abstract boolean isSlee11();

	/**
	 * Validates the component.
	 * 
	 * @return
	 * @throws DependencyException
	 * @throws DeploymentException
	 */
	public abstract boolean validate() throws DependencyException,
			DeploymentException;

	@Override
	public int hashCode() {
		return getComponentID().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj.getClass() == this.getClass()) {
			return ((SleeComponent) obj).getComponentID().equals(
					this.getComponentID());
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return getComponentID().toString();
	}

	/**
	 * Retrieves the JAIN SLEE specs component descriptor
	 * 
	 * @return
	 */
	public abstract ComponentDescriptor getComponentDescriptor();
	
	public abstract void processSecurityPermissions() throws DeploymentException;
	
	/**
	 * Indicates that the component was undeployed and thus should clean up any resources 
	 */
	public void undeployed() {
		
		classLoader = null;
		
		if (classLoaderDomain != null) {				
			classLoaderDomain.clean();
			classLoaderDomain = null;				
		}
		
		if (classPool != null) {
			classPool.clean();
			classPool = null;
		}
		
		if (permissions != null) {
			permissions.clear();
			permissions = null;
		}
	}

}
