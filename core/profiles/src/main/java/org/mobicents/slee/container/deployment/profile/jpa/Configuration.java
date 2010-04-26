/**
 * 
 */
package org.mobicents.slee.container.deployment.profile.jpa;

/**
 * @author martins
 *
 */
public class Configuration {

	private boolean persistProfiles;
	
	private boolean clusteredProfiles;
	
	private String hibernateDatasource;
	
	private String hibernateDialect;

	/**
	 *  
	 * @param persistProfiles the persistProfiles to set
	 */
	public void setPersistProfiles(boolean persistProfiles) {
		this.persistProfiles = persistProfiles;
	}

	/**
	 *  
	 * @return the clusteredProfiles
	 */
	public boolean isClusteredProfiles() {
		return clusteredProfiles;
	}

	/**
	 *  
	 * @param clusteredProfiles the clusteredProfiles to set
	 */
	public void setClusteredProfiles(boolean clusteredProfiles) {
		this.clusteredProfiles = clusteredProfiles;
	}

	/**
	 *  
	 * @return the persistProfiles
	 */
	public boolean isPersistProfiles() {
		return persistProfiles;
	}

	/**
	 *  
	 * @return the hibernateDatasource
	 */
	public String getHibernateDatasource() {
		return hibernateDatasource;
	}

	/**
	 *  
	 * @return the hibernateDialect
	 */
	public String getHibernateDialect() {
		return hibernateDialect;
	}

	/**
	 * Sets 
	 * @param hibernateDatasource the hibernateDatasource to set
	 */
	public void setHibernateDatasource(String hibernateDatasource) {
		this.hibernateDatasource = hibernateDatasource;
	}

	/**
	 *  
	 * @param hibernateDialect the hibernateDialect to set
	 */
	public void setHibernateDialect(String hibernateDialect) {
		this.hibernateDialect = hibernateDialect;
	}	
	
}
