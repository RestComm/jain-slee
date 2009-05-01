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
import javax.slee.profile.query.QueryExpression;
import javax.transaction.Transaction;
import javax.xml.parsers.DocumentBuilderFactory;

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

  public final static JPAUtils INSTANCE = new JPAUtils();
  
  private JPAUtils() {
//    Properties properties = new Properties();
//
//    properties.setProperty(Environment.DRIVER, "org.hsqldb.jdbcDriver");
//    properties.setProperty(Environment.URL, "jdbc:mysql://127.0.0.1:3306/mobicents");
//    properties.setProperty(Environment.USER, "sa");
//    properties.setProperty(Environment.PASS, "");
//    properties.setProperty(Environment.AUTOCOMMIT, "false");
//    
//    properties.setProperty(Environment.DATASOURCE, "java:/DefaultDS");
//    properties.setProperty(Environment.DIALECT, "org.hibernate.dialect.HSQLDialect");
//    properties.setProperty(Environment.SHOW_SQL, "true");
//    properties.setProperty(Environment.FLUSH_BEFORE_COMPLETION, "false");
//    properties.setProperty(Environment.HBM2DDL_AUTO, "create");
//    properties.setProperty(Environment.USE_REFLECTION_OPTIMIZER, "true");
//    
//    config = (AnnotationConfiguration)(new AnnotationConfiguration().setProperties(properties));
//
//    emf = Persistence.createEntityManagerFactory( "JSLEEProfiles" );
  }
  
  private HashMap<ProfileSpecificationID, String> jpaTableToClassMap = new HashMap<ProfileSpecificationID, String>();

  public EntityManager getEntityManager(ComponentID componentId)
  {
    return psidEMFs.get( String.valueOf(componentId.hashCode()) ).createEntityManager();
  }

  public void addAnnotattedClass(String className)
  {
//    if(config.getClassMapping( className ) == null)
//    {
//      try
//      {
//        config.addAnnotatedClass(Thread.currentThread().getContextClassLoader().loadClass( className ));
//        emf = config.();
//      }
//      catch ( MappingException e )
//      {
//        // TODO Auto-generated catch block
//        e.printStackTrace();
//      }
//      catch ( ClassNotFoundException e )
//      {
//        // TODO Auto-generated catch block
//        e.printStackTrace();
//      }
//    }

//    try {
//      // Create a builder factory
//      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//      factory.setValidating(false);
//
//      // Create the builder and parse the file
//      InputStream is = SleeContainer.class.getClassLoader().getResourceAsStream("../META-INF/persistence.xml");
//      Document doc = factory.newDocumentBuilder().parse(is);
//
//      NodeList classes = doc.getElementsByTagName("class");
//      for(int i = 0; i < classes.getLength(); i++)
//      {
//        if( classes.item(i).getTextContent().equals(className) )
//          return;
//      }
//      
//      Element classElement = doc.createElement( "class" );
//      classElement.setTextContent( className );
//
//      Node puElement = doc.getElementsByTagName( "persistence-unit" ).item(0);
//
//      if(puElement.hasChildNodes()) {
//        puElement.insertBefore(classElement, puElement.getLastChild());
//      }
//      else {
//        puElement.appendChild(classElement);
//      }
//
//      javax.xml.transform.TransformerFactory tfactory = TransformerFactory.newInstance();
//      javax.xml.transform.Transformer xform = tfactory.newTransformer();
//      javax.xml.transform.Source src = new DOMSource(doc);
//      java.io.StringWriter writer = new StringWriter();
//      Result results = new StreamResult(writer);
//      xform.transform(src, results);
//      System.out.println(writer.toString());
//
//
//      Transformer transformer = TransformerFactory.newInstance().newTransformer();
//      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//
//      //initialize StreamResult with File object to save to le
//      StreamResult result = new StreamResult(new StringWriter());
//      DOMSource source = new DOMSource(doc);
//      transformer.transform(source, result);
//
//      // Write to file
//      File file = new File(SleeContainer.class.getClassLoader().getResource("../META-INF/persistence.xml").getPath());
//      Result resultx = new StreamResult(file);
//
//      // Write the DOM document to the file
//      Transformer xformer = TransformerFactory.newInstance().newTransformer();
//      xformer.transform(source, resultx);
//      
//      emf = Persistence.createEntityManagerFactory( "JSLEEProfiles" );
//    }
//    catch (Exception e) {
//      e.printStackTrace();
//    }
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

  public Object find(String profileTableName, String profileName)
  {
//    String jpaTableName = ptc.getProfileSpecificationComponent().getProfileCmpConcreteClass().getName();
//
//    Query createProfileQuery = getEntityManager(ptc.getProfileSpecificationComponent().getComponentID()).createQuery("FROM " + jpaTableName + " WHERE tableName = ?1 AND profileName = ?2").setParameter(1, profileTableName).setParameter( 2, profileName );
//
//    try {
//      return createProfileQuery.getSingleResult();
//    }
//    catch (NoResultException e) {
//    }
    
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
      profileObject.getProfileConcrete().getClass().getMethod( "setProfileName", String.class ).invoke( profileConcrete, profileObject.getProfileName() );
      profileObject.getProfileConcrete().getClass().getMethod( "setTableName", String.class ).invoke( profileConcrete, profileObject.getProfileTableConcrete().getProfileTableName() );
    }
    catch (Exception e) {
      // ignore, no problem.. we hope.
    }
    
    getEntityManager(profileObject.getProfileSpecificationComponent().getComponentID()).persist(profileObject.getProfileConcrete());
  }
  
  public EntityManagerFactory createPersistenceUnit(ProfileSpecificationComponent profileComponent)
  {
    
    //properties.setProperty(Environment.TRANSACTION_MANAGER_STRATEGY, "org.hibernate.transaction.JBossTransactionManagerLookup");
    //properties.setProperty(Environment.CACHE_PROVIDER, "org.hibernate.cache.HashtableCacheProvider");
    //properties.setProperty("hibernate.jndi.java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
    //properties.setProperty("hibernate.jndi.java.naming.factory.url.pkgs", "org.jboss.naming:org.jnp.interfaces");
    //properties.setProperty(Environment.USE_REFLECTION_OPTIMIZER, "false");
    //properties.setProperty(Environment.BYTECODE_PROVIDER, "javassist");
    
    
//    properties.setProperty(Environment.DRIVER, "org.hsqldb.jdbcDriver");
//    //properties.setProperty(Environment.URL, "jdbc:mysql://127.0.0.1:3306/mobicents");
//    //properties.setProperty(Environment.USER, "sa");
//    //properties.setProperty(Environment.PASS, "");
//    //properties.setProperty(Environment.AUTOCOMMIT, "false");
//    properties.setProperty(Environment.DIALECT, "org.hibernate.dialect.HSQLDialect");
//    properties.setProperty(Environment.SHOW_SQL, "true");
    //properties.setProperty(Environment.FLUSH_BEFORE_COMPLETION, "false");
    //properties.setProperty(Environment.HBM2DDL_AUTO, "create");
    
    try {
      // Create a builder factory
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setValidating(false);

      //Persistence.createEntityManagerFactory( null, jpaTableToClassMap )
      
      // Create the builder and parse the file
      //Document doc = factory.newDocumentBuilder().parse(this.getClass().getClassLoader().getResourceAsStream( "META-INF/was-persistence.xml" ));
      
//      AnnotationConfiguration config = (AnnotationConfiguration)(new AnnotationConfiguration().addProperties( properties ));
//
//      return config.buildSessionFactory();
      
      // PersistenceUnitMetaData@1d22f35{provider=org.hibernate.ejb.HibernatePersistence, 
      // jta-data-source=java:/DefaultDS, non-jta-data-source=null, non-jta-data-source=null, 
      // classes=[org.mobicents.slee.container.deployment.profile.jpa.JPAProfileId], excludeUnlistedClasses=false, 
      // properties={hibernate.hbm2ddl.auto=create-drop, hibernate.dialect=org.hibernate.dialect.HSQLDialect}, 
      // name=JSLEEProfiles, transactionType=null}
      
      
      HibernatePersistence hp = new HibernatePersistence();
      PersistenceUnitMetaData pumd = new PersistenceUnitMetaData();
      
      pumd.setProvider("org.hibernate.ejb.HibernatePersistence");
      pumd.setJtaDataSource( "java:/DefaultDS" );
      pumd.setExcludeUnlistedClasses( false );
      
      Map pumdProps = new HashMap();
      pumdProps.put( "hibernate.hbm2ddl.auto","create-drop" );
      pumdProps.put( "hibernate.dialect","org.hibernate.dialect.HSQLDialect" );
      
      pumd.setProperties( pumdProps );
      pumd.setName( "JSLEEProfiles" + profileComponent.getComponentID().hashCode() );
      
      Set classes = new HashSet<String>();
      classes.add( profileComponent.getProfileCmpInterfaceClass().getName() + "Impl" );
      
      pumd.setClasses(classes);
      
      Properties properties = new Properties();

      properties.setProperty(Environment.DATASOURCE, "java:/DefaultDS");
      properties.setProperty( Environment.TRANSACTION_STRATEGY, "org.hibernate.ejb.transaction.JoinableCMTTransactionFactory" );
      properties.setProperty( "hibernate.connection.provider_class", "org.hibernate.ejb.connection.InjectedDataSourceConnectionProvider" );
      properties.setProperty("hibernate.jndi.java.naming.factory.url.pkgs","org.jboss.naming:org.jnp.interfaces");
      properties.setProperty("hibernate.cache.provider_class","org.hibernate.cache.HashtableCacheProvider");
      properties.setProperty("hibernate.transaction.manager_lookup_class","org.hibernate.transaction.JBossTransactionManagerLookup");
      properties.setProperty("hibernate.jndi.java.naming.factory.initial","org.jnp.interfaces.NamingContextFactory");
      properties.setProperty("hibernate.dialect","org.hibernate.dialect.HSQLDialect");
      properties.setProperty("hibernate.jacc.ctx.id","persistence.xml");
      properties.setProperty("hibernate.cache.region_prefix","persistence.unit:unitName=#" + pumd.getName() );
      properties.setProperty("hibernate.session_factory_name","persistence.unit:unitName=#" + pumd.getName() );
      properties.setProperty("hibernate.hbm2ddl.auto","create-drop");
      properties.setProperty("hibernate.bytecode.use_reflection_optimizer","false");
      properties.setProperty("hibernate.bytecode.provider","javassist");
      
      for(String className : pumd.getClasses())
      {
        ClassLoader newClassLoader = profileComponent.getClassLoader();
        //ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
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
