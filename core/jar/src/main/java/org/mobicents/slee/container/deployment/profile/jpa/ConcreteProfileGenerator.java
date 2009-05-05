package org.mobicents.slee.container.deployment.profile.jpa;

import java.io.File;
import java.io.InputStream;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtPrimitiveType;

import javax.persistence.EntityManager;
import javax.slee.SLEEException;
import javax.slee.profile.Profile;
import javax.slee.profile.ProfileManagement;
import javax.slee.profile.ProfileSpecificationID;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.component.ProfileSpecificationComponent;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ProfileSpecificationDescriptorImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.MCMPField;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.MProfileCMPInterface;
import org.mobicents.slee.container.deployment.ClassUtils;
import org.mobicents.slee.container.deployment.ConcreteClassGeneratorUtils;
import org.mobicents.slee.container.deployment.profile.SleeProfileClassCodeGenerator;
import org.mobicents.slee.container.profile.ProfileConcrete;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * 
 * ConcreteProfileJPAGenerator.java
 *
 * <br>Project:  mobicents
 * <br>11:16:57 AM Mar 23, 2009 
 * <br>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
@SuppressWarnings("deprecation")
public class ConcreteProfileGenerator {

  private static final Logger logger = Logger.getLogger(ConcreteProfileGenerator.class);

  private static final String PROFILE_TABLE_IDENTIFIER = "tableName";
  private static final String PROFILE_IDENTIFIER = "profileName";

  private ProfileSpecificationComponent profileComponent;
  private ProfileSpecificationDescriptorImpl profileDescriptor;

  private int profileCombination = -1;
  
  private String deployDir;

  public ConcreteProfileGenerator(ProfileSpecificationComponent profileComponent)
  {
    this.profileComponent = profileComponent;
    this.profileDescriptor = profileComponent.getDescriptor();

    this.profileCombination = SleeProfileClassCodeGenerator.checkCombination(profileComponent);
    logger.info( "Profile combination for " + profileComponent.getProfileSpecificationID() + " = " + this.profileCombination );

    this.deployDir = profileComponent.getDeploymentDir().getAbsolutePath();

    ClassGeneratorUtils.setClassPool( this.profileComponent.getClassPool().getClassPool() );
  }

  public Class generateConcreteProfile()
  {
    Class clazz = null;
    
    try
    {
      /*
       * 10.12  Profile concrete class 
       * A Profile concrete class is implemented by the SLEE when a Profile Specification is deployed. The Profile
       * concrete class extends the Profile abstract class and implements the Profile CMP methods.
       * 
       * The following rules apply to the Profile concrete class implemented by the SLEE:
       * - If a Profile abstract class is defined, then the SLEE implemented Profile concrete class extends the
       *   Profile abstract class and implements the Profile CMP methods.
       *   
       * - If a Profile abstract class is not defined, then the SLEE implemented Profile concrete class
       *   provides an implementation of the Profile CMP interface.
       */

      MProfileCMPInterface cmpInterface = this.profileDescriptor.getProfileCMPInterface();

      String concreteClassName = ConcreteClassGeneratorUtils.PROFILE_CONCRETE_CLASS_NAME_PREFIX + cmpInterface.getProfileCmpInterfaceName() + ConcreteClassGeneratorUtils.PROFILE_CONCRETE_CLASS_NAME_SUFFIX;

      // Create the Impl class
      CtClass profileConcreteClass = ClassGeneratorUtils.createClass(concreteClassName, new String[]{cmpInterface.getProfileCmpInterfaceName(), ProfileConcrete.class.getName(), Cloneable.class.getName()});

      // If this is combination 3 or 4, the the concrete class extends the Concrete Profile Management Abstract Class
      if( profileCombination >= 3 )
      {
        if(profileDescriptor.getProfileAbstractClass() != null)
        {
          ClassGeneratorUtils.createInheritanceLink( profileConcreteClass, profileDescriptor.getProfileAbstractClass().getProfileAbstractClassName() );
        }
        
        if(profileDescriptor.getProfileManagementInterface() != null)
        {
          ClassGeneratorUtils.createInterfaceLinks( profileConcreteClass, new String[]{profileDescriptor.getProfileManagementInterface().getProfileManagementInterfaceName()} );
        }
      }
      
      // Annotate class with @Entity
      ClassGeneratorUtils.addAnnotation( "javax.persistence.Entity", new LinkedHashMap<String, Object>(), profileConcreteClass );

      // Add the table name to map it to ProfileSpecification ID
      LinkedHashMap<String, Object> tableMVs = new LinkedHashMap<String, Object>();

      ProfileSpecificationID psid = profileDescriptor.getProfileSpecificationID();
      tableMVs.put( "name", "JSLEEProfile" + Math.abs((psid.getName() + "#" + psid.getVendor() + "#" + psid.getVersion()).hashCode()) + "" );

      ClassGeneratorUtils.addAnnotation( "javax.persistence.Table", tableMVs, profileConcreteClass );

      // Add profileName and profileTableName fields
      generateProfileIdentifiers(profileConcreteClass);
      
      CtField fSleeTransactionManager = ClassGeneratorUtils.addField( ClassGeneratorUtils.getClass("javax.slee.transaction.SleeTransactionManager"), "sleeTransactionManager", profileConcreteClass, Modifier.PRIVATE, "org.mobicents.slee.container.SleeContainer.lookupFromJndi().getTransactionManager()" );
      ClassGeneratorUtils.addAnnotation( "javax.persistence.Transient", null, fSleeTransactionManager );

      // We also need getter/setter for profileObject and profileTableConcrete
      CtField fProfileObject = ClassGeneratorUtils.addField( ClassGeneratorUtils.getClass("org.mobicents.slee.container.profile.ProfileObject"), "profileObject", profileConcreteClass );
      ClassGeneratorUtils.addAnnotation( "javax.persistence.Transient", null, fProfileObject );
      ClassGeneratorUtils.generateGetterAndSetter( fProfileObject, null );
      
      CtField fProfileTableConcrete = ClassGeneratorUtils.addField( ClassGeneratorUtils.getClass("org.mobicents.slee.container.profile.ProfileTableConcrete"), "profileTableConcrete", profileConcreteClass );
      ClassGeneratorUtils.addAnnotation( "javax.persistence.Transient", null, fProfileTableConcrete );
      ClassGeneratorUtils.generateGetterAndSetter( fProfileTableConcrete, null );
      
      // CMP fields getters and setters
      generateCMPFieldsWithGettersAndSetters(profileConcreteClass);
      
//      CtField fProfileDirty = ClassGeneratorUtils.addField( CtClass.booleanType, "profileDirty", profileConcreteClass, Modifier.PRIVATE, "false" );
//      ClassGeneratorUtils.addAnnotation( "javax.persistence.Transient", null, fProfileDirty );
//      ClassGeneratorUtils.generateGetterAndSetter( fProfileDirty, null );

      generateConstructors(profileConcreteClass);
      
      // Profile Management methods for JAIN SLEE 1.1
      Map<String, CtMethod> profileManagementMethods = ClassUtils.getInterfaceMethodsFromInterface(ClassGeneratorUtils.getClass(Profile.class.getName()));
      
      // Profile Management methods for JAIN SLEE 1.0
      profileManagementMethods.putAll(ClassUtils.getInterfaceMethodsFromInterface(ClassGeneratorUtils.getClass(ProfileManagement.class.getName())));

      // Check for a Profile Management Interface
      Class profileManagementInterface = this.profileComponent.getProfileManagementInterfaceClass();
      
      if (profileManagementInterface != null) {
        profileManagementMethods.putAll(org.mobicents.slee.container.deployment.ClassUtils.getInterfaceMethodsFromInterface(ClassGeneratorUtils.getClass(profileManagementInterface.getName())));
      }
      
//      if (profileManagementAbstractClass != null) {
//        // We do get concrete methods, so we can intercept calls before delegating them
//        profileManagementMethods.putAll(ClassUtils.getConcreteMethodsFromClass(profileManagementAbstractClass));
//        
//        Map<String, CtMethod> abstractMethods = ClassUtils.getSuperClassesAbstractMethodsFromClass(profileManagementAbstractClass);
//        abstractMethods.putAll(ClassUtils.getAbstractMethodsFromClass(profileManagementAbstractClass));
//        // First we generate Usage methods and remove them from management
//        // methods map
//
//        createDefaultUsageParameterGetter(abstractMethods, cmpProfileConcreteClass);
//        createNamedUsageParameterGetter(abstractMethods, cmpProfileConcreteClass);
//
//      }

      Map<String, CtMethod> cmpInterfaceMethods = ClassUtils.getInterfaceMethodsFromInterface(ClassGeneratorUtils.getClass(this.profileComponent.getProfileCmpInterfaceClass().getName()));
      generateBusinessMethods(profileConcreteClass, profileManagementMethods, cmpInterfaceMethods);


      profileConcreteClass.getClassFile().setVersionToJava5();
      
      logger.info( "Writing PROFILE CONCRETE CLASS to: " + deployDir );
      
      profileConcreteClass.writeFile( deployDir );
      
      clazz = Thread.currentThread().getContextClassLoader().loadClass(profileConcreteClass.getName());
      
      profileConcreteClass.defrost();
      
    }
    catch ( Exception e ) {
      throw new SLEEException(e.getMessage(),e);
    }
    
    return clazz;
  }

  private void generateConstructors(CtClass profileConcreteClass)
  {
    ClassGeneratorUtils.generateDefaultConstructor(profileConcreteClass);
  }
  
  private void generateBusinessMethods(CtClass profileConcreteClass, Map<String, CtMethod> methods, Map<String, CtMethod> cmpInterfaceMethods)
  {
    //boolean useInterceptor = true;
    Class abstractClass = this.profileComponent.getProfileAbstractClass();

    Iterator<Map.Entry<String, CtMethod>> mm = methods.entrySet().iterator();

    while (mm.hasNext())
    {
      String interceptor = ClassGeneratorUtils.MANAGEMENT_HANDLER;
      
      Map.Entry<String, CtMethod> entry = mm.next();
      
      CtMethod method = entry.getValue();
      
      if(cmpInterfaceMethods.containsKey(entry.getKey()))
      {
        // This was already implemented by generateCMP...
        continue;
      }
      if (entry.getKey().contains("commitChanges"))
      {
        interceptor = ClassGeneratorUtils.CMP_HANDLER;
      }
      else if(abstractClass != null)
      {
        try
        {
          int i = 0;
          Class[] pTypes = new Class[method.getParameterTypes().length];
        
          for(CtClass pType : method.getParameterTypes())
          {
            if(pType.isPrimitive())
              pTypes[i++] = ((Class)Class.forName( ((CtPrimitiveType)pType).getWrapperName() ).getField( "TYPE" ).get( null ));
            else
              pTypes[i++] = Class.forName(pType.getClassFile().getName());
          }
          
          Method m = abstractClass.getMethod( method.getName(), pTypes );
          interceptor = Modifier.isAbstract(m.getModifiers()) ? interceptor : "super";
        }
        catch ( Exception e ) {
          if (!(e instanceof NoSuchMethodException))
            throw new SLEEException("Problem with Business method generation: " + method.getName(), e);
            
          // else ignore... we are using default interceptor.
        }
      }
      
      try
      {
        ClassGeneratorUtils.generateDelegateMethod( profileConcreteClass, entry.getValue(), interceptor, false );
      }
      catch ( Exception e ) {
        throw new SLEEException(e.getMessage(),e);
      }
    }
//      useInterceptor = true;
//      
//      if(abstractClass != null)
//      {
//        try
//        {
//          int i = 0;
//          Class[] pTypes = new Class[method.getParameterTypes().length];
//        
//          for(CtClass ctClass : method.getParameterTypes())
//          {
//            pTypes[i++]  = ctClass.toClass();
//          }
//          
//          abstractClass.getMethod( method.getName(), pTypes );
//          useInterceptor = false;
//        }
//        catch ( Exception e ) {
//          // ignore... we are using interceptor.
//        }
  }
  
  private void generateProfileIdentifiers(CtClass profileConcreteClass) throws Exception
  {
    // Create the IdClass value
    LinkedHashMap<String, Object> idClassMVs = new LinkedHashMap<String, Object>();
    idClassMVs.put( "value", JPAProfileId.class );

    // Annotate class with @IdClass(org.mobicents.slee.container.deployment.profile.jpa.JPAProfileId)
    ClassGeneratorUtils.addAnnotation( "javax.persistence.IdClass", idClassMVs, profileConcreteClass );

    // Add Table Name Attribute
    CtField tableNameField = ClassGeneratorUtils.addField( ClassGeneratorUtils.getClass(String.class.getName()), PROFILE_TABLE_IDENTIFIER, profileConcreteClass, Modifier.PUBLIC);

    LinkedHashMap<String, Object> columnAttrs = new LinkedHashMap<String, Object>();
    columnAttrs.put( "nullable", true );
    columnAttrs.put( "unique", true );

    ClassGeneratorUtils.addAnnotation( "javax.persistence.Id", new LinkedHashMap<String, Object>(), tableNameField );
    ClassGeneratorUtils.addAnnotation( "javax.persistence.Column", columnAttrs, tableNameField );
    ClassGeneratorUtils.generateGetterAndSetter( tableNameField, null );

    // Add Profile Name Attribute
    CtField profileNameField = ClassGeneratorUtils.addField( ClassGeneratorUtils.getClass(String.class.getName()), PROFILE_IDENTIFIER, profileConcreteClass, Modifier.PUBLIC);

    ClassGeneratorUtils.addAnnotation( "javax.persistence.Id", new LinkedHashMap<String, Object>(), profileNameField );
    ClassGeneratorUtils.addAnnotation( "javax.persistence.Column", columnAttrs, profileNameField );

    ClassGeneratorUtils.generateGetterAndSetter( profileNameField, null );    
  }
  
  private void generateCMPFieldsWithGettersAndSetters(CtClass profileConcreteClass) throws Exception
  {
    // Get the CMP interface to generate the getters/setters
    MProfileCMPInterface cmpInterface = profileDescriptor.getProfileCMPInterface();
    
    CtClass cmpInterfaceClass = ClassGeneratorUtils.getClass( cmpInterface.getProfileCmpInterfaceName() );
    
    HashMap<String, CtClass> fieldNames = new HashMap<String, CtClass>(); 

    for(CtMethod method : cmpInterfaceClass.getDeclaredMethods())
    {
      if(method.getName().startsWith( "get" ))
      {
        String fieldName = method.getName().replaceFirst( "get", "" );

        if(!fieldNames.containsKey( fieldName ))
        {
          fieldNames.put( fieldName, method.getReturnType() );
          
          CtField genField = ClassGeneratorUtils.addField( method.getReturnType(), fieldName, profileConcreteClass );

          ClassGeneratorUtils.generateCMPHandlers( genField );
        }
      }
    }

    for(MCMPField cmpField : cmpInterface.getCmpFields())
    {
      if( cmpField.getUnique() )
      {
        CtField field = profileConcreteClass.getField( cmpField.getCmpFieldName() );

        LinkedHashMap<String, Object> mvs = new LinkedHashMap<String, Object>();
        mvs.put( "unique", true );

        ClassGeneratorUtils.addAnnotation( "javax.persistence.Column", mvs, field );
      }
    }
  }
  
  public void addClassToPersistenceXML(String className)
  {
    try {
      // Create a builder factory
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setValidating(false);

      // Create the builder and parse the file
      InputStream is = this.getClass().getClassLoader().getResourceAsStream( "META-INF/persistence.xml" );
      Document doc = factory.newDocumentBuilder().parse(is);

      Element classElement = doc.createElement( "class" );
      classElement.setTextContent( className );

      doc.getElementsByTagName( "persistence-unit" ).item(0).appendChild( classElement );

      javax.xml.transform.TransformerFactory tfactory = TransformerFactory.newInstance();
      javax.xml.transform.Transformer xform = tfactory.newTransformer();
      javax.xml.transform.Source src = new DOMSource(doc);
      java.io.StringWriter writer = new StringWriter();
      Result results = new StreamResult(writer);
      xform.transform(src, results);
      System.out.println(writer.toString());


      Transformer transformer = TransformerFactory.newInstance().newTransformer();
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");

      //initialize StreamResult with File object to save to file
      StreamResult result = new StreamResult(new StringWriter());
      DOMSource source = new DOMSource(doc);
      transformer.transform(source, result);

      // Write to file
      File file = new File(this.getClass().getClassLoader().getResource( "META-INF/persistence.xml" ).toURI());
      Result resultx = new StreamResult(file);

      // Write the DOM document to the file
      Transformer xformer = TransformerFactory.newInstance().newTransformer();
      xformer.transform(source, resultx);
    }
    catch (Exception e) {
    	throw new SLEEException(e.getMessage(),e);
    }
  }

  public void persist(Object[] objects, EntityManager em)
  {  
    em.getTransaction().begin();

    try
    {
      for(Object object : objects)
        em.persist(object);

      em.getTransaction().commit();  
    }
    catch (Exception e) {  
      em.getTransaction().rollback();  
      throw new SLEEException(e.getMessage(),e);
    }
    finally
    {  
      em.close();  
    }  
  }

  public void dumpClassInfo(String className) throws Exception
  {
    System.out.println("****************************** BEGIN OF CLASS DUMP ******************************");
    System.out.println("-< INTERFACES >-");
    for( java.lang.Class clazz : Class.forName( className ).getInterfaces() )
    {
      System.out.println( clazz.toString() );
    }
    
    System.out.println("-< CLASS >-");
    for( java.lang.annotation.Annotation annot : Class.forName( className ).getDeclaredAnnotations() )
    {
      System.out.println( annot.toString() );
    }

    System.out.println("-< METHODS >-");
    for( Method method : Class.forName( className ).getDeclaredMethods() )
    {
      for( java.lang.annotation.Annotation annot : method.getAnnotations() )
        System.out.println( annot.toString() );
      
      System.out.println( method.toString() );
    }

    System.out.println("-< FIELDS >-");
    for( Field field : Class.forName( className ).getDeclaredFields() )
    {
      for( java.lang.annotation.Annotation annot : field.getAnnotations() )
        System.out.println( annot.toString() );
      
      System.out.println( field.toString() );
    }
    System.out.println("******************************* END OF CLASS DUMP *******************************");
  }

}
