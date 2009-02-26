package org.mobicents.slee.container.component;

import java.net.URL;
import java.util.Set;

import javassist.LoaderClassPath;

import javax.slee.ComponentID;
import javax.slee.management.ComponentDescriptor;
import javax.slee.management.DependencyException;
import javax.slee.management.DeploymentException;

import org.jboss.classloader.spi.ClassLoaderDomain;
import org.mobicents.slee.container.component.deployment.ClassPool;
import org.mobicents.slee.container.component.deployment.DeployableUnit;

/**
 * Base class for a SLEE component, providing features related with class
 * loading, deployable unit and other component references
 * 
 * @author martins
 * 
 */
public abstract class SleeComponent {

	/**
	 * the class loader domain for the component, where no only its own class
	 * loader policy is registred, but also the policies for all components it
	 * depends.
	 */
	private ClassLoaderDomain classLoaderDomain;

	/**
	 * the component class loader
	 */
	private ClassLoader classLoader;

	/**
	 * the javassist class pool
	 */
	private ClassPool classPool;
	
	/**
	 * the DU the component belongs
	 */
	private DeployableUnit deployableUnit;
	
	/**
	 * the URL where this component is deployed
	 */
	private URL deploymentDir;
	
	/**
	 * the source for this component (component jar/service descriptor) in the deployable unit
	 */
	private String deploymentUnitSource;
	
	/**
	 * Retrieves the component class loader
	 * 
	 * @return
	 */
	public ClassLoader getClassLoader() {
		return classLoader;
	}

	/**
	 * Sets the component class loader
	 * 
	 * @param classLoader
	 */
	public void setClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
		if (classPool != null) {
			classPool.clean();
		}
		classPool = new ClassPool();
    	classPool.appendClassPath(new LoaderClassPath(this.classLoader)); 
	}

	/**
	 * Sets the component javassist class pool
	 * @param classPool
	 */
	public void setClassPool(ClassPool classPool) {
		this.classPool = classPool;
	}
	
	/**
	 * Retrieves the component javassist class pool
	 * @return
	 */
	public ClassPool getClassPool() {
		return classPool;
	}
	
	/**
	 * Retrieves the component class loader domain
	 * 
	 * @return
	 */
	public ClassLoaderDomain getClassLoaderDomain() {
		return classLoaderDomain;
	}

	/**
	 * Sets the component class loader domain
	 * 
	 * @param classLoaderDomain
	 */
	public void setClassLoaderDomain(ClassLoaderDomain classLoaderDomain) {
		this.classLoaderDomain = classLoaderDomain;
	}

	/**
	 * Retrieves the URL where this component is deployed
	 * @return
	 */
	public URL getDeploymentDir() {
		return deploymentDir;
	}
	
	/**
	 * Sets the URL where this component is deployed
	 * @param deploymentDir
	 */
	public void setDeploymentDir(URL deploymentDir) {
		this.deploymentDir = deploymentDir;
	}

	/**
	 * Retrieves the source for this component (component jar/service descriptor) in the deployable unit
	 * @return
	 */
	public String getDeploymentUnitSource() {
		return deploymentUnitSource;
	}

	/**
	 * Sets the source for this component (component jar/service descriptor) in the deployable unit
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
	 * @throws IllegalStateException
	 *             if this method is invoked and the deployable unit was already
	 *             set before
	 */
	public void setDeployableUnit(DeployableUnit deployableUnit) {
		if (this.deployableUnit != null) {
			throw new IllegalStateException(
					"deployable unit already set. du = " + this.deployableUnit);
		}
		this.deployableUnit = deployableUnit;
		addToDeployableUnit();
	}

	/**
	 * adds the component to the deployable unit
	 */
	abstract void addToDeployableUnit(); 
	
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
	public abstract boolean validate() throws DependencyException, DeploymentException;
	
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
	 * @return
	 */
	public abstract ComponentDescriptor getComponentDescriptor();

}
