package org.mobicents.slee.container.deployment.profile.jpa;

import java.net.URL;
import java.util.ArrayList;
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
import javax.slee.profile.ProfileTableAlreadyExistsException;
import javax.transaction.Transaction;

import org.apache.log4j.Logger;
import org.hibernate.cfg.Environment;
import org.hibernate.ejb.HibernatePersistence;
import org.jboss.jpa.deployment.PersistenceUnitInfoImpl;
import org.jboss.metadata.jpa.spec.PersistenceUnitMetaData;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.ProfileSpecificationComponent;
import org.mobicents.slee.container.management.SleeProfileTableManager;
import org.mobicents.slee.container.management.jmx.MobicentsManagement;

/**
 * 
 * JPAProfileTableFramework.java
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class JPAProfileTableFramework {

  private static Logger logger = Logger.getLogger(JPAProfileTableFramework.class);

  private EntityManagerFactory entityManagerFactory;
  private final SleeProfileTableManager sptm;

  private boolean isInitialized = false;
  private static Boolean isLoading = false;

  public JPAProfileTableFramework(SleeProfileTableManager sptm) {
    this.sptm = sptm;
  }

  private void createPersistenceUnit()
  {
    try {
      HibernatePersistence hp = new HibernatePersistence();
      PersistenceUnitMetaData pumd = new PersistenceUnitMetaData();

      pumd.setProvider("org.hibernate.ejb.HibernatePersistence");
      pumd.setJtaDataSource("java:/DefaultDS");
      pumd.setExcludeUnlistedClasses(false);

      boolean persistProfiles = MobicentsManagement.persistProfiles;

      Map pumdProps = new HashMap();
      pumdProps.put(Environment.HBM2DDL_AUTO, persistProfiles ? "update" : "create-drop");
      pumdProps.put(Environment.DIALECT, "org.hibernate.dialect.HSQLDialect");

      pumd.setProperties(pumdProps);
      pumd.setName("mobicents-profile-persistence-pu");

      Set classes = new HashSet<String>();
      classes.add(JPAProfileTable.class.getName());

      pumd.setClasses(classes);

      Properties properties = new Properties();

      properties.setProperty(Environment.DATASOURCE, "java:/DefaultDS");
      properties.setProperty(Environment.TRANSACTION_STRATEGY, "org.hibernate.ejb.transaction.JoinableCMTTransactionFactory");
      properties.setProperty(Environment.CONNECTION_PROVIDER, "org.hibernate.ejb.connection.InjectedDataSourceConnectionProvider");
      properties.setProperty("hibernate.jndi.java.naming.factory.url.pkgs", "org.jboss.naming:org.jnp.interfaces");
      properties.setProperty(Environment.CACHE_PROVIDER, "org.hibernate.cache.HashtableCacheProvider");
      properties.setProperty(Environment.TRANSACTION_MANAGER_STRATEGY, "org.hibernate.transaction.JBossTransactionManagerLookup");
      properties.setProperty("hibernate.jndi.java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
      properties.setProperty(Environment.DIALECT, "org.hibernate.dialect.HSQLDialect");
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
        tx = SleeContainer.lookupFromJndi().getTransactionManager().suspend();
        entityManagerFactory = hp.createContainerEntityManagerFactory(pi, null);
        isInitialized = true;        
      } catch (Exception e) {
        logger.error("Failure creating Persistence Unit for profile persistency.", e);
      } catch (Throwable t) {
        logger.error("Failure creating Persistence Unit for profile persistency.", t);
      } finally {
        if (tx != null)
          SleeContainer.lookupFromJndi().getTransactionManager().resume(tx);
      }
    }
    catch (Exception e) {
      logger.error("Failure creating Persistence Unit for profile persistency.", e);
    }
  }

  public void storeProfileTable(JPAProfileTable profileTable) {
    if(isLoading) {
      return;
    }

    if(logger.isDebugEnabled()) {
      logger.debug( "Storing into backend storage profile table " + profileTable );
    }

    EntityManager em = null;

    try {
      em = entityManagerFactory.createEntityManager();
      em.persist(profileTable);
    }
    finally {
      if(em != null) {
        em.close();
        em = null;
      }
    }
  }

  public void removeProfileTable(String profileTableName) {
    if(logger.isDebugEnabled()) {
      logger.debug( "Removing from backend storage profile table" + profileTableName );
    }

    EntityManager em = null;

    try {
      em = entityManagerFactory.createEntityManager();

      Query q = em.createQuery("DELETE FROM " + JPAProfileTable.class.getName() + " x WHERE x.profileTableName = :profileTableName")
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
    if(logger.isDebugEnabled()) {
      logger.debug("Renaming " + oldProfileTableName + " to " + newProfileTableName + " in backend storage.");
    }

    EntityManager em = null;

    try {
      em = entityManagerFactory.createEntityManager();

      Query q = em.createQuery("UPDATE " + JPAProfileTable.class.getName() + " x SET x.profileTableName = :newProfileTableName WHERE x.profileTableName = :oldProfileTableName")
      .setParameter("newProfileTableName", newProfileTableName).setParameter("oldProfileTableName", oldProfileTableName);

      q.executeUpdate();
    }
    finally {
      if(em != null) {
        em.close();
        em = null;
      }
    }
  }

  public void loadProfileTables(ProfileSpecificationComponent component) throws SLEEException, ProfileTableAlreadyExistsException {
    if(logger.isDebugEnabled()) {
      logger.debug("Loading from backend storage profile tables for " + component.getProfileSpecificationID());
    }

    EntityManager em = null;
    
    try {
      
      if(!isInitialized) {
        createPersistenceUnit();
      }
      
      em = entityManagerFactory.createEntityManager();
  
      Query q = em.createQuery("SELECT x FROM " + JPAProfileTable.class.getName() + " x WHERE x.profileSpecId = :profileSpecId")
      .setParameter("profileSpecId", component.getProfileSpecificationID().toString());
  
      List<JPAProfileTable> tables = q.getResultList();
  
      for(JPAProfileTable table : tables) {
        if(logger.isDebugEnabled()) {
          logger.debug("Table [" + table.getProfileTableName() + "] found in storage.");
        }
        synchronized (isLoading) {
          isLoading = true;
          sptm.addProfileTable(table.getProfileTableName(), component);
          isLoading = false;
        }
      }
    }
    finally {
      if(em != null) {
        em.close();
        em = null;
      }
    }
  }

}
