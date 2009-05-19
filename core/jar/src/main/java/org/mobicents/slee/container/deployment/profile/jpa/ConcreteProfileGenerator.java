package org.mobicents.slee.container.deployment.profile.jpa;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.CtPrimitiveType;
import javassist.NotFoundException;

import javax.slee.SLEEException;
import javax.slee.profile.Profile;
import javax.slee.profile.ProfileManagement;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.component.ProfileSpecificationComponent;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ProfileSpecificationDescriptorImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.MCMPField;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.MProfileCMPInterface;
import org.mobicents.slee.container.deployment.ClassUtils;
import org.mobicents.slee.container.deployment.profile.SleeProfileClassCodeGenerator;
import org.mobicents.slee.container.profile.ProfileCmpHandler;
import org.mobicents.slee.container.profile.ProfileConcrete;
import org.mobicents.slee.container.profile.ProfileObject;

/**
 * 
 * Generates the ProfileConcrete impl for a specific Profile Specification.
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

      String concreteClassName = cmpInterface.getProfileCmpInterfaceName() + "Impl";

      // Create the Impl class
      CtClass profileConcreteClass = ClassGeneratorUtils.createClass(concreteClassName, new String[]{cmpInterface.getProfileCmpInterfaceName(), ProfileConcrete.class.getName()});

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
 
      // add profile object field and getter/setter
      CtField fProfileObject = ClassGeneratorUtils.addField( ClassGeneratorUtils.getClass(ProfileObject.class.getName()), "profileObject", profileConcreteClass );
      ClassGeneratorUtils.generateGetterAndSetter( fProfileObject, null );
      
      // CMP fields getters and setters
      generateCMPFieldsWithGettersAndSetters(profileConcreteClass);
      
      generateConstructors(profileConcreteClass);
      
      // Profile Management methods for JAIN SLEE 1.1
      Map<String, CtMethod> profileManagementMethods = ClassUtils.getInterfaceMethodsFromInterface(ClassGeneratorUtils.getClass(Profile.class.getName()));
      
      // Profile Management methods for JAIN SLEE 1.0
      profileManagementMethods.putAll(ClassUtils.getInterfaceMethodsFromInterface(ClassGeneratorUtils.getClass(ProfileManagement.class.getName())));

      // Check for a Profile Management Interface
      Class profileManagementInterface = this.profileComponent.getProfileManagementInterfaceClass();
      
      if (profileManagementInterface != null) {
        profileManagementMethods.putAll(ClassUtils.getInterfaceMethodsFromInterface(ClassGeneratorUtils.getClass(profileManagementInterface.getName())));
      }
      
      Class profileLocalInterface = this.profileComponent.getProfileLocalInterfaceClass();
      
      if(profileLocalInterface != null) {
        profileManagementMethods.putAll(ClassUtils.getAbstractMethodsFromClass((ClassGeneratorUtils.getClass(profileLocalInterface.getName()))));
      }

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
      
      // We should use key, but ClassUtils has different behaviors... go safe!
      String methodKey = method.getName() + method.getSignature();
      
      if(cmpInterfaceMethods.containsKey(methodKey))
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
        ClassGeneratorUtils.generateDelegateMethod( profileConcreteClass, entry.getValue(), interceptor, true );
      }
      catch ( Exception e ) {
        throw new SLEEException(e.getMessage(),e);
      }
    }
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
          generateCMPGetter( genField );
          generateCMPSetter( genField );
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
  
  
  /**
   * Generates a getter for the field (get<FieldName>) and adds it to the declaring class.
   * 
   * @param field
   * @return
   * @throws NotFoundException
   * @throws CannotCompileException
   */
  private CtMethod generateCMPGetter(CtField field) throws NotFoundException, CannotCompileException
  {
    CtMethod getter = CtNewMethod.getter( "get" + ClassGeneratorUtils.capitalize(field.getName()), field );
    CtClass classToBeInstrumented = field.getDeclaringClass();
    
    if (logger.isDebugEnabled()) {
      logger.debug("About to instrument: " + getter.getName() + ", into: " + classToBeInstrumented.getName());
    }
    
    String getterBody = 
    	"{" +
    		ProfileCmpHandler.class.getName() + ".beforeGetCmpField(profileObject);" +
        "	try {" +
        (field.getType().isPrimitive() ? 
        "    return ($r) (("+profileComponent.getProfileEntityClass().getName()+")profileObject.getProfileEntity()).get" + ClassGeneratorUtils.getPojoCmpAccessorSufix(field.getName()) + "();" : 
        "    return ($r) " + ProfileEntity.class.getName() + ".makeDeepCopy(" + "(("+profileComponent.getProfileEntityClass().getName()+")profileObject.getProfileEntity()).get" + ClassGeneratorUtils.getPojoCmpAccessorSufix(field.getName()) + "()" + ");") +
        " }" +
        "	finally {" +
        		ProfileCmpHandler.class.getName() + ".afterGetCmpField(profileObject);" +
        "	};"+
    	"}";

    getter.setBody(getterBody);
    classToBeInstrumented.addMethod(getter);
    
    return getter;
  }

  /**
   * Generates a getter for the field (get<FieldName>) and adds it to the declaring class.
   * 
   * @param field
   * @return
   * @throws NotFoundException
   * @throws CannotCompileException
   */
  private CtMethod generateCMPSetter(CtField field) throws NotFoundException, CannotCompileException
  {
    CtMethod setter = CtNewMethod.setter( "set" + ClassGeneratorUtils.capitalize(field.getName()), field );
    CtClass classToBeInstrumented = field.getDeclaringClass();
    
    if (logger.isDebugEnabled()) {
      logger.debug("About to instrument: " + setter.getName() + ", into: " + classToBeInstrumented.getName());
    }

    String setterBody = 
    	"{" +
    		ProfileCmpHandler.class.getName() + ".beforeSetCmpField(profileObject);" +
        "	try {" +
        (field.getType().isPrimitive() ? 
        "		(("+profileComponent.getProfileEntityClass().getName()+")profileObject.getProfileEntity()).set" + ClassGeneratorUtils.getPojoCmpAccessorSufix(field.getName()) + "($1);" :
        "   (("+profileComponent.getProfileEntityClass().getName()+")profileObject.getProfileEntity()).set" + ClassGeneratorUtils.getPojoCmpAccessorSufix(field.getName()) + "((" + field.getType().getName() + ")" + ProfileEntity.class.getName() + ".makeDeepCopy($1));") +
        "	}" +
        "	finally {" +
        		ProfileCmpHandler.class.getName() + ".afterSetCmpField(profileObject);" +
        "	};"+
    	"}";
    
    setter.setBody(setterBody);
    classToBeInstrumented.addMethod(setter);

    return setter;
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
