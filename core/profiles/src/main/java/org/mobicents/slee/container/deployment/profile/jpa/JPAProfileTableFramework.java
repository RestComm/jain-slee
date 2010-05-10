package org.mobicents.slee.container.deployment.profile.jpa;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.slee.SLEEException;
import javax.slee.profile.ProfileSpecificationID;
import javax.slee.profile.ProfileTableAlreadyExistsException;
import javax.transaction.Transaction;

import org.apache.log4j.Logger;
import org.hibernate.cfg.Environment;
import org.hibernate.ejb.HibernatePersistence;
import org.jboss.jpa.deployment.PersistenceUnitInfoImpl;
import org.jboss.metadata.jpa.spec.PersistenceUnitMetaData;
import org.mobicents.slee.container.component.profile.ProfileSpecificationComponent;
import org.mobicents.slee.container.management.ProfileManagementImpl;
import org.mobicents.slee.container.profile.ProfileTableImpl;
import org.mobicents.slee.container.transaction.SleeTransactionManager;

/**
 * 
 * JPAProfileTableFramework.java
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class JPAProfileTableFramework {

  private static Logger logger = Logger.getLogger(JPAProfileTableFramework.class);

  private EntityManagerFactory entityManagerFactory;
  private final ProfileManagementImpl sptm;

  private boolean isInitialized = false;
  private static Boolean isLoading = false;

  private final SleeTransactionManager sleeTransactionManager;

  private final Configuration configuration;
  
  public JPAProfileTableFramework(ProfileManagementImpl sptm,SleeTransactionManager sleeTransactionManager,Configuration configuration) {
    this.sptm = sptm;
    this.sleeTransactionManager = sleeTransactionManager;
    this.configuration = configuration;
  }

  private void createPersistenceUnit()
  {
    try {
      HibernatePersistence hp = new HibernatePersistence();
      PersistenceUnitMetaData pumd = new PersistenceUnitMetaData();

      pumd.setProvider("org.hibernate.ejb.HibernatePersistence");
      pumd.setJtaDataSource(configuration.getHibernateDatasource());
      pumd.setExcludeUnlistedClasses(false);

      boolean persistProfiles = configuration.isPersistProfiles();

      Map pumdProps = new HashMap();
      pumdProps.put(Environment.HBM2DDL_AUTO, persistProfiles ? "update" : "create-drop");
      pumdProps.put(Environment.DIALECT,configuration.getHibernateDialect());
      pumd.setProperties(pumdProps);
      pumd.setName("mobicents-profile-persistence-pu");

      Set classes = new HashSet<String>();
      classes.add(JPAProfileTable.class.getName());

      pumd.setClasses(classes);

      Properties properties = new Properties();

      properties.setProperty(Environment.DATASOURCE,configuration.getHibernateDatasource());
    		  
      properties.setProperty(Environment.TRANSACTION_STRATEGY, "org.hibernate.ejb.transaction.JoinableCMTTransactionFactory");
      properties.setProperty(Environment.CONNECTION_PROVIDER, "org.hibernate.ejb.connection.InjectedDataSourceConnectionProvider");
      properties.setProperty("hibernate.jndi.java.naming.factory.url.pkgs", "org.jboss.naming:org.jnp.interfaces");
      properties.setProperty(Environment.CACHE_PROVIDER, "org.hibernate.cache.HashtableCacheProvider");
      properties.setProperty(Environment.TRANSACTION_MANAGER_STRATEGY, "org.hibernate.transaction.JBossTransactionManagerLookup");
      properties.setProperty("hibernate.jndi.java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
      properties.setProperty(Environment.DIALECT,configuration.getHibernateDialect());
      // FIXME: Should be Environment.JACC_CONTEXTID but it's
      // hibernate.jacc_context_id vs hibernate.jacc.ctx.id. Bug?
      properties.setProperty("hibernate.jacc.ctx.id", "persistence.xml");
      properties.setProperty(Environment.CACHE_REGION_PREFIX, "persistence.unit:unitName=#" + pumd.getName());
      properties.setProperty(Environment.SESSION_FACTORY_NAME, "persistence.unit:unitName=#" + pumd.getName());
      properties.setProperty(Environment.HBM2DDL_AUTO, persistProfiles ? "update" : "create-drop");
      properties.setProperty(Environment.USE_REFLECTION_OPTIMIZER, "false");
      properties.setProperty(Environment.BYTECODE_PROVIDER, "javassist");
      properties.setProperty(Environment.STATEMENT_BATCH_SIZE, "0");
      properties.setProperty(Environment.SHOW_SQL, "false");
      properties.setProperty(Environment.FORMAT_SQL, "false");

      PersistenceUnitInfoImpl pi = new PersistenceUnitInfoImpl(pumd,
          properties, Thread.currentThread().getContextClassLoader(),
          ClassLoader.getSystemResource("."), new ArrayList<URL>(),
          new InitialContext());

      Transaction tx = null;

      try {
        tx = sleeTransactionManager.suspend();
        entityManagerFactory = hp.createContainerEntityManagerFactory(pi, null);
        isInitialized = true;        
      } catch (Exception e) {
        logger.error("Failure creating Persistence Unit for profile persistency.", e);
      } catch (Throwable t) {
        logger.error("Failure creating Persistence Unit for profile persistency.", t);
      } finally {
        if (tx != null)
          sleeTransactionManager.resume(tx);
      }
    }
    catch (Exception e) {
      logger.error("Failure creating Persistence Unit for profile persistency.", e);
    }
  }

  public void storeProfileTable(ProfileTableImpl profileTable) {
    if(isLoading) {
      return;
    }

    if(logger.isTraceEnabled()) {
      logger.trace( "Storing into backend storage profile table " + profileTable );
    }

    EntityManager em = null;

    try {
      em = entityManagerFactory.createEntityManager();
      em.persist(new JPAProfileTable(profileTable));
      em.flush();
    }
    finally {
      if(em != null) {
        em.close();
        em = null;
      }
    }
  }

  public void removeProfileTable(String profileTableName) {
    if(logger.isTraceEnabled()) {
      logger.trace( "Removing from backend storage profile table" + profileTableName );
    }

    EntityManager em = null;

    try {
      em = entityManagerFactory.createEntityManager();

      Query q = em.createNamedQuery(JPAProfileTable.JPA_NAMED_QUERY_DELETE_TABLE)
      	.setParameter("profileTableName", profileTableName);

      q.executeUpdate();
    }
    finally {
      if(em != null) {
        em.close();
        em = null;
      }
    }
  }

  public void renameProfileTable(String oldProfileTableName, String newProfileTableName) {
    if(logger.isTraceEnabled()) {
      logger.trace("Renaming " + oldProfileTableName + " to " + newProfileTableName + " in backend storage.");
    }

    EntityManager em = null;

    try {
      em = entityManagerFactory.createEntityManager();

      Query q = em.createNamedQuery(JPAProfileTable.JPA_NAMED_QUERY_RENAME_TABLE)
      	.setParameter("newProfileTableName", newProfileTableName)
      	.setParameter("oldProfileTableName", oldProfileTableName);

      q.executeUpdate();
    }
    finally {
      if(em != null) {
        em.close();
        em = null;
      }
    }
  }

  public ProfileSpecificationID getProfileSpecificationID(String profileTableName) {
	  
	  EntityManager em = null;
	    
	    try {	  
	      
	      em = entityManagerFactory.createEntityManager();
	  
	      Query q = em.createNamedQuery(JPAProfileTable.JPA_NAMED_QUERY_FIND_TABLE_BY_NAME)
	      	.setParameter("profileTableName", profileTableName);
	  
	      List<JPAProfileTable> tables = q.getResultList();
	      if (tables.isEmpty()) {
	    	  return null;
	      }
	      else {
	    	  // single result only, since profile table name is primary key
	    	  return tables.get(0).getProfileSpecId();
	      }
	    }
	    finally {
	      if(em != null) {
	        em.close();
	      }
	    }
	    
  }
  
  public void loadProfileTables(ProfileSpecificationComponent component) throws SLEEException, ProfileTableAlreadyExistsException {
   
	if(logger.isTraceEnabled()) {
      logger.trace("Loading from backend storage profile tables for " + component.getProfileSpecificationID());
    }

	for(String profileTableName : _getProfileTableNames(component.getProfileSpecificationID())) {
		if(logger.isDebugEnabled()) {
			logger.debug("Profile Table named " + profileTableName + " found in JPA data source.");
		}
		synchronized (isLoading) {
			isLoading = true;
			// ask for the profile table, the manager will build the local object
			try {
				sptm.loadProfileTableLocally(profileTableName, component);
			}
			catch (Throwable e) {
				if(logger.isDebugEnabled()) {
					logger.debug("Unable to load profile table named "+profileTableName,e);
				}
			}
			isLoading = false;
		}
	}
  }

  /**
   * Retrieves profile table names from database
   * @return
   */
  public Set<String> getProfileTableNames() {
	  return _getProfileTableNames(null);
  }
  
  /**
   * Retrieves profile table names from database, which are bound to the specified {@link ProfileSpecificationID}
   * @return
   */
  public Set<String> getProfileTableNames(ProfileSpecificationID profileSpecificationID) {	  
	  return _getProfileTableNames(profileSpecificationID);
  }
  
  /**
   * @return
   */
  public Set<String> _getProfileTableNames(ProfileSpecificationID profileSpecificationID) {
	  
	  Set<String> resultSet = null;
	  
	  EntityManager em = null;
	    
	    try {
	      
	    	if(!isInitialized) {
	    		createPersistenceUnit();
	    	}

	    	em = entityManagerFactory.createEntityManager();

	    	Query q = null;
	    	if (profileSpecificationID == null) {
	    		q = em.createNamedQuery(JPAProfileTable.JPA_NAMED_QUERY_SELECT_ALL_TABLES);
	    	}
	    	else {
	    		q = em.createNamedQuery(JPAProfileTable.JPA_NAMED_QUERY_SELECT_TABLES_BY_PROFILE_SPEC_ID)
	    		.setParameter("profileSpecId", profileSpecificationID);
	    	}
	    	List<JPAProfileTable> resultList = q.getResultList();	  
	    	if (resultList.isEmpty()) {
	    		resultSet = Collections.emptySet();
	    	}
	    	else {
	    		resultSet = new HashSet<String>();
	    		for(JPAProfileTable result : resultList) {
	    			resultSet.add(result.getProfileTableName());	  	        
	    		} 
	    	}	      	      
	    }
	    finally {
	    	if(em != null) {
	    		em.close();
	    		em = null;
	    	}
	    }

	    return resultSet;
  }
  
  /**
   * @return
   */
  public Configuration getConfiguration() {
	  return configuration;
  }

}
