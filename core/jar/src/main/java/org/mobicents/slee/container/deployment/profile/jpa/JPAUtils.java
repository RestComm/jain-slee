package org.mobicents.slee.container.deployment.profile.jpa;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.slee.SLEEException;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.profile.ProfileID;
import javax.slee.profile.ProfileSpecificationID;
import javax.slee.profile.UnrecognizedProfileTableNameException;
import javax.slee.profile.query.QueryExpression;
import javax.transaction.Transaction;

import org.apache.log4j.Logger;
import org.hibernate.cfg.Environment;
import org.hibernate.ejb.HibernatePersistence;
import org.jboss.jpa.deployment.PersistenceUnitInfoImpl;
import org.jboss.metadata.jpa.spec.PersistenceUnitMetaData;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.ProfileSpecificationComponent;
import org.mobicents.slee.container.profile.ProfileConcrete;
import org.mobicents.slee.container.profile.ProfileDataSource;
import org.mobicents.slee.container.profile.ProfileObject;
import org.mobicents.slee.container.profile.ProfileTableConcrete;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;
import org.mobicents.slee.runtime.transaction.TransactionalAction;

/**
 * 
 * JPAUtils.java
 *
 * <br>Project:  mobicents
 * <br>2:05:25 PM Mar 26, 2009 
 * <br>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class JPAUtils implements ProfileDataSource {

  private static Logger logger = Logger.getLogger(JPAUtils.class);

  public static final JPAUtils INSTANCE = new JPAUtils();

  private static final String DEFAULT_PROFILE_NAME = "";

  private static final SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
  
  private Map<ProfileSpecificationID,EntityManagerFactory > factories = new HashMap<ProfileSpecificationID, EntityManagerFactory>();
  
  private JPAUtils() { }

  public void install(ProfileSpecificationComponent component) {
	  synchronized (this) {
		  // 1. Generate CMP Interface Impl with JPA Annotations
		  Class<?> c = new ConcreteProfileGenerator(component).generateConcreteProfile();
		  component.setProfileCmpConcreteClass(c);
		  // 2. Create the corresponding JPA PU -- FIXME: Should be somewhere else?
		  createPersistenceUnit( component );
	  }	
	}
  
  private void createPersistenceUnit(ProfileSpecificationComponent profileComponent)
  {
    try
    {
      HibernatePersistence hp = new HibernatePersistence();
      PersistenceUnitMetaData pumd = new PersistenceUnitMetaData();

      pumd.setProvider("org.hibernate.ejb.HibernatePersistence");
      pumd.setJtaDataSource("java:/DefaultDS");
      pumd.setExcludeUnlistedClasses(false);

      Map pumdProps = new HashMap();
      pumdProps.put( Environment.HBM2DDL_AUTO, "create-drop" );
      pumdProps.put( Environment.DIALECT, "org.hibernate.dialect.HSQLDialect" );

      pumd.setProperties( pumdProps );
      pumd.setName( "JSLEEProfiles" + profileComponent.getComponentID().hashCode() );

      Set classes = new HashSet<String>();
      classes.add( profileComponent.getProfileCmpInterfaceClass().getName() + "Impl" );

      pumd.setClasses(classes);

      Properties properties = new Properties();

      properties.setProperty(Environment.DATASOURCE, "java:/DefaultDS");
      properties.setProperty(Environment.TRANSACTION_STRATEGY, "org.hibernate.ejb.transaction.JoinableCMTTransactionFactory" );
      properties.setProperty(Environment.CONNECTION_PROVIDER, "org.hibernate.ejb.connection.InjectedDataSourceConnectionProvider" );
      properties.setProperty("hibernate.jndi.java.naming.factory.url.pkgs", "org.jboss.naming:org.jnp.interfaces");
      properties.setProperty(Environment.CACHE_PROVIDER, "org.hibernate.cache.HashtableCacheProvider");
      properties.setProperty(Environment.TRANSACTION_MANAGER_STRATEGY, "org.hibernate.transaction.JBossTransactionManagerLookup");
      properties.setProperty("hibernate.jndi.java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
      properties.setProperty(Environment.DIALECT, "org.hibernate.dialect.HSQLDialect");
      // FIXME: Should be Environment.JACC_CONTEXTID but it's hibernate.jacc_context_id vs hibernate.jacc.ctx.id. Bug?
      properties.setProperty("hibernate.jacc.ctx.id", "persistence.xml");
      properties.setProperty(Environment.CACHE_REGION_PREFIX, "persistence.unit:unitName=#" + pumd.getName() );
      properties.setProperty(Environment.SESSION_FACTORY_NAME, "persistence.unit:unitName=#" + pumd.getName() );
      properties.setProperty(Environment.HBM2DDL_AUTO, "create-drop");
      properties.setProperty(Environment.USE_REFLECTION_OPTIMIZER, "false");
      properties.setProperty(Environment.BYTECODE_PROVIDER, "javassist");

      ClassLoader newClassLoader = profileComponent.getClassLoader();
      Thread.currentThread().setContextClassLoader( newClassLoader );

      for(String className : pumd.getClasses())
      {
        Thread.currentThread().getContextClassLoader().loadClass( className );
      }

      PersistenceUnitInfoImpl pi = new PersistenceUnitInfoImpl(pumd, properties, Thread.currentThread().getContextClassLoader(), ClassLoader.getSystemResource( "." ), new ArrayList<URL>(), new InitialContext());

      Transaction tx = null;
      EntityManagerFactory actualFactory = null;

      try {
        tx = sleeContainer.getTransactionManager().suspend();
        actualFactory = hp.createContainerEntityManagerFactory(pi, null);
      }
      catch (Exception e) {
        logger.error("Failure creating Persistence Unit.", e);
      }
      catch (Throwable t) {
        logger.error("Failure creating Persistence Unit.", t);
      }
      finally {
        if(tx!=null) sleeContainer.getTransactionManager().resume(tx);
      }

      factories.put(profileComponent.getProfileSpecificationID(), actualFactory );
    }
    catch (Exception e) {
      logger.error("Failure creating Persistence Unit.", e);
    }
    
  }
  
  public void uninstall(ProfileSpecificationComponent component) {
	  synchronized (this) {
		  SleeTransactionManager sleeTransactionManager = sleeContainer.getTransactionManager();
		  Transaction tx = null;
		  try {
			  tx = sleeTransactionManager.suspend();
		  } catch (Throwable e) {
			  throw new SLEEException(e.getMessage(),e);
		  }
		  EntityManagerFactory factory = factories.get(component.getProfileSpecificationID());
		  if (factory != null) {
			  factory.close();
		  }
		  try {
			  sleeTransactionManager.resume(tx);
		  } catch (Throwable e) {
			  throw new SLEEException(e.getMessage(),e);
		  }
	  }
	}
  
  private final static String TX_CONTEXT_EM_PREFIX = "JPA.EM." ;
  
  private EntityManager getEntityManager(ProfileSpecificationID componentId)
  {
	  
	  // look in tx
	  Map transactionContextData = null;
	  try {
		transactionContextData = sleeContainer.getTransactionManager().getTransactionContext().getData();
	  } catch (Throwable e1) {
		  throw new SLEEException(e1.getMessage(),e1);
	  }
	  String dataKey = TX_CONTEXT_EM_PREFIX + componentId.toString();
	  EntityManager result = (EntityManager) transactionContextData.get(dataKey);
	  if (result == null) {
		  // create using factory
		  result = factories.get(componentId).createEntityManager();
		  // store in tx context data
		  transactionContextData.put(dataKey, result);
		  // add a tx action to close it before tx commits
		  final EntityManager em = result;
		  TransactionalAction action = new TransactionalAction() {
			  public void execute() {
				try {
					em.close();
				}
				catch (Throwable e) {
					logger.error(e.getMessage(),e);
				}
			  }
		  };		  
		  try {
			  sleeContainer.getTransactionManager().addBeforeCommitAction(action);
		  } catch (Throwable e1) {
			  throw new SLEEException(e1.getMessage(),e1);
		  }		  
	  }
	  return result;
	  
  }

  public Object find(String profileTableName, String profileName) throws SLEEException, UnrecognizedProfileTableNameException
  {    
    if (profileName == null) {
    	throw new NullPointerException("null profile name");
    }

    ProfileTableConcrete ptc = sleeContainer.getSleeProfileTableManager().getProfileTable(profileTableName);
    String jpaTableName = ptc.getProfileSpecificationComponent().getProfileCmpConcreteClass().getName();

    EntityManager em = getEntityManager(ptc.getProfileSpecificationComponent().getProfileSpecificationID());
    Query createProfileQuery = em.createQuery("FROM " + jpaTableName + " WHERE tableName = ?1 AND safeProfileName = ?2").setParameter(1, profileTableName).setParameter(2, profileName);

    try {
    	return createProfileQuery.getSingleResult();
    }
    catch (NoResultException e) {
    	// we ignore it.
    	return null;
    }
  }

  public Collection<Object> findAll(String profileTableName)
  {
    // TODO: complete.
    return null;
  }

  public Object findProfileByAttribute(String profileTableName, String attributeName, Object attributeValue)
  {
    // TODO: complete.
    return null;
  }

  public Collection<Object> findProfilesByAttribute(String profileTableName, String attributeName, Object attributeValue)
  {
    // TODO: complete.
    return null;
  }

  public Collection<ProfileID> getProfilesIDs(ProfileTableConcrete ptc)
  {
	  Collection<ProfileID> result = new ArrayList<ProfileID>();

	  ProfileSpecificationID psid = ptc.getProfileSpecificationComponent().getProfileSpecificationID();

	  String jpaTableName = ptc.getProfileSpecificationComponent().getProfileCmpConcreteClass().getName();
	  String profileTableName = ptc.getProfileTableName();

	  EntityManager em = getEntityManager(psid);
	  Query createProfileQuery = em.createQuery( "FROM " + jpaTableName + " WHERE tableName = ?1 AND safeProfileName <> ?2").setParameter(1, profileTableName).setParameter(2, DEFAULT_PROFILE_NAME);

	  for(Object o : createProfileQuery.getResultList())
	  {
		  result.add( new ProfileID(profileTableName, ((ProfileConcrete)o).getProfileName()) );
	  }

	  return result;
  }

  public boolean find(ProfileTableConcrete ptc, String profileName)
  {
	  EntityManager em = null;

	  if (profileName == null) {
		  throw new NullPointerException("null profile name");
	  }


	  String jpaTableName = ptc.getProfileSpecificationComponent().getProfileCmpConcreteClass().getName();
	  String profileTableName = ptc.getProfileTableName();

	  em = getEntityManager(ptc.getProfileSpecificationComponent().getProfileSpecificationID());
	  Query createProfileQuery = em.createQuery("FROM " + jpaTableName + " WHERE tableName = ?1 AND safeProfileName = ?2").setParameter(1, profileTableName).setParameter( 2, profileName );

	  try
	  {
		  createProfileQuery.getSingleResult();
		  return true;        
	  }
	  catch (NoResultException e) {
		  // we ignore this
		  return false;
	  }   
  }

  public List<String> findAllNames(ProfileTableConcrete ptc) throws NullPointerException, TransactionRequiredLocalException, SLEEException
  {
    ArrayList<String> profileNames = new ArrayList<String>();
    EntityManager em = null;

    
      String jpaTableName = ptc.getProfileSpecificationComponent().getProfileCmpConcreteClass().getName();
      String profileTableName = ptc.getProfileTableName();

      em = getEntityManager(ptc.getProfileSpecificationComponent().getProfileSpecificationID());
      Query createProfileQuery = em.createQuery("FROM " + jpaTableName + " WHERE tableName = ?1 AND safeProfileName <> ?2").setParameter(1, profileTableName).setParameter(2, DEFAULT_PROFILE_NAME);
      List results = createProfileQuery.getResultList();

      for(Object result : results)
      {
        profileNames.add( ((ProfileConcrete)result).getProfileName());
      }
    

    return profileNames;
    //new ProfileLocalObjectConcreteImpl(ptc.getProfileTableName(), ptc.getProfileSpecificationComponent().getProfileSpecificationID(), profileName, null, false);
  }

  // ProfileProvisioningMBean Operations

  public ProfileSpecificationID getProfileSpecification(String profileTableName)
  {
    // TODO: complete.
    return null;
  }


  public Collection getProfileTables(ProfileSpecificationID id)
  {
    // TODO: complete.
    return null;
  }

  public Collection getProfiles(String profileTableName)
  {
    // TODO: complete.
    return null;
  }

  public Collection getProfilesByAttribute(String profileTableName, String attributeName, Object attributeValue)
  {
    // TODO: complete.
    return null;
  }

  public Collection getProfilesByStaticQuery(String profileTableName, String queryName, Object[] parameters) 
  {
    // TODO: complete.
    return null;
  }

  public Collection getProfilesByDynamicQuery(String profileTableName, QueryExpression expr)
  {
    // TODO: complete.
    return null;
  }

  @Deprecated
  public Collection getProfilesByIndexedAttribute(String profileTableName, String attributeName, Object attributeValue)
  {
    // TODO: complete.
    return null;
  }

  public void persistProfile(ProfileObject profileObject)
  {
    EntityManager em = null;
    
      em = getEntityManager(profileObject.getProfileSpecificationComponent().getProfileSpecificationID());
      em.persist(profileObject.getProfileConcrete()); 
    
  }
  
  public ProfileConcrete retrieveProfile(ProfileTableConcrete profileTable, String profileName)
  {
    EntityManager em = null;
    
    if (profileName == null) {
    	profileName = DEFAULT_PROFILE_NAME;
    }
    
    
      ProfileSpecificationComponent psc = profileTable.getProfileSpecificationComponent();
  
      em = getEntityManager(psc.getProfileSpecificationID());
      Query q = em.createQuery("FROM " + psc.getProfileCmpConcreteClass().getName() + " WHERE tableName = ?1 AND safeProfileName = ?2").setParameter(1, profileTable.getProfileTableName()).setParameter(2, profileName);
  
      List resultList = q.getResultList();
      if (resultList.size() > 0) {
        return (ProfileConcrete) resultList.get(0);        
      }
      else {
        return null;
      }
    
  }

  public boolean removeprofile(ProfileTableConcrete profileTable, String profileName)
  {
    EntityManager em = null;
  
    if (profileName == null) {
    	profileName = DEFAULT_PROFILE_NAME;
    }
    
    
      ProfileSpecificationComponent psc = profileTable.getProfileSpecificationComponent();

      em = getEntityManager(psc.getProfileSpecificationID());
      Query q = em.createQuery("DELETE FROM " + psc.getProfileCmpConcreteClass().getName() + " WHERE tableName = ?1 AND profileName = ?2").setParameter(1, profileTable.getProfileTableName()).setParameter(2, profileName);

      return q.executeUpdate() > 0;
    
  }
  
  public void removeprofile(ProfileObject profileObject)
  {
    EntityManager em = null;
  
    
      ProfileSpecificationComponent psc = profileObject.getProfileSpecificationComponent();

      em = getEntityManager(psc.getProfileSpecificationID());
      em.remove(profileObject.getProfileConcrete());
    
  }

  
  

}
