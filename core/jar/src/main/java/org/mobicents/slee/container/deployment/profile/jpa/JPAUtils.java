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

import javax.management.ObjectName;
import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.slee.ComponentID;
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
import org.mobicents.slee.container.profile.ProfileObject;
import org.mobicents.slee.container.profile.ProfileTableConcrete;

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
public class JPAUtils {

  private static Logger logger = Logger.getLogger(JPAUtils.class);

  public static final JPAUtils INSTANCE = new JPAUtils();

  private static final String DEFAULT_PROFILE_NAME = "";
  
  private HashMap<ProfileSpecificationID, String> jpaTableToClassMap = new HashMap<ProfileSpecificationID, String>();

  private JPAUtils() { }
  
  public EntityManager getEntityManager(ComponentID componentId)
  {
    return psidEMFs.get( String.valueOf(componentId.hashCode()) ).createEntityManager();
  }

  private String getJPATable(ProfileSpecificationID psid)
  {
    //return "`" + psid.getName() + "#" + psid.getVendor() + "#" + psid.getVersion() + "`";
    return psid.toString();//jpaTableToClassMap.get( psid );
  }

  public boolean registerProfileSpecification(ProfileSpecificationID psid)
  {
    return jpaTableToClassMap.put(psid, psid.toString()) == null;
  }

  // Profile Table Operations
  public Object create(ProfileTableConcrete ptc, String profileName)
  {
    return null;
  }

  public Object find(String profileTableName, String profileName) throws SLEEException, UnrecognizedProfileTableNameException
  {
    ProfileTableConcrete ptc = SleeContainer.lookupFromJndi().getSleeProfileTableManager().getProfileTable(profileTableName);
    String jpaTableName = ptc.getProfileSpecificationComponent().getProfileCmpConcreteClass().getName();

    Query createProfileQuery = getEntityManager(ptc.getProfileSpecificationComponent().getComponentID()).createQuery("FROM " + jpaTableName + " WHERE tableName = ?1 AND profileName = ?2").setParameter(1, profileTableName).setParameter( 2, profileName );

    try {
      return createProfileQuery.getSingleResult();
    }
    catch (NoResultException e) {
    }
    
    return null;
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

  public boolean remove(String profileTableName, String profileName)
  {
    // TODO: complete.
    return false;
  }

  // Extra ...
  public Collection<ProfileID> getProfilesIDs(ProfileTableConcrete ptc)
  {
    Collection<ProfileID> result = new ArrayList<ProfileID>();

    try
    {
      ProfileSpecificationID psid = ptc.getProfileSpecificationComponent().getProfileSpecificationID();
      String jpaTableName = getJPATable(psid);
      String profileTableName = ptc.getProfileTableName();

      Query createProfileQuery = getEntityManager(psid).createQuery( "FROM " + jpaTableName + " WHERE tableName = ?1").setParameter(1, profileTableName);

      for(Object o : createProfileQuery.getResultList())
      {
        String profileName = o.getClass().getField("profileName").get(o).toString();
        result.add( new ProfileID(profileTableName, profileName) );
      }
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }

    return result;
  }

  public boolean find(ProfileTableConcrete ptc, String profileName) throws NullPointerException, TransactionRequiredLocalException, SLEEException
  {
    String jpaTableName = ptc.getProfileSpecificationComponent().getProfileCmpConcreteClass().getName();
    String profileTableName = ptc.getProfileTableName();

    Query createProfileQuery = getEntityManager(ptc.getProfileSpecificationComponent().getComponentID()).createQuery("FROM " + jpaTableName + " WHERE tableName = ?1 AND profileName = ?2").setParameter(1, profileTableName).setParameter( 2, profileName );

    try
    {
      createProfileQuery.getSingleResult();
    }
    catch (NoResultException e) {
      return false;
    }
    
    return true;
  }

  public List findAll(ProfileTableConcrete ptc) throws NullPointerException, TransactionRequiredLocalException, SLEEException
  {
    ArrayList<String> profileNames = new ArrayList<String>();

    try
    {
      String jpaTableName = ptc.getProfileSpecificationComponent().getProfileCmpConcreteClass().getName();
      String profileTableName = ptc.getProfileTableName();

      Query createProfileQuery = getEntityManager(ptc.getProfileSpecificationComponent().getComponentID()).createQuery("FROM " + jpaTableName + " WHERE tableName = ?1").setParameter(1, profileTableName);

      List results = createProfileQuery.getResultList();

      for(Object result : results)
      {
        profileNames.add( result.getClass().getField("profileName").get(result).toString() );
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }

    return profileNames;
    //new ProfileLocalObjectConcreteImpl(ptc.getProfileTableName(), ptc.getProfileSpecificationComponent().getProfileSpecificationID(), profileName, null, false);
  }

  // ProfileProvisioningMBean Operations

  public void createProfileTable(ProfileSpecificationID id, String newProfileTableName)
  {

  }

  public void removeProfileTable(String profileTableName)
  {

  }

  public void renameProfileTable(String oldProfileTableName, String newProfileTableName)
  {

  }

  public ProfileSpecificationID getProfileSpecification(String profileTableName)
  {
    // TODO: complete.
    return null;
  }

  public ObjectName getDefaultProfile(String profileTableName)
  {
    // TODO: complete.
    return null;
  }

  public ObjectName createProfile(String profileTableName, String newProfileName)
  {
    // TODO: complete.
    return null;
  }

  public void removeProfile(String profileTableName, String profileName)
  {

  }

  public ObjectName getProfile(String profileTableName, String profileName)
  {
    // TODO: complete.
    return null;
  }

  public Collection getProfileTables()
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
    try
    {
      ProfileConcrete profileConcrete = profileObject.getProfileConcrete();
      String profileName = profileObject.getProfileName();
      if (profileName == null) {
        profileName = DEFAULT_PROFILE_NAME;
      }
      profileObject.getProfileConcrete().getClass().getMethod( "setProfileName", String.class ).invoke( profileConcrete, profileName);
      profileObject.getProfileConcrete().getClass().getMethod( "setTableName", String.class ).invoke( profileConcrete, profileObject.getProfileTableConcrete().getProfileTableName() );
    }
    catch (Exception e) {
      // ignore, no problem.. we hope.
    }
    
    getEntityManager(profileObject.getProfileSpecificationComponent().getComponentID()).persist(profileObject.getProfileConcrete());
  }
  
  public ProfileConcrete retrieveProfile(ProfileObject profileObject)
  {
    String profileName = profileObject.getProfileName();
    if (profileName == null) {
      profileName = DEFAULT_PROFILE_NAME;
    }
    String profileTable = profileObject.getProfileTableConcrete().getProfileTableName();
    
    try
    {
      ProfileConcrete profileConcrete = profileObject.getProfileConcrete();

      profileObject.getProfileConcrete().getClass().getMethod( "setProfileName", String.class ).invoke( profileConcrete, profileName);
      profileObject.getProfileConcrete().getClass().getMethod( "setTableName", String.class ).invoke( profileConcrete, profileTable );
    }
    catch (Exception e) {
      // ignore, no problem.. we hope.
    }

    ProfileSpecificationComponent psc = profileObject.getProfileSpecificationComponent();
    
    Query q = getEntityManager(psc.getComponentID()).createQuery("FROM " + psc.getProfileCmpConcreteClass().getName() + " WHERE tableName = ?1 AND profileName = ?2").setParameter(1, profileTable).setParameter(2, profileName);
    
    try{
      return (ProfileConcrete) q.getSingleResult();
    }
    catch (Exception e) {
      logger.error("Failure retrieving profile.", e);
    }
    
    return null;
  }

  
  public EntityManagerFactory createPersistenceUnit(ProfileSpecificationComponent profileComponent)
  {
    try {
      
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
      
      for(String className : pumd.getClasses())
      {
        ClassLoader newClassLoader = profileComponent.getClassLoader();

        Thread.currentThread().setContextClassLoader( newClassLoader );

        Thread.currentThread().getContextClassLoader().loadClass( className );
      }
      
      PersistenceUnitInfoImpl pi = new PersistenceUnitInfoImpl(pumd, properties, Thread.currentThread().getContextClassLoader(), ClassLoader.getSystemResource( "." ), new ArrayList<URL>(), new InitialContext());
      
      Transaction tx = null;
      EntityManagerFactory actualFactory = null;
      
      try {
       tx = SleeContainer.lookupFromJndi().getTransactionManager().suspend();
       actualFactory = hp.createContainerEntityManagerFactory(pi, null);
      }
      catch (Exception e) {
        e.printStackTrace();
      }
      catch (Throwable t) {
        t.printStackTrace();
      }
      finally {
        if(tx!=null) SleeContainer.lookupFromJndi().getTransactionManager().resume(tx);
      }

      psidEMFs.put( String.valueOf(profileComponent.getComponentID().hashCode()), actualFactory );
      
      return actualFactory;
  }
    catch (Exception e) {
      e.printStackTrace();
  }
    return null;
  }
  
  private HashMap<String, EntityManagerFactory> psidEMFs = new HashMap();
}
