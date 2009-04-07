package org.mobicents.slee.container.deployment.profile.jpa;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.management.ObjectName;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.slee.SLEEException;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.profile.ProfileID;
import javax.slee.profile.ProfileSpecificationID;
import javax.slee.profile.query.QueryExpression;

import org.hibernate.cfg.Environment;
import org.hibernate.ejb.Ejb3Configuration;
import org.mobicents.slee.container.component.ProfileSpecificationComponent;
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
  
  private static Ejb3Configuration config;
  private static EntityManagerFactory emf;
  
  static {
    Properties properties = new Properties();
    properties.setProperty(Environment.DRIVER, "com.mysql.jdbc.Driver");
    properties.setProperty(Environment.URL, "jdbc:mysql://127.0.0.1:3306/mobicents");
    properties.setProperty(Environment.USER, "root");
    properties.setProperty(Environment.PASS, "mysql");
    properties.setProperty(Environment.DIALECT, "org.hibernate.dialect.MySQLInnoDBDialect");
    properties.setProperty(Environment.SHOW_SQL, "true");
    properties.setProperty(Environment.FLUSH_BEFORE_COMPLETION, "false");
    properties.setProperty(Environment.HBM2DDL_AUTO, "create");
    
    config = new Ejb3Configuration().addProperties(properties);
    emf = config.buildEntityManagerFactory();
  }
  
  private static HashMap<ProfileSpecificationID, String> jpaTableToClassMap = new HashMap<ProfileSpecificationID, String>();
  
  public void addProfileSpecificationToMap(ProfileSpecificationComponent psc)
  {
    jpaTableToClassMap.put(psc.getProfileSpecificationID(), psc.getDescriptor().getProfileCMPInterface().getProfileCmpInterfaceName() + "Impl");
  }
  
  public static EntityManager getEntityManager()
  {
    return emf.createEntityManager();
  }
  
  public static void addAnnotattedClass(Class clazz)
  {
    config.addAnnotatedClass(clazz);
    emf = config.buildEntityManagerFactory(); //Create the entity manager factory
  }
  
  private static String getJPATable(ProfileSpecificationID psid)
  {
    //return "`" + psid.getName() + "#" + psid.getVendor() + "#" + psid.getVersion() + "`";
    return jpaTableToClassMap.get( psid );
  }
  
  // Profile Table Operations
  public static Object create(ProfileTableConcrete ptc, String profileName)
  {
    return null;
  }

  public static Object find(String profileTableName, String profileName)
  {
    // TODO: complete.
    return null;
  }

  public static Collection<Object> findAll(String profileTableName)
  {
    // TODO: complete.
    return null;
  }

  public static Object findProfileByAttribute(String profileTableName, String attributeName, Object attributeValue)
  {
    // TODO: complete.
    return null;
  }
  
  public static Collection<Object> findProfilesByAttribute(String profileTableName, String attributeName, Object attributeValue)
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
  public static Collection<ProfileID> getProfilesIDs(ProfileTableConcrete ptc)
  {
    Collection<ProfileID> result = new ArrayList<ProfileID>();
    
    try
    {
      ProfileSpecificationID psid = ptc.getProfileSpecificationComponent().getProfileSpecificationID();
      String jpaTableName = getJPATable(psid);
      String profileTableName = ptc.getProfileTableName();
  
      Query createProfileQuery = getEntityManager().createQuery( "FROM " + jpaTableName + " WHERE tableName = ?1").setParameter(1, profileTableName);
      
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

  public static boolean find(ProfileTableConcrete ptc, String profileName) throws NullPointerException, TransactionRequiredLocalException, SLEEException
  {
    ProfileSpecificationID psid = ptc.getProfileSpecificationComponent().getProfileSpecificationID();
    String jpaTableName = getJPATable(psid);
    String profileTableName = ptc.getProfileTableName();

    Query createProfileQuery = getEntityManager().createQuery("FROM " + jpaTableName + " WHERE tableName = ?1 AND profileName = ?2").setParameter(1, profileTableName).setParameter(2, profileName);

    List results = createProfileQuery.getResultList();
    
    return results.size() > 0;
    //new ProfileLocalObjectConcreteImpl(ptc.getProfileTableName(), ptc.getProfileSpecificationComponent().getProfileSpecificationID(), profileName, null, false);
  }

  public static List findAll(ProfileTableConcrete ptc) throws NullPointerException, TransactionRequiredLocalException, SLEEException
  {
    ArrayList<String> profileNames = new ArrayList<String>();
    
    try
    {
      ProfileSpecificationID psid = ptc.getProfileSpecificationComponent().getProfileSpecificationID();
      String jpaTableName = getJPATable(psid);
      String profileTableName = ptc.getProfileTableName();
  
      Query createProfileQuery = getEntityManager().createQuery("FROM " + jpaTableName + " WHERE tableName = ?1").setParameter(1, profileTableName);
  
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

}
