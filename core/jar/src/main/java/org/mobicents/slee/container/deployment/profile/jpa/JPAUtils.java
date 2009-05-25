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
import javax.persistence.Query;
import javax.slee.InvalidArgumentException;
import javax.slee.SLEEException;
import javax.slee.profile.AttributeTypeMismatchException;
import javax.slee.profile.ProfileSpecificationID;
import javax.slee.profile.UnrecognizedAttributeException;
import javax.slee.profile.UnrecognizedQueryNameException;
import javax.slee.profile.query.QueryExpression;
import javax.transaction.Transaction;

import org.apache.log4j.Logger;
import org.hibernate.cfg.Environment;
import org.hibernate.ejb.HibernatePersistence;
import org.jboss.jpa.deployment.PersistenceUnitInfoImpl;
import org.jboss.metadata.jpa.spec.PersistenceUnitMetaData;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.ProfileSpecificationComponent;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.MCMPField;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.MProfileIndex;
import org.mobicents.slee.container.profile.ProfileDataSource;
import org.mobicents.slee.container.profile.ProfileObject;
import org.mobicents.slee.container.profile.ProfileTableImpl;
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
 * @author martins
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
		  // generate profile pojo
		  component.setProfileEntityClass(new ConcreteProfileEntityGenerator(component).generateClass());
		  // 1. Generate CMP Interface Impl with JPA Annotations		  
		  component.setProfileCmpConcreteClass(new ConcreteProfileGenerator(component).generateConcreteProfile());
		  // 2. Create the corresponding JPA PU -- FIXME: Should be somewhere else?
		  createPersistenceUnit( component );
	  }	
	}
  
  @SuppressWarnings("unchecked")
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
      classes.add( profileComponent.getProfileEntityClass().getName());

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
  
  @SuppressWarnings("unchecked")
private EntityManager getEntityManager(ProfileSpecificationID componentId) {
	  
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

  public Collection<ProfileEntity> findAll(ProfileTableImpl profileTable) {
	 return findProfilesByAttribute(profileTable, null, null);
  }
  
  @SuppressWarnings("unchecked")
public Collection<ProfileEntity> findProfilesByAttribute(ProfileTableImpl profileTable, String attributeName, Object attributeValue) {
	  
	  String jpaTableName = profileTable.getProfileSpecificationComponent().getProfileEntityClass().getName();
	  String queryString = "FROM " + jpaTableName + " WHERE tableName = ?1 AND safeProfileName <> ''";
	  if (attributeName != null) {
		  queryString += " AND C" + attributeName + " = ?2";
	  }

	  EntityManager em = getEntityManager(profileTable.getProfileSpecificationComponent().getProfileSpecificationID());
	  Query query = em.createQuery(queryString).setParameter(1, profileTable.getProfileTableName());
	  if (attributeName != null) {
		  query.setParameter(2, attributeValue);
	  }
	  return query.getResultList();	    
  }
  
  public ProfileEntity findProfile(ProfileTableImpl profileTable, String profileName) {
	  
	  String jpaTableName = profileTable.getProfileSpecificationComponent().getProfileEntityClass().getName();
	  EntityManager em = getEntityManager(profileTable.getProfileSpecificationComponent().getProfileSpecificationID());
	  Query query = em.createQuery("FROM " + jpaTableName + " WHERE tableName = ?1 AND safeProfileName = ?2").setParameter(1, profileTable.getProfileTableName()).setParameter( 2, profileName );
	
	  List<?> resultList = query.getResultList();
	  if (resultList.isEmpty()) {
		  return null;
	  }
	  else {
		  return (ProfileEntity) resultList.get(0);
	  }
  }

  

  // ProfileProvisioningMBean Operations


  @SuppressWarnings("unchecked")
public Collection<ProfileEntity> getProfilesByStaticQuery(ProfileTableImpl profileTable, String queryName, Object[] parameters) throws NullPointerException, UnrecognizedQueryNameException,
	AttributeTypeMismatchException, InvalidArgumentException {
    
	// TODO check for exceptions
	  
      QueryWrapper wQuery = JPAQueryBuilder.getQuery(queryName);
  
      String jpaTableName = profileTable.getProfileSpecificationComponent().getProfileEntityClass().getName();
  
      EntityManager em = getEntityManager(profileTable.getProfileSpecificationComponent().getProfileSpecificationID());
      Query staticQuery = em.createQuery(wQuery.getQuerySQL(jpaTableName));
      
      if(wQuery.getMaxMatches() > 0)
        staticQuery.setMaxResults((int)wQuery.getMaxMatches());
      
      for(int i = 0; i < parameters.length; i++)
      {
        try{
          staticQuery.setParameter( i+1, parameters[i] );          
        }
        catch (Exception ignore) {
          // We don't care, it's because there's no such parameter.
        }
      }
      
      return staticQuery.getResultList();
     
    
  }

  @SuppressWarnings("unchecked")
public Collection<ProfileEntity> getProfilesByDynamicQuery(ProfileTableImpl profileTable, QueryExpression expr) throws UnrecognizedAttributeException,
	AttributeTypeMismatchException {
    
	  // TODO check for exceptions
	  
      QueryWrapper wQuery = JPAQueryBuilder.parseDynamicQuery(expr);
  
      String jpaTableName = profileTable.getProfileSpecificationComponent().getProfileEntityClass().getName();
  
      EntityManager em = getEntityManager(profileTable.getProfileSpecificationComponent().getProfileSpecificationID());
      Query dynamicQuery = em.createQuery(wQuery.getQuerySQL(jpaTableName));
      
      int i = 1;
      for(Object param : wQuery.getDynamicParameters())
      {
        dynamicQuery.setParameter( i++, param );
      }
      
      if(wQuery.getMaxMatches() > 0)
        dynamicQuery.setMaxResults((int)wQuery.getMaxMatches());
      
      return dynamicQuery.getResultList();  
    
  }

  
  public void persistProfile(ProfileObject profileObject)
  {
    EntityManager em = null;
    
    if(checkUniqueFields(profileObject))
    {
      em = getEntityManager(profileObject.getProfileTable().getProfileSpecificationComponent().getProfileSpecificationID());
      em.persist(profileObject.getProfileEntity()); 
    }
    else
    {
    	// FIXME: We need to throw this PVException!
      //throw new ProfileVerificationException("Failed to persist profile due to uniqueness constraint.");
      throw new SLEEException("Failed to persist profile due to uniqueness constraint.");
    }
  }
  
  public ProfileEntity retrieveProfile(ProfileTableImpl profileTable, String profileName)
  {

	  if (logger.isDebugEnabled()) {
		  logger.debug("retrieveProfile( profileTableName = "+profileTable.getProfileTableName()+" , profileName = "+profileName+" )");  
	  }

	  EntityManager em = null;

	  if (profileName == null) {
		  profileName = DEFAULT_PROFILE_NAME;
	  }


	  ProfileSpecificationComponent psc = profileTable.getProfileSpecificationComponent();

	  em = getEntityManager(psc.getProfileSpecificationID());
	  Query q = em.createQuery("FROM " + psc.getProfileEntityClass().getName() + " WHERE tableName = ?1 AND safeProfileName = ?2").setParameter(1, profileTable.getProfileTableName()).setParameter(2, profileName);

	  List<?> resultList = q.getResultList();
	  if (resultList.size() > 0) {
		  if (logger.isDebugEnabled()) {
				logger.debug("ProfileEntity retrieved -> "+resultList.get(0));  
			  }
		  return (ProfileEntity) resultList.get(0);        
	  }
	  else {
		  return null;
      }
    
  }
  
  public void removeprofile(ProfileObject profileObject)
  {
    EntityManager em = null;
  
    
      ProfileSpecificationComponent psc = profileObject.getProfileTable().getProfileSpecificationComponent();

      em = getEntityManager(psc.getProfileSpecificationID());
      em.remove(profileObject.getProfileEntity());
    
  }

  private boolean checkUniqueFields(ProfileObject profileObject)
  {
    try
    {
      ArrayList<Object> attrValues = new ArrayList<Object>();
      ProfileSpecificationComponent psc = profileObject.getProfileTable().getProfileSpecificationComponent();
      
      String sqlQuery = "FROM " + psc.getProfileEntityClass().getName() + " WHERE tableName = ?1 AND safeProfileName <> ''";

      if(psc.isSlee11())
      {
        int i = 2;
        for(MCMPField cmpField : psc.getDescriptor().getProfileCMPInterface().getCmpFields())
        {
          if(cmpField.getUnique())
          {
            // Get field name and capitalize it
            String fieldName = cmpField.getCmpFieldName();
            fieldName = fieldName.replaceFirst( "" + fieldName.charAt(0), "" + Character.toUpperCase(fieldName.charAt(0)) );

            // Invoke getter for obtaining value
            Object value = profileObject.getProfileConcrete().getClass().getMethod("get" + fieldName).invoke(profileObject.getProfileConcrete());
            attrValues.add(value);

            // Compose the SQL Query with new field name
            sqlQuery += (i == 2 ? " AND ( C" : " OR C") + fieldName + " = ?" + i++;
          }
        }      
      }
      else
      {
        int i = 2;
        for(MProfileIndex indexedAttribute : psc.getDescriptor().getIndexedAttributes())
        {
          if(indexedAttribute.getUnique())
          {
            // Get field name and capitalize it
            String fieldName = indexedAttribute.getName();
            fieldName = fieldName.replaceFirst( "" + fieldName.charAt(0), "" + Character.toUpperCase(fieldName.charAt(0)) );
            
            // Invoke getter for obtaining value
            Object value = profileObject.getProfileConcrete().getClass().getMethod("get" + fieldName).invoke(profileObject.getProfileConcrete());
            attrValues.add(value);

            // Compose the SQL Query with new field name
            sqlQuery += (i == 2 ? " AND ( C" : " OR C") + fieldName + " = ?" + i++;
          }
        }
      }

      sqlQuery += ")";
      
      if(attrValues.size() > 0)
      {
        EntityManager em = getEntityManager(profileObject.getProfileTable().getProfileSpecificationComponent().getProfileSpecificationID());

        Query q = em.createQuery(sqlQuery);
        q.setParameter( 1, profileObject.getProfileTable().getProfileTableName() );
        
        int i = 2;
        for(Object attrValue : attrValues)
        {
          q.setParameter( i++, attrValue );
        }
  
        if(q.getResultList().size() > 0)
          return false;
      }
    }
    catch (Exception e) {
      logger.error( "Unable to verify unique constraints.", e );
    }

    return true;
  }

}
