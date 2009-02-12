package org.mobicents.slee.container.component;

import java.util.HashSet;
import java.util.Set;

import javax.slee.ComponentID;

import org.jboss.classloader.spi.ClassLoaderDomain;
import org.jboss.classloader.spi.ClassLoaderPolicy;

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
	 * the component class loader policy, "pointing" to all classes of the
	 * component
	 */
	private ClassLoaderPolicy classLoaderPolicy;

	/**
	 * the component class loader
	 */
	private ClassLoader classLoader;

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
	 * Retrieves the component class loader policy
	 * 
	 * @return
	 */
	public ClassLoaderPolicy getClassLoaderPolicy() {
		return classLoaderPolicy;
	}

	/**
	 * Sets the component class loader policy
	 * 
	 * @param classLoaderPolicy
	 */
	public void setClassLoaderPolicy(ClassLoaderPolicy classLoaderPolicy) {
		this.classLoaderPolicy = classLoaderPolicy;
	}

	/**
	 * the DU the component belongs
	 */
	private DeployableUnit deployableUnit;

	/**
	 * Retrieves the Deployable Unit this component belongs
	 * 
	 * @return
	 */
	public DeployableUnit getDeployableUnit() {
		return deployableUnit;
	}

	/**
	 * Specifies the the Deployable Unit this component belongs
	 * 
	 * @param deployableUnit
	 */
	public void setDeployableUnit(DeployableUnit deployableUnit) {
		this.deployableUnit = deployableUnit;
	}

	/**
	 * the set of component IDs this component depends
	 */
	private Set<ComponentID> dependenciesSet = new HashSet<ComponentID>();

	/**
	 * Retrieves the set of components IDs this component depends
	 * 
	 * @return
	 */
	public Set<ComponentID> getDependenciesSet() {
		return dependenciesSet;
	}
	
	/**
	 * Retrieves the ID of this component
	 * @return
	 */
	public abstract ComponentID getComponentID();
	
	/**
	 * Indicates if the component is new to SLEE 1.1 specs 
	 * @return
	 */
	public abstract boolean isSlee11();
	
	@Override
	public int hashCode() {		
		return getComponentID().hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj.getClass() == this.getClass()) {
			return ((SleeComponent)obj).getComponentID().equals(this.getComponentID());
		}
		else {
			return false;
		}
	}
}
